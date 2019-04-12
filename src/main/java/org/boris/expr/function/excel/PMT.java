package org.boris.expr.function.excel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLOutput;

import org.boris.expr.Expr;
import org.boris.expr.ExprDecimal;
import org.boris.expr.ExprException;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprNumber;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

/**
 * Arg 1 = i
 * Arg 2 = n
 * Arg 3 = A
 *
 * n = 360 (30 years times 12 monthly payments per year)
 * i = .005 (6 percent annually expressed as .06, divided by 12 monthly payments per year—learn how to convert percentages to decimal format)
 * D = 166.7916 ({[(1+.005)^360] - 1} / [.005(1+.005)^360])
 * P = A / D = 100,000 / 166.7916 = 599.55
 */
public class PMT extends AbstractFunction
{
    public Expr evaluate(IEvaluationContext context, Expr[] args) throws ExprException {
        assertArgCount(args, 3);

        BigDecimal rate = asDecimal(context, args[0], false);
        int numPayments = asInteger(context, args[1], false);
        BigDecimal loanValue = asDecimal(context, args[2], false);

        // If any of the values are not present, returning a missing expression so error doesn't
        // result when calculation is performed
        if (rate == BigDecimal.ZERO || numPayments == 0 || loanValue == BigDecimal.ZERO) {
            return new ExprMissing();
        } else {
            BigDecimal discountFactor = rate.add(BigDecimal.ONE)
                    .pow(numPayments)
                    .subtract(BigDecimal.ONE)
                    .divide(rate.multiply(rate.add(BigDecimal.ONE)
                            .pow(numPayments)), ExprDecimal.MATH_CONTEXT);

            return new ExprDecimal(loanValue.divide(discountFactor, ExprDecimal.MATH_CONTEXT));
        }
    }
}