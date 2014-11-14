package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.function.ForEachNumberFunction;
import org.boris.expr.util.Counter;

public class KURT extends ForEachNumberFunction
{
    public KURT() {
        setIterations(3);
    }

    protected void value(Counter counter, BigDecimal value) {
        switch (counter.iteration) {
        case 1:
            average(counter, value);
            break;
        case 2:
            stdev(counter, value);
            break;
        case 3:
            kurt(counter, value);
        }
    }

    protected void iteration(Counter counter) {
        switch (counter.iteration) {
        case 2:
            if (counter.count < 4) {
                counter.doit = false;
                counter.result = ExprError.DIV0;
            }
            counter.value = counter.value.divide(new BigDecimal(counter.count));
            break;
        case 3:
            counter.value2 = counter.value2.divide(new BigDecimal(counter.count - 1), ExprDecimal.MATH_CONTEXT);
            counter.value2 = new BigDecimal(Double.toString(Math.sqrt(counter.value2.doubleValue())));
            if (counter.value2.compareTo(BigDecimal.ZERO) == 0) {
                counter.doit = false;
                counter.result = ExprError.DIV0;
            }
        }
    }

    private void kurt(Counter counter, BigDecimal value) {
        BigDecimal subValue = value.subtract(counter.value).divide(counter.value2, ExprDecimal.MATH_CONTEXT);        
        counter.value3 = counter.value3.add(subValue.pow(4));
    }

    private void stdev(Counter counter, BigDecimal value) {
        counter.value2 = counter.value2.add(value.subtract(counter.value)).pow(2);
    }

    private void average(Counter counter, BigDecimal value) {
        counter.count++;
        counter.value = counter.value.add(value);
    }

    protected Expr evaluate(Counter counter) throws ExprException {
        double n = counter.count;
        
        double nCalculation = n * (n + 1) / ((n - 1) * (n - 2) * (n - 3));
        double nPowerCalculation = (3 * Math.pow(n - 1, 2)) / ((n - 2) * (n - 3));
        
        return new ExprDecimal(counter.value3.multiply(new BigDecimal(Double.toString(nCalculation)).subtract(new BigDecimal(Double.toString(nPowerCalculation)))));
    }
}
