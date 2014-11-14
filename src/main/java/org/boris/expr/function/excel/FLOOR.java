package org.boris.expr.function.excel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class FLOOR extends AbstractFunction
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
        BigDecimal m = val.remainder(rnd).setScale(8, RoundingMode.HALF_UP);        
        if (m == rnd)
            m = BigDecimal.ZERO;
        return new ExprDecimal(val.subtract(m));
    }
}
