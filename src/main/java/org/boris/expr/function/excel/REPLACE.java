package org.boris.expr.function.excel;

import org.boris.expr.Expr;
import org.boris.expr.ExprError;
import org.boris.expr.ExprException;
import org.boris.expr.ExprString;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class REPLACE extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 4);
        String str = asString(context, args[0], false);
        int start = asInteger(context, args[1], true);
        if (start < 1)
            return ExprError.VALUE;
        int len = asInteger(context, args[2], true);
        if (len < 0)
            return ExprError.VALUE;
        String rep = asString(context, args[3], false);
        StringBuilder sb = new StringBuilder();
        int stlen = str.length();
        if (start < stlen) {
            sb.append(str.substring(0, start - 1));
        }
        sb.append(rep);
        if (start + len <= stlen) {
            sb.append(str.substring(start + len - 1));
        }
        return new ExprString(sb.toString());
    }
}
