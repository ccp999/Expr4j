package org.boris.expr.function.excel;

import org.boris.expr.Expr;
import org.boris.expr.ExprDouble;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class RAND extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args) throws ExprException {
        assertArgCount(args, 0);
        return new ExprDouble(Math.random());
    }
}
