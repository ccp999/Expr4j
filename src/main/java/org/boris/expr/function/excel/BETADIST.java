package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.util.Statistics;

public class BETADIST extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 3);
        assertMaxArgCount(args, 5);
        Expr eX = evalArg(context, args[0]);
        if (!isNumber(eX))
            return ExprError.VALUE;
        BigDecimal x = ((ExprDecimal) eX).decimalValue();
        Expr eAlpha = evalArg(context, args[1]);
        if (!isNumber(eAlpha))
            return ExprError.VALUE;
        BigDecimal alpha = ((ExprDecimal) eAlpha).decimalValue();
        if (alpha.compareTo(BigDecimal.ZERO) <= 0)
            return ExprError.NUM;
        Expr eBeta = evalArg(context, args[2]);
        if (!isNumber(eBeta))
            return ExprError.VALUE;
        BigDecimal beta = ((ExprDecimal) eBeta).decimalValue();
        if (beta.compareTo(BigDecimal.ZERO) <= 0)
            return ExprError.NUM;
        BigDecimal a = BigDecimal.ZERO;
        BigDecimal b = BigDecimal.ONE;
        if (args.length > 3) {
            Expr eA = evalArg(context, args[3]);
            if (!isNumber(eA))
                return ExprError.VALUE;
            a = ((ExprDecimal) eA).decimalValue();
        }
        if (args.length > 4) {
            Expr eB = evalArg(context, args[4]);
            if (!isNumber(eB))
                return ExprError.VALUE;
            b = ((ExprDecimal) eB).decimalValue();
        }
        if (x.compareTo(a) < 0 || x.compareTo(b) > 0  || a.compareTo(b) == 0)
            return ExprError.NUM;
        return new ExprDecimal(Statistics.betaDist(x, alpha, beta, a, b));
    }
}
