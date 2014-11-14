package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class TRUNC extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 1);
        assertMaxArgCount(args, 2);
        BigDecimal num = asDecimal(context, args[0], true);
        int dig = 1;
        if (args.length == 2)
            dig = asInteger(context, args[1], true);
        if (dig == 1) {
            return new ExprDecimal(new BigDecimal(num.intValue()));
        } else {
            int div = (int) Math.pow(10, dig);
            int v = num.multiply(new BigDecimal(div)).intValue();
            double d = (double) v / div;
            return new ExprDecimal(new BigDecimal(Double.toString(d)));
        }
    }
}
