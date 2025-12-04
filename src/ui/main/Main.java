package ui.main;

import main.VocManager;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        String name = JOptionPane.showInputDialog(null, "이름을 입력하세요:", "사용자 이름", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "이름을 입력해야 합니다.");
            System.exit(0);
        }

        int choice = JOptionPane.showOptionDialog(
                null,
                "단어장을 선택하세요",
                "단어장 선택",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"기본 단어장", "나만의 단어장"},
                "기본 단어장"
        );

        String filePath = "res/words.txt"; // 기본 단어장

        if (choice == 1) { // 나만의 단어장
            JFileChooser fileChooser = new JFileChooser();
            int fcResult = fileChooser.showOpenDialog(null);
            if (fcResult == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();
            } else {
                JOptionPane.showMessageDialog(null, "파일을 선택하지 않아 기본 단어장을 사용합니다.");
            }
        }

        VocManager manager = new VocManager(name);
        boolean loaded = manager.makeVoc(filePath);
        if (!loaded) {
            JOptionPane.showMessageDialog(null, "단어장을 불러오는 데 실패했습니다. 프로그램을 종료합니다.");
            System.exit(0);
        }

        MainFrame mainFrame = new MainFrame(manager);
        mainFrame.setVisible(true);
    }
}
