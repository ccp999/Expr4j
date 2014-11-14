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

public class ExprAddition extends AbstractMathematicalOperator
{
    public ExprAddition(Expr lhs, Expr rhs) {
        super(ExprType.Addition, lhs, rhs);
    }
    
    protected Expr evaluate(ExprNumber lhs, ExprNumber rhs) throws ExprException {
        if (lhs.isDecimal() || rhs.isDecimal()) {
            return new ExprDecimal(lhs.decimalValue().add(rhs.decimalValue()));
        }
        else {
            return new ExprInteger(lhs.intValue() + rhs.intValue());
        }
    }

    public void validate() throws ExprException {
        if (lhs != null)
            lhs.validate();
        if (rhs == null)
            throw new ExprException("RHS of operator missing");
        rhs.validate();
    }

    public String toString() {
        if (lhs == null)
            return rhs.toString();
        else
            return lhs + "+" + rhs;
    }

    @Override
    protected ExprError assertTypeLeft(Expr le, Expr re) throws ExprException {
        if (re != null && re.type == ExprType.Date) {    
            return assertType(le, ExprError.NUM, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
        } else {
            return assertType(le, ExprError.NUM_OR_DATE, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Date, ExprType.Formatted, ExprType.NumberText);
        }        
    }

    @Override
    protected ExprError assertTypeRight(Expr le, Expr re) throws ExprException {
        if (le != null && le.type == ExprType.Date) {    
            return assertType(re, ExprError.NUM, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
        } else {
            return assertType(re, ExprError.NUM_OR_DATE, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Date, ExprType.Formatted, ExprType.NumberText);
        }        
    }

    @Override
    protected ExprNumber getDefaultValueForMissing() {
        return new ExprInteger(0);
    }
}
