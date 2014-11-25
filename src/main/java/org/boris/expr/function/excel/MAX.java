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

public class MAX extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 1);
        return max(context, args);
    }

    public static Expr max(IEvaluationContext context, Expr[] args)
            throws ExprException {
        BigDecimal d = new BigDecimal(Double.toString(Double.MAX_VALUE)).negate();
        
        if (allArgsMissing(context, args)) {
            return new ExprMissing();
        }
        
        for (Expr a : args) {
            Expr res = max(context, a);
            if (res instanceof ExprError) {
                return res;
            } 
            else if (res.type == ExprType.Missing) {
                // Missing values should not be factored in. i.e. -5 > missing
                continue;
            }
            else {
                BigDecimal r = ((ExprDecimal) res).decimalValue();
                if (r.compareTo(d) > 0)
                    d = r;
            }
        }
        return new ExprDecimal(d);
    }

    public static Expr max(IEvaluationContext context, Expr arg)
            throws ExprException {
        String variableName = getVariableName(arg);
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }
        
        if (arg != null && arg.type == ExprType.Missing) {
            return arg;
        }
        
        validateEvalType(arg, ExprError.generateError(ExprError.NUM), variableName, ExprType.Decimal, ExprType.Integer, ExprType.Array);

        if (arg instanceof ExprNumber) {
            return new ExprDecimal(((ExprNumber) arg).decimalValue());
        }

        if (arg instanceof ExprArray) {
            return max(context, ((ExprArray) arg).getInternalArray());
        }

        if (arg instanceof ExprError) {
            return arg;
        }

        return ExprError.VALUE;
    }
}
