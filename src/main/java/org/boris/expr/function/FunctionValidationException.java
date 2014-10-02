/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Jason Steinbrunner
 *******************************************************************************/
package org.boris.expr.function;

import org.boris.expr.ExprException;

public class FunctionValidationException extends ExprException {
    private static final long serialVersionUID = 1L;
    public FunctionValidationException(String message) {
        super(message);
    }
}
