package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class N extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 1);
        Expr a = evalArg(context, args[0]);
        if (a instanceof ExprArray) {
            ExprArray ar = (ExprArray) a;
            a = ar.get(0);
        }
        if (a instanceof ExprNumber) {
            return new ExprDecimal(((ExprNumber) a).decimalValue());
        }
        return new ExprDecimal(BigDecimal.ZERO);
    }
}
