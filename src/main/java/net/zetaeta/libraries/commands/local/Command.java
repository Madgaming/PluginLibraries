package net.zetaeta.libraries.commands.local;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String[] aliases();
    String value() default "";
    String[] usage();
    String[] shortUsage();
    boolean useCommandArguments() default false;
    String[] boolFlags() default {};
    String[] valueFlags() default {};
    String permission();
    boolean checkPermissions() default true;
    boolean playersOnly() default false;
}
