package org.boris.expr.function.excel;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.function.DoubleInOutFunction;

public class ABS extends AbstractFunction {
    public final Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 1);

        Expr expr0 = evalArg(context, args[0]);
        if (expr0.getType() == ExprType.Missing) {
            return new ExprMissing();
        }

        return new ExprDecimal(Double.toString(evaluate(asDouble(context, expr0, true))));
    }

    private  double evaluate(double value) throws ExprException {
        return Math.abs(value);
    }
}
