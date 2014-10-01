package org.boris.expr.function;

import org.boris.expr.ExprException;

public class FunctionValidationException extends ExprException {
    private static final long serialVersionUID = 1L;
    public FunctionValidationException(String message) {
        super(message);
    }
}
