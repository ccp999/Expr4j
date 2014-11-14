package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.util.Statistics;

public class FACT extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 1);
        double value = asDouble(context, args[0], true);
        if (value < 0)
            return ExprError.NUM;

        return new ExprDecimal(new BigDecimal(Double.toString(Statistics.factorial((int) value).doubleValue())));
    }
}
