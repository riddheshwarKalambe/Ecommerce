package com.ecommerce.validate;

//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.FIELD, ElementType.PARAMETER})  //where to use
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class) // where to write logic
public @interface  ImageNameValid {

    // error message
    String message() default "Invalid Image Name !!";

    // represent group of constraints
    Class<?>[] groups() default { };

    //additional information about annotation
    Class<? extends Payload>[] payload() default { };



}
