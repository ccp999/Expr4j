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

public class ExprMultiplication extends AbstractMathematicalOperator
{
    public ExprMultiplication(Expr lhs, Expr rhs) {
        super(ExprType.Multiplication, lhs, rhs);
    }

    protected Expr evaluate(ExprNumber lhs, ExprNumber rhs) throws ExprException {
        return new ExprDecimal(lhs.decimalValue().multiply(rhs.decimalValue()));
    }
    
    public String toString() {
        return lhs + "*" + rhs;
    }
    
    @Override
    protected ExprError assertTypeLeft(Expr le, Expr re) throws ExprException {
        return assertType(le, ExprError.NUM, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }

    @Override
    protected ExprError assertTypeRight(Expr le, Expr re) throws ExprException {
        return assertType(re, ExprError.NUM, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }

    @Override
    protected ExprNumber getDefaultValueForMissing() {
        return new ExprInteger(0);
    }    
}