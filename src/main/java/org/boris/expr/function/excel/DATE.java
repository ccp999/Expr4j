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

public class DATE extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 3);
        Expr eY = evalArg(context, args[0]);
        if (!isNumber(eY))
            return ExprError.VALUE;
        BigDecimal y = ((ExprNumber) eY).decimalValue();
        Expr eM = evalArg(context, args[1]);
        if (!isNumber(eM))
            return ExprError.VALUE;
        BigDecimal m = ((ExprNumber) eM).decimalValue();
        Expr eD = evalArg(context, args[1]);
        if (!isNumber(eD))
            return ExprError.VALUE;
        BigDecimal d = ((ExprNumber) eD).decimalValue();
        BigDecimal r = ExcelDate.date(y, m, d);
        if (r.compareTo(BigDecimal.ZERO) < 0)
            return ExprError.NUM;
        return new ExprDecimal(r);
    }

}
