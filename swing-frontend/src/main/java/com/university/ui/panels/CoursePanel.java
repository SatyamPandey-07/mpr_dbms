package com.university.ui.panels;

import com.google.gson.reflect.TypeToken;
import com.university.model.*;
import com.university.service.ApiClient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class CoursePanel extends JPanel {
    private final ApiClient apiClient;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private List<Course> currentCourses;

    public CoursePanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new MigLayout("inset 40, fill, wrap 1", "[grow]", "[]20[]10[grow]"));

        // Table
        String[] columns = {"ID", "Title", "Credits", "Department ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);

        // Header
        JPanel headerPanel = new JPanel(new MigLayout("inset 0, fillx", "[grow][]"));
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Course Catalog");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(title);

        JButton addButton = new JButton("+ Create Course");
        addButton.setBackground(new Color(16, 185, 129));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showCourseDialog(null));
        headerPanel.add(addButton);

        add(headerPanel, "growx");

        // Actions
        JPanel actionPanel = new JPanel(new MigLayout("inset 0", "[]10[]"));
        actionPanel.setOpaque(false);
        
        JButton editBtn = new JButton("Edit Details");
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showCourseDialog(currentCourses.get(row));
        });

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(220, 38, 38));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> handleDelete());

        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        add(actionPanel, "growx");

        add(new JScrollPane(table), "grow");

        refreshData();
    }

    private void refreshData() {
        apiClient.get("/courses", new TypeToken<ApiResponse<List<Course>>>(){})
                .thenAccept(res -> {
                    this.currentCourses = res.getData();
                    updateTable(res.getData());
                });
    }

    private void updateTable(List<Course> courses) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Course c : courses) {
                Vector<Object> row = new Vector<>();
                row.add(c.getCourseId());
                row.add(c.getTitle());
                row.add(c.getCredits());
                row.add(c.getDeptId());
                tableModel.addRow(row);
            }
        });
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Integer id = (Integer) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete course " + id + "?") == JOptionPane.YES_OPTION) {
            apiClient.delete("/courses/" + id).thenRun(this::refreshData);
        }
    }

    private void showCourseDialog(Course course) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Course Information", true);
        dialog.setLayout(new MigLayout("wrap 2, inset 20", "[][grow]"));
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JTextField title = new JTextField(course != null ? course.getTitle() : "");
        JTextField credits = new JTextField(course != null ? String.valueOf(course.getCredits()) : "3");
        
        JComboBox<Department> deptBox = new JComboBox<>();
        apiClient.get("/departments", new TypeToken<ApiResponse<List<Department>>>(){})
                .thenAccept(res -> SwingUtilities.invokeLater(() -> {
                    res.getData().forEach(deptBox::addItem);
                    if (course != null) {
                        for (int i = 0; i < deptBox.getItemCount(); i++) {
                            if (deptBox.getItemAt(i).getDeptId().equals(course.getDeptId())) {
                                deptBox.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                }));

        dialog.add(new JLabel("Course Title:")); dialog.add(title, "growx");
        dialog.add(new JLabel("Credits:")); dialog.add(credits, "growx");
        dialog.add(new JLabel("Department:")); dialog.add(deptBox, "growx");

        JButton saveBtn = new JButton("Confirm and Save");
        saveBtn.addActionListener(e -> {
            Course c = course != null ? course : new Course();
            c.setTitle(title.getText());
            c.setCredits(Integer.parseInt(credits.getText()));
            Department d = (Department) deptBox.getSelectedItem();
            if (d != null) c.setDeptId(d.getDeptId());

            if (c.getCourseId() != null) {
                apiClient.put("/courses/" + c.getCourseId(), c, new TypeToken<ApiResponse<Course>>(){}).thenRun(() -> { dialog.dispose(); refreshData(); });
            } else {
                apiClient.post("/courses", c, new TypeToken<ApiResponse<Course>>(){}).thenRun(() -> { dialog.dispose(); refreshData(); });
            }
        });

        dialog.add(saveBtn, "span 2, growx, gapy 20");
        dialog.setVisible(true);
    }
}
