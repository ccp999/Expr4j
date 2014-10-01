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

public class ExprDivision extends AbstractMathematicalOperator
{
    public ExprDivision(Expr lhs, Expr rhs) {
        super(ExprType.Division, lhs, rhs);
    }

    protected Expr evaluate(double lhs, double rhs) throws ExprException {
        if (rhs == 0.) {
            if (this.rhs != null && this.rhs.type == ExprType.Variable) {
                return ExprError.generateError(ExprError.DIV0, this.rhs.toString());
            }
            
            return ExprError.DIV0;
        }
        return new ExprDouble(lhs / rhs);
    }

    public String toString() {
        return lhs + "/" + rhs;
    }

    @Override
    protected ExprError assertTypeLeft(Expr le) throws ExprException {
        return assertType(le, ExprError.NUM, ExprType.Integer, ExprType.Double);
    }

    @Override
    protected ExprError assertTypeRight(Expr re) throws ExprException {
        return assertType(re, ExprError.NUM, ExprType.Integer, ExprType.Double);
    }
}