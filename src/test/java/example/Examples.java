package example;

import jzap.Logger;
import jzap.Zap;
import jzap.zapcore.*;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jzap.zapcore.Level.*;
import static jzap.zapcore.TimeEncoder.ISO8601TimeEncoder;

public class Examples {

    public static void main(String[] args) {
        var config = Zap.newProductionEncoderConfig();
        config.setEncodeTime(ISO8601TimeEncoder);
        config.setEncodeLevel(LevelEncoder.CapitalColorLevelEncoder);
        var fileEncoder = ZapCore.newJSONEncoder(config);
        var consoleEncoder = ZapCore.newConsoleEncoder(config);
        var logFile = new File("json.log");
        var writer = ZapCore.addSync(logFile);
        var defaultLogLevel = DEBUG;
        var core = ZapCore.newTee(
                ZapCore.newCore(fileEncoder, writer, defaultLogLevel),
                ZapCore.newCore(consoleEncoder, ZapCore.addSync(System.out), defaultLogLevel)
        );
        var logger = Zap.newLogger(core, Zap.addCaller(), Zap.addStacktrace(ERROR));

        logger.info("xxxxxxxx");
        log(logger);
        System.out.println("++++++++++++++++++++++");

        BigInteger x = new BigInteger("530500452766");
        byte[] byteArray = x.toByteArray();
        String s = new String(byteArray);
        byteArray = s.getBytes();
        x = new BigInteger(byteArray);



        System.out.println(x);


        System.out.println(">>>>>>>>>>>" + Integer.parseInt("11111111111111111111111", 2));

        var num = 1234567890;
        System.out.println(">>>>>>" + Integer.toBinaryString(num));
        float f = num;
        System.out.println(">>>>>>" + f);
        System.out.println(">>>>>>" + Integer.toBinaryString(Float.floatToRawIntBits(f)));
    }

    public static void log(Logger logger) {
        logger.error("yyyyyyyyy");
    }

    public static void modify(List<List<String>> list) {

        list.set( 1, Arrays.asList("forty-two")); // No warning
//        list.add(Arrays.asList(42));

        for (List<String> ls : list) {
            for (String string : ls) {            // ClassCastException on 42
                System.out.println(string);
            }
        }
    }

    public static void main1(String[] args) {
        List<String> s = Arrays.asList("foo", "bar");
        List<String> s2 = Arrays.asList("baz", "quux");
        List<List<String>> list = new ArrayList<List<String>>();
        list.add(s);
        list.add(s2);
        modify(list);
    }

}
