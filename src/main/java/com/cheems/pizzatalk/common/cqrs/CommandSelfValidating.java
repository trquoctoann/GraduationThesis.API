package com.cheems.pizzatalk.common.cqrs;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract class CommandSelfValidating<T> extends Command {

    private Validator validator;

    public CommandSelfValidating() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @SuppressWarnings("unchecked")
    public void validationSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
