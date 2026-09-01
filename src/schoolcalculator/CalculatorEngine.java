package schoolcalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class CalculatorEngine {
    private static final MathContext DIVISION_CONTEXT = new MathContext(12, RoundingMode.HALF_UP);

    private String entry = "0";
    private BigDecimal storedValue;
    private Character pendingOperation;
    private boolean replaceEntry = true;
    private boolean error;
    private String expression = "";

    public void inputDigit(int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("A calculator digit must be from 0 to 9.");
        }
        beginFreshCalculationIfNeeded();
        if (replaceEntry || "0".equals(entry)) {
            entry = String.valueOf(digit);
            replaceEntry = false;
        } else {
            entry += digit;
        }
    }

    public void inputDecimal() {
        beginFreshCalculationIfNeeded();
        if (replaceEntry) {
            entry = "0.";
            replaceEntry = false;
        } else if (!entry.contains(".")) {
            entry += ".";
        }
    }

    public void backspace() {
        if (error || replaceEntry) {
            return;
        }
        entry = entry.length() <= 1 ? "0" : entry.substring(0, entry.length() - 1);
        if ("-".equals(entry)) {
            entry = "0";
        }
    }

    public void clear() {
        entry = "0";
        storedValue = null;
        pendingOperation = null;
        replaceEntry = true;
        error = false;
        expression = "";
    }

    public void chooseOperation(char operation) {
        if (operation != '+' && operation != '-' && operation != '*' && operation != '/') {
            throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
        if (error) {
            return;
        }

        BigDecimal current = valueOfEntry();
        if (pendingOperation != null && !replaceEntry) {
            BigDecimal result = calculate(storedValue, current, pendingOperation);
            if (error) {
                return;
            }
            storedValue = result;
            entry = format(result);
        } else if (storedValue == null) {
            storedValue = current;
        }

        pendingOperation = operation;
        expression = format(storedValue) + " " + symbol(operation);
        replaceEntry = true;
    }

    public void equals() {
        if (error || pendingOperation == null || storedValue == null) {
            return;
        }
        BigDecimal secondValue = valueOfEntry();
        String completedExpression = format(storedValue) + " " + symbol(pendingOperation)
                + " " + format(secondValue) + " =";
        BigDecimal result = calculate(storedValue, secondValue, pendingOperation);
        if (error) {
            return;
        }
        entry = format(result);
        storedValue = result;
        pendingOperation = null;
        replaceEntry = true;
        expression = completedExpression;
    }

    public String getDisplay() {
        return entry;
    }

    public String getExpression() {
        return expression;
    }

    public boolean hasError() {
        return error;
    }

    private void beginFreshCalculationIfNeeded() {
        if (error || (pendingOperation == null && replaceEntry && !expression.isEmpty())) {
            clear();
        }
    }

    private BigDecimal valueOfEntry() {
        return new BigDecimal(entry.endsWith(".") ? entry + "0" : entry);
    }

    private BigDecimal calculate(BigDecimal left, BigDecimal right, char operation) {
        try {
            switch (operation) {
                case '+': return left.add(right);
                case '-': return left.subtract(right);
                case '*': return left.multiply(right);
                case '/':
                    if (BigDecimal.ZERO.compareTo(right) == 0) {
                        showError("Cannot divide by zero");
                        return BigDecimal.ZERO;
                    }
                    return left.divide(right, DIVISION_CONTEXT);
                default: throw new IllegalStateException("Unknown operation");
            }
        } catch (ArithmeticException exception) {
            showError("That number is too large");
            return BigDecimal.ZERO;
        }
    }

    private void showError(String message) {
        entry = message;
        expression = "Try C and start again";
        error = true;
        replaceEntry = true;
        pendingOperation = null;
        storedValue = null;
    }

    private static String format(BigDecimal value) {
        BigDecimal tidy = value.stripTrailingZeros();
        return tidy.scale() < 0 ? tidy.setScale(0).toPlainString() : tidy.toPlainString();
    }

    private static String symbol(char operation) {
        if (operation == '*') return "×";
        if (operation == '/') return "÷";
        return String.valueOf(operation);
    }
}
