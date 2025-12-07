package ui.main.edit;

import main.VocManager;
import main.Word;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EditWordDialog extends JDialog {
    JTextField engField;
    JTextField valueField;
    JComboBox<String> optionBox;
    MainFrame mainFrame;
    VocManager vm;

    private class Action extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) action();
        }
    }

    public EditWordDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"단어 수정",true);

        this.mainFrame = mainFrame;
        this.vm = vm;
        setLayout(new GridLayout(4,2,10,10));
        setSize(450,200);
        setLocationRelativeTo(mainFrame);

        JLabel engLabel = new JLabel("수정할 단어:");
        engField = new JTextField();

        JLabel optionLabel = new JLabel("수정 옵션:");
        String[] options = {"영단어 수정", "한글뜻 추가", "한글뜻 전체 수정"};
        optionBox = new JComboBox<>(options);

        JLabel valueLabel = new JLabel("새로운 값:");
        valueField = new JTextField();

        JButton applyBtn = new JButton("적용");
        JButton cancelBtn = new JButton("취소");

        add(engLabel);
        add(engField);
        add(optionLabel);
        add(optionBox);
        add(valueLabel);
        add(valueField);
        add(applyBtn);
        add(cancelBtn);

        applyBtn.addActionListener(e -> action());
        optionBox.addKeyListener(new Action());
        valueField.addKeyListener(new Action());
        engField.addKeyListener(new Action());

        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    public void action() {
        String eng = engField.getText().trim();
        Word targetWord = vm.getVoc().get(eng);
        if (targetWord == null) {
            JOptionPane.showMessageDialog(this, "단어를 찾을 수 없습니다.");
            return;
        }

        String value = valueField.getText().trim();
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "값을 입력하세요!");
            return;
        }

        int selected = optionBox.getSelectedIndex();
        switch (selected) {
            case 0 -> { // 영단어 수정
                String newEng = value;
                int originalIndex = vm.getOrderedEnglish().indexOf(targetWord.getEng());
                vm.voc.remove(targetWord.getEng(), targetWord);
                vm.orderedEnglish.remove(originalIndex);

                targetWord.setEng(newEng);
                vm.voc.put(newEng, targetWord);
                vm.orderedEnglish.insertElementAt(newEng, originalIndex);
                JOptionPane.showMessageDialog(this, "영단어가 수정되었습니다.");
            }
            case 1 -> { // 한글뜻 추가
                targetWord.setKor(value, false); // 기존 뜻 유지하며 추가
                JOptionPane.showMessageDialog(this, "뜻이 추가되었습니다.");
            }
            case 2 -> { // 한글뜻 전체 수정
                targetWord.setKor(value); // 기존 뜻 모두 대체
                JOptionPane.showMessageDialog(this, "뜻이 전체 수정되었습니다.");
            }
            default -> JOptionPane.showMessageDialog(this, "잘못된 옵션입니다.");
        }

        mainFrame.initCardWordsLayout();
        dispose();
    }
}
