package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.ExprException;
import org.boris.expr.function.DoubleInOutFunction;
import org.boris.expr.util.ExcelDate;

public class DAY extends DoubleInOutFunction
{
    protected double evaluate(double value) throws ExprException {
        return ExcelDate.getDayOfMonth(new BigDecimal(Double.toString(value)));
    }
}
