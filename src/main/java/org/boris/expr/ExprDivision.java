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

import java.math.BigDecimal;

public class ExprDivision extends AbstractMathematicalOperator
{
    public ExprDivision(Expr lhs, Expr rhs) {
        super(ExprType.Division, lhs, rhs);
    }

    protected Expr evaluate(ExprNumber lhs, ExprNumber rhs) throws ExprException {
        if (rhs.decimalValue().compareTo(BigDecimal.ZERO) == 0) {         
            return ExprError.generateError(ExprError.DIV0, this.rhs.toString());            
        }
        
        return new ExprDecimal(lhs.decimalValue().divide(rhs.decimalValue(), ExprDecimal.MATH_CONTEXT));
    }    

    public String toString() {
        return lhs + "/" + rhs;
    }

    @Override
    protected ExprError assertTypeLeft(Expr le, Expr re) throws ExprException {
        return assertType(le, ExprError.NUM, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }

    @Override
    protected ExprError assertTypeRight(Expr le, Expr re) throws ExprException {
        return assertType(re, ExprError.NUM, ExprType.Integer, ExprType.Decimal, ExprType.Missing, ExprType.Formatted, ExprType.NumberText);
    }

    protected ExprNumber getDefaultValueForMissing() {
        return new ExprInteger(0);
    }   
}