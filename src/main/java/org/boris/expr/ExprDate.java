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

import java.util.Date;

public class ExprDate extends ExprNumber
{
    public final Date value;

    public ExprDate(Date value) {
        super(ExprType.Date);
        this.value = value;
    }

    @Override
    public int intValue() {
        return (int) value.getTime();
    }

    @Override
    public double doubleValue() {
        return value.getTime();
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
