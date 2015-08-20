package org.boris.expr;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExprDecimal extends ExprNumber
{
    public static final MathContext MATH_CONTEXT = new MathContext(50, RoundingMode.HALF_UP);
    
    protected BigDecimal value;

    public ExprDecimal(BigDecimal value) {
        super(ExprType.Decimal);
        this.value = value;
    }

    public ExprDecimal(ExprType type, BigDecimal value) {
        super(type);
        this.value = value;
    }

    public ExprDecimal(String value) {
        super(ExprType.Decimal);
        this.value = new BigDecimal(value);
    }

    public ExprDecimal(ExprType type, String value) {
        super(type);
        this.value = new BigDecimal(value);
    }

    public ExprDecimal(ExprType type) {
        super(type);
    }

    protected void setValue(String value) {
        this.value = new BigDecimal(value);
    }

    protected void setValue(BigDecimal value) {
        this.value = value;
    }

    public int intValue() {
        return value.intValue();
    }

    public BigDecimal decimalValue() {
        return value;
    }

    public String toString() {
        return this.value.toString();
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ExprDecimal) {
            return ((ExprDecimal) obj).decimalValue().compareTo(this.value) == 0;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isDecimal() {
        return true;
    }
}

