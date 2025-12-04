package ui.main.search;

import main.VocManager;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SearchWordDialog extends JDialog {
    public SearchWordDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame, "단어 검색", true);

        setSize(400, 200);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel();
        JTextField wordField = new JTextField(15);
        JButton searchButton = new JButton("검색");

        inputPanel.add(new JLabel("영단어: "));
        inputPanel.add(wordField);
        inputPanel.add(searchButton);

        JLabel resultLabel = new JLabel(" ", SwingConstants.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(resultLabel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String eng = wordField.getText().trim();
            String kor = vm.searchVoc(eng);

            if (kor != null)
                resultLabel.setText("단어의 뜻: " + kor);
            else
                resultLabel.setText("단어를 찾을 수 없습니다.");
        });

        setVisible(true);

    }
}
