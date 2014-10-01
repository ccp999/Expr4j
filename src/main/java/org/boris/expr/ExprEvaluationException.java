package org.boris.expr;

public class ExprEvaluationException extends ExprException { 
    private static final long serialVersionUID = 6093868124776696735L;
    private ExprError error;
    
    public ExprEvaluationException(ExprError error) {
        this.error = error;
    }

    public ExprError getError() {
        return error;
    }
}
