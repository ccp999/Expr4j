package org.boris.expr.function.excel;

import org.boris.expr.Expr;
import org.boris.expr.ExprDate;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.ExprString;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;
import org.boris.expr.function.FunctionValidationException;

public class JOIN extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args)
            throws ExprException {
        assertMinArgCount(args, 2);
        StringBuilder sb = new StringBuilder();
        Expr delimiterExpr = args[args.length - 1];
        if (delimiterExpr.getType() != ExprType.String) {
            throw new FunctionValidationException("Last argument must be a valid delimiter");
        }
        
        String delimiter = delimiterExpr.toString();
        
        for (int i=0; i<args.length-1; i++) {
            Expr a = args[i];
            a = evalArg(context, a);
            
            if (a != null && a.getType() != ExprType.Missing) {
                if (sb.length() > 0) {
                    sb.append(delimiter);
                }
                
                if (a instanceof ExprString) {
                    sb.append(((ExprString) a).str);
                }
                else if (a.getType() == ExprType.Date) {
                    sb.append(((ExprDate) a).toString());
                }
                else if (a instanceof ExprNumber) {                     
                    sb.append(a.toString());
                }
            }
        }
        
        return new ExprString(sb.toString());
    }
}
