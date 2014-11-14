package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class CEILING extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 2);
        BigDecimal val = asDecimal(context, args[0], true);
        BigDecimal rnd = asDecimal(context, args[1], true);
        if (rnd.compareTo(BigDecimal.ZERO) == 0)
            return new ExprDecimal(BigDecimal.ZERO);
        if ((val.compareTo(BigDecimal.ZERO) < 0 && rnd.compareTo(BigDecimal.ZERO) > 0)
                || (val.compareTo(BigDecimal.ZERO) > 0 && rnd.compareTo(BigDecimal.ZERO) < 0))
            return ExprError.NUM;
        BigDecimal m = val.remainder(rnd);
        if (rnd.compareTo(BigDecimal.ZERO) < 0)
            rnd = BigDecimal.ZERO;
        return new ExprDecimal(val.subtract(m).add(rnd));
    }

    public boolean equalish(double d1, double d2) {
        return Math.abs(d1 - d2) < 0.0000000001;
    }
}
