package org.boris.expr.function.excel;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.boris.expr.Expr;
import org.boris.expr.ExprException;
import org.boris.expr.ExprInteger;
import org.boris.expr.ExprMissing;
import org.boris.expr.ExprType;
import org.boris.expr.IEvaluationContext;
import org.boris.expr.function.AbstractFunction;

public class DATEDIF extends AbstractFunction {
    private static String YEAR = "y";
    private static String MONTH = "m";
    private static String YEAR_MONTH = "ym";
    private static String YEAR_MONTH_DAY = "ymd";
    @Override
    public Expr evaluate(IEvaluationContext context, Expr[] args) throws ExprException {
        assertArgCount(args,2);

        String type = asString(context, args[1], false);

        Expr expr0 = evalArg(context, args[0]);
        if (expr0.getType() == ExprType.Missing) {
            return new ExprMissing();
        }

        Integer dayCount = asInteger(context, expr0, false);

        LocalDate today = LocalDate.now();
        LocalDate adjustedDate = today.plusDays(Math.abs(dayCount));

        Period diff = Period.between(today, adjustedDate);
        int years = diff.getYears();
        int months = diff.getMonths();
        int days = diff.getDays();

        if (type.equals(YEAR)) {
            return new ExprInteger(years);
        } else if (type.equals(MONTH)) {
            int totalMonths = (int) ChronoUnit.MONTHS.between(today, adjustedDate);
            return new ExprInteger(totalMonths);

        } else if (type.equals(YEAR_MONTH)) {
            return new ExprInteger(months);

        } else if (type.equals(YEAR_MONTH_DAY)) {
            return new ExprInteger(days);
        }

        return new ExprInteger(0);
    }
}