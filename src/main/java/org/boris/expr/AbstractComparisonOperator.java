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


public abstract class AbstractComparisonOperator extends AbstractBinaryOperator
{
    public AbstractComparisonOperator(ExprType type, Expr lhs, Expr rhs) {
        super(type, lhs, rhs);
    }

    protected Expr compare(IEvaluationContext context) throws ExprException {
        Expr l = eval(lhs, context);
        Expr r = eval(rhs, context);

        if (l.getType() == ExprType.Missing || r.getType() == ExprType.Missing) {
            return new ExprMissing();
        }
        else if (l instanceof ExprString || r instanceof ExprString) {
            String lStr = l.toString();
            String rStr = r.toString();
            
            return new ExprInteger(lStr.compareTo(rStr));
        }

        if (l instanceof ExprDecimal && r instanceof ExprDecimal) {
            int result =  ((ExprDecimal) l).decimalValue().compareTo( ((ExprDecimal) r).decimalValue());
            return new ExprInteger(result);
        }

        else if (l instanceof ExprNumber && r instanceof ExprNumber) {
            int result = (((ExprNumber) l).intValue() - ((ExprNumber) r).intValue());
            return new ExprInteger(result);
        }

        return new ExprInteger(0);
    }
}
