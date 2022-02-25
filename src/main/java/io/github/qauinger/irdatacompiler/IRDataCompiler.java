package main.java.io.github.qauinger.irdatacompiler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IRDataCompiler extends JFrame {

    private static final String title = "IRDataCompiler - v1.1";

    public static final String[] extensions = new String[]{"txt", "dpt", "csv"};

    private final JTextField sourceField;
    private final JTextField outputField;
    BufferedImage icon;
    BufferedImage octocat;

    public static void main(String[] args) {
        IRDataCompiler window = new IRDataCompiler();
        window.setVisible(true);
    }

    public IRDataCompiler() {
        try {
            icon = ImageIO.read(ClassLoader.getSystemResource("icon.png"));
            octocat = ImageIO.read(ClassLoader.getSystemResource("octocat.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        setTitle(title);
        setIconImage(icon);
        setResizable(false);
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel sourceLabel = new JLabel("Source Directory:");
        sourceLabel.setBounds(10, 10, 110, 22);
        sourceLabel.setToolTipText("File path to the folder containing the files you wish to compile.");
        sourceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(sourceLabel);

        sourceField = new JTextField();
        sourceField.setBounds(130, 10, 260, 22);
        contentPane.add(sourceField);
        sourceField.setColumns(10);

        JButton sourceOpen = new JButton("Select");
        sourceOpen.setBounds(400, 10, 75, 22);
        sourceOpen.setFocusPainted(false);
        sourceOpen.addActionListener(e -> {
            File directory = Utils.chooseFolder("Select a Folder", sourceOpen.getText());
            if (directory != null)
                sourceField.setText(directory.toString());
        });
        contentPane.add(sourceOpen);

        JLabel outputLabel = new JLabel("Output Directory:");
        outputLabel.setBounds(10, 40, 110, 22);
        outputLabel.setToolTipText("File path to the folder where the compiled file will be dumped.");
        outputLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(outputLabel);

        outputField = new JTextField();
        outputField.setBounds(130, 40, 260, 22);
        contentPane.add(outputField);
        outputField.setColumns(10);

        JButton outputOpen = new JButton("Select");
        outputOpen.setBounds(400, 40, 75, 22);
        outputOpen.setFocusPainted(false);
        outputOpen.addActionListener(e -> {
            File directory = Utils.chooseFile("Select an Output File", outputOpen.getText());
            if (directory != null)
                outputField.setText(directory.toString());
        });
        contentPane.add(outputOpen);

        JLabel sortLabel = new JLabel("Sort Order:");
        sortLabel.setBounds(10, 70, 110, 22);
        sortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(sortLabel);

        ButtonGroup sortGroup = new ButtonGroup();

        JRadioButton sortAscending = new JRadioButton("Ascending");
        sortAscending.setBounds(130, 70, 100, 22);
        sortAscending.setActionCommand("ascending");
        sortAscending.setSelected(true);
        sortAscending.setFocusPainted(false);
        sortGroup.add(sortAscending);
        contentPane.add(sortAscending);

        JRadioButton sortDescending = new JRadioButton("Descending");
        sortDescending.setBounds(230, 70, 100, 22);
        sortDescending.setActionCommand("descending");
        sortDescending.setFocusPainted(false);
        sortGroup.add(sortDescending);
        contentPane.add(sortDescending);

        JLabel optionsLabel = new JLabel("Options:");
        optionsLabel.setBounds(10, 100, 110, 22);
        optionsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(optionsLabel);

        JCheckBox normalizeCheckBox = new JCheckBox("Normalize");
        normalizeCheckBox.setBounds(130, 100, 100, 22);
        normalizeCheckBox.setToolTipText("Normalize values.");
        normalizeCheckBox.setFocusPainted(false);
        contentPane.add(normalizeCheckBox);

        JButton githubLink = new JButton("qauinger");
        githubLink.setBounds(-8, 130, 110, 22);
        githubLink.setIcon(new ImageIcon(octocat));
        githubLink.setToolTipText("https://github.com/qauinger/");
        githubLink.setHorizontalAlignment(SwingConstants.LEFT);
        githubLink.setOpaque(false);
        githubLink.setContentAreaFilled(false);
        githubLink.setBorderPainted(false);
        githubLink.setFocusPainted(false);
        githubLink.addActionListener(e -> {
            if(!Utils.openWebpage("https://github.com/qauinger/")) {
                Utils.showDialog("GitHub", "qauinger on GitHub:\nhttps://github.com/qauinger", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            }
        });

        contentPane.add(githubLink);

        JButton compileButton = new JButton("Compile");
        compileButton.setBounds(192, 130, 100, 22);
        compileButton.setFocusPainted(false);
        compileButton.addActionListener(e -> {
            File dir = new File(sourceField.getText());
            File output = new File(outputField.getText());
            if(dir.isDirectory()) {
                if(output.isDirectory() || output.exists()) {
                    int fileCount = Utils.countFiles(dir, extensions);
                    if(fileCount < 2) {
                        Utils.showDialog(getProgramTitle(), "There must be 2 or more files to compile in directory.", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                    } else {
                        String message = "Compile " + fileCount + " files?";
                        if(normalizeCheckBox.isSelected())
                            message = message + "\n • Normalize";
                        int result = Utils.showDialog(getProgramTitle(), message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        if(result == JOptionPane.OK_OPTION) {
                            if(!Compiler.compile(dir, output, sortGroup.getSelection().getActionCommand().equals("ascending") ? Compiler.Sort.ASCENDING : Compiler.Sort.DESCENDING, normalizeCheckBox.isSelected())) {
                                Utils.showDialog(getProgramTitle(), "An unexpected error has occured.", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else {
                    Utils.showDialog(getProgramTitle(), "You must supply a valid output directory. This may be a folder or a file.", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Utils.showDialog(getProgramTitle(), "You must supply a valid source directory.", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPane.add(compileButton);
    }

    public static String getProgramTitle() {
        return title;
    }
}
