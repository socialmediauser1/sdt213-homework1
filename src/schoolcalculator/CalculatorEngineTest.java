package schoolcalculator;

public final class CalculatorEngineTest {
    private static int checks;

    public static void main(String[] args) {
        testAddition();
        testSubtraction();
        testMultiplication();
        testDivisionAndDecimal();
        testDivideByZero();
        testChainedCalculation();
        testClearAndBackspace();
        System.out.println("All " + checks + " calculator checks passed.");
    }

    private static void testAddition() {
        CalculatorEngine calculator = calculate("12", '+', "8");
        expect("addition", "20", calculator.getDisplay());
    }

    private static void testSubtraction() {
        CalculatorEngine calculator = calculate("15", '-', "21");
        expect("subtraction", "-6", calculator.getDisplay());
    }

    private static void testMultiplication() {
        CalculatorEngine calculator = calculate("7", '*', "8");
        expect("multiplication", "56", calculator.getDisplay());
    }

    private static void testDivisionAndDecimal() {
        CalculatorEngine calculator = calculate("7.5", '/', "2.5");
        expect("decimal division", "3", calculator.getDisplay());
    }

    private static void testDivideByZero() {
        CalculatorEngine calculator = calculate("9", '/', "0");
        expect("divide-by-zero error", "Cannot divide by zero", calculator.getDisplay());
        if (!calculator.hasError()) throw new AssertionError("divide-by-zero should be an error state");
        checks++;
    }

    private static void testChainedCalculation() {
        CalculatorEngine calculator = new CalculatorEngine();
        type(calculator, "2");
        calculator.chooseOperation('+');
        type(calculator, "3");
        calculator.chooseOperation('*');
        type(calculator, "4");
        calculator.equals();
        expect("left-to-right chaining", "20", calculator.getDisplay());
    }

    private static void testClearAndBackspace() {
        CalculatorEngine calculator = new CalculatorEngine();
        type(calculator, "123");
        calculator.backspace();
        expect("backspace", "12", calculator.getDisplay());
        calculator.clear();
        expect("clear", "0", calculator.getDisplay());
    }

    private static CalculatorEngine calculate(String first, char operation, String second) {
        CalculatorEngine calculator = new CalculatorEngine();
        type(calculator, first);
        calculator.chooseOperation(operation);
        type(calculator, second);
        calculator.equals();
        return calculator;
    }

    private static void type(CalculatorEngine calculator, String text) {
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            if (character == '.') calculator.inputDecimal();
            else calculator.inputDigit(character - '0');
        }
    }

    private static void expect(String scenario, String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(scenario + ": expected " + expected + ", got " + actual);
        }
        checks++;
    }
}
