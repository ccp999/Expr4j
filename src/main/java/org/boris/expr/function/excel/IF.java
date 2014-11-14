package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprBoolean;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class IF extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 2);
        assertMaxArgCount(args, 3);

        Expr cond = evalArg(context, args[0]);
        Expr yRes = args[1];
        Expr nRes = null;
        if (args.length == 2) {
            nRes = ExprBoolean.FALSE;
        } else {
            nRes = args[2];
        }
        if (cond instanceof ExprNumber) {
            Expr res = evalArg(context,
                    ((ExprNumber) cond).booleanValue() ? yRes : nRes);
            if (res instanceof ExprMissing) {
                res = new ExprDecimal(BigDecimal.ZERO);
            }
            return res;
        }
        return ExprError.VALUE;
    }
}
