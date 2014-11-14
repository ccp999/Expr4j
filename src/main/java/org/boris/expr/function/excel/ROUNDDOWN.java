package org.boris.expr.function.excel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class ROUNDDOWN extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 2);
        BigDecimal num = asDecimal(context, args[0], true);
        int dps = asInteger(context, args[1], true);
        return new ExprDecimal(num.setScale(dps, RoundingMode.DOWN));
    }
}
