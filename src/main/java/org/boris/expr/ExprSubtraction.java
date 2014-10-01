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


public class ExprSubtraction extends AbstractMathematicalOperator
{
    public ExprSubtraction(Expr lhs, Expr rhs) {
        super(ExprType.Subtraction, lhs, rhs);
    }

    protected Expr evaluate(double lhs, double rhs) throws ExprException {
        return new ExprDouble(lhs - rhs);
    }

    public void validate() throws ExprException {
        if (rhs == null)
            throw new ExprException("RHS of operator missing");
    }

    public String toString() {
        if (lhs == null)
            return "-" + rhs;
        else
            return lhs + "-" + rhs;
    }
    
    @Override
    protected ExprError assertTypeLeft(Expr le) throws ExprException {
        return assertType(le, ExprError.NUM_OR_DATE, ExprType.Integer, ExprType.Double, ExprType.Date);
    }

    @Override
    protected ExprError assertTypeRight(Expr re) throws ExprException {
        return assertType(re, ExprError.NUM_OR_DATE, ExprType.Integer, ExprType.Double, ExprType.Date);
    }    
}
