package src.tw.waterballsa.c2m1h1;

import java.util.HashSet;
import java.util.Set;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */

public class Utils {
    public static <T> int intersectionSize(Set<T> s1, Set<T> s2) {
        return intersection(s1, s2).size();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Set<T> intersection(Set<T> s1, Set<T> s2) {
        Set temp = new HashSet(s1);
        temp.retainAll(s2);
        return temp;
    }
}
