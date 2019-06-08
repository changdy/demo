package rpn.calculate;

/**
 * Created by Changdy on 2018/7/26.
 */
public class CalculatorFactory {
    public static Calculator getCalculator(String symbol) {
        switch (symbol) {
            case "+":
                return new AddCalculator();
            case "-":
                return new SubtractCalculator();
            case "*":
                return new MultiplyCalculator();
            case "/":
                return new DivideCalculator();
        }
        throw new RuntimeException("未知符号:" + symbol);
    }
}
