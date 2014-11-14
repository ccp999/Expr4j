/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.expr.function;

import java.math.BigDecimal;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprBoolean;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprError;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprEvaluationException;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprString;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.IExprFunction;

public abstract class AbstractFunction implements IExprFunction
{
    public boolean isVolatile() {
        return false;
    }

    protected void assertArgCount(Expr[] args, int count) throws ExprException {
        if (args == null && count != 0) {
            throw new FunctionValidationException(getClass().getSimpleName() +
                    " function takes no arguments");
        }

        if (args.length != count)
            throw new FunctionValidationException(getClass().getSimpleName() +
                    " function takes " + count + " arguments");
    }

    protected void assertArgTypes(Expr[] args, ExprType... types)
            throws ExprException {
        assertArgCount(args, types.length);
        for (int i = 0; i < args.length; i++) {
            if (!args[i].type.equals(types[i])) {
                throw new FunctionValidationException("Invalid argument (" + i + 1 +
                        ") to function: " + getClass().getSimpleName());
            }
        }
    }

    protected double asDouble(IEvaluationContext context, Expr arg, boolean strict) throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }
        if (arg instanceof ExprNumber) {
            return ((ExprNumber) arg).decimalValue().doubleValue();
        }
        if (!strict) return 0;
        throw new FunctionValidationException("Invalid argument type for function " + getClass().getSimpleName());
    }
    
    protected double asDouble(IEvaluationContext context, ExprArray knownY, int index) throws ExprException {
        if (index < 0 || index >= knownY.length()) return 0;

        Expr e = knownY.get(index);
        return asDouble(context, e, false);
    }
    
    protected BigDecimal asDecimal(IEvaluationContext context, Expr arg,
            boolean strict) throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }
        if (arg instanceof ExprNumber) {
            return ((ExprNumber) arg).decimalValue();
        }
        if (!strict)
            return BigDecimal.ZERO;
        throw new FunctionValidationException("Invalid argument type for function " +
                getClass().getSimpleName());
    }

    protected int asInteger(IEvaluationContext context, Expr arg, boolean strict)
            throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }
        if (arg instanceof ExprNumber) {
            return ((ExprNumber) arg).intValue();
        }
        if (!strict)
            return 0;
        throw new FunctionValidationException("Invalid argument type for function " +
                getClass().getSimpleName());
    }

    protected boolean asBoolean(IEvaluationContext context, Expr arg,
            boolean strict) throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }
        if (arg instanceof ExprNumber) {
            return ((ExprNumber) arg).booleanValue();
        }
        if (!strict)
            return false;
        throw new FunctionValidationException("Invalid argument type for function " +
                getClass().getSimpleName());
    }

    protected String asString(IEvaluationContext context, Expr arg,
            boolean strict) throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            arg = ((ExprEvaluatable) arg).evaluate(context);
        }
        if (arg instanceof ExprString) {
            return ((ExprString) arg).str;
        }
        if (!strict) {
            if (arg instanceof ExprNumber) {
                return arg.toString();
            }
            return "";
        }
        throw new FunctionValidationException("Invalid argument type for function " +
                getClass().getSimpleName());
    }

    protected void assertArgType(Expr expr, ExprType type) throws ExprException {
        if (expr == null) {
            if (type != null)
                throw new FunctionValidationException("Invalid empty argument for function " +
                        getClass().getSimpleName());

        } else {
            if (!expr.type.equals(type)) {
                throw new FunctionValidationException("Invalid argument type for function " +
                        getClass().getSimpleName());
            }
        }
    }

    protected void assertArgCount(Expr[] args, int min, int max)
            throws ExprException {
        assertMinArgCount(args, min);
        assertMaxArgCount(args, max);
    }

    protected void assertMinArgCount(Expr[] args, int count)
            throws ExprException {
        if (args.length < count)
            throw new FunctionValidationException("Too few arguments to function " +
                    getClass().getSimpleName());
    }

    protected void assertMaxArgCount(Expr[] args, int count)
            throws ExprException {
        if (args.length > count)
            throw new FunctionValidationException("Too many arguments to function " +
                    getClass().getSimpleName());
    }

    public static Expr evalArg(IEvaluationContext context, Expr arg)
            throws ExprException {
        if (arg instanceof ExprEvaluatable) {
            return ((ExprEvaluatable) arg).evaluate(context);
        }
        return arg;
    }

    protected int getLength(Expr range) {
        if (range instanceof ExprArray) {
            return ((ExprArray) range).length();
        } else {
            return 1;
        }
    }

    protected Expr get(Expr range, int index) {
        if (range instanceof ExprArray) {
            ExprArray a = (ExprArray) range;
            if (index >= 0 && index < a.length()) {
                return a.get(index);
            }
        } else if (index == 0) {
            return range;
        }

        return ExprMissing.MISSING;
    }

    protected ExprBoolean bool(boolean bool) {
        return bool ? ExprBoolean.TRUE : ExprBoolean.FALSE;
    }

    protected BigDecimal asDecimal(IEvaluationContext context, ExprArray knownY,
            int index) throws ExprException {
        if (index < 0 || index >= knownY.length())
            return new BigDecimal("0");

        Expr e = knownY.get(index);
        return asDecimal(context, e, false);
    }

    protected boolean isOneOf(Expr expr, ExprType... types) {
        for (ExprType t : types) {
            if (expr.type.equals(t))
                return true;
        }
        return false;
    }

    protected ExprArray asArray(IEvaluationContext context, Expr expr,
            boolean strict) throws ExprException {
        if (expr instanceof ExprEvaluatable) {
            expr = ((ExprEvaluatable) expr).evaluate(context);
        }

        if (expr instanceof ExprArray) {
            return (ExprArray) expr;
        }

        if (strict)
            throw new FunctionValidationException("Argument not an array for function: " +
                    getClass().getSimpleName());

        ExprArray ea = new ExprArray(1, 1);
        ea.set(0, expr);
        return ea;
    }

    protected static void validateEvalType(Expr arg, ExprError validationError, String variableName, ExprType... validTypes)
            throws ExprEvaluationException {
        
        if (arg == null) {
            return;
        }
        else if (arg.type == ExprType.Error) {
            throw new ExprEvaluationException((ExprError) arg);
        }
        else {
            for (ExprType validType : validTypes) {
                if (arg.getType() == validType) {
                    return;
                }
            }
        }
        
        validationError.setValue(arg.toString());
        validationError.setVariableName(variableName);

        throw new ExprEvaluationException(validationError);        
    }
    
    public static String getVariableName(Expr arg) {
        if (arg != null && arg.type == ExprType.Variable) {
            return arg.toString();
        }
        
        return null;
    }
    
    protected boolean isNumber(Expr x) {
        return x instanceof ExprDecimal || x instanceof ExprInteger;
    }
}
