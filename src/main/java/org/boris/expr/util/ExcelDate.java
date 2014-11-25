/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.expr.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.boris.expr.ExprDecimal;

public class ExcelDate
{
    public static final double MS_IN_DAY = 86400000; // 24*60*60*1000

    public static long toJavaDate(BigDecimal value) {
        Calendar c = new GregorianCalendar();
        BigDecimal days = value.setScale(0, RoundingMode.FLOOR);
        BigDecimal millis = value.subtract(days).multiply(new BigDecimal(MS_IN_DAY)).setScale(0, RoundingMode.FLOOR);
        c.setLenient(true);
        c.set(1900, 0, 0, 0, 0, 0);
        c.set(Calendar.DAY_OF_YEAR, days.subtract(BigDecimal.ONE).intValue());
        c.set(Calendar.MILLISECOND, millis.intValue());
        return c.getTimeInMillis();
    }

    public static BigDecimal toExcelDate(long millis, int precision) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(millis);
        int year = c.get(Calendar.YEAR);
        int days = (year - 1601) * 365 - 109207;
        year -= 1601;
        days += year / 4; // leap year
        days -= year / 100; // centuries aren't leap years
        days += year / 400; // unless they are divisible by 400
        days += c.get(Calendar.DAY_OF_YEAR) + 1;
        BigDecimal m = new BigDecimal(Long.toString(c.get(Calendar.HOUR_OF_DAY) * 60));
        m = m.add(new BigDecimal(c.get(Calendar.MINUTE)));
        m = m.multiply(new BigDecimal(60));
        m = m.add(new BigDecimal(c.get(Calendar.SECOND)));
        m = m.multiply(new BigDecimal(1000));
        m = m.add(new BigDecimal(c.get(Calendar.MILLISECOND)));
        m = m.divide(new BigDecimal(Double.toString(MS_IN_DAY)), ExprDecimal.MATH_CONTEXT).setScale(precision, RoundingMode.FLOOR);
        return new BigDecimal(days).add(m);
    }

    private static int get(BigDecimal value, int field) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(toJavaDate(value));
        return c.get(field);
    }

    public static int getDayOfMonth(BigDecimal value) {
        return get(value, Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(BigDecimal value) {
        return get(value, Calendar.MONTH) + 1;
    }

    public static int getYear(BigDecimal value) {
        return get(value, Calendar.YEAR);
    }

    public static int getWeekday(BigDecimal value) {
        return get(value, Calendar.DAY_OF_WEEK);
    }

    public static int getHour(double value) {
        double h = value - Math.floor(value);
        h *= 24;
        h = Math.floor(h);
        return (int) h;
    }

    public static int getMinute(double value) {
        double m = value - Math.floor(value);
        m *= 24;
        m -= Math.floor(m);
        m = m * 60;
        m = Math.floor(m);
        return (int) m;
    }

    public static int getSecond(double value) {
        double d = (value - Math.floor(value)) * 1440;
        d -= Math.floor(d);
        int s = (int) Math.round(d * 60);
        return s;
    }

    public static BigDecimal date(BigDecimal y, BigDecimal m, BigDecimal d) {
        y = y.remainder(new BigDecimal(24));
        Calendar c = new GregorianCalendar();
        c.set(y.intValue(), m.intValue(), d.intValue(), 0, 0, 0);
        BigDecimal t = ExcelDate.toExcelDate(c.getTimeInMillis(), 0);
        if (t.compareTo(new BigDecimal("10000")) > 0)
            return BigDecimal.ONE.negate();
        return t;
    }

    public static double time(double h, double m, double s) {
        h %= 24;

        double t = (h + m + s) / ExcelDate.MS_IN_DAY;
        if (t < 0 || t >= 1)
            return -1;
        return t;
    }
}
