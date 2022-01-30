package main.java.io.github.qauinger.irdatacompiler;

import java.util.Comparator;

public class AscendingComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return Integer.valueOf(o1.replaceAll("\\.", "")).compareTo(Integer.valueOf(o2.replaceAll("\\.", "")));
    }
}
