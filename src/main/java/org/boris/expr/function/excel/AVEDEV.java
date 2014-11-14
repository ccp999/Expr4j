package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprString;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.util.Counter;

public class AVEDEV extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 1);

        Counter counter = new Counter();
        counter.count = 0;
        counter.value = BigDecimal.ZERO;
        
        for (Expr a : args)
            AVERAGE.eval(context, a, counter, true);

        if (counter.count == 0) {
            return ExprError.NUM;
        }

        BigDecimal average = counter.value.divide(new BigDecimal(counter.count), ExprDecimal.MATH_CONTEXT);

        counter.count = 0;
        counter.value = BigDecimal.ZERO;

        for (Expr a : args)
            eval(context, a, average, counter, true);

        return new ExprDecimal(counter.value.divide(new BigDecimal(counter.count), ExprDecimal.MATH_CONTEXT));
    }

    public static void eval(IEvaluationContext context, Expr a, BigDecimal average,
            Counter counter, boolean strict) throws ExprException {
        if (a instanceof ExprEvaluatable)
            a = ((ExprEvaluatable) a).evaluate(context);

        if (a == null)
            return;

        if (a instanceof ExprMissing)
            return;

        if (a instanceof ExprString) {
            if (strict)
                throw new ExprException("Unexpected argument for AVERAGE: " + a);
            else
                return;
        }

        if (a instanceof ExprDecimal || a instanceof ExprInteger) {
            BigDecimal d = ((ExprNumber) a).decimalValue();
            counter.value = counter.value.add(average.subtract(d).abs());
            counter.count++;
            return;
        }

        if (a instanceof ExprArray) {
            ExprArray arr = (ExprArray) a;
            int rows = arr.rows();
            int cols = arr.columns();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    eval(context, arr.get(i, j), average, counter, false);
                }
            }

            return;
        }

        throw new ExprException("Unexpected argument for AVEDEV: " + a);
    }
}
