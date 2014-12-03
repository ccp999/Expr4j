package org.boris.expr.function.excel;

import org.boris.expr.Expr;
import org.boris.expr.ExprBoolean;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class EMPTY extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 1);
        Expr expr = args[0];
        
        expr = ((ExprEvaluatable) expr).evaluate(context);
        if (expr != null) {
            if (expr.toString() == null || expr.toString().equals("")) {
                return new ExprBoolean(true);
            }
        }
        
        return new ExprBoolean(false);
    }
}
