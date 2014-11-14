package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.function.ForEachNumberFunction;
import org.boris.expr.util.Counter;

public class HARMEAN extends ForEachNumberFunction
{
    protected Expr evaluate(Counter counter) throws ExprException {
        if (counter.result != null)
            return counter.result;
        return new ExprDecimal(BigDecimal.ONE.divide(counter.value.divide(new BigDecimal(counter.count), ExprDecimal.MATH_CONTEXT), ExprDecimal.MATH_CONTEXT));
    }

    @Override
    protected void value(Counter counter, BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            counter.doit = false;
            counter.result = ExprError.NUM;
            return;
        }
        counter.count++;
        counter.value = counter.value.add(BigDecimal.ONE.divide(value, ExprDecimal.MATH_CONTEXT));        
    }
}
