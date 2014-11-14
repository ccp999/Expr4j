/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.expr.engine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.boris.expr.Expr;
import org.boris.expr.ExprArray;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.ExprException;
import org.boris.expr.ExprNumber;
import org.boris.expr.IOperandConversionVisitor;

public class IterationEngine extends AbstractCalculationEngine
{
    private int maxIterations = 100;
    private BigDecimal maxChange = new BigDecimal("0.0001");
    private Map<Range, Expr> inputExprs = new HashMap();

    public IterationEngine(EngineProvider provider) {
        super(provider);
    }

    public void setMaxIterations(int iterations) {
        this.maxIterations = iterations;
    }

    public void setMaxChange(BigDecimal change) {
        this.maxChange = change.abs();
    }

    public void calculate(boolean force) throws ExprException {
        if (autoCalculate && !force)
            return;

        calc();
    }

    private void calc() throws ExprException {
        Set<Range> inputs = getInputRanges();
        Map<Range, Expr> valueChanges = new HashMap();
        for (int i = 0; i < maxIterations; i++) {
            BigDecimal change = BigDecimal.ZERO;
            for (Range r : inputs) {
                Expr e = inputExprs.get(r);
                if (e instanceof ExprEvaluatable) {
                    e = ((ExprEvaluatable) e).evaluate(this);
                    values.put(r, e);
                    valueChanges.put(r, e);
                    BigDecimal c = findChange(values.get(r), e);
                    if (c.compareTo(change) > 0)
                        change = c;
                }
            }

            if (change.compareTo(maxChange) < 0)
                break;
        }

        for (Range r : valueChanges.keySet()) {
            provider.valueChanged(r, valueChanges.get(r));
        }
    }

    private BigDecimal findChange(Expr old, Expr nu) {
        if (old == null || nu == null)
            return BigDecimal.ZERO;

        if (old.type != nu.type)
            return BigDecimal.ZERO;

        if (nu instanceof ExprNumber) {
            return ((ExprNumber) old).decimalValue().subtract(((ExprNumber) nu).decimalValue()).abs();
        }

        if (nu instanceof ExprArray) {
            Expr[] oldA = ((ExprArray) old).getInternalArray();
            Expr[] nuA = ((ExprArray) nu).getInternalArray();

            BigDecimal change = BigDecimal.ZERO;
            for (int i = 0; i < oldA.length && i < nuA.length; i++) {
                BigDecimal c = findChange(oldA[i], nuA[i]);
                if (c.compareTo(change) > 0)
                    change = c;
            }

            return change;
        }

        return BigDecimal.ZERO;
    }

    public void set(Range range, String expression) throws ExprException {
        validateRange(range);

        // If null then remove all references
        if (expression == null) {
            rawInputs.remove(range);
            inputExprs.remove(range);
            values.remove(range);
            inputs.remove(range);
            return;
        }

        rawInputs.put(range, expression);

        Expr expr = parseExpression(expression);
        inputExprs.put(range, expr);

        // Set the inputs
        provider.inputChanged(range, expr);
        inputs.put(range, expr);

        // Always evaluate the expression entered
        if (expr.evaluatable) {
            Expr eval = ((ExprEvaluatable) expr).evaluate(this);
            provider.valueChanged(range, eval);
            values.put(range, eval);
        } else {
            provider.valueChanged(range, expr);
            values.put(range, expr);
        }

        // Recalculate the dependencies if required
        if (autoCalculate) {
            calc();
        }
    }

    public IOperandConversionVisitor getOperandConversionVisitor() {
        return null;
    }

    @Override
    public boolean throwEvalErrors() {
        return false;
    }
}
