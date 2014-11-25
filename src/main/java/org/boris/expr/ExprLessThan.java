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

public class ExprLessThan extends AbstractComparisonOperator
{
    public ExprLessThan(Expr lhs, Expr rhs) {
        super(ExprType.LessThan, lhs, rhs);
    }

    public Expr evaluate(IEvaluationContext context) throws ExprException {
        Expr result = compare(context);
        if (result.getType() == ExprType.Missing) {
            return bool(false);
        }
        else if (result.getType() == ExprType.Integer) {
            return bool(((ExprInteger) result).intValue() < 0);
        }
        else {
            return bool(false);
        }        
    }

    public String toString() {
        return lhs + "<" + rhs;
    }
}