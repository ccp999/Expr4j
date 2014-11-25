package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class SUM extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 1);
        return sum(context, args);
    }

    public static Expr sum(IEvaluationContext context, Expr[] args)
            throws ExprException {
        BigDecimal res = BigDecimal.ZERO;
        
        if (allArgsMissing(context, args)) {
            return new ExprMissing();
        }
        
        for (Expr arg : args)
            res = res.add(sum(context, arg));
        return new ExprDecimal(res);
    }

    public static BigDecimal sum(IEvaluationContext context, Expr arg)
            throws ExprException {
        String variableName = getVariableName(arg);
        
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }
                
        validateEvalType(arg, ExprError.generateError(ExprError.NUM), variableName, ExprType.Missing, ExprType.Decimal, ExprType.Integer, ExprType.Array);
        
        if (arg instanceof ExprNumber) {
            return ((ExprNumber) arg).decimalValue();
        } else if (arg instanceof ExprArray) {
            ExprArray a = (ExprArray) arg;
            int rows = a.rows();
            int cols = a.columns();
            BigDecimal res = BigDecimal.ZERO;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    res = res.add(sum(context, a.get(i, j)));
                }
            }
            return res;
        }
        
        return BigDecimal.ZERO;
    }
}
