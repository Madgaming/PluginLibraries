package net.zetaeta.bukkit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark method parameters that must not be <code>null</code>, 
 * and if they are a {@link NullPointerException} or an {@link IllegalArgumentException} will be thrown.
 * @author Zetaeta
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
@Target(ElementType.PARAMETER)
public @interface NotNull {
    
}
