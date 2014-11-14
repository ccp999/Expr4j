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

    protected Expr evaluate(ExprNumber lhs, ExprNumber rhs) throws ExprException {
        if (lhs.isDecimal() || rhs.isDecimal()) {
            return new ExprDecimal(lhs.decimalValue().subtract(rhs.decimalValue()));
        }
        else {
            return new ExprInteger(lhs.intValue() - rhs.intValue());
        }
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
    protected ExprError assertTypeLeft(Expr le, Expr re) throws ExprException {
        return assertType(le, ExprError.NUM_OR_DATE, ExprType.Integer, ExprType.Decimal, ExprType.Date, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }

    @Override
    protected ExprError assertTypeRight(Expr le, Expr re) throws ExprException {
        return assertType(re, ExprError.NUM_OR_DATE, ExprType.Integer, ExprType.Decimal, ExprType.Date, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }

    @Override
    protected ExprNumber getDefaultValueForMissing() {
        return new ExprInteger(0);
    }
}
