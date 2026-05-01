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

public class DepartmentPanel extends JPanel {
    private final ApiClient apiClient;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private List<Department> currentDepartments;

    public DepartmentPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new MigLayout("inset 40, fill, wrap 1", "[grow]", "[]20[]10[grow]"));

        // Table
        String[] columns = {"ID", "Name", "Office", "Chair ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);

        // Header
        JPanel headerPanel = new JPanel(new MigLayout("inset 0, fillx", "[grow][]"));
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Department Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(title);

        JButton addButton = new JButton("+ New Department");
        addButton.setBackground(new Color(217, 119, 6)); // amber-600
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showDepartmentDialog(null));
        headerPanel.add(addButton);

        add(headerPanel, "growx");

        // Actions
        JPanel actionPanel = new JPanel(new MigLayout("inset 0", "[]10[]"));
        actionPanel.setOpaque(false);
        
        JButton editBtn = new JButton("Edit");
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showDepartmentDialog(currentDepartments.get(row));
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
        apiClient.get("/departments", new TypeToken<ApiResponse<List<Department>>>(){})
                .thenAccept(res -> {
                    this.currentDepartments = res.getData();
                    updateTable(res.getData());
                });
    }

    private void updateTable(List<Department> departments) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Department d : departments) {
                Vector<Object> row = new Vector<>();
                row.add(d.getDeptId());
                row.add(d.getName());
                row.add(d.getOffice());
                row.add(d.getChairId() != null ? d.getChairId() : "None");
                tableModel.addRow(row);
            }
        });
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Integer id = (Integer) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete department " + id + "?") == JOptionPane.YES_OPTION) {
            apiClient.delete("/departments/" + id).thenRun(this::refreshData);
        }
    }

    private void showDepartmentDialog(Department dept) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Department Setup", true);
        dialog.setLayout(new MigLayout("wrap 2, inset 20", "[][grow]"));
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JTextField name = new JTextField(dept != null ? dept.getName() : "");
        JTextField office = new JTextField(dept != null ? dept.getOffice() : "");
        
        JComboBox<Instructor> chairBox = new JComboBox<>();
        apiClient.get("/instructors", new TypeToken<ApiResponse<List<Instructor>>>(){})
                .thenAccept(res -> SwingUtilities.invokeLater(() -> {
                    res.getData().forEach(chairBox::addItem);
                    if (dept != null && dept.getChairId() != null) {
                        for (int i = 0; i < chairBox.getItemCount(); i++) {
                            if (chairBox.getItemAt(i).getInstructorId().equals(dept.getChairId())) {
                                chairBox.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                }));

        dialog.add(new JLabel("Department Name:")); dialog.add(name, "growx");
        dialog.add(new JLabel("Office Location:")); dialog.add(office, "growx");
        dialog.add(new JLabel("Assign Chair:")); dialog.add(chairBox, "growx");

        JButton saveBtn = new JButton("Save Department");
        saveBtn.addActionListener(e -> {
            Department d = dept != null ? dept : new Department();
            d.setName(name.getText());
            d.setOffice(office.getText());
            Instructor inst = (Instructor) chairBox.getSelectedItem();
            if (inst != null) d.setChairId(inst.getInstructorId());

            if (d.getDeptId() != null) {
                apiClient.put("/departments/" + d.getDeptId(), d, new TypeToken<ApiResponse<Department>>(){}).thenRun(() -> { dialog.dispose(); refreshData(); });
            } else {
                apiClient.post("/departments", d, new TypeToken<ApiResponse<Department>>(){}).thenRun(() -> { dialog.dispose(); refreshData(); });
            }
        });

        dialog.add(saveBtn, "span 2, growx, gapy 20");
        dialog.setVisible(true);
    }
}
