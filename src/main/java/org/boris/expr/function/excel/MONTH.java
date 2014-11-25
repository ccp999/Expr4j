package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.ExprException;
import org.boris.expr.function.DoubleInOutFunction;
import org.boris.expr.util.ExcelDate;

public class MONTH extends DoubleInOutFunction
{
    protected double evaluate(double value) throws ExprException {
        return ExcelDate.getMonth(new BigDecimal(Double.toString(value)));
    }
}
