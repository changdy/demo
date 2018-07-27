package pojo;

/**
 * Created by Changdy on 2018/7/25.
 */
public class OperationUnit {
    private Float number;
    private String symbol;

    public OperationUnit() {

    }

    public OperationUnit(String symbol) {
        this.symbol = symbol;
    }

    public OperationUnit(Float number) {
        this.number = number;
    }

    public boolean isNumber() {
        return number != null;
    }

    public Float getNumber() {
        return number;
    }

    public void setNumber(Float number) {
        this.number = number;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isAdditionOrSubtraction() {
        return ("+").equals(symbol) || ("-").equals(symbol);
    }

    public boolean isLeftBracket() {
        return ("(").equals(symbol);
    }

    public boolean isRightBracket() {
        return (")").equals(symbol);
    }


    @Override
    public String toString() {
        if (isNumber()) {
            return String.valueOf(number);
        } else {
            return symbol;
        }
    }
}
