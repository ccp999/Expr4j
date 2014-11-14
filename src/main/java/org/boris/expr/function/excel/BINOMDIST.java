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

public class BINOMDIST extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 4);
        Expr ens = evalArg(context, args[0]);
        if (!isNumber(ens))
            return ExprError.VALUE;
        int nums = ((ExprNumber) ens).intValue();
        if (nums < 0)
            return ExprError.NUM;
        Expr et = evalArg(context, args[1]);
        if (!isNumber(et))
            return ExprError.VALUE;
        int trials = ((ExprNumber) et).intValue();
        if (nums > trials)
            return ExprError.NUM;
        Expr ep = evalArg(context, args[2]);
        if (!isNumber(ep))
            return ExprError.VALUE;
        BigDecimal p = ((ExprNumber) ep).decimalValue();
        if (p.compareTo(BigDecimal.ZERO) < 0 || p.compareTo(BigDecimal.ONE) > 0)
            return ExprError.NUM;
        Expr ec = evalArg(context, args[3]);
        if (!(ec instanceof ExprNumber))
            return ExprError.VALUE;
        boolean c = ((ExprNumber) ec).booleanValue();

        return new ExprDecimal(Statistics.binomDist(nums, trials, p, c));
    }
}
