package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.util.ExcelDate;

public class TIME extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 3);
        Expr eH = evalArg(context, args[0]);
        if (!isNumber(eH))
            return ExprError.VALUE;
        double h = ((ExprNumber) eH).decimalValue().doubleValue();
        Expr eM = evalArg(context, args[1]);
        if (!isNumber(eM))
            return ExprError.VALUE;
        double m = ((ExprNumber) eM).decimalValue().doubleValue();
        Expr eS = evalArg(context, args[1]);
        if (!isNumber(eS))
            return ExprError.VALUE;
        double s = ((ExprNumber) eS).decimalValue().doubleValue();
        double r = ExcelDate.time(h, m, s);
        if (r < 0)
            return ExprError.NUM;
        return new ExprDecimal(new BigDecimal(Double.toString(r)));
    }
}
