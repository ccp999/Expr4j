package org.boris.expr.function;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprString;
import org.boris.expr.util.Counter;

public class AbstractVarianceFunction extends ForEachFunction
{
    private final boolean includeLogical;
    private final boolean allPopulation;

    public AbstractVarianceFunction(boolean includeLogical,
            boolean allPopulation) {
        this.includeLogical = includeLogical;
        this.allPopulation = allPopulation;
        setIterations(2);
    }

    protected void iteration(Counter c) {
        switch (c.iteration) {
        case 2:
            c.value = c.value.divide(new BigDecimal(c.count), ExprDecimal.MATH_CONTEXT);
            break;
        }
    }

    protected void value(Counter counter, BigDecimal value) {
        switch (counter.iteration) {
        case 1:
            average(counter, value);
            break;
        case 2:
            var(counter, value);
            break;
        }
    }

    private void var(Counter counter, BigDecimal value) {
        counter.value2 = counter.value2.add(value.subtract(counter.value).pow(2));
    }

    private void average(Counter counter, BigDecimal value) {
        counter.value = counter.value.add(value);
        counter.count++;
    }

    protected Expr evaluate(Counter counter) throws ExprException {
        BigDecimal count = new BigDecimal(counter.count - (allPopulation ? 0 :1));
        return new ExprDecimal(counter.value2.divide(count, ExprDecimal.MATH_CONTEXT));        
    }

    protected void initialize(Counter counter) throws ExprException {
    }

    protected void value(Counter counter, Expr value) throws ExprException {
        if (includeLogical) {
            if (value instanceof ExprNumber) {
                value(counter, ((ExprNumber) value).decimalValue());
            } else if (value instanceof ExprString) {
                value(counter, BigDecimal.ZERO);
            }
        } else {
            if (value instanceof ExprDecimal || value instanceof ExprInteger) {
                value(counter, ((ExprNumber) value).decimalValue());
            }
        }
    }
}
