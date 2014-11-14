package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class MOD extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 2);

        Expr n = args[0];
        if (n instanceof ExprEvaluatable) {
            n = ((ExprEvaluatable) n).evaluate(context);
        }
        if (n instanceof ExprArray) {
            ExprArray a = (ExprArray) n;
            if (a.rows() > 1) {
                return ExprError.VALUE;
            }

            n = a.get(0, 0);
        }
        if (!(n instanceof ExprNumber)) {
            return ExprError.VALUE;
        }

        BigDecimal num = ((ExprNumber) n).decimalValue();

        Expr d = args[1];
        if (d instanceof ExprEvaluatable) {
            d = ((ExprEvaluatable) d).evaluate(context);
        }
        if (!(d instanceof ExprNumber)) {
            return ExprError.VALUE;
        }

        BigDecimal div = ((ExprNumber) d).decimalValue();

        // Need to match sign with implementation
        BigDecimal mod = num.remainder(div);
        if ((mod.compareTo(BigDecimal.ZERO) > 0 && div.compareTo(BigDecimal.ZERO) < 0)
                || (mod.compareTo(BigDecimal.ZERO) < 0 && div.compareTo(BigDecimal.ZERO) > 0))
            mod.negate();

        return new ExprDecimal(mod);
    }
}
