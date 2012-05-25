package net.zetaeta.bukkit.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface CommandHandler {
	String value();
	int minArgs() default 0;
	int maxArgs() default -1;
	String permission() default "";
}
