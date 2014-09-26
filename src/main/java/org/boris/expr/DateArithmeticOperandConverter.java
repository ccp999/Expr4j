package org.boris.expr;

public class DateArithmeticOperandConverter implements IOperandConversionVisitor {
    private static int MILLISECONDS_IN_DAY = (1000 * 60 * 60 * 24);
    
    @Override
    public void convertOperands(Operands operands) {
        // If left operand is a date and right is a double, we need to convert the right operand
        // to milliseconds because we are adding days to a date.
        if (operands.getLeftOperand().type == ExprType.Date && operands.getRightOperand().type == ExprType.Double) {
            ExprDouble exprDouble = (ExprDouble) operands.getRightOperand();
            operands.setRightOperand(new ExprDouble(exprDouble.doubleValue() * MILLISECONDS_IN_DAY));
        }
        // If both operands are a date, the result should be in days so convert the operands from
        // milliseconds to days.
        else if (operands.getLeftOperand().type == ExprType.Date
                        && operands.getRightOperand().type == ExprType.Date) {
            
            ExprDate lExprDate = (ExprDate) operands.getLeftOperand();
            ExprDate rExprDate = (ExprDate) operands.getRightOperand();
            
            operands.setLeftOperand(new ExprDouble(lExprDate.doubleValue() / MILLISECONDS_IN_DAY));
            operands.setRightOperand(new ExprDouble(rExprDate.doubleValue() / MILLISECONDS_IN_DAY));
        }
    }
}
