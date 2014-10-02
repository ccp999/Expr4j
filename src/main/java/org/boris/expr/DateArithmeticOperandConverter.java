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

public class DateArithmeticOperandConverter implements IOperandConversionVisitor {
    private static int MILLISECONDS_IN_DAY = (1000 * 60 * 60 * 24);
    
    @Override
    public void convertOperands(Operands operands) {
        // If both operands are a date, the result should be in days so convert the operands from
        // milliseconds to days.
        if (operands.getLeftOperand() != null && operands.getRightOperand() != null
                && operands.getLeftOperand().type == ExprType.Date && operands.getRightOperand().type == ExprType.Date) {
            
            ExprDate lExprDate = (ExprDate) operands.getLeftOperand();
            ExprDate rExprDate = (ExprDate) operands.getRightOperand();
            
            operands.setLeftOperand(new ExprDouble(lExprDate.doubleValue() / MILLISECONDS_IN_DAY));
            operands.setRightOperand(new ExprDouble(rExprDate.doubleValue() / MILLISECONDS_IN_DAY));
        }        
        // If left operand is a date and right is a double, we need to convert the right operand
        // to milliseconds because we are adding days to a date.
        else if (operands.getLeftOperand() != null && operands.getLeftOperand().type == ExprType.Date
                && operands.getRightOperand() instanceof ExprNumber) {
            ExprNumber exprNumber = (ExprNumber) operands.getRightOperand();
            operands.setRightOperand(new ExprDouble(exprNumber.doubleValue() * MILLISECONDS_IN_DAY));
        }
    }
}
