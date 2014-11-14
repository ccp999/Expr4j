package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.function.ForEachNumberFunction;
import org.boris.expr.util.Counter;

public class GEOMEAN extends ForEachNumberFunction
{
    protected void initialize(Counter counter) throws ExprException {
        counter.value = BigDecimal.ONE;
    }

    protected Expr evaluate(Counter counter) throws ExprException {
        return new ExprDecimal(counter.value.pow(1 / counter.count));
    }

    @Override
    protected void value(Counter counter, BigDecimal value) {
        counter.count++;
        counter.value = counter.value.multiply(value);        
    }
}
