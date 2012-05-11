package net.zetaeta.bukkit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Useless stuff that may be implemented in the future.
 * 
 * @author Zetaeta
 *
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface Useless {
    
    /**
     * Reason for or comment on uselessness.
     */
    public String value() default "";
}
