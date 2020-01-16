package com.gralevskyi.resttextparser.constraints;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueWordsUnitNameForCurrentUserValidator.class)
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface UniqueWordsUnitNameForCurrentUser {
	String message() default "You have already created words unit with this name. Please, choose another name.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
