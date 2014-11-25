package org.boris.expr;

import java.io.IOException;
import java.math.BigDecimal;

import junit.framework.TestCase;

import org.boris.expr.util.GraphCycleException;

public class BasicEvaluationCallBackTest extends TestCase {
    
    public void testCyclicDependency() throws GraphCycleException, IOException, ExprException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A2+1");
        
        b.addVariable("A1", expr);
        b.addVariable("A2", b.parse("A3+1"));
        b.addVariable("A3", b.parse("A1+1"));
        
        boolean isException = false;
        try {
            ((ExprEvaluatable) expr).evaluate(b);
        }
        catch (GraphCycleException e) {
            isException = true;
        }
        
        assertTrue(isException);
    }
    
    public void testTestOperation() throws GraphCycleException, IOException, ExprException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A1+1");
        
        b.addVariable("A1", new ExprInteger(10));        
        Expr result = ((ExprEvaluatable) expr).evaluate(b);
        
        assertEquals(result, new ExprDecimal(new BigDecimal(11)));
    }    
    
    public void testMissingOperand() throws GraphCycleException, IOException, ExprException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A1+A2");
        
        b.addVariable("A1", new ExprInteger(10));        
        Expr result = ((ExprEvaluatable) expr).evaluate(b);
        
        assertEquals("#NAME?", ((ExprError) result).getErrType());
    }      
}
