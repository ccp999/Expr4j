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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.boris.expr.engine.GridMap;
import org.boris.expr.engine.Range;
import org.boris.expr.function.ExcelFunctionProvider;
import org.boris.expr.function.FunctionManager;
import org.boris.expr.function.IFunctionProvider;
import org.boris.expr.parser.ExprLexer;
import org.boris.expr.parser.ExprParser;
import org.boris.expr.parser.IParserVisitor;
import org.boris.expr.util.Exprs;
import org.boris.expr.util.GraphCycleException;

public class BasicEvaluationCallback implements IEvaluationContext,
        IParserVisitor
{
    private Map<String, Expr> variables = new HashMap<String, Expr>();
    private FunctionManager functions = new FunctionManager();
    private GridMap grid = new GridMap();
    private Map<String, List<ExprVariable>> dependencyMap;
    private IOperandConversionVisitor operandConversionVisitor;
    private boolean throwEvalErrors;
    
    public BasicEvaluationCallback() {
        functions.add(new ExcelFunctionProvider());
        variables.put("TRUE", ExprBoolean.TRUE);
        variables.put("FALSE", ExprBoolean.FALSE);
        dependencyMap = new TreeMap<>();
    }

    public BasicEvaluationCallback(IFunctionProvider baseSupportedFunctions, boolean throwEvalErrors) {
        functions.add(baseSupportedFunctions);
        variables.put("TRUE", ExprBoolean.TRUE);
        variables.put("FALSE", ExprBoolean.FALSE);
        dependencyMap = new TreeMap<>();
        this.throwEvalErrors = throwEvalErrors;
    }
    
    public ExprVariable[] addVariable(String name, Expr value) throws GraphCycleException {
        variables.put(name, value);
        ExprVariable[] vars = ExprVariable.findVariables(value);
        
        if (vars != null && vars.length > 0) {
            List<ExprVariable> dependencies = new ArrayList<>(Arrays.asList(vars));
            dependencyMap.put(name, dependencies);
        }
        
        checkCycle(name, new TreeSet<String>());
        
        return vars;
    }

    public void addFunction(String name, IExprFunction function) {
        functions.add(name, function);
    }

    private void checkCycle(String name, Set<String> visitedNames) throws GraphCycleException {
        if (visitedNames.contains(name)) {
            throw new GraphCycleException("Circular reference found.");    
        }
        
        visitedNames.add(name);
        List<ExprVariable> dependencies = this.dependencyMap.get(name);
        if (dependencies != null) {
            for (ExprVariable dependencyName : dependencies) {
                checkCycle(dependencyName.getName(), visitedNames);
            }
        }
    }
    
    public Expr evaluateFunction(ExprFunction function) throws ExprException {
        return functions.evaluate(this, function);
    }

    public Expr evaluateVariable(ExprVariable variable) throws ExprException {
        String name = variable.getName().toUpperCase();
        if (variables.containsKey(name)) {
            Expr variableExpr = variables.get(name);            
            return variable.eval(variableExpr, this);
        }
        Object ann = variable.getAnnotation();
        if (ann instanceof Range) {
            Expr rangeExpr = grid.get((Range) ann);
            if (rangeExpr != null) {
                return rangeExpr;
            }
        }
        Expr e = variables.get(name);
        if (e == null) {
            return ExprError.generateError(ExprError.NAME, name);
        }

        return e;
    }

    public Expr parse(String expr) throws IOException, ExprException {
        ExprParser ep = new ExprParser();
        ep.setParserVisitor(this);
        ep.parse(new ExprLexer(expr));
        return ep.get();
    }

    public void set(String range, Object value) throws ExprException {
        this.set(Range.valueOf(range), Exprs.convertObject(value));
    }

    public void set(Range range, Expr value) {        
        grid.put(range, value);
    }

    public void set(ExprArray array) {
        set(Range.toRange(array, null), array);
    }

    public void annotateFunction(ExprFunction function) throws ExprException {
    }

    public void annotateVariable(ExprVariable variable) throws ExprException {
        variable.setAnnotation(Range.valueOf(variable.getName()));
    }

    public IOperandConversionVisitor getOperandConversionVisitor() {
        return this.operandConversionVisitor;
    }

    public void setOperandConversionVisitor(IOperandConversionVisitor operandConversionVisitor) {
        this.operandConversionVisitor = operandConversionVisitor;
    }

    public void setThrowEvalErrors(boolean throwEvalErrors) {
        this.throwEvalErrors = throwEvalErrors;
    }

    public boolean throwEvalErrors() {
        return this.throwEvalErrors;
    }
}