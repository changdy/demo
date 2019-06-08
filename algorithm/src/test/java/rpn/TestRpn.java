package rpn;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by Changdy on 2018/7/25.
 */
public class TestRpn {
    // 使用js引擎校对 计算结果
    @Test
    public void test() throws IOException, ScriptException, URISyntaxException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
        Path path = Paths.get(this.getClass().getResource("/rpn.txt").toURI());
        List<String> equations = Files.readAllLines(path);
        for (String equation : equations) {
            float resultByJS = Float.valueOf(String.valueOf(engine.eval(equation)));
            float myResult = Operate.getResult(Operate.convertToRPN(Operate.splitToUnit(equation)));
            boolean b = Math.abs(myResult - resultByJS) < 0.001;
            System.out.println(b + "\t" + formatValue(resultByJS) + formatValue(myResult));
        }
    }

    private String formatValue(float value) {
        return String.format("%1$-14s", String.valueOf(value));
    }
}
