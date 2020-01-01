package com.gralevskyi.resttextparser.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueWordsListNameForCurrentUserValidator.class)
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface UniqueWordsListNameForCurrentUser {
	String message() default "You have already created List with this name. Chose another name or delete saved list.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
