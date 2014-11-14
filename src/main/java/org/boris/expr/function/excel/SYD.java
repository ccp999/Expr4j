package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class SYD extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 4);
        BigDecimal cost = asDecimal(context, args[0], true);
        BigDecimal salvage = asDecimal(context, args[1], true);
        BigDecimal life = asDecimal(context, args[2], true);
        BigDecimal per = asDecimal(context, args[3], true);
        BigDecimal syd = cost.subtract(salvage)
                .multiply(life.subtract(per).add(BigDecimal.ONE).multiply(new BigDecimal("2")))
                .divide(life.multiply(life.add(BigDecimal.ONE)), ExprDecimal.MATH_CONTEXT);
        
        return new ExprDecimal(syd);
    }
}
