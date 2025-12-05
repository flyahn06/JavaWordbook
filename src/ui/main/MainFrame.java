package ui.main;

import main.VocManager;
import ui.main.edit.AddWordDialog;
import ui.main.edit.DeleteWordDialog;
import ui.main.edit.EditWordDialog;
import ui.main.file.LoadFileDialog;
import ui.main.file.SaveFileDialog;
import ui.main.quiz.QuizDialog;
import ui.main.quiz.QuizTop10Dialog;
import ui.main.search.PartialSearchDialog;
import ui.main.search.PrintAllWordsDialog;
import ui.main.search.SearchWordDialog;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    Container frame = this.getContentPane();
    JPanel cardWordsPanel;
    VocManager vm;

    public MainFrame(VocManager vm) {
        this.vm = vm;
        setTitle(vm.getUserName() + "님의 단어장");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initLayout();
    }

    private void initLayout() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.add(createMenuSection("검색",
                new String[]{"단어 검색", "부분 검색", "전체 단어 출력"},
                new Runnable[]{
                        () -> new SearchWordDialog(this, vm),
                        () -> new PartialSearchDialog(this, vm),
                        () -> new PrintAllWordsDialog(this, vm)
                }
        ));

        menuPanel.add(createMenuSection("수정",
                new String[]{"단어 추가", "단어 수정", "단어 삭제"},
                new Runnable[]{
                        () -> new AddWordDialog(this, vm),
                        () -> new EditWordDialog(this, vm),
                        () -> new DeleteWordDialog(this, vm)
                }
        ));

        menuPanel.add(createMenuSection("퀴즈",
                new String[]{"퀴즈 풀기", "오답률 Top10 집중학습"},
                new Runnable[]{
                        () -> new QuizDialog(this, vm),
                        () -> new QuizTop10Dialog(this, vm)
                }
        ));

        menuPanel.add(createMenuSection("파일",
                new String[]{"파일 저장", "파일 불러오기"},
                new Runnable[]{
                        () -> new SaveFileDialog(this, vm),
                        () -> new LoadFileDialog(this, vm)
                }
        ));
        
        initCardWordsLayout();

        this.frame.add(new JScrollPane(menuPanel), BorderLayout.WEST);
    }

    private void initCardWordsLayout() {
        this.cardWordsPanel = new JPanel(new GridLayout(5, 5));

        this.updateCardPanel();

        this.frame.add(new JScrollPane(this.cardWordsPanel), BorderLayout.CENTER);
    }

    private void updateCardPanel() {
        CardEntity card1 = new CardEntity(this.vm.getVoc().get("consider"));
        CardEntity card2 = new CardEntity(this.vm.getVoc().get("law"));
        CardEntity card3 = new CardEntity(this.vm.getVoc().get("considerable"));

        this.cardWordsPanel.add(card1.entity);
        this.cardWordsPanel.add(card2.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
        this.cardWordsPanel.add(card3.entity);
    }

    private JPanel createMenuSection(String title, String[] subTitles, Runnable[] actions) {

        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));

        // 상단메뉴 버튼
        JButton menuBtn = new JButton(title + " ▼");
        menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 서브메뉴 패널
        JPanel submenuPanel = new JPanel();
        submenuPanel.setLayout(new BoxLayout(submenuPanel, BoxLayout.Y_AXIS));
        submenuPanel.setVisible(false);
        submenuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < subTitles.length; i++) {
            JButton btn = new JButton("   · " + subTitles[i]);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            final int idx = i;
            btn.addActionListener(e -> actions[idx].run());

            submenuPanel.add(btn);
        }

        menuBtn.addActionListener(e -> {
            boolean visible = submenuPanel.isVisible();
            submenuPanel.setVisible(!visible);

            menuBtn.setText(title + (visible ? " ▼" : " ▲"));

            SwingUtilities.getWindowAncestor(menuBtn).validate();
        });

        section.add(menuBtn);
        section.add(submenuPanel);

        return section;
    }
}
