package ui.main.file;

import main.VocManager;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class LoadFileDialog extends JDialog {
    public LoadFileDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"파일 불러오기",true);

        setSize(400, 220);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout(10,10));

        JLabel msg = new JLabel("현재 파일의 변경사항을 저장하시겠습니까?", SwingConstants.CENTER);

        JButton yesBtn = new JButton("예 (저장)");
        JButton noBtn = new JButton("아니오 (저장 안 함)");

        JPanel btnPanel = new JPanel();
        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);

        add(msg,BorderLayout.CENTER);
        add(btnPanel,BorderLayout.SOUTH);

        yesBtn.addActionListener(e -> {
            try(PrintWriter outfile = new PrintWriter(vm.getFileName())) {
                vm.fileWriter(outfile);
                JOptionPane.showMessageDialog(this,"현재 파일에 저장되었습니다.");
            } catch (FileNotFoundException error) {
                JOptionPane.showMessageDialog(this,"파일 저장에 문제가 발생했습니다.");
            }
            fileLoad(vm);
            dispose();
        });

        noBtn.addActionListener(e -> {
            fileLoad(vm);
            dispose();
        });

        setVisible(true);
    }

    private void fileLoad(VocManager vm) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("불러올 단어장 파일 선택");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            boolean ok = vm.makeVoc(file.getPath());
            if (ok)
                JOptionPane.showMessageDialog(this, "파일을 성공적으로 불러왔습니다.");
            else
                JOptionPane.showMessageDialog(this, "파일을 읽는 중 오류가 발생했습니다.");
        }
    }
}
