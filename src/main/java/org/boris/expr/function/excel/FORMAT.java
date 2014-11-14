package org.boris.expr.function.excel;

import java.text.DecimalFormat;

import org.boris.expr.Expr;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprFormatted;
import org.boris.expr.ExprNumber;
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

        if (expression.type == ExprType.Error) {
            return expression;
        }
        
        try {
            ExprTypes.assertType(expression, ExprType.Decimal, ExprType.Integer);
        }
        catch (ExprException e) {
            return ExprError.generateError(ExprError.NUM);
        }                                

        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setRoundingMode(java.math.RoundingMode.HALF_UP);
        if (((ExprNumber) expression).isDecimal()) {
            return new ExprFormatted(decimalFormat.format(((ExprNumber) expression).decimalValue()));    
        }
        else {
            return new ExprFormatted(decimalFormat.format(((ExprNumber) expression).intValue()));
        }        
    }
}
