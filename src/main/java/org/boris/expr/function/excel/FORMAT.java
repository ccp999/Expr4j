package org.boris.expr.function.excel;

import java.text.DecimalFormat;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprString;
import org.boris.expr.ExprType;
import org.boris.expr.ExprTypes;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class FORMAT extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertArgCount(args, 2);
        String format = args[1].toString();
        return format(context, format, args[0]);
    }

    public static Expr format(IEvaluationContext context, String format, Expr expression)
            throws ExprException {
        
        if (expression instanceof ExprEvaluatable) {
            expression = ((ExprEvaluatable) expression).evaluate(context);
        }

        try {
            ExprTypes.assertType(expression, ExprType.Double, ExprType.Integer);
        }
        catch (ExprException e) {
            return ExprError.generateError(ExprError.NUM);
        }                                

        DecimalFormat decimalFormat = new DecimalFormat(format);
        return new ExprString(decimalFormat.format( ((ExprNumber) expression).doubleValue()));
    }

    public static double sum(IEvaluationContext context, Expr arg)
            throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }

        if (arg instanceof ExprNumber) {
            return ((ExprNumber) arg).doubleValue();
        } else if (arg instanceof ExprArray) {
            ExprArray a = (ExprArray) arg;
            int rows = a.rows();
            int cols = a.columns();
            double res = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    res += sum(context, a.get(i, j));
                }
            }
            return res;
        }
        return 0;
    }
}
