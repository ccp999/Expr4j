package org.boris.expr.function.excel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class ROUNDUP extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 2);

        int dps = asInteger(context, args[1], true);

        Expr expr0 = evalArg(context, args[0]);
        if (expr0.getType() == ExprType.Missing) {
            return new ExprMissing();
        }

        BigDecimal num = asDecimal(context, expr0, true);

        return new ExprDecimal(num.setScale(dps, RoundingMode.UP));
    }
}
