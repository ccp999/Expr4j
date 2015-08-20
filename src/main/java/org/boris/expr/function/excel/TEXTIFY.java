package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprNumberText;
import org.boris.expr.ExprType;
import org.boris.expr.ExprTypes;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.util.NumberToText;

public class TEXTIFY extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 1);
        assertMaxArgCount(args, 2);
        
        boolean isCurrency = false;
        if (args.length == 2) {
            isCurrency = new Boolean(args[1].toString());
        }
        return textify(context, args[0], isCurrency);
    }

    public static Expr textify(IEvaluationContext context, Expr expression, boolean isCurrency)
            throws ExprException {
        
        if (expression instanceof ExprEvaluatable) {
            expression = ((ExprEvaluatable) expression).evaluate(context);
        }
        
        if (expression != null && expression.type == ExprType.Missing) {
            return expression;
        }
        
        try {
            ExprTypes.assertType(expression, ExprType.Decimal, ExprType.Integer, ExprType.Formatted);
        }
        catch (ExprException e) {
            return ExprError.generateError(ExprError.NUM);
        }                                

        BigDecimal value = ((ExprNumber) expression).decimalValue().setScale(2, BigDecimal.ROUND_HALF_UP);
        String text = NumberToText.convert(value);
        if (isCurrency) {
            text = NumberToText.convertToCurrency(text, value);
        }
        return new ExprNumberText(text, ((ExprNumber) expression).decimalValue());
    }
}
