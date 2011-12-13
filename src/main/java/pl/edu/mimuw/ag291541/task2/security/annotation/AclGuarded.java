package pl.edu.mimuw.ag291541.task2.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If a class is annotated with it then ACL checking is runned for entities of
 * this class loaded through Hibernate.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AclGuarded {

}
