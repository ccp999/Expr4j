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

import org.boris.expr.util.NumberToText;


public class ExprNumberText extends ExprNumber
{
    public double value;
    public final String text;

    public ExprNumberText(String text) {               
        super(ExprType.NumberText);
        this.text = text;
        try {
            this.value = NumberToText.convert(text);
        } catch (Exception e) {
           this.value = 0;
        }
    }

    public ExprNumberText(String text, double number) {               
        super(ExprType.NumberText);
        this.text = text;
        this.value = number;
    }

    public int intValue() {
        return (int) value;
    }

    public double doubleValue() {
        return value;
    }

    public String toString() {
        return this.text;
    }

    public int hashCode() {
        return (int) value * 100;
    }

    public boolean equals(Object obj) {
        return obj instanceof ExprNumberText &&
                Math.abs(value - ((ExprNumberText) obj).value) < 1.0e-10;
    }
}
