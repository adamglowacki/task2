package pl.edu.mimuw.ag291541.task2.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If a class is annotated with it then ACL checking is performed for instances
 * of this class loaded through Hibernate (but not necessarily for instances of
 * its subclasses).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AclGuarded {
}
