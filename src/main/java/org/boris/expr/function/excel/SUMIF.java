package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.util.Condition;

public class SUMIF extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 2);

        /*
        // First argument must be a reference to a range
        if (!(args[0] instanceof ExprVariable)) {
            throw new ExprException(
                    "First argument to SUMIF must be a reference");
        }

        // Sum range (if present) must be a reference to a range
        if (args.length > 2 && !(args[2] instanceof ExprVariable)) {
            throw new ExprException(
                    "Third argument to SUMIF must be a reference");
        }*/

        Expr range = evalArg(context, args[0]);
        int len = getLength(range);
        Condition cond = Condition.valueOf(evalArg(context, args[1]));
        Expr sumrange = args.length == 3 ? evalArg(context, args[2]) : range;

        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < len; i++) {
            sum = sum.add(eval(get(range, i), cond, get(sumrange, i)));
        }

        return new ExprDecimal(sum);
    }

    protected BigDecimal eval(Expr item, Condition c, Expr value)
            throws ExprException {
        if (c.eval(item)) {
            if (value instanceof ExprDecimal || value instanceof ExprInteger) {
                return ((ExprNumber) value).decimalValue();
            }
        }
        return BigDecimal.ZERO;
    }
}
