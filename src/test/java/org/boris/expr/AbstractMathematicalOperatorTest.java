package org.boris.expr;

import java.io.IOException;
import java.math.BigDecimal;

import junit.framework.TestCase;

import org.boris.expr.util.GraphCycleException;

public class AbstractMathematicalOperatorTest extends TestCase {
    
    public void testMissingOperands() throws GraphCycleException, IOException, ExprException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A1+A2");
        
        b.addVariable("A1", new ExprMissing());        
        b.addVariable("A2", new ExprMissing());
        
        Expr result = ((ExprEvaluatable) expr).evaluate(b);
        
        assertEquals(result.type, ExprType.Missing);
    } 
    
    public void testOneMissingOperands() throws GraphCycleException, IOException, ExprException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr exprAdd = b.parse("A1+A2");
        Expr exprMultiply = b.parse("A1*A2");
        Expr exprDivide = b.parse("A2/A1");
        Expr exprSubtract = b.parse("A2-A1");
        
        b.addVariable("A1", new ExprInteger(10));        
        b.addVariable("A2", new ExprMissing());
        
        Expr resultAdd = ((ExprEvaluatable) exprAdd).evaluate(b);
        Expr resultMultiply = ((ExprEvaluatable) exprMultiply).evaluate(b);
        Expr resultDivide = ((ExprEvaluatable) exprDivide).evaluate(b);
        Expr resultSubtract = ((ExprEvaluatable) exprSubtract).evaluate(b);
        
        assertEquals(resultAdd, new ExprDecimal(new BigDecimal(10)));
        assertEquals(resultMultiply, new ExprDecimal(BigDecimal.ZERO));
        assertEquals(resultDivide, new ExprDecimal(BigDecimal.ZERO));
        assertEquals(resultSubtract, new ExprDecimal(new BigDecimal(10).negate()));
    }     
    
    public void testCalculationError() throws GraphCycleException, IOException, ExprException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr exprAdd = b.parse("A1+A3");
        Expr exprMultiply = b.parse("A1*A3");
        Expr exprDivide = b.parse("A1/A2");
        Expr exprSubtract = b.parse("A2-A3");
        
        b.addVariable("A1", new ExprInteger(10));        
        b.addVariable("A2", new ExprMissing());
        b.addVariable("A3", new ExprString("abc"));
        
        int exceptionCount = 0;
        
        try {
            ((ExprEvaluatable) exprAdd).evaluate(b);
        }
        catch (ExprEvaluationException e) {
            assertEquals("A3", e.getError().getVariableName());
            assertEquals("abc", e.getError().getValue());
            assertEquals("#NUM_OR_DATE!", e.getError().getErrType());
            exceptionCount++;
        }
        try {
            ((ExprEvaluatable) exprMultiply).evaluate(b);
        }
        catch (ExprEvaluationException e) {
            assertEquals("A3", e.getError().getVariableName());
            assertEquals("abc", e.getError().getValue());
            assertEquals("#NUM!", e.getError().getErrType());
            exceptionCount++;
        }
        try {
            ((ExprEvaluatable) exprSubtract).evaluate(b);
        }
        catch (ExprEvaluationException e) {
            assertEquals("A3", e.getError().getVariableName());
            assertEquals("abc", e.getError().getValue());
            assertEquals("#NUM_OR_DATE!", e.getError().getErrType());
            exceptionCount++;
        }
        
        Expr resultDivide = ((ExprEvaluatable) exprDivide).evaluate(b);
        assertEquals("A2", ((ExprError) resultDivide).getVariableName());
        assertEquals("#DIV/0!", ((ExprError) resultDivide).getErrType());
        assertEquals(3, exceptionCount);
    }
    
    public void testCancelEvalOnMissing() throws GraphCycleException, IOException, ExprException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A1+A2");
        
        Expr operand = new ExprMissing();
        operand.cancelEvalOnMissing = true;
        
        b.addVariable("A1", new ExprInteger(10));        
        b.addVariable("A2", operand);
        
        Expr result = ((ExprEvaluatable) expr).evaluate(b);
        assertEquals(result.type, ExprType.Missing);
    }      
}
