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
        if (l instanceof ExprEvaluatable)
            l = ((ExprEvaluatable) lhs).evaluate(context);
        if (l instanceof ExprNumber) {
            // This will allow for any precision on the double, but not add ".0" to a whole number
            long longValue = (long) ((ExprNumber) l).doubleValue();
            double doubleValue = ((ExprNumber) l).doubleValue();
            if (longValue == doubleValue) {
                l = new ExprString(new Long(longValue).toString());
            }
            else {
                l = new ExprString(new Double(doubleValue).toString());
            }
        }
        Expr r = rhs;
        if (r instanceof ExprEvaluatable)
            r = ((ExprEvaluatable) rhs).evaluate(context);
        if (r instanceof ExprNumber)
            r = new ExprString(r.toString());

        if (l == null && r == null) {
            return new ExprString("");
        }
        else if (l != null && r != null && l.type.equals(ExprType.String) && r.type.equals(ExprType.String)) {
            return new ExprString(((ExprString) l).str + ((ExprString) r).str);
        }        
        else if (l == null && r != null && r.type.equals(ExprType.String)) {
            return new ExprString(((ExprString) r).str);
        }
        else if (r == null && l != null && l.type.equals(ExprType.String)) {
            return new ExprString(((ExprString) l).str);
        }
        throw new ExprException("Unexpected arguments for string concatenation");
    }

    public String toString() {
        return lhs + "&" + rhs;
    }
}
