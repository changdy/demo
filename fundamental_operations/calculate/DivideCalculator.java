package calculate;

/**
 * Created by Changdy on 2018/7/26.
 */
public class DivideCalculator implements Calculator {

    @Override
    public float calculate(float preNumber, float postNumber) {
        return preNumber / postNumber;
    }
}
