package main.java.io.github.qauinger.irdatacompiler;

import java.util.Comparator;

public class DescendingComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return Integer.valueOf(o2.replaceAll("\\.", "")).compareTo(Integer.valueOf(o1.replaceAll("\\.", "")));
    }
}
