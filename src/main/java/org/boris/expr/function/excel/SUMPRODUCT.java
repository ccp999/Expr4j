package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class SUMPRODUCT extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 2);

        int len = 0;
        Expr[] ea = new Expr[args.length];

        for (int i = 0; i < args.length; i++) {
            ea[i] = evalArg(context, args[i]);
            if (i == 0) {
                len = getLength(ea[i]);
            } else {
                if (len != getLength(ea[i]))
                    return ExprError.VALUE;
            }
        }

        BigDecimal sum = BigDecimal.ZERO;

        for (int i = 0; i < len; i++) {
            BigDecimal p = BigDecimal.ONE;
            for (int j = 0; j < ea.length; j++) {
                Expr a = get(ea[j], i);
                if (a instanceof ExprDecimal || a instanceof ExprInteger) {
                    p = p.multiply(((ExprNumber) a).decimalValue());
                } else {
                    p = BigDecimal.ZERO;
                    break;
                }
            }
            sum = sum.add(p);
        }

        return new ExprDecimal(sum);
    }
}
