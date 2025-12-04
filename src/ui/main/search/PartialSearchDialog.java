package ui.main.search;

import main.VocManager;
import main.Word;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;

public class PartialSearchDialog extends JDialog {
    public PartialSearchDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"부분 검색",true);

        setSize(400, 350);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        JTextField searchField = new JTextField(15);
        JButton btn = new JButton("검색");

        top.add(new JLabel("검색어:"));
        top.add(searchField);
        top.add(btn);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        btn.addActionListener(e -> {
            String sWord = searchField.getText().trim();
            area.setText("");

            boolean found = false;

            for (Word w : vm.getVoc().values()) {
                if (w.getEng().indexOf(sWord) == 0) {
                    area.append(w.toString() + "\n");
                    found = true;
                }
            }

            if (!found)
                area.setText("찾으시는 단어가 없습니다.");
        });

        setVisible(true);
    }
}
