package ui.chartUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * scores.txt 파일에서 데이터를 읽어 정답률(rate)을 기준으로 막대 그래프를 생성하는 UI 애플리케이션
 * JFreeChart 라이브러리가 필요합니다.
 */

public class ScoreChartUI extends JFrame {

    private static final String FILE_NAME = "res/scores.txt";
    private JComboBox<String> detailComboBox;

    // 1. 원본 데이터를 저장할 멤버 변수
    private List<ScoreData> originalDataList;

    // 2. 차트 패널을 교체하기 위해 멤버 변수로 선언
    private ChartPanel chartPanel;

    // 파일에서 읽어온 데이터를 저장할 클래스
    private static class ScoreData {
        int idx;
        String name;
        String filename;
        double rate; // 정답률

        public ScoreData(int idx, String name, String filename, double rate) {
            this.idx = idx;
            this.name = name;
            this.filename = filename;
            this.rate = rate;
        }
    }

    public ScoreChartUI() {
        super("정답률 그래프");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. 데이터 읽기 및 저장
        this.originalDataList = readDataFromFile(FILE_NAME);
        List<String> comboboxItems = createUniqueNameFileCombinations(originalDataList);

        // 2. 콤보박스 생성
        String[] displayItems = comboboxItems.toArray(new String[0]);
        detailComboBox = new JComboBox<>(displayItems);
        detailComboBox.addActionListener(e -> updateDisplay());

        // 3. UI 구성
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("기록 선택:"));
        controlPanel.add(detailComboBox);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 4. 초기 그래프 생성 및 패널에 추가
        DefaultCategoryDataset initialDataset = createDataset(originalDataList);
        JFreeChart initialChart = createChart(initialDataset, "전체 사용자 정답률");

        this.chartPanel = new ChartPanel(initialChart);
        this.chartPanel.setPreferredSize(new Dimension(800, 600));

        // 데이터 로드 상태 표시
        JLabel statusLabel = new JLabel("총 " + originalDataList.size() + "개의 데이터 로드됨");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentPane.add(this.chartPanel, BorderLayout.CENTER);
        contentPane.add(statusLabel, BorderLayout.SOUTH);
        contentPane.add(controlPanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);

        // 초기 데이터 로드 후, 콤보박스 첫 항목으로 그래프를 업데이트 (선택 사항)
        if (displayItems.length > 0) {
            detailComboBox.setSelectedIndex(0);
        }
    }

    /**
     * 콤보박스 선택이 변경될 때 호출되며, 필터링된 데이터로 차트를 업데이트합니다.
     */
    private void updateDisplay() {
        String selectedItem = (String) detailComboBox.getSelectedItem();
        if (selectedItem == null) return;

        // 1. 선택 항목 파싱: "이름-파일명" -> [이름, 파일명]
        String[] parts = selectedItem.split("-");
        if (parts.length != 2) return;

        String targetName = parts[0];
        String targetFilename = parts[1];

        // 2. 원본 데이터 필터링
        List<ScoreData> filteredList = originalDataList.stream()
                .filter(data -> data.name.equals(targetName) && data.filename.equals(targetFilename))
                .collect(Collectors.toList());

        // 3. 필터링된 데이터로 새 데이터셋 및 차트 생성
        DefaultCategoryDataset newDataset = createDatasetForFilteredData(filteredList);

        // 4. 차트 제목 설정
        String newTitle = targetName + " (" + targetFilename + ") 정답률 기록";
        JFreeChart newChart = createChart(newDataset, newTitle);

        // 5. ChartPanel에 새 차트 설정
        this.chartPanel.setChart(newChart);
    }

    /**
     * 필터링된 단일 조합 데이터로 데이터셋을 생성합니다.
     * (항목이 여러 개일 수 있으므로, 각 기록을 별도의 카테고리로 표시합니다.)
     */
    private DefaultCategoryDataset createDatasetForFilteredData(List<ScoreData> dataList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 단일 조합을 선택했으므로, 이 목록에는 중복된 name-filename 쌍이 포함됩니다.
        // 이를 개별 항목으로 구분하여 표시하기 위해 idx를 함께 사용합니다.
        for (ScoreData data : dataList) {
            // 카테고리를 (idx)로 설정하여 해당 기록을 고유하게 식별
            String category = String.format("(idx: %d)", data.idx);
            String series = data.name + "-" + data.filename;

            // 정답률 (rate) 값 추가
            dataset.addValue(data.rate, series, category);
        }
        return dataset;
    }



    /**
     * 파일에서 데이터를 읽어 ScoreData 리스트로 반환
     */
    private List<ScoreData> readDataFromFile(String fileName) {
        List<ScoreData> dataList = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            System.err.println("오류: 파일을 찾을 수 없습니다. " + fileName);
            JOptionPane.showMessageDialog(this, "파일을 찾을 수 없습니다: " + fileName, "오류", JOptionPane.ERROR_MESSAGE);
            return dataList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 4) {
                    try {
                        int idx = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String filename = parts[2].trim();
                        double rate = Double.parseDouble(parts[3].trim());

                        dataList.add(new ScoreData(idx, name, filename, rate * 100)); //rate를 %로 변경
                    } catch (NumberFormatException e) {
                        System.err.println("데이터 변환 오류 발생: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "파일 읽기 중 오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
        return dataList;
    }

    public List<String> createUniqueNameFileCombinations(List<ScoreData> dataList) {
        Set<String> uniqueSet = new HashSet<>();
        for (ScoreData data : dataList) {
            String combination = data.name + "-" + data.filename;
            uniqueSet.add(combination);
        }
        List<String> resultList = new ArrayList<>(uniqueSet);
        Collections.sort(resultList);
        return resultList;
    }

    /**
     * (초기 화면에서만 사용) 모든 데이터를 표시하는 데이터셋 생성
     */
    private DefaultCategoryDataset createDataset(List<ScoreData> dataList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series = "정답률 (%)";

        for (ScoreData data : dataList) {
            // 카테고리: 이름 (파일명)
            String category = data.name + " (" + data.filename + ")";
            dataset.addValue(data.rate, series, category);
        }
        return dataset;
    }

    /**
     * 데이터셋과 제목을 받아 차트 객체 생성
     */
    private JFreeChart createChart(DefaultCategoryDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createBarChart(
                title, // 동적으로 제목 설정
                "기록 항목", // X축 레이블
                "정답률 (%)", // Y축 레이블
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        setChartFont(chart, new Font("Malgun Gothic", Font.PLAIN, 12));

        return chart;
    }

    private void setChartFont(JFreeChart chart, Font font) {
        if (chart.getTitle() != null) {
            chart.getTitle().setFont(font.deriveFont(Font.BOLD, 16f));
        }
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(font.deriveFont(12f));
        }
        if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            if (plot.getDomainAxis() != null) {
                plot.getDomainAxis().setLabelFont(font.deriveFont(Font.BOLD, 14f));
                plot.getDomainAxis().setTickLabelFont(font.deriveFont(12f));
            }
            if (plot.getRangeAxis() != null) {
                plot.getRangeAxis().setLabelFont(font.deriveFont(Font.BOLD, 14f));
                plot.getRangeAxis().setTickLabelFont(font.deriveFont(12f));
            }
            CategoryItemRenderer renderer = plot.getRenderer();
            if (renderer != null && renderer.getBaseItemLabelFont() != null) {
                renderer.setBaseItemLabelFont(font.deriveFont(12f));
            }
        }
    }

    public static void main(String[] args) {
        new ScoreChartUI().setVisible(true);
    }
}