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

import java.text.DecimalFormat;
import java.text.ParseException;


public class ExprFormatted extends ExprNumber
{
    public double value;
    public final String formattedValue;

    public ExprFormatted(String formattedValue) {               
        super(ExprType.Formatted);
        DecimalFormat format = new DecimalFormat();
        this.formattedValue = formattedValue;
        try {
            this.value = format.parse(formattedValue).doubleValue();
        } catch (ParseException e) {
           this.value = 0;
        }
    }

    public int intValue() {
        return (int) value;
    }

    public double doubleValue() {
        return value;
    }

    public String toString() {
        return this.formattedValue;
    }

    public int hashCode() {
        return (int) value * 100;
    }

    public boolean equals(Object obj) {
        return obj instanceof ExprFormatted &&
                Math.abs(value - ((ExprFormatted) obj).value) < 1.0e-10;
    }
}
