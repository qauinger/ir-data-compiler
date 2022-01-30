package main.java.io.github.qauinger.irdatacompiler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class Utils {

    private static JFileChooser folderChooser;

    static {
        folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setAcceptAllFileFilterUsed(true);
    }

    public static File chooseFolder(String title, String dir) {
        folderChooser.setDialogTitle(title);
        folderChooser.setCurrentDirectory(new File(dir));
        return (folderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) ? folderChooser.getSelectedFile() : null;
    }

    public static int showDialog(String title, String message, int optionType, int messageType) {
        return JOptionPane.showConfirmDialog(null, message, title, optionType, messageType);
    }

    public static boolean openWebpage(String url) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL(url).toURI());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int countFiles(File dir, String... extensions) {
        int counter = 0;
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                counter += countFiles(file, extensions);
            } else if (hasFileExtension(file.getAbsolutePath(), extensions)) {
                counter++;
            }
        }
        return counter;
    }

    public static boolean hasFileExtension(String file, String... extensions) {
        int lastIndex = file.lastIndexOf('.');
        if (lastIndex > 0) {
            String fileExtension = file.substring(lastIndex + 1);
            for (String extension : extensions) {
                if (extension.equalsIgnoreCase(fileExtension)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<File> getFiles(File dir, String... extensions) {
        ArrayList<File> files = new ArrayList<>();
        File[] inDir = dir.listFiles();
        assert inDir != null;
        for (File file : inDir) {
            if(hasFileExtension(file.getAbsolutePath(), extensions)) {
                files.add(file);
            }
        }
        return files;
    }
}
