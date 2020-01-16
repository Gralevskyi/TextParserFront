package com.gralevskyi.resttextparser.constraints;

// check if a username is already existed in DataBase

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface UniqueUsername {
	String message() default "There is already a user with this username. Please, choose another one.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
