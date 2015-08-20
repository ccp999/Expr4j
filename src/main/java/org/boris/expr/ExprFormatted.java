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

public class ExprFormatted extends ExprDecimal
{
    public final String formattedValue;
    
    public ExprFormatted(DecimalFormat format, ExprNumber expression) {       
        super(ExprType.Formatted);

        if (expression.isDecimal()) {
            this.formattedValue = format.format(expression.decimalValue());    
            this.setValue(expression.decimalValue());
        }
        else {
            this.formattedValue = format.format(expression.intValue());
            this.setValue(Integer.toString(expression.intValue()));
        }        
    }
        
    public String toString() {
        return this.formattedValue;
    }
}
