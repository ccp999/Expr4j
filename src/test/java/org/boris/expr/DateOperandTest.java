package org.boris.expr;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.boris.expr.util.GraphCycleException;

public class DateOperandTest extends TestCase {
    
    public void testDateAdditionInvalid() throws IOException, ExprException, GraphCycleException, ParseException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A1+A2");
                
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        b.addVariable("A1", new ExprDate(format.parse("01/01/2014")));
        b.addVariable("A2", new ExprDate(format.parse("01/02/2014")));

        boolean isException = false;
        
        try {
            ((ExprEvaluatable) expr).evaluate(b);
        }
        catch (ExprEvaluationException e) {
            assertEquals("#NUM!", e.getError().getErrType());
            assertEquals("A1", e.getError().getVariableName());
            assertEquals("01/01/2014", e.getError().getValue());
            isException = true;
        }
        
        assertTrue(isException);
    }
    
    public void testDateAddition() throws IOException, ExprException, GraphCycleException, ParseException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        b.setOperandConversionVisitor(new DateArithmeticOperandConverter());
        
        Expr expr = b.parse("A1+A2");
                        
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        b.addVariable("A1", new ExprDate(format.parse("01/01/2014")));
        b.addVariable("A2", new ExprInteger(2));

        Expr result = ((ExprEvaluatable) expr).evaluate(b);
        
        long resultLong = ((ExprNumber) result).decimalValue().longValue();
        assertEquals("01/03/2014", format.format(new Date(resultLong)));
    }
    
    public void testDateSubtractNumber() throws IOException, ExprException, GraphCycleException, ParseException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        b.setOperandConversionVisitor(new DateArithmeticOperandConverter());
        
        Expr expr = b.parse("A1-A2");
                        
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        b.addVariable("A1", new ExprDate(format.parse("01/10/2014")));
        b.addVariable("A2", new ExprInteger(2));

        Expr result = ((ExprEvaluatable) expr).evaluate(b);
        
        long resultLong = ((ExprNumber) result).decimalValue().longValue();
        assertEquals("01/08/2014", format.format(new Date(resultLong)));
    }    
    
    public void testDateMultiplyInvalid() throws IOException, ExprException, GraphCycleException, ParseException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A1*A2");
                
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        b.addVariable("A1", new ExprDate(format.parse("01/01/2014")));
        b.addVariable("A2", new ExprDate(format.parse("01/02/2014")));

        boolean isException = false;
        
        try {
            ((ExprEvaluatable) expr).evaluate(b);
        }
        catch (ExprEvaluationException e) {
            assertEquals("#NUM!", e.getError().getErrType());
            assertEquals("A1", e.getError().getVariableName());
            assertEquals("01/01/2014", e.getError().getValue());
            isException = true;
        }
        
        assertTrue(isException);
    }
    
    public void testDateDivideInvalid() throws IOException, ExprException, GraphCycleException, ParseException {
        BasicEvaluationCallback b = new BasicEvaluationCallback();
        Expr expr = b.parse("A1/A2");
                
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        b.addVariable("A1", new ExprDate(format.parse("01/01/2014")));
        b.addVariable("A2", new ExprDate(format.parse("01/02/2014")));

        boolean isException = false;
        
        try {
            ((ExprEvaluatable) expr).evaluate(b);
        }
        catch (ExprEvaluationException e) {
            assertEquals("#NUM!", e.getError().getErrType());
            assertEquals("A1", e.getError().getVariableName());
            assertEquals("01/01/2014", e.getError().getValue());
            isException = true;
        }
        
        assertTrue(isException);
    }     
}
