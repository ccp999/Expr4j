package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprBoolean;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprString;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class TYPE extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 1);
        Expr a = evalArg(context, args[0]);
        if (a instanceof ExprString) {
            return new ExprDecimal(new BigDecimal("2"));
        } else if (a instanceof ExprInteger || a instanceof ExprDecimal) {
            return new ExprDecimal(BigDecimal.ONE);
        } else if (a instanceof ExprBoolean) {
            return new ExprDecimal(new BigDecimal("4"));
        } else if (a instanceof ExprArray) {
            return new ExprDecimal(new BigDecimal("64"));
        } else {
            return new ExprDecimal(new BigDecimal("16"));
        }
    }
}
