package utils;

import static java.lang.String.format;

public class ValidationUtils {

    public static int shouldWithinRange(String name, int value, int min, int max) {
        if(min > max) {
            throw new IllegalArgumentException("min must be smaller than max.");
        }

        if (value < min || value > max) {
            throw new IllegalArgumentException(format("'%s' must be within range.", name));
        }
        return value;
    }
}
