package src.tw.SherryTseng.MatchSystem;

import java.util.HashSet;
import java.util.Set;

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