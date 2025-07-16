package com.expenseapp.ui.report;

import com.expenseapp.config.AppConstants;
import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.ExpenseDAO;
import com.expenseapp.model.ExpenseEntry;
import com.expenseapp.model.Passbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsPanel extends JPanel {
    private ExpenseDAO expenseDAO;
    private Passbook passbook;
    private JTable resultTable;

    private JTextField fromField, toField;
    private JComboBox<String> quickRangeCombo, reportTypeCombo;
    private JCheckBox categoryCheck, personCheck, groupByCategoryCheck, groupByPersonCheck;
    private JCheckBox cashInCheck, cashOutCheck;
    private JButton selectCategoryBtn, selectPersonBtn;

    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedPersons = new HashSet<>();

    public ReportsPanel(CardLayout cardLayout, JPanel parentPanel, Passbook passbook) {
        this.passbook = passbook;
        this.expenseDAO = new ExpenseDAO();

        setLayout(null);
        setBackground(UIConfig.BACKGROUND_COLOR);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setBounds(30, 20, 50, 25);
        add(fromLabel);
        fromField = new JTextField("2023-01-01");
        fromField.setBounds(80, 20, 100, 25);
        add(fromField);

        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(200, 20, 30, 25);
        add(toLabel);
        toField = new JTextField("2023-12-31");
        toField.setBounds(230, 20, 100, 25);
        add(toField);

        quickRangeCombo = new JComboBox<>(new String[]{"Custom", "Last Month", "Last Quarter", "Last Half Year", "Last Year", "Last Month to Till Date"});
        quickRangeCombo.setBounds(350, 20, 180, 25);
        add(quickRangeCombo);

        categoryCheck = new JCheckBox("Category");
        categoryCheck.setBounds(30, 60, 100, 25);
        add(categoryCheck);

        selectCategoryBtn = new JButton("Select Categories");
        selectCategoryBtn.setBounds(140, 60, 150, 25);
        add(selectCategoryBtn);

        personCheck = new JCheckBox("Person");
        personCheck.setBounds(310, 60, 100, 25);
        add(personCheck);

        selectPersonBtn = new JButton("Select Persons");
        selectPersonBtn.setBounds(420, 60, 150, 25);
        add(selectPersonBtn);

        cashInCheck = new JCheckBox("Cash In", true);
        cashInCheck.setBounds(30, 100, 80, 25);
        add(cashInCheck);

        cashOutCheck = new JCheckBox("Cash Out", true);
        cashOutCheck.setBounds(120, 100, 90, 25);
        add(cashOutCheck);

        reportTypeCombo = new JComboBox<>(new String[]{"Pie Chart", "Bar Chart"});
        reportTypeCombo.setBounds(230, 100, 120, 25);
        add(reportTypeCombo);

        groupByCategoryCheck = new JCheckBox("Group by Category");
        groupByCategoryCheck.setBounds(370, 100, 160, 25);
        add(groupByCategoryCheck);

        groupByPersonCheck = new JCheckBox("Group by Person");
        groupByPersonCheck.setBounds(550, 100, 150, 25);
        add(groupByPersonCheck);

        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setBounds(30, 140, 150, 25);
        add(generateBtn);

        resultTable = new JTable();
        resultTable.setEnabled(false);
        JScrollPane tableScroll = new JScrollPane(resultTable);
        tableScroll.setBounds(30, 180, 700, 250);
        add(tableScroll);

        JButton backButton = new JButton("Back");
        backButton.setBounds(300, 450, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(backButton);

        quickRangeCombo.addActionListener(e -> updateDatesFromRange());

        selectCategoryBtn.addActionListener(e -> showMultiSelectPopup("Select Categories", AppConstants.CATEGORIES, selectedCategories));
        selectPersonBtn.addActionListener(e -> showMultiSelectPopup("Select Persons", AppConstants.PERSONS, selectedPersons));

        generateBtn.addActionListener(e -> generateReport());
        backButton.addActionListener(e -> cardLayout.show(parentPanel, "dashboard"));
    }

    private void updateDatesFromRange() {
        String selected = (String) quickRangeCombo.getSelectedItem();
        if (selected == null || "Custom".equals(selected)) return;

        Calendar cal = Calendar.getInstance();
        Date toDate = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        switch (selected) {
            case "Last Month" -> cal.add(Calendar.MONTH, -1);
            case "Last Quarter" -> cal.add(Calendar.MONTH, -3);
            case "Last Half Year" -> cal.add(Calendar.MONTH, -6);
            case "Last Year" -> cal.add(Calendar.YEAR, -1);
            case "Last Month to Till Date" -> {
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        }

        Date fromDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fromField.setText(sdf.format(fromDate));
        toField.setText(sdf.format(toDate));
    }

    private void showMultiSelectPopup(String title, List<String> options, Set<String> selectedSet) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new BorderLayout());

        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));

        List<JCheckBox> checkBoxes = new ArrayList<>();
        for (String opt : options) {
            JCheckBox cb = new JCheckBox(opt, selectedSet.contains(opt) || selectedSet.isEmpty());
            checkBoxes.add(cb);
            checkPanel.add(cb);
        }

        JButton selectAll = new JButton("Select All");
        JButton clearAll = new JButton("Clear All");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(selectAll);
        buttonPanel.add(clearAll);

        selectAll.addActionListener(e -> checkBoxes.forEach(cb -> cb.setSelected(true)));
        clearAll.addActionListener(e -> checkBoxes.forEach(cb -> cb.setSelected(false)));

        JButton doneBtn = new JButton("Done");
        doneBtn.addActionListener(e2 -> {
            selectedSet.clear();
            for (JCheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    selectedSet.add(cb.getText());
                }
            }
            dialog.dispose();
        });

        dialog.add(new JScrollPane(checkPanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.NORTH);
        dialog.add(doneBtn, BorderLayout.SOUTH);

        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void generateReport() {
        try {
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromField.getText());
            Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(toField.getText());

            List<ExpenseEntry> allEntries = expenseDAO.getExpensesByPassbook(passbook.getId());

            List<ExpenseEntry> filtered = allEntries.stream()
                    .filter(e -> !e.getDate().before(fromDate) && !e.getDate().after(toDate))
                    .filter(e -> (cashInCheck.isSelected() && "Cash In".equals(e.getType()))
                              || (cashOutCheck.isSelected() && "Cash Out".equals(e.getType())))
                    .filter(e -> !categoryCheck.isSelected() || selectedCategories.isEmpty() || selectedCategories.contains(e.getCategory()))
                    .filter(e -> !personCheck.isSelected() || selectedPersons.isEmpty() || selectedPersons.contains(e.getPersonName()))
                    .collect(Collectors.toList());

            updateTable(filtered);
            showChartPopup(filtered);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<ExpenseEntry> entries) {
    String[] columns;
    DefaultTableModel model;

    boolean groupCat = groupByCategoryCheck.isSelected();
    boolean groupPer = groupByPersonCheck.isSelected();

    if (groupCat || groupPer) {
        if (groupCat && groupPer) {
            columns = new String[]{"Category", "Person", "Total Amount"};
            model = new DefaultTableModel(columns, 0);
            Map<String, Map<String, Double>> grouped = new HashMap<>();

            for (ExpenseEntry e : entries) {
                grouped
                    .computeIfAbsent(e.getCategory(), k -> new HashMap<>())
                    .merge(e.getPersonName(), e.getAmount(), Double::sum);
            }

            for (var catEntry : grouped.entrySet()) {
                for (var perEntry : catEntry.getValue().entrySet()) {
                    model.addRow(new Object[]{catEntry.getKey(), perEntry.getKey(), String.format("₹%.2f", perEntry.getValue())});
                }
            }
        } else if (groupCat) {
            columns = new String[]{"Category", "Total Amount"};
            model = new DefaultTableModel(columns, 0);
            Map<String, Double> grouped = new HashMap<>();
            for (ExpenseEntry e : entries) {
                grouped.merge(e.getCategory(), e.getAmount(), Double::sum);
            }
            for (var entry : grouped.entrySet()) {
                model.addRow(new Object[]{entry.getKey(), String.format("₹%.2f", entry.getValue())});
            }
        } else {
            columns = new String[]{"Person", "Total Amount"};
            model = new DefaultTableModel(columns, 0);
            Map<String, Double> grouped = new HashMap<>();
            for (ExpenseEntry e : entries) {
                grouped.merge(e.getPersonName(), e.getAmount(), Double::sum);
            }
            for (var entry : grouped.entrySet()) {
                model.addRow(new Object[]{entry.getKey(), String.format("₹%.2f", entry.getValue())});
            }
        }
    } else {
        // Neither group selected: show Cash In vs Cash Out summary
        columns = new String[]{"Type", "Total Amount"};
        model = new DefaultTableModel(columns, 0);

        double totalIn = entries.stream().filter(e -> "Cash In".equals(e.getType())).mapToDouble(ExpenseEntry::getAmount).sum();
        double totalOut = entries.stream().filter(e -> "Cash Out".equals(e.getType())).mapToDouble(ExpenseEntry::getAmount).sum();

        model.addRow(new Object[]{"Cash In", String.format("₹%.2f", totalIn)});
        model.addRow(new Object[]{"Cash Out", String.format("₹%.2f", totalOut)});
    }

    resultTable.setModel(model);
}

    private void showChartPopup(List<ExpenseEntry> entries) {
    String chartType = (String) reportTypeCombo.getSelectedItem();

    Map<String, Double> groupMap = new HashMap<>();
    boolean groupCat = groupByCategoryCheck.isSelected();
    boolean groupPer = groupByPersonCheck.isSelected();

    if (groupCat && groupPer) {
        for (ExpenseEntry e : entries) {
            String key = e.getCategory() + " - " + e.getPersonName();
            groupMap.merge(key, e.getAmount(), Double::sum);
        }
    } else if (groupCat) {
        for (ExpenseEntry e : entries) {
            groupMap.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
    } else if (groupPer) {
        for (ExpenseEntry e : entries) {
            groupMap.merge(e.getPersonName(), e.getAmount(), Double::sum);
        }
    } else {
        // Neither group: Cash In vs Cash Out
        double totalIn = entries.stream().filter(e -> "Cash In".equals(e.getType())).mapToDouble(ExpenseEntry::getAmount).sum();
        double totalOut = entries.stream().filter(e -> "Cash Out".equals(e.getType())).mapToDouble(ExpenseEntry::getAmount).sum();
        groupMap.put("Cash In", totalIn);
        groupMap.put("Cash Out", totalOut);
    }

    JFreeChart chart;
    if ("Pie Chart".equals(chartType)) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        groupMap.forEach(dataset::setValue);
        chart = ChartFactory.createPieChart("Expenses", dataset, true, true, false);
    } else {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        groupMap.forEach((key, value) -> dataset.addValue(value, "Amount", key));
        chart = ChartFactory.createBarChart("Expenses", "Group", "Amount", dataset);
    }

    JDialog chartDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chart", true);
    chartDialog.setLayout(new BorderLayout());
    chartDialog.add(new ChartPanel(chart), BorderLayout.CENTER);

    JButton closeBtn = new JButton("Close");
    closeBtn.addActionListener(e -> chartDialog.dispose());
    chartDialog.add(closeBtn, BorderLayout.SOUTH);

    chartDialog.setSize(600, 500);
    chartDialog.setLocationRelativeTo(this);
    chartDialog.setVisible(true);
}

}
