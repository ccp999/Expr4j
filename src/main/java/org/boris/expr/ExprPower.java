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

public class ExprPower extends AbstractMathematicalOperator
{
    public ExprPower(Expr lhs, Expr rhs) {
        super(ExprType.Power, lhs, rhs);
    }

    protected Expr evaluate(double lhs, double rhs) throws ExprException {
        return new ExprDouble(Math.pow(lhs, rhs));
    }

    public String toString() {
        return lhs + "^" + rhs;
    }

    @Override
    protected ExprError assertTypeLeft(Expr le, Expr re) throws ExprException {
        return assertType(le, ExprError.NUM, ExprType.Integer, ExprType.Double, ExprType.Formatted);
    }

    @Override
    protected ExprError assertTypeRight(Expr le, Expr re) throws ExprException {
        return assertType(re, ExprError.NUM, ExprType.Integer, ExprType.Double, ExprType.Formatted);
    }      
}