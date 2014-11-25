/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *     Jason Steinbrunner
 *******************************************************************************/
package org.boris.expr;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExprDate extends ExprDecimal
{
    public ExprDate(Date value) {
        super(ExprType.Date, Long.toString(value.getTime()));
        this.cancelEvalOnMissing = true;
    }
    
    public int hashCode() {
        return value.hashCode();
    }
    
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        if (this.value == null) {
            return "";
        }
        
        try {
            return format.format(this.value);
        }
        catch (Exception e) {
            return this.value.toString();
        }
    }
}
