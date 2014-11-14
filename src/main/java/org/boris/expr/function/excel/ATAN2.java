package org.boris.expr.function.excel;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class ATAN2 extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 2);
        double x = asDouble(context, args[0], true);
        double y = asDouble(context, args[1], true);
        return new ExprDecimal(Double.toString(Math.atan2(y, x)));
    }
}
