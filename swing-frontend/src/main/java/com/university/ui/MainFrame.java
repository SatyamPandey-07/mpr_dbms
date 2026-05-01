package com.university.ui;

import com.university.service.ApiClient;
import com.university.ui.panels.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final ApiClient apiClient;
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final DashboardPanel dashboardPanel;

    public MainFrame() {
        this.apiClient = new ApiClient();
        
        setTitle("LEARN-UNIVERSITY Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main layout: Sidebar (Left) + Content (Center)
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Content Area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Add Panels
        dashboardPanel = new DashboardPanel(apiClient);
        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(new StudentPanel(apiClient), "Students");
        contentPanel.add(new InstructorPanel(apiClient), "Instructors");
        contentPanel.add(new CoursePanel(apiClient), "Courses");
        contentPanel.add(new DepartmentPanel(apiClient), "Departments");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new MigLayout("wrap 1, inset 20, fillx", "[fill]"));
        panel.setBackground(new Color(15, 23, 42)); // slate-900

        // Logo/Title
        JLabel title = new JLabel("LEARN-UNIVERSITY");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(56, 189, 248)); // primary-400
        panel.add(title, "gapbottom 5");

        JLabel subtitle = new JLabel("MANAGEMENT");
        subtitle.setFont(new Font("Monospaced", Font.BOLD, 12));
        subtitle.setForeground(new Color(148, 163, 184)); // slate-400
        panel.add(subtitle, "gapbottom 30");

        // Nav Buttons
        panel.add(createNavButton("📊 Dashboard", "Dashboard"), "gapy 5");
        panel.add(createNavButton("👥 Students", "Students"), "gapy 5");
        panel.add(createNavButton("👨‍🏫 Instructors", "Instructors"), "gapy 5");
        panel.add(createNavButton("📚 Courses", "Courses"), "gapy 5");
        panel.add(createNavButton("🏢 Departments", "Departments"), "gapy 5");

        return panel;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59)); // slate-800
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            if ("Dashboard".equals(cardName)) {
                dashboardPanel.refreshStats();
            }
        });
        
        return btn;
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel p = new JPanel(new GridBagLayout());
        JLabel l = new JLabel(title + " (Coming Soon)");
        l.setFont(new Font("SansSerif", Font.BOLD, 24));
        p.add(l);
        return p;
    }
}
