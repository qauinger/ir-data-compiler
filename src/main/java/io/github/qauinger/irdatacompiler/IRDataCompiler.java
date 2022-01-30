package main.java.io.github.qauinger.irdatacompiler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IRDataCompiler extends JFrame {

    public static final String[] extensions = new String[]{"txt", "dpt", "csv"};

    private JPanel contentPane;
    private JTextField sourceField;
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

        setTitle("IRDataCompiler - v1.0");
        setIconImage(icon);
        setResizable(false);
        setSize(500, 170);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        contentPane = new JPanel();
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
        sourceOpen.addActionListener(e -> {
            File directory = Utils.chooseFolder("Select a Folder", ".");
            if (directory != null)
                sourceField.setText(directory.toString());
        });
        contentPane.add(sourceOpen);

        JLabel sortLabel = new JLabel("Sort Order:");
        sortLabel.setBounds(10, 40, 110, 22);
        sortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(sortLabel);

        ButtonGroup sortGroup = new ButtonGroup();

        JRadioButton sortAscending = new JRadioButton("Ascending");
        sortAscending.setBounds(130, 40, 100, 22);
        sortAscending.setActionCommand("ascending");
        sortAscending.setSelected(true);
        sortGroup.add(sortAscending);
        contentPane.add(sortAscending);

        JRadioButton sortDescending = new JRadioButton("Descending");
        sortDescending.setBounds(230, 40, 100, 22);
        sortDescending.setActionCommand("descending");
        sortGroup.add(sortDescending);
        contentPane.add(sortDescending);

        JLabel optionsLabel = new JLabel("Options:");
        optionsLabel.setBounds(10, 70, 110, 22);
        optionsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(optionsLabel);

        JCheckBox normalizeCheckBox = new JCheckBox("Normalize");
        normalizeCheckBox.setBounds(130, 70, 100, 22);
        normalizeCheckBox.setToolTipText("Normalize values.");
        contentPane.add(normalizeCheckBox);

        JButton githubLink = new JButton("Qauinger");
        githubLink.setBounds(-8, 100, 110, 22);
        githubLink.setIcon(new ImageIcon(octocat));
        githubLink.setToolTipText("https://github.com/qauinger/");
        githubLink.setHorizontalAlignment(SwingConstants.LEFT);
        githubLink.setOpaque(false);
        githubLink.setContentAreaFilled(false);
        githubLink.setBorderPainted(false);
        githubLink.addActionListener(e -> Utils.openWebpage("https://github.com/qauinger/"));
        contentPane.add(githubLink);

        JButton compileButton = new JButton("Compile");
        compileButton.setBounds(192, 100, 100, 22);
        compileButton.addActionListener(e -> {
            File dir = new File(sourceField.getText());
            if(dir.isDirectory()) {
                int fileCount = Utils.countFiles(dir, extensions);
                if(fileCount < 2) {
                    Utils.showDialog(getTitle(), "There must be 2 or more files to compile in directory.", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                } else {
                    String message = "Compile " + fileCount + " files?";
                    if(normalizeCheckBox.isSelected())
                        message = message + "\n • Normalize";
                    int result = Utils.showDialog(getTitle(), message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if(result == JOptionPane.OK_OPTION) {
                        Compiler.compile(dir, sortGroup.getSelection().getActionCommand().equals("ascending") ? Compiler.sort.ASCENDING : Compiler.sort.DESCENDING, normalizeCheckBox.isSelected());
                    }
                }
            } else {
                Utils.showDialog(getTitle(), "You must supply a valid source directory.", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
        contentPane.add(compileButton);
    }
}
