package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.function.ForEachNumberFunction;
import org.boris.expr.util.Counter;

public class DEVSQ extends ForEachNumberFunction
{
    public DEVSQ() {
        setIterations(2);
    }

    protected void value(Counter counter, BigDecimal value) {
        switch (counter.iteration) {
        case 1:
            avg(counter, value);
            break;
        case 2:
            if (!counter.flag) {
                counter.value = counter.value.divide(new BigDecimal(counter.count), ExprDecimal.MATH_CONTEXT);
                counter.flag = true;
            }
            devsq(counter, value);
            break;
        }
    }

    private void devsq(Counter counter, BigDecimal value) {
        counter.value2 = counter.value2.add(value.subtract(counter.value).pow(2));
    }

    private void avg(Counter counter, BigDecimal value) {
        counter.value = counter.value.add(value);
        counter.count++;
    }

    protected Expr evaluate(Counter counter) throws ExprException {
        return new ExprDecimal(counter.value2);
    }
}
