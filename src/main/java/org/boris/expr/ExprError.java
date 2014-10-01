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


public class ExprError extends Expr
{
    public static final Expr NULL = new ExprError("#NULL!", "Null Error");
    public static final Expr DIV0 = new ExprError("#DIV/0!",
            "Divide by Zero Error");
    public static final Expr VALUE = new ExprError("#VALUE", "Error in Value");
    public static final Expr REF = new ExprError("#REF!", "Reference Error");
    public static final Expr NAME = new ExprError("#NAME?",
            "Invalid Name Error");
    public static final Expr NUM = new ExprError("#NUM!", "Number Error");
    public static final Expr DATE = new ExprError("#DATE!", "Date Error");
    public static final Expr NUM_OR_DATE = new ExprError("#NUM_OR_DATE!", "Number or Date Error");
    public static final Expr NA = new ExprError("#N/A", "Value not Available");

    private String errType;
    private String message;
    private String variableName;
    private String value;

    public static ExprError generateError(Expr expr, String variableName) {
        ExprError error = (ExprError) expr;
        ExprError generatedError = new ExprError(error.getErrType(), error.getMessage());
        generatedError.setVariableName(variableName);
        
        return generatedError;
    }    
    public static ExprError generateError(Expr expr) {
        ExprError error = (ExprError) expr;
        ExprError generatedError = new ExprError(error.getErrType(), error.getMessage());
        
        return generatedError;
    }    

    public ExprError(String type, String message) {
        super(ExprType.Error, false);
        this.errType = type;
        this.message = message;
    }

    public String getErrType() {
        return errType;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return "#" + message;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
