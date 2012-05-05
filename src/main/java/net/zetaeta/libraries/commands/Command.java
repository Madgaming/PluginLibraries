package net.zetaeta.libraries.commands;

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
    public String usage() default "/<command>";
    
    /**
     * Command's aliases.
     * */
    public String[] aliases() default {};
    
    /**
     * Command's permission.
     */
    public String permission() default "";
    
    /**
     * Message to display if the user has not permission for the command.
     */
    public String permissionMessage() default "§cYou do not have access to that command!";
}
