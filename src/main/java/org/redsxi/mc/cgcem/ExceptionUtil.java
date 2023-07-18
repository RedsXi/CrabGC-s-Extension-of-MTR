package org.redsxi.mc.cgcem;

import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ExceptionUtil {
    public static void printStackTraceToLogger(Logger logger, Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(baos));
        String[] lines = new String(baos.toByteArray(), StandardCharsets.UTF_8).split("\r\n");
        for(String l : lines) logger.error(l);
    }
}
