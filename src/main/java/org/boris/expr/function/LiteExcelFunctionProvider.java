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
package org.boris.expr.function;

import java.util.HashMap;
import java.util.Map;

import org.boris.expr.Expr;
import org.boris.expr.ExprException;
import org.boris.expr.ExprFunction;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.IExprFunction;
import org.boris.expr.function.excel.AVERAGE;
import org.boris.expr.function.excel.CONCATENATE;
import org.boris.expr.function.excel.FORMAT;
import org.boris.expr.function.excel.IF;
import org.boris.expr.function.excel.JOIN;
import org.boris.expr.function.excel.MAX;
import org.boris.expr.function.excel.MIN;
import org.boris.expr.function.excel.PATTERN;
import org.boris.expr.function.excel.SUM;
import org.boris.expr.function.excel.TEXTIFY;

public class LiteExcelFunctionProvider implements IFunctionProvider
{
    private static Map<String, IExprFunction> functions = new HashMap<>();

    static {
        functions.put("AVERAGE", new AVERAGE());
        functions.put("CONCATENATE", new CONCATENATE());
        functions.put("IF", new IF());
        functions.put("SUM", new SUM());
        functions.put("MIN", new MIN());
        functions.put("MAX", new MAX());
        functions.put("FORMAT", new FORMAT());
        functions.put("TEXTIFY", new TEXTIFY());
        functions.put("JOIN",  new JOIN());
        functions.put("PATTERN",  new PATTERN());
    }

    public boolean hasFunction(ExprFunction function) {
        return functions.containsKey(function.getName().toUpperCase());
    }

    public Expr evaluate(IEvaluationContext context, ExprFunction function)
            throws ExprException {
        IExprFunction f = functions.get(function.getName().toUpperCase());
        if (f != null)
            return f.evaluate(context, function.getArgs());
        return null;
    }
}
