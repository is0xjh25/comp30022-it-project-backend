package tech.crm.crmserver.common.validator;

import org.hibernate.validator.constraints.ConstraintComposition;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.validator.constraints.CompositionType.OR;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

/**
 * null or not blank<br/>
 * https://stackoverflow.com/a/43716689<br/>
 * (CC BY-SA 3.0)
 */
@ConstraintComposition(OR)
@Null
@NotBlank
@ReportAsSingleViolation
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { })
public @interface NullOrNotBlank {
    String message() default "{org.hibernate.validator.constraints.NullOrNotBlank.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
