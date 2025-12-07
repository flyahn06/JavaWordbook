package ui.main.edit;

import main.VocManager;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddWordDialog extends JDialog {
    MainFrame mainFrame;
    VocManager vm;
    JTextField engField;
    JTextField korField;

    private class GetMeaningWorker extends SwingWorker<String, Void> {
        private final String eng;

        public GetMeaningWorker(String eng) {
            this.eng = eng;
        }

        @Override
        protected String doInBackground() {
            return VocManager.translator.getMeaning(eng);
        }

        @Override
        protected void done() {
            try {
                String kor = get();

                if (kor == null) {
                    JOptionPane.showMessageDialog(AddWordDialog.this, "단어의 뜻을 찾을 수 없습니다!");
                    engField.setText("");
                    korField.setText("");
                } else {
                    korField.setText(kor);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(AddWordDialog.this, "오류가 발생했습니다.");
            }
        }
    }

    public AddWordDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"단어 추가",true);

        this.mainFrame = mainFrame;
        this.vm = vm;
        setLayout(new GridLayout(3, 2, 10, 10));
        setSize(300, 150);
        setLocationRelativeTo(mainFrame);

        JLabel engLabel = new JLabel("추가할 단어:");
        engField = new JTextField();
        JLabel korLabel = new JLabel("뜻 (빈칸이면 검색):");
        korField = new JTextField();

        JButton addBtn = new JButton("추가");
        JButton cancelBtn = new JButton("취소");

        add(engLabel);
        add(engField);
        add(korLabel);
        add(korField);
        add(addBtn);
        add(cancelBtn);

        addBtn.addActionListener(e -> action());
        engField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    action();
            }
        });
        korField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    action();
            }
        });

        getContentPane().requestFocusInWindow();
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    public void action() {
        String eng = engField.getText().trim();
        String kor = korField.getText().trim();

        if (eng.isEmpty()) {
            JOptionPane.showMessageDialog(this, "단어를 잘 입력하세요!");
            return;
        }

        if (kor.trim().equals("검색 중...")) {
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
            this.korField.setText("검색 중...");
            new GetMeaningWorker(eng).execute();
            return;
        }

        vm.addWord(eng, kor, 0);
        JOptionPane.showMessageDialog(this, "단어가 잘 추가되었습니다.");
        mainFrame.initCardWordsLayout();
        dispose();
    }
}
