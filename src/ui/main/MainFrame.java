package ui.main;

import java.util.List;
import main.VocManager;
import main.Word;
import ui.game.rain.RainFrame;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class MainFrame extends JFrame {
    Container frame = this.getContentPane();
    JPanel cardWordsPanel;
    VocManager vm;
    JScrollPane scrollPane;
    Vector<WordCard> wordCards;

    public MainFrame(VocManager vm) {
        this.vm = vm;
        this.wordCards = new Vector<>();
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

        menuPanel.add(createMenuSection("퀴즈 / 게임",
                new String[]{"퀴즈 풀기", "오답률 Top10 집중학습", "산성비"},
                new Runnable[]{
                        () -> new QuizDialog(this, vm),
                        () -> new QuizTop10Dialog(this, vm),
                        () -> new RainFrame(this, vm)
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
        initSearchLayout();

        this.frame.add(new JScrollPane(menuPanel), BorderLayout.WEST);
    }

    private void initSearchLayout() {
        JPanel northPanel = new JPanel();

        JTextField engField = new JTextField(30);
        JButton searchButton = new JButton("검색");

        engField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            public void update() {
                String searchText = engField.getText().trim().toLowerCase();

                if (searchText.isEmpty()) {
                    for (WordCard wordCard: wordCards)
                        wordCard.setVisible(true);
                }

                for (WordCard wordCard: wordCards) {
                    wordCard.setVisible(wordCard.word.getEng().indexOf(searchText) == 0);
                }

                revalidate();
                repaint();
            }
        });

        searchButton.addActionListener(e -> {
            String searchText = engField.getText().trim().toLowerCase();

            if (searchText.isEmpty()) {
                for (WordCard wordCard: this.wordCards)
                    wordCard.setVisible(true);
            }

            for (WordCard wordCard: this.wordCards) {
                wordCard.setVisible(wordCard.word.getEng().indexOf(searchText) == 0);
            }

            revalidate();
            repaint();
        });

        northPanel.add(new JLabel("검색"));
        northPanel.add(engField);
        northPanel.add(searchButton);

        this.frame.add(northPanel, BorderLayout.NORTH);

    }

    public void initCardWordsLayout() {
        // 1. 세로로 구간들을 쌓을 메인 패널 생성
        JPanel mainListPanel = new JPanel();
        mainListPanel.setLayout(new BoxLayout(mainListPanel, BoxLayout.Y_AXIS));
        mainListPanel.setBackground(Color.WHITE);

        // 2. 스크롤 패널에 메인 패널 담기
        if (scrollPane != null) {
            this.frame.remove(scrollPane);
        }
        scrollPane = new JScrollPane(mainListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 스크롤 속도
        this.frame.add(scrollPane, BorderLayout.CENTER);

        // 3. VocManager에서 단어 데이터 가져오기
        List<Word> allWords = vm.getAllWords();

        if (allWords == null || allWords.isEmpty()) {
            JLabel emptyLabel = new JLabel("단어가 없습니다. 단어를 추가해주세요.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
            mainListPanel.add(emptyLabel);
            return;
        }

        // 4. 단어를 10개씩 잘라서 구간(SectionPanel) 만들기
        int sectionSize = 10;
        int totalWords = allWords.size();
        int sectionCount = (int) Math.ceil((double) totalWords / sectionSize);

        for (int i = 0; i < sectionCount; i++) {
            int start = i * sectionSize;
            int end = Math.min(start + sectionSize, totalWords);

            // 해당 구간의 단어 리스트 추출
            List<Word> subList = allWords.subList(start, end);

            // 구간 패널 생성 및 추가 (내부 클래스 사용)
            SectionPanel section = new SectionPanel((i + 1) + "구간", subList);
            mainListPanel.add(section);
            mainListPanel.add(Box.createVerticalStrut(15)); // 구간 사이 여백
        }

        revalidate();
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

    // ===================================================================================
    // 내부 클래스: WordCard (단어 카드 UI)
    // ===================================================================================
    class WordCard extends JPanel {
        private final Word word; // 사용자 정의 Word 객체
        private boolean isEnglishVisible = true;
        private final JLabel label;

        public WordCard(Word word) {
            this.word = word;
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(180, 100)); // 카드 크기
            setBackground(Color.WHITE);
            setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // 초기 상태: 영어 단어 표시
            label = new JLabel(word.getEng(), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            add(label, BorderLayout.CENTER);

            // 마우스 클릭 이벤트: 뒤집기
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    toggleCard();
                }
            });
        }

        private void toggleCard() {
            isEnglishVisible = !isEnglishVisible;
            if (isEnglishVisible) {
                // 영어 보여주기
                label.setText(word.getEng());
                label.setFont(new Font("Arial", Font.BOLD, 16));
                setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            } else {
                // 한글 뜻 보여주기 (Word 클래스의 getKor() 사용)
                // getKor()은 Vector의 내용을 문자열로 합쳐서 반환함
                label.setText("<html><div style='text-align:center;'>" + word.getKor() + "</div></html>");
                label.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
                setBorder(new LineBorder(new Color(100, 200, 100), 2)); // 초록색 테두리
            }
            repaint();
        }
    }

    // ===================================================================================
    // 내부 클래스: SectionPanel (구간 UI)
    // ===================================================================================
    class SectionPanel extends JPanel {
        private final JPanel contentPanel;
        private final JButton toggleButton;

        public SectionPanel(String title, List<Word> words) {
            WordCard card;
            setLayout(new BorderLayout());
            setBackground(new Color(245, 245, 245));
            setBorder(new EmptyBorder(10, 10, 10, 10));

            // 상단 헤더 (제목 + 접기 버튼)
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.setBorder(new EmptyBorder(0, 0, 10, 0));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 16));

            toggleButton = new JButton("펼치기 ∨");
            toggleButton.setBackground(Color.WHITE);
            toggleButton.setFocusPainted(false);

            header.add(titleLabel, BorderLayout.WEST);
            header.add(toggleButton, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            // 카드 컨텐츠 패널 (4열 그리드)
            contentPanel = new JPanel(new GridLayout(0, 4, 10, 10));
            contentPanel.setOpaque(false);

            // 전달받은 Word 리스트로 카드 생성
            for (Word w : words) {
                card = new WordCard(w);
                contentPanel.add(card);
                wordCards.add(card);
            }
            add(contentPanel, BorderLayout.CENTER);

            contentPanel.setVisible(false);

            // 접기/펼치기 기능
            toggleButton.addActionListener(e -> {
                boolean isVisible = contentPanel.isVisible();
                contentPanel.setVisible(!isVisible);
                toggleButton.setText(isVisible ? "펼치기 ∨" : "접기 ∧");
                revalidate();
                repaint();
            });
        }
    }
}
