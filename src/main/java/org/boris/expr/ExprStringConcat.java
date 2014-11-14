/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.expr;

public class ExprStringConcat extends AbstractBinaryOperator
{
    public ExprStringConcat(Expr lhs, Expr rhs) {
        super(ExprType.StringConcat, lhs, rhs);
    }

    public Expr evaluate(IEvaluationContext context) throws ExprException {
        Expr l = lhs;
        if (l instanceof ExprEvaluatable) {
            l = ((ExprEvaluatable) lhs).evaluate(context);
        }
    
        Expr r = rhs;
        if (r instanceof ExprEvaluatable) {
            r = ((ExprEvaluatable) rhs).evaluate(context);
        }

        if (l == null && r == null) {
            return new ExprString("");
        }
        else if (l != null && r != null) {
            return new ExprString(l.toString() + r.toString());
        }        
        else if (l == null && r != null) {
            return new ExprString(r.toString());
        }
        else if (r == null && l != null) {
            return new ExprString(l.toString());
        }
        
        throw new ExprException("Unexpected arguments for string concatenation");
    }

    public String toString() {
        return lhs + "&" + rhs;
    }
}
