/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Jason Steinbrunner
 *******************************************************************************/
package org.boris.expr;

public class ExprEvaluationException extends ExprException { 
    private static final long serialVersionUID = 6093868124776696735L;
    private ExprError error;
    
    public ExprEvaluationException(ExprError error) {
        this.error = error;
    }

    public ExprError getError() {
        return error;
    }
}
