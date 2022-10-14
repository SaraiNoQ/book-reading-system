package com.dms.guyiyao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 *
 */
public final class Loggers {
    private static final String FQCN = Loggers.class.getName();

    private Loggers() {
    }

    private static LocationAwareLogger of() {
        return (LocationAwareLogger) LoggerFactory.getLogger(CALLER_CONTEXT.getCallerClass().getName());
    }

    public static Logger of(String name) {
        return LoggerFactory.getLogger(name);
    }

    public static void trace(String s) {
        of().log(null, FQCN, LocationAwareLogger.TRACE_INT, s, null, null);
    }

    public static void trace(String s, Object... objects) {
        of().log(null, FQCN, LocationAwareLogger.TRACE_INT, s, objects, null);
    }

    public static void trace(String s, Throwable throwable) {
        of().log(null, FQCN, LocationAwareLogger.TRACE_INT, s, null, throwable);
    }

    public static void debug(String s) {
        of().log(null, FQCN, LocationAwareLogger.DEBUG_INT, s, null, null);
    }

    public static void debug(String s, Object... objects) {
        of().log(null, FQCN, LocationAwareLogger.DEBUG_INT, s, objects, null);
    }

    public static void debug(String s, Throwable throwable) {
        of().log(null, FQCN, LocationAwareLogger.DEBUG_INT, s, null, throwable);
    }

    public static void info(String s) {
        of().log(null, FQCN, LocationAwareLogger.INFO_INT, s, null, null);
    }

    public static void info(String s, Object... objects) {
        of().log(null, FQCN, LocationAwareLogger.INFO_INT, s, objects, null);
    }

    public static void info(String s, Throwable throwable) {
        of().log(null, FQCN, LocationAwareLogger.INFO_INT, s, null, throwable);
    }

    public static void warn(String s) {
        of().log(null, FQCN, LocationAwareLogger.WARN_INT, s, null, null);
    }

    public static void warn(String s, Object... objects) {
        of().log(null, FQCN, LocationAwareLogger.WARN_INT, s, objects, null);
    }

    public static void warn(String s, Throwable throwable) {
        of().log(null, FQCN, LocationAwareLogger.WARN_INT, s, null, throwable);
    }

    public static void error(String s) {
        of().log(null, FQCN, LocationAwareLogger.ERROR_INT, s, null, null);
    }

    public static void error(String s, Object... objects) {
        of().log(null, FQCN, LocationAwareLogger.ERROR_INT, s, objects, null);
    }

    public static void error(String s, Throwable throwable) {
        of().log(null, FQCN, LocationAwareLogger.ERROR_INT, s, null, throwable);
    }

    private static final class CallerContext extends SecurityManager {
        Class<?> getCallerClass() {
            return super.getClassContext()[3];
        }
    }

    private static final CallerContext CALLER_CONTEXT = new CallerContext();
}

