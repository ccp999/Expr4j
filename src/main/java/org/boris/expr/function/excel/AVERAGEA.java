package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.function.ForEachNumberAFunction;
import org.boris.expr.util.Counter;

public class AVERAGEA extends ForEachNumberAFunction
{
    protected Expr evaluate(Counter counter) throws ExprException {
        return new ExprDecimal(counter.value.divide(new BigDecimal(counter.count), ExprDecimal.MATH_CONTEXT));
    }

    protected void value(Counter counter, BigDecimal value) {
        counter.count++;
        counter.value = counter.value.add(value);        
    }
}
