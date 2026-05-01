package com.university.ui.panels;

import com.google.gson.reflect.TypeToken;
import com.university.model.*;
import com.university.service.ApiClient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class DashboardPanel extends JPanel {
    private final ApiClient apiClient;
    private final JLabel studentCountLabel = new JLabel("0");
    private final JLabel instructorCountLabel = new JLabel("0");
    private final JLabel courseCountLabel = new JLabel("0");
    private final JLabel departmentCountLabel = new JLabel("0");

    public DashboardPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new MigLayout("inset 40, fillx, wrap 4", "[fill, grow]", "[]20[]"));
        
        // Header
        JLabel header = new JLabel("University Overview");
        header.setFont(new Font("SansSerif", Font.BOLD, 32));
        add(header, "span 4, gapbottom 20");

        // Stat Cards
        add(createStatCard("Total Students", studentCountLabel, new Color(59, 130, 246)));
        add(createStatCard("Total Instructors", instructorCountLabel, new Color(168, 85, 247)));
        add(createStatCard("Courses Offered", courseCountLabel, new Color(16, 185, 129)));
        add(createStatCard("Departments", departmentCountLabel, new Color(245, 158, 11)));

        // Recent Activity Placeholder
        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(activityTitle, "span 4, gapy 40 10");
        
        JPanel activityPanel = new JPanel(new MigLayout("fillx, wrap 1, inset 20"));
        activityPanel.setBackground(new Color(30, 41, 59));
        activityPanel.add(new JLabel("Initialization reporting engine..."), "gapbottom 10");
        activityPanel.add(new JLabel("System online and connected."));
        add(activityPanel, "span 4, grow");

        refreshStats();
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new MigLayout("inset 20, fill", "[][grow]"));
        card.setBackground(new Color(30, 41, 59)); // slate-800
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel iconBox = new JPanel();
        iconBox.setBackground(color);
        iconBox.setPreferredSize(new Dimension(50, 50));
        card.add(iconBox, "width 50!, height 50!");

        JPanel textPanel = new JPanel(new MigLayout("inset 0, wrap 1"));
        textPanel.setOpaque(false);
        
        JLabel t = new JLabel(title.toUpperCase());
        t.setFont(new Font("SansSerif", Font.BOLD, 10));
        t.setForeground(new Color(148, 163, 184));
        textPanel.add(t);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        textPanel.add(valueLabel);

        card.add(textPanel);
        return card;
    }

    public void refreshStats() {
        setLoadingState();

        apiClient.get("/students", new TypeToken<ApiResponse<List<Student>>>(){})
                .thenAccept(res -> updateCountLabel(studentCountLabel, res.getData()))
                .exceptionally(ex -> {
                    setErrorState(studentCountLabel);
                    return null;
                });
        
        apiClient.get("/instructors", new TypeToken<ApiResponse<List<Instructor>>>(){})
                .thenAccept(res -> updateCountLabel(instructorCountLabel, res.getData()))
                .exceptionally(ex -> {
                    setErrorState(instructorCountLabel);
                    return null;
                });
        
        apiClient.get("/courses", new TypeToken<ApiResponse<List<Course>>>(){})
                .thenAccept(res -> updateCountLabel(courseCountLabel, res.getData()))
                .exceptionally(ex -> {
                    setErrorState(courseCountLabel);
                    return null;
                });
        
        apiClient.get("/departments", new TypeToken<ApiResponse<List<Department>>>(){})
                .thenAccept(res -> updateCountLabel(departmentCountLabel, res.getData()))
                .exceptionally(ex -> {
                    setErrorState(departmentCountLabel);
                    return null;
                });
    }

    private void setLoadingState() {
        SwingUtilities.invokeLater(() -> {
            studentCountLabel.setText("...");
            instructorCountLabel.setText("...");
            courseCountLabel.setText("...");
            departmentCountLabel.setText("...");
        });
    }

    private void updateCountLabel(JLabel label, List<?> data) {
        List<?> safeData = data != null ? data : Collections.emptyList();
        SwingUtilities.invokeLater(() -> label.setText(String.valueOf(safeData.size())));
    }

    private void setErrorState(JLabel label) {
        SwingUtilities.invokeLater(() -> label.setText("N/A"));
    }
}
