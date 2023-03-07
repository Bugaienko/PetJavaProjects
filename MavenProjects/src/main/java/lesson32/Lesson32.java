package lesson32;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Sergii Bugaienko
 */

public class Lesson32 {
    private static final Logger LOGGER = LogManager.getLogger(Lesson32.class);
    public static void main(String[] args) {
        String error = "Error in 1 line";
        //Level INFO
        LOGGER.info("log info: {}", error);
        LOGGER.error("log error");
        //Level DEBUG
        LOGGER.debug("log debug");
        LOGGER.warn("log warning");
        LOGGER.fatal("log fatal");


    }
}
