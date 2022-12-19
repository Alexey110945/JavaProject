import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Charts{
    private final Sqlite sqlite;
    private String[] titles;
    private Color[] colors;
    private String[] rowKeys;
    private String[] columnKeys;
    private String valueAxisLabel;

    public Charts(Sqlite sqlite){
        this.sqlite = sqlite;
    }

    public void SaveCharts(String path) throws IOException {
        setTitlesColorsForScores();
        saveChart(path,
                createChartScoreAllStudents("Успеваемость студентов",
                        sqlite.getMeanScoresAllStudent("")), 1500, 1500);
        saveChart(path,
                createChartScoreAllStudents("Успеваемость студентов по упражнениям",
                        sqlite.getMeanScoresAllStudent("exercises")), 1500, 1500);
        saveChart(path,
                createChartScoreAllStudents("Успеваемость студентов по практикам",
                        sqlite.getMeanScoresAllStudent("homework")), 1500, 1500);
        saveChart(path,
                createChartScoreAllStudents("Успеваемость студентов уровня Комфорт",
                        sqlite.getMeanScoresKS("У1")), 1500, 1500);
        saveChart(path,
                createChartScoreAllStudents("Успеваемость студентов уровня Спорт",
                        sqlite.getMeanScoresKS("У2")), 1500, 1500);

        setTitlesColorsForGender();
        saveChart(path,
                createChartScoreAllStudents("Распределение по полу",
                        sqlite.getGenderCount()), 1500, 1500);
        setKeysValueAxisLabelForStudentMovement();
        saveChart(path,
                createHistogram("Движение студентов",
                        sqlite.getOldStudentCount()), 1000, 700);
        setKeysValueAxisLabelForProgressModule();
        saveChart(path,
                createHistogram("Успеваемость по модулям",
                        sqlite.getMeanScoreModules()), 2300, 1000);
        setKeysValueAxisLabelForCities();
        saveChart(path, createHistogram("Города",
                sqlite.getCountStudentsFromCities()), 1200, 700);

    }

    private void setTitlesColorsForScores(){
        titles = new String[]{"0-40 Баллов ", "40-60 Баллов ", "60-80 Баллов ", "80-100 Баллов "};
        colors = new Color[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};
    }

    private void setTitlesColorsForGender(){
        titles = new String[]{"Male ", "Female ", "Unknown "};
        colors = new Color[]{Color.BLUE, Color.YELLOW, Color.GREEN};
    }

    private JFreeChart createChartScoreAllStudents(String title, Integer[] data) {
        var dataset = new DefaultPieDataset<String>();
        for (var i = 0; i < titles.length; i++)
            dataset.setValue(titles[i] + data[i], data[i]);

        JFreeChart chart = ChartFactory.createPieChart(
                title, dataset, false, true, false);
        chart.setBackgroundPaint(Color.GRAY);

        var t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.CENTER);
        t.setPaint(Color.WHITE);
        t.setFont(new Font("Arial", Font.BOLD, 60));

        var plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        for (var i = 0; i < titles.length; i++)
            plot.setSectionPaint(titles[i] + data[i],  colors[i]);

        plot.setLabelFont(new Font("Courier New", Font.BOLD, 40));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(2.0f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(null);

        return chart;
    }

    private void setKeysValueAxisLabelForStudentMovement(){
        rowKeys = new String[] {"Студенты", "Студенты", "Студенты"};
        columnKeys = new String[] {"Остались на курсе", "Покинули курс", "Пришли на курс"};
        valueAxisLabel = "Количество человек";
    }

    private void setKeysValueAxisLabelForProgressModule(){
        valueAxisLabel = "Средний балл";
        columnKeys =  sqlite.getModuleNames();
        rowKeys = new String[columnKeys.length];
        for (var i = 0; i < columnKeys.length; i++)
            rowKeys[i] = "Средний балл за модуль";
    }

    private void setKeysValueAxisLabelForCities(){
        valueAxisLabel = "Количество человек";
        columnKeys = sqlite.getCities();
        rowKeys = new String[columnKeys.length];
        for (var i = 0; i < columnKeys.length; i++)
            rowKeys[i] = "Студенты";
    }

    private JFreeChart createHistogram(String title, Integer[] data)
    {
        var dataset = new DefaultCategoryDataset();
        for (var i = 0; i < data.length; i++)
            dataset.addValue(data[i], rowKeys[i], columnKeys[i] + " (" + data[i] + ")");

        JFreeChart chart = ChartFactory.createBarChart(title,
                null, valueAxisLabel, dataset);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        var rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        var renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }

    private void saveChart(String path, JFreeChart chart, int width, int height) {
        var chartsSavePath = Paths.get(path).toAbsolutePath();
        var fileName = chart.getTitle().getText() + ".png";
        var file = chartsSavePath.resolve(fileName).toFile();
        try {
            if (!Files.exists(chartsSavePath))
                Files.createDirectories(chartsSavePath);
            ChartUtils.saveChartAsPNG(file, chart, width, height);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
