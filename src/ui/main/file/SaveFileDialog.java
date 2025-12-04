package ui.main.file;

import main.VocManager;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SaveFileDialog extends JDialog {
    public SaveFileDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"파일 저장",true);

        setSize(400, 200);
        setLocationRelativeTo(mainFrame);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel("파일 저장 옵션을 선택하세요:", SwingConstants.CENTER);

        JButton overwriteBtn = new JButton("현재 파일에 덮어쓰기");
        JButton newFileBtn = new JButton("새로운 파일로 저장하기");

        add(label);
        add(overwriteBtn);
        add(newFileBtn);

        overwriteBtn.addActionListener(e -> {
            try(PrintWriter outfile = new PrintWriter(vm.getFileName())) {
                vm.fileWriter(outfile);
                JOptionPane.showMessageDialog(this,"현재 파일에 저장되었습니다.");
            } catch (FileNotFoundException error) {
                JOptionPane.showMessageDialog(this,"파일 저장에 문제가 발생했습니다.");
            }
            dispose();
        });

        newFileBtn.addActionListener(e -> {
            String filename = JOptionPane.showInputDialog(
                    this,
                    "새 파일의 이름을 입력하세요: ",
                    "새 파일 저장",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (filename == null || filename.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "파일명이 올바르지 않습니다.");
                return;
            }

            filename = filename.trim().replaceAll("\\s+", "_");

            if (!(filename.indexOf(".txt") == filename.length() - 4))
                filename = filename + ".txt";

            filename = "res/" + filename;

            try(PrintWriter outfile = new PrintWriter(filename)) {
                vm.fileWriter(outfile);
                JOptionPane.showMessageDialog(this,"새로운 파일에 저장되었습니다.");
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this,"파일 저장에 문제가 발생했습니다.");
            }
            dispose();
        });

        setVisible(true);
    }
}
