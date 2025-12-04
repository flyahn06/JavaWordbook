package ui.main.search;

import main.VocManager;
import ui.main.MainFrame;

import javax.swing.*;

public class PrintAllWordsDialog extends JDialog {
    public PrintAllWordsDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame,"전체 단어 출력",true);

        setSize(400, 400);
        setLocationRelativeTo(mainFrame);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        for (String eng : vm.getOrderedEnglish()) {
            area.append(vm.getVoc().get(eng).toString() + "\n");
        }

        add(new JScrollPane(area));
        setVisible(true);
    }
}
