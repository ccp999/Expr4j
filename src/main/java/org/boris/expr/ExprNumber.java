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

public abstract class ExprNumber extends Expr
{
    private BigDecimal decimalValue;

    ExprNumber(ExprType type) {
        super(type, false);
    }

    public void validate() throws ExprException {
    }

    public boolean booleanValue() {
        return intValue() != 0;
    }

    public abstract int intValue();
    public abstract boolean isDecimal();

    public BigDecimal decimalValue() {
        if (this.decimalValue == null) {
            this.decimalValue = new BigDecimal(intValue());
        }
        
        return this.decimalValue;
    }
}
