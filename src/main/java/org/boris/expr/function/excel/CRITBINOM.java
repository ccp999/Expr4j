package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.util.Statistics;

public class CRITBINOM extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 3);
        Expr et = evalArg(context, args[0]);
        if (!isNumber(et))
            return ExprError.VALUE;
        int trials = ((ExprNumber) et).intValue();
        if (trials < 0)
            return ExprError.NUM;
        Expr ep = evalArg(context, args[1]);
        if (!isNumber(ep))
            return ExprError.VALUE;
        BigDecimal p = ((ExprNumber) ep).decimalValue();
        if (p.compareTo(BigDecimal.ZERO) < 0 || p.compareTo(BigDecimal.ONE) > 0)
            return ExprError.NUM;
        Expr ea = evalArg(context, args[2]);
        if (!isNumber(ea))
            return ExprError.VALUE;
        BigDecimal alpha = ((ExprNumber) ea).decimalValue();
        if (alpha.compareTo(BigDecimal.ZERO) < 0 || alpha.compareTo(BigDecimal.ONE) > 0)
            return ExprError.NUM;

        return new ExprDecimal(Statistics.critBinom(trials, p, alpha));
    }
}
