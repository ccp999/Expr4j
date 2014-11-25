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

public class ExcelCompatTest extends TH
{
    public void test1() throws Exception {
        assertResult("-1.3E1/3", new BigDecimal(1.3E1).divide(new BigDecimal(3), ExprDecimal.MATH_CONTEXT).negate());
        assertResult("1.3E-4/3", new BigDecimal("0.00013").divide(new BigDecimal(3), ExprDecimal.MATH_CONTEXT));
        assertResult("1300000000000000/3", new BigDecimal("1300000000000000").divide(new BigDecimal(3), ExprDecimal.MATH_CONTEXT));
        assertResult(
                "-10E-1/3.1E2*4E3/3E4",
                BigDecimal.ONE.negate().divide(new BigDecimal("310"), ExprDecimal.MATH_CONTEXT)
                        .multiply(new BigDecimal("4000")).divide(new BigDecimal("30000"), ExprDecimal.MATH_CONTEXT));        
    }

    public void testReferences() throws Exception {
        assertResult("'Quotes Needed Here &#$@'!A1", new ExprVariable(
                "'Quotes Needed Here &#$@'!A1"));
    }

/*    public void testSumIf() throws Exception {
        assertResult("SUMIF(A1:A5,\">4000\",B1:B5)", 0.);
    }*/
}