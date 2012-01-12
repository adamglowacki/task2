package pl.edu.mimuw.ag291541.task2.check;

import static org.junit.Assert.assertTrue;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

import pl.edu.mimuw.ag291541.task2.dao.ContentDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.AceDAO;
import pl.edu.mimuw.ag291541.task2.security.dao.UserDAO;
import pl.edu.mimuw.ag291541.task2.security.service.ACLService;
import pl.edu.mimuw.ag291541.task2.service.AnnouncementService;
import pl.edu.mimuw.ag291541.task2.service.ContentService;
import pl.edu.mimuw.ag291541.task2.util.TransactionUtilLibrary;

/**
 * Runs some test methods from <code>AclListenerTest</code> in parallel.
 */
@ContextConfiguration(locations = { "classpath:pl/edu/mimuw/ag291541/task2/task2-spring-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
public class ParallelAclListenerTest {
	private AclListenerTest aclListenerTest;
	private Logger log = LoggerFactory.getLogger(ParallelAclListenerTest.class);
	/* Having this field makes this object stateful. */
	private boolean caughtNoUnexpected;

	@Autowired
	protected UserDAO userDao;
	@Autowired
	protected ContentDAO contentDao;
	@Autowired
	protected AceDAO aceDao;
	@Autowired
	protected ContentService contentService;
	@Autowired
	protected AnnouncementService announcementService;
	@Autowired
	protected ACLService aclService;
	@Autowired
	protected JdbcTemplate template;
	@Autowired
	protected PlatformTransactionManager txManager;
	@Autowired
	protected TransactionUtilLibrary txUtil;

	@Before
	public void createTestCase() {
		aclListenerTest = new AclListenerTest();
		aclListenerTest.injectDependencies(userDao, contentDao, aceDao,
				contentService, announcementService, aclService, template,
				txManager, txUtil);
		aclListenerTest.loadData();
	}

	@After
	public void tearDownDb() {
		aclListenerTest.removeData();
	}

	@Test
	public void readAllowedAndWriteForbidden() {
		TestRunner readAllowed = new TestRunner(aclListenerTest, "readAllowed");
		TestRunner writeForbidden = new TestRunner(aclListenerTest,
				"writeForbidden");
		assertTrue(runTests(readAllowed, writeForbidden));
	}

	@Test
	public void manyManipulatingAcl() {
		TestRunner revokeWriteFromRecipient = new TestRunner(aclListenerTest,
				"revokeWriteFromRecipient");
		TestRunner revokeReadFromRecipient = new TestRunner(aclListenerTest,
				"revokeReadFromRecipient");
		TestRunner revokeReadFromCreator = new TestRunner(aclListenerTest,
				"revokeReadFromCreator");
		TestRunner getAllWithPrivilege = new TestRunner(aclListenerTest,
				"getAllWithPrivilege");
		TestRunner getAllWithoutPrivilege = new TestRunner(aclListenerTest,
				"getAllWithoutPrivilege");
		assertTrue(runTests(revokeWriteFromRecipient, revokeReadFromRecipient,
				revokeReadFromCreator, getAllWithPrivilege,
				getAllWithoutPrivilege));
	}

	private Thread[] packToThreads(UncaughtExceptionHandler exceptionHandler,
			Runnable[] runnables) {
		Thread[] threads = new Thread[runnables.length];
		for (int i = 0; i < runnables.length; i++) {
			threads[i] = new Thread(runnables[i]);
			threads[i].setUncaughtExceptionHandler(exceptionHandler);
		}
		return threads;
	}

	private boolean runTests(TestRunner... tests) {
		caughtNoUnexpected = true;
		UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				synchronized (ParallelAclListenerTest.this) {
					caughtNoUnexpected = false;
				}
			}
		};
		Thread[] threads = packToThreads(exceptionHandler, tests);
		for (Thread thread : threads)
			thread.start();
		for (Thread thread : threads)
			try {
				thread.join();
			} catch (InterruptedException e1) {
				log.error("Exception on joining thread:", e1);
				throw new RuntimeException(e1);
			}
		return caughtNoUnexpected;
	}

	private class TestRunner implements Runnable {
		Object testCase;
		Method testMethod;
		Class<? extends Throwable> expected;

		public TestRunner(Object testCase, String testMethodName) {
			this.testCase = testCase;
			this.testMethod = null;
			try {
				testMethod = testCase.getClass().getMethod(testMethodName);
			} catch (NoSuchMethodException e) {
				log.error("Typed test method not found!");
				assertTrue(false);
			}
			Test testAnnotation = testMethod.getAnnotation(Test.class);
			expected = testAnnotation != null ? testAnnotation.expected()
					: null;
		}

		@Override
		public void run() {
			try {
				testMethod.invoke(testCase);
				if (!expected.equals(Test.None.class))
					throw new AssertionError();
				else
					log.info("Parallel invocation of {} succeeded.",
							testMethod.getName());
			} catch (IllegalAccessException ex) {
				throw new RuntimeException(ex);
			} catch (InvocationTargetException ex) {
				if (expected.isAssignableFrom(ex.getTargetException()
						.getClass())) {
					log.info(
							"Parallel invocation of {} raised an expected exception.",
							testMethod.getName());
				} else {
					log.error("Unexpected exception caught:", ex.getCause());
					throw new RuntimeException(ex);
				}
			}
		}
	}
}
