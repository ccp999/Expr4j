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

import org.boris.expr.util.NumberToText;


public class ExprNumberText extends ExprDecimal
{
    public final String text;

    public ExprNumberText(String text) {               
        super(ExprType.NumberText);
        this.text = text;
        try {
            setValue(Double.toString(NumberToText.convert(text)));
        } catch (Exception e) {
           setValue("0");
        }
    }

    public ExprNumberText(String text, BigDecimal number) {               
        super(ExprType.NumberText, number);
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
