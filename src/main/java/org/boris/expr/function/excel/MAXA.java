package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.function.ForEachNumberAFunction;
import org.boris.expr.util.Counter;

public class MAXA extends ForEachNumberAFunction
{
    protected void initialize(Counter counter) throws ExprException {
        counter.value = new BigDecimal(Double.toString(Double.MAX_VALUE)).negate();
    }

    protected Expr evaluate(Counter counter) throws ExprException {
        return new ExprDecimal(counter.value);
    }

    @Override
    protected void value(Counter counter, BigDecimal value) {
        if (value.compareTo(counter.value) > 0)
            counter.value = value;        
    }
}
