package org.boris.expr.function.excel;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.function.FunctionValidationException;
import org.boris.expr.util.Counter;

public class AVERAGE extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 1);

        return average(context, args);
    }

    public static Expr average(IEvaluationContext context, Expr... args)
            throws ExprException {
        
        Counter counter = new Counter();
        counter.count = 0;
        counter.value = BigDecimal.ZERO;
        
        for (Expr a : args) {
            eval(context, a, counter, true);
        }
        
        if (counter.count == 0) {            
            if (args != null && args.length > 1 && args[1].type == ExprType.Variable) {
                return ExprError.generateError(ExprError.DIV0, args[1].toString());
            }
            
            return ExprError.DIV0;
        }

        return new ExprDecimal(counter.value.divide(new BigDecimal(counter.count), ExprDecimal.MATH_CONTEXT));
    }

    public static void eval(IEvaluationContext context, Expr a, Counter counter, boolean strict) throws ExprException {
        String variableName = getVariableName(a);
        
        if (a instanceof ExprEvaluatable) {
            a = ((ExprEvaluatable) a).evaluate(context);
        }
        if (a == null)
            return;

        if (a instanceof ExprMissing)
            return;

        validateEvalType(a, ExprError.generateError(ExprError.NUM), variableName, ExprType.Decimal, ExprType.Integer, ExprType.Array);

        if (a instanceof ExprDecimal || a instanceof ExprInteger) {
            ExprNumber number = ((ExprNumber) a);
            counter.value = counter.value.add(number.decimalValue());
            counter.count++;
            return;
        }

        if (a instanceof ExprArray) {
            ExprArray arr = (ExprArray) a;
            int rows = arr.rows();
            int cols = arr.columns();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    eval(context, arr.get(i, j), counter, false);
                }
            }

            return;
        }

        throw new FunctionValidationException("Unexpected argument for AVERAGE: " + a);
    }
}
