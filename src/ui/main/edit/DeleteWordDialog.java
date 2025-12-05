package ui.main.edit;

import main.VocManager;
import main.Word;
import ui.main.MainFrame;

import javax.swing.*;

public class DeleteWordDialog extends JDialog {
    public DeleteWordDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"단어 삭제",true);

        setSize(300, 150);
        setLocationRelativeTo(mainFrame);

        JTextField textField = new JTextField(10);
        JButton deleteButton = new JButton("삭제");

        JPanel panel = new JPanel();
        panel.add(new JLabel("삭제할 영단어: "));
        panel.add(textField);
        panel.add(deleteButton);
        add(panel);

        deleteButton.addActionListener(e -> {
            String eng = textField.getText().trim();
            Word targetWord = vm.getVoc().get(eng);

            if (targetWord == null) {
                JOptionPane.showMessageDialog(this, "단어를 찾을 수 없습니다.");
                return;
            } else {
                vm.voc.remove(eng,targetWord);
                vm.orderedEnglish.remove(eng);
                JOptionPane.showMessageDialog(this, "단어가 삭제되었습니다.");
            }

            mainFrame.initCardWordsLayout();
            dispose();
        });

        setVisible(true);
    }
}
