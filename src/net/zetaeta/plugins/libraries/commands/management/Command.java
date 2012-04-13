package net.zetaeta.plugins.libraries.commands.management;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a command executor method. Mark command executor methods in PluginCommandExecutors with this (unless onCommand is used).
 * 
 * @author Zetaeta
 *
 */

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    
    /**
     * Command name.
     * */
    public String value();
    
    /**
     * Command description.
     * */
    public String description() default "";
    
    /**
     * Command usage, returned if command fails.
     * */
    public String usage() default "";
    
    /**
     * 
     * */
    public String[] aliases() default {};
}
