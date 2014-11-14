package org.boris.expr.function.excel;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.boris.expr.Expr;
import org.boris.expr.ExprDate;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprString;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.function.FunctionValidationException;

public class PATTERN extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 2);

        Expr patternExpr = args[args.length - 1];
        if (patternExpr.getType() != ExprType.String) {
            throw new FunctionValidationException("Last argument must be a valid pattern");
        }
        
        String pattern = patternExpr.toString();
        
        ArrayList<String> argumentList = new ArrayList<>();
        
        for (int i=0; i<args.length-1; i++) {
            Expr argumentExpr = args[i];
            argumentExpr = evalArg(context, argumentExpr);
            
            if (argumentExpr.getType() == ExprType.Missing)
                argumentList.add("___");
            else if (argumentExpr instanceof ExprString) {
                argumentList.add(((ExprString) argumentExpr).str);
            }
            else if (argumentExpr.getType() == ExprType.Date) {
                argumentList.add(((ExprDate) argumentExpr).toString());
            }
            else if (argumentExpr instanceof ExprNumber) {
                argumentList.add(argumentExpr.toString());
            }
        }
       
        return new ExprString(MessageFormat.format(pattern, argumentList.toArray()));
    }
}
