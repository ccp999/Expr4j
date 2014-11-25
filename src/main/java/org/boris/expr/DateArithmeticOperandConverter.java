/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Jason Steinbrunner
 *******************************************************************************/
package org.boris.expr;

import java.math.BigDecimal;

public class DateArithmeticOperandConverter implements IOperandConversionVisitor {
    private static BigDecimal MILLISECONDS_IN_DAY = new BigDecimal(Integer.toString(1000 * 60 * 60 * 24));
    
    @Override
    public void convertOperands(Operands operands) {
        // If both operands are a date, the result should be in days so convert the operands from
        // milliseconds to days.
        if (operands.getLeftOperand() != null && operands.getRightOperand() != null
                && operands.getLeftOperand().type == ExprType.Date && operands.getRightOperand().type == ExprType.Date) {
            
            ExprDate lExprDate = (ExprDate) operands.getLeftOperand();
            ExprDate rExprDate = (ExprDate) operands.getRightOperand();
            
            operands.setLeftOperand(new ExprDecimal(lExprDate.decimalValue().divide(MILLISECONDS_IN_DAY, ExprDecimal.MATH_CONTEXT).setScale(0, java.math.RoundingMode.DOWN)));
            operands.setRightOperand(new ExprDecimal(rExprDate.decimalValue().divide(MILLISECONDS_IN_DAY, ExprDecimal.MATH_CONTEXT).setScale(0, java.math.RoundingMode.DOWN)));
        }        
        // If left operand is a date and right is a decimal, we need to convert the right operand
        // to milliseconds because we are adding days to a date.7
        else if (isDate(operands.getLeftOperand()) && isNumber(operands.getRightOperand())) {
            ExprNumber exprNumber = (ExprNumber) operands.getRightOperand();
            operands.setRightOperand(new ExprDecimal(exprNumber.decimalValue().multiply(MILLISECONDS_IN_DAY)));
        }
        else if (isDate(operands.getRightOperand()) && isNumber(operands.getLeftOperand())) {
            ExprNumber exprNumber = (ExprNumber) operands.getLeftOperand();
            operands.setLeftOperand(new ExprDecimal(exprNumber.decimalValue().multiply(MILLISECONDS_IN_DAY)));
        }
    }
    
    private boolean isDate(Expr expr) {
        if (expr != null && expr.type == ExprType.Date) {
            return true;
        }
        
        return false;
    }
    
    private boolean isNumber(Expr expr) {
        if (expr != null && expr instanceof ExprNumber) {
            return true;
        }
        
        return false;
    }    
}
