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


public abstract class AbstractMathematicalOperator extends
        AbstractBinaryOperator
{
    public AbstractMathematicalOperator(ExprType type, Expr lhs, Expr rhs) {
        super(type, lhs, rhs);
    }

    protected ExprNumber evaluateExpr(Expr e, IEvaluationContext context)
            throws ExprException {
        e = eval(e, context);
        if (e == null)
            return new ExprInteger(0);
        if (e.type == ExprType.Missing)
            return getDefaultValueForMissing();
        
        return ((ExprNumber) e);
    }

    public Expr evaluate(IEvaluationContext context) throws ExprException {
        Expr l = eval(lhs, context);
        if (l instanceof ExprError) {
            return l;
        }
        Expr r = eval(rhs, context);
        if (r instanceof ExprError) {
            return r;
        }

        boolean lMissing = isMissing(l);
        boolean rMissing = isMissing(r);
        
        // If both inputs are missing, evaluate to missing.
        // If either input specifies to cancel evaluation when it is missing, evaluate to missing
        if (lMissing & rMissing) {
            return new ExprMissing();
        }
        else if ((l != null && lMissing && l.isCancelEvalOnMissing()) || (r != null && rMissing && r.isCancelEvalOnMissing())) {
            return new ExprMissing();
        }
        
        if (l != null && l.getType() == ExprType.Error) {
            throw new ExprEvaluationException((ExprError) l);
        }
        else if (r != null && r.getType() == ExprType.Error) {
            throw new ExprEvaluationException((ExprError) r);
        }
        
        ExprError leftAssertError = assertTypeLeft(l, r);
        ExprError rightAssertError= assertTypeRight(l, r);
        
        inspectAssertError(leftAssertError, l, this.lhs);
        inspectAssertError(rightAssertError, r, this.rhs);
        
        Operands operands = new Operands(l, r);
        
        if (context.getOperandConversionVisitor() != null) {            
            context.getOperandConversionVisitor().convertOperands(operands);
        }
        
        if (l == null && r == null) {
            return new ExprMissing();
        }
        else {
            return evaluate(evaluateExpr(operands.getLeftOperand(), context),
                    evaluateExpr(operands.getRightOperand(), context));
        }
    }

    private boolean isMissing(Expr expr) {
        if (expr == null) {
            return true;
        }
        else if (expr.getType() == ExprType.Missing) {
            return true;
        }
        
        return false;
    }
    
    private void inspectAssertError(ExprError error, Expr valueExpr, Expr variableExpr) throws ExprEvaluationException {
        if (error != null) {
            if (variableExpr != null && variableExpr.type == ExprType.Variable) {
                error.setVariableName(variableExpr.toString());
            }
            
            if (valueExpr != null) {
                error.setValue(valueExpr.toString());
            }            

            throw new ExprEvaluationException(error);
        }
    }
    
    protected abstract ExprNumber getDefaultValueForMissing();
    
    protected abstract Expr evaluate(ExprNumber lhs, ExprNumber rhs)
            throws ExprException;

    protected abstract ExprError assertTypeLeft(Expr le, Expr re)
            throws ExprException;
    
    protected abstract ExprError assertTypeRight(Expr le, Expr re)
            throws ExprException;
    
    protected ExprError assertType(Expr expr, Expr generatedErrorType, ExprType... types) throws ExprException {
        if (expr != null && !expr.evaluatable) {
            try {
                ExprTypes.assertType(expr, types);
            }
            catch (ExprException e) {
                return ExprError.generateError(generatedErrorType);
            }                        
        }
        
        return null;
    }
    
}
