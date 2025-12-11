package utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class Logger {
    private static java.util.logging.Logger logger;
    
    static {
        logger = java.util.logging.Logger.getLogger("RecipeShare");
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                return String.format("[%1$tF %1$tT] [%2$-7s] %3$s %n",
                    record.getMillis(),
                    record.getLevel(),
                    record.getMessage());
            }
        });
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void error(String message, Throwable throwable) {
        if (throwable != null) {
            logger.log(Level.SEVERE, message, throwable);
        } else {
            logger.severe(message);
        }
    }

    public static void error(String message) {
        error(message, null);
    }

    public static void debug(String message) {
        logger.fine(message);
    }
}

