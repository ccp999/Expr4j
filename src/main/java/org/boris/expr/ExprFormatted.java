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



public class ExprFormatted extends ExprDecimal
{
    public final String formattedValue;
    public ExprFormatted(String formattedValue) {
        
        super(ExprType.Formatted);
        
        DecimalFormat format = new DecimalFormat();
        this.formattedValue = formattedValue;
        try {
            this.setValue(Double.toString(format.parse(formattedValue).doubleValue()));
        } catch (ParseException e) {
           this.setValue("0");
        }       
    }
    
    public String toString() {
        return this.formattedValue;
    }
}
