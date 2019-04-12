package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class MOD extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args,2);
        assertMaxArgCount(args, 3);

        Expr expr0 = evalArg(context, args[0]);
        Expr expr1 = evalArg(context, args[1]);

        boolean isStrict = args.length == 3 && args[2] != null && "strict".equals(args[2].toString());

        if (isStrict) {
            if (expr0.getType() == ExprType.Missing && expr1.getType() == ExprType.Missing) {
                return new ExprMissing();
            }
        } else {
            if (expr0.getType() == ExprType.Missing || expr1.getType() == ExprType.Missing) {
                return new ExprMissing();
            }
        }

        Expr n = expr0;
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

        Expr d = expr1;
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
