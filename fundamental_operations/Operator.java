import calculate.Calculator;
import calculate.CalculatorFactory;
import pojo.OperationUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by Changdy on 2018/7/25.
 */
public class Operator {
    // 使用js引擎校对 计算结果
    public static void main(String[] args) throws IOException, ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
        List<String> equations = Files.readAllLines(Paths.get("/test.txt"));
        for (String equation : equations) {
            float resultByJS = Float.valueOf(String.valueOf(engine.eval(equation)));
            float myResult = getResult(convertToRPN(splitToUnit(equation)));
            boolean b = Math.abs(myResult - resultByJS) < 0.001;
            System.out.println(b + "\t" + String.format("%1$-14s", String.valueOf(resultByJS)) + String.format("%1$-14s", String.valueOf(myResult)));
        }
    }

    // 分析算数表达式,拆分符号和数字
    private static List<OperationUnit> splitToUnit(String equation) {
        char[] chars = equation.replaceAll(" ", "").toCharArray();
        StringBuilder temp = new StringBuilder();
        List<OperationUnit> list = new ArrayList<>();
        for (char c : chars) {
            if (c <= '9' && c >= '0' || c == '.') {
                temp.append(c);
            } else {
                // 判断长度,确定是否有数字需要转换
                addUnit(temp, list);
                list.add(new OperationUnit(String.valueOf(c)));
            }
        }
        addUnit(temp, list);
        return list;
    }

    private static void addUnit(StringBuilder temp, List<OperationUnit> list) {
        if (temp.length() != 0) {
            list.add(new OperationUnit(Float.valueOf(temp.toString())));
            temp.delete(0, temp.length());
        }
    }

    // 完成转换后缀表达式
    private static Stack<OperationUnit> convertToRPN(List<OperationUnit> operationUnits) {
        Stack<OperationUnit> symbolStack = new Stack<>();
        Stack<OperationUnit> postfixNotation = new Stack<>();
        for (OperationUnit operationUnit : operationUnits) {
            if (operationUnit.isNumber()) {
                postfixNotation.push(operationUnit);
            } else {
                if (symbolStack.isEmpty()) {
                    symbolStack.push(operationUnit);
                    continue;
                }
                // 左括号,入栈
                OperationUnit topUnit = symbolStack.peek();
                if (topUnit.isLeftBracket() || operationUnit.isLeftBracket()) {
                    symbolStack.push(operationUnit);
                } else if (operationUnit.isRightBracket()) {
                    // 右括号,全部出栈
                    while (!symbolStack.peek().isLeftBracket()) {
                        postfixNotation.add(symbolStack.pop());
                    }
                    symbolStack.pop();
                } else if (operationUnit.isAdditionOrSubtraction()) {
                    if (!postfixNotation.peek().isLeftBracket()) {
                        postfixNotation.push(symbolStack.pop());
                        if (!symbolStack.isEmpty() && !symbolStack.peek().isLeftBracket()) {
                            postfixNotation.push(symbolStack.pop());
                        }
                    }
                    symbolStack.push(operationUnit);
                } else if (topUnit.isAdditionOrSubtraction()) {
                    symbolStack.push(operationUnit);
                } else {
                    postfixNotation.push(symbolStack.pop());
                    symbolStack.push(operationUnit);
                }
            }
        }
        while (!symbolStack.isEmpty()) {
            postfixNotation.push(symbolStack.pop());
        }
        return postfixNotation;
    }
    
    // 计算最终结果,这一步实际上比较简单
    private static float getResult(Stack<OperationUnit> operationUnits) {
        for (int i = 2; i < operationUnits.size(); i++) {
            OperationUnit operationUnit = operationUnits.get(i);
            if (!operationUnit.isNumber()) {
                Calculator calculator = CalculatorFactory.getCalculator(operationUnit.getSymbol());
                float result = calculator.calculate(operationUnits.get(i - 2).getNumber(), operationUnits.get(i - 1).getNumber());
                operationUnits.set(i - 2, new OperationUnit(result));
                operationUnits.remove(i);
                operationUnits.remove(i - 1);
                i = i - 2;
            }
        }
        return operationUnits.pop().getNumber();
    }
}
