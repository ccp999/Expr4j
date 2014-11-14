package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class COVAR extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 2);
        ExprArray array1 = asArray(context, args[0], false);
        ExprArray array2 = asArray(context, args[1], false);

        if (array1.length() != array2.length())
            return ExprError.NA;
        if (array1.length() == 0 || array2.length() == 0)
            return ExprError.DIV0;

        Expr ea1 = AVERAGE.average(context, array1);
        if (ea1 instanceof ExprError)
            return ea1;
        BigDecimal average1 = ((ExprNumber) ea1).decimalValue();

        Expr ea2 = AVERAGE.average(context, array2);
        if (ea2 instanceof ExprError)
            return ea2;
        BigDecimal average2 = ((ExprNumber) ea2).decimalValue();

        int count = 0;
        BigDecimal p = BigDecimal.ZERO;

        int len = array1.length();
        for (int i = 0; i < len; i++) {
            Expr x = array1.get(i);
            Expr y = array2.get(i);
            if (isNumber(x) && isNumber(y)) {
                p = p.add((asDecimal(context, x, true).subtract(average1)).multiply(
                        (asDecimal(context, y, true).subtract(average2))));
                count++;
            }
        }

        if (count == 0)
            return ExprError.DIV0;

        return new ExprDecimal(p.divide(new BigDecimal(count), ExprDecimal.MATH_CONTEXT));
    }
}
