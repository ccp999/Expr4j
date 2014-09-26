package org.boris.expr;

public class Operands {
    Expr leftOperand;
    Expr rightOperand;
    
    public Operands(Expr leftOperand, Expr rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
    
    public Expr getLeftOperand() {
        return leftOperand;
    }
    public void setLeftOperand(Expr leftOperand) {
        this.leftOperand = leftOperand;
    }
    public Expr getRightOperand() {
        return rightOperand;
    }
    public void setRightOperand(Expr rightOperand) {
        this.rightOperand = rightOperand;
    }    
}
