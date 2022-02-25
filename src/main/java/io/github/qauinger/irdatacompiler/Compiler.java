package main.java.io.github.qauinger.irdatacompiler;

import javax.swing.*;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Compiler {

    public enum Sort {
        ASCENDING, DESCENDING
    }

    public static boolean compile(File dir, File output, Sort order, boolean normalize) {
        Comparator<? extends String> comparator = order == Sort.ASCENDING ? new AscendingComparator() : new DescendingComparator();
        TreeMap<String, String[]> dataMap = new TreeMap<>();
        List<File> files = Utils.getFiles(dir, IRDataCompiler.extensions);
        for(File file : files) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    for(String line : st.split("\\n")) {
                        line = line.replaceAll("\\s", "");
                        String[] l = line.split(",", 2);
                        String[] v = (dataMap.get(l[0]) == null ? l[1].split(",") : concatenate(dataMap.get(l[0]), l[1].split(",")));
                        dataMap.put(l[0], v);
                    }
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        if(normalize) {
            normalize(dataMap);
        }
        write(dataMap, output);
        return true;
    }

    private static String[] concatenate(String[]... arrays) {
        int length = 0;
        for (String[] array : arrays) {
            length += array.length;
        }
        String[] result = new String[length];
        int pos = 0;
        for (String[] array : arrays) {
            for (String element : array) {
                result[pos] = element;
                pos++;
            }
        }
        return result;
    }

    private static void write(Map<String, String[]> file, File output) {
        int num = 0;
        File f;
        while(true) {
            String suf = "";
            if(num != 0) {
                suf = "(" + num + ")";
            }
            if(output.isDirectory()) {
                File fTest = new File(output.getAbsolutePath() + "/IRDataCompiler_Compiled_Data" + suf + ".dpt");
                if(!fTest.exists()) {
                    f = fTest;
                    break;
                } else {
                    num++;
                }
            } else {
                if(output.exists()) {
                    int result = Utils.showDialog(IRDataCompiler.getProgramTitle(), "This action will overwrite the contents of \n\"" + output.getAbsolutePath() + "\"!\nAre you sure you want to continue?", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(result == JOptionPane.NO_OPTION) {
                        return;
                    } else {
                        f = output;
                        break;
                    }
                } else {
                    f = new File(output.getAbsolutePath());
                    break;
                }
            }
        }

        try {
            FileWriter fw = new FileWriter(f);
            for (String key : file.keySet()) {
                String[] val = file.get(key);
                fw.write(key + "," + String.join(",", val) + "\n");
            }
            fw.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        Utils.showDialog(IRDataCompiler.getProgramTitle(), "Compiling completed.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    private static void normalize(TreeMap<String, String[]> data) {
        double max = 0;
        try {
            for(String key : data.keySet()) {
                for(String val : data.get(key)) {
                    if(Double.parseDouble(val) > max) {
                        max = Double.parseDouble(val);
                    }
                }
            }
        } catch(NumberFormatException ex) {
            ex.printStackTrace();
        }

        for(String key : data.keySet()) {
            String[] g = data.get(key);
            for(int i = 0; i < g.length; i++) {
                g[i] = String.valueOf(Double.parseDouble(g[i]) / max);
            }
        }
    }
}
