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

    protected Expr evaluate(double lhs, double rhs) throws ExprException {
        return new ExprDouble(lhs * rhs);
    }

    public String toString() {
        return lhs + "*" + rhs;
    }
    
    @Override
    protected ExprError assertTypeLeft(Expr le, Expr re) throws ExprException {
        return assertType(le, ExprError.NUM, ExprType.Integer, ExprType.Double, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }

    @Override
    protected ExprError assertTypeRight(Expr le, Expr re) throws ExprException {
        return assertType(re, ExprError.NUM, ExprType.Integer, ExprType.Double, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }    
}