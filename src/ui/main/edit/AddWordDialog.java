package ui.main.edit;

import main.VocManager;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AddWordDialog extends JDialog {
    public AddWordDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"단어 추가",true);

        setLayout(new GridLayout(3, 2, 10, 10));
        setSize(300, 150);
        setLocationRelativeTo(mainFrame);

        JLabel engLabel = new JLabel("추가할 단어:");
        JTextField engField = new JTextField();
        JLabel korLabel = new JLabel("뜻 (빈칸이면 검색):");
        JTextField korField = new JTextField();

        JButton addBtn = new JButton("추가");
        JButton cancelBtn = new JButton("취소");

        add(engLabel);
        add(engField);
        add(korLabel);
        add(korField);
        add(addBtn);
        add(cancelBtn);

        addBtn.addActionListener(e -> {
            String eng = engField.getText().trim();
            String kor = korField.getText().trim();

            if (eng.isEmpty()) {
                JOptionPane.showMessageDialog(this, "단어를 잘 입력하세요!");
                return;
            }

            if (vm.getVoc().get(eng) != null) {
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "[주의] 단어가 이미 존재합니다.\n기존 단어를 대체하시겠습니까?",
                        "단어 중복",
                        JOptionPane.YES_NO_OPTION
                );
                if (option != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            if (kor.isEmpty() || kor.equals("?")) {
                kor = VocManager.translator.getMeaning(eng);
                if (kor == null) {
                    JOptionPane.showMessageDialog(this, "단어의 뜻을 찾을 수 없습니다!");
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "검색된 뜻: " + kor);
                }
            }


            vm.addWord(eng, kor, 0);
            JOptionPane.showMessageDialog(this, "단어가 잘 추가되었습니다.");
            mainFrame.initCardWordsLayout();
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }
}
