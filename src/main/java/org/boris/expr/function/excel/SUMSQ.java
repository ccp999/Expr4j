package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class SUMSQ extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        BigDecimal res = BigDecimal.ZERO;
        for (Expr arg : args)
            res = res.add(sumsq(context, arg));
        return new ExprDecimal(res);
    }

    private BigDecimal sumsq(IEvaluationContext context, Expr arg)
            throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }

        if (arg instanceof ExprNumber) {
            return ((ExprNumber) arg).decimalValue().pow(2);
        } else if (arg instanceof ExprArray) {
            ExprArray a = (ExprArray) arg;
            int rows = a.rows();
            int cols = a.columns();
            BigDecimal res = BigDecimal.ZERO;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    res = res.add(sumsq(context, a.get(i, j)));
                }
            }
            return res;
        }

        return BigDecimal.ZERO;
    }
}
