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

public class InstructorPanel extends JPanel {
    private final ApiClient apiClient;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private List<Instructor> currentInstructors;

    public InstructorPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        // Table
        String[] columns = {"ID", "First Name", "Last Name", "Rank", "Salary", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);

        // Header
        JPanel headerPanel = new JPanel(new MigLayout("inset 0, fillx", "[grow][]"));
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Instructor Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(title);

        JButton addButton = new JButton("+ Add Instructor");
        addButton.setBackground(new Color(37, 99, 235));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showInstructorDialog(null));
        headerPanel.add(addButton);

        add(headerPanel, "growx");

        // Actions
        JPanel actionPanel = new JPanel(new MigLayout("inset 0", "[]10[]10[]"));
        actionPanel.setOpaque(false);
        
        JButton editBtn = new JButton("Edit Selected");
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showInstructorDialog(currentInstructors.get(row));
        });

        JButton assignBtn = new JButton("Assign Course");
        assignBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showAssignCourseDialog(currentInstructors.get(row));
        });

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(220, 38, 38));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> handleDelete());

        actionPanel.add(editBtn);
        actionPanel.add(assignBtn);
        actionPanel.add(deleteBtn);
        add(actionPanel, "growx");

        add(new JScrollPane(table), "grow");

        refreshData();
    }

    private void refreshData() {
        apiClient.get("/instructors", new TypeToken<ApiResponse<List<Instructor>>>(){})
                .thenAccept(res -> {
                    this.currentInstructors = res.getData();
                    updateTable(res.getData());
                });
    }

    private void updateTable(List<Instructor> instructors) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Instructor i : instructors) {
                Vector<Object> row = new Vector<>();
                row.add(i.getInstructorId());
                row.add(i.getPerson().getFirstName());
                row.add(i.getPerson().getLastName());
                row.add(i.getRank());
                row.add(String.format("$%.2f", i.getSalary()));
                row.add(i.getPerson().getEmail());
                tableModel.addRow(row);
            }
        });
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        Integer id = (Integer) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete instructor " + id + "?") == JOptionPane.YES_OPTION) {
            apiClient.delete("/instructors/" + id).thenRun(this::refreshData);
        }
    }

    private void showInstructorDialog(Instructor inst) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Instructor Details", true);
        dialog.setLayout(new MigLayout("wrap 2, inset 20", "[][grow]"));
        dialog.setSize(400, 550);
        dialog.setLocationRelativeTo(this);

        JTextField fn = new JTextField(inst != null ? inst.getPerson().getFirstName() : "");
        JTextField ln = new JTextField(inst != null ? inst.getPerson().getLastName() : "");
        JTextField ssn = new JTextField(inst != null ? inst.getPerson().getSsn() : "");
        JTextField email = new JTextField(inst != null ? inst.getPerson().getEmail() : "");
        JComboBox<String> rank = new JComboBox<>(new String[]{"Professor", "Associate Professor", "Assistant Professor", "Lecturer"});
        if (inst != null) rank.setSelectedItem(inst.getRank());
        JTextField salary = new JTextField(inst != null ? String.valueOf(inst.getSalary()) : "0");

        dialog.add(new JLabel("First Name:")); dialog.add(fn, "growx");
        dialog.add(new JLabel("Last Name:")); dialog.add(ln, "growx");
        dialog.add(new JLabel("SSN:")); dialog.add(ssn, "growx");
        dialog.add(new JLabel("Email:")); dialog.add(email, "growx");
        dialog.add(new JLabel("Rank:")); dialog.add(rank, "growx");
        dialog.add(new JLabel("Salary:")); dialog.add(salary, "growx");

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            Instructor i = inst != null ? inst : new Instructor();
            if (i.getPerson() == null) i.setPerson(new Person());
            i.getPerson().setFirstName(fn.getText());
            i.getPerson().setLastName(ln.getText());
            i.getPerson().setSsn(ssn.getText());
            i.getPerson().setEmail(email.getText());
            i.setRank((String) rank.getSelectedItem());
            i.setSalary(Double.parseDouble(salary.getText()));

            if (i.getInstructorId() != null) {
                apiClient.put("/instructors/" + i.getInstructorId(), i, new TypeToken<ApiResponse<Instructor>>(){}).thenRun(() -> { dialog.dispose(); refreshData(); });
            } else {
                apiClient.post("/instructors", i, new TypeToken<ApiResponse<Instructor>>(){}).thenRun(() -> { dialog.dispose(); refreshData(); });
            }
        });

        dialog.add(saveBtn, "span 2, growx, gapy 20");
        dialog.setVisible(true);
    }

    private void showAssignCourseDialog(Instructor inst) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Assign Course", true);
        dialog.setLayout(new MigLayout("wrap 2, inset 20", "[][grow]"));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JComboBox<Course> courseBox = new JComboBox<>();
        apiClient.get("/courses", new TypeToken<ApiResponse<List<Course>>>(){})
                .thenAccept(res -> SwingUtilities.invokeLater(() -> res.getData().forEach(courseBox::addItem)));

        JComboBox<String> semester = new JComboBox<>(new String[]{"Fall", "Spring", "Summer"});
        JTextField year = new JTextField("2024");

        dialog.add(new JLabel("Course:")); dialog.add(courseBox, "growx");
        dialog.add(new JLabel("Semester:")); dialog.add(semester, "growx");
        dialog.add(new JLabel("Year:")); dialog.add(year, "growx");

        JButton assignBtn = new JButton("Assign");
        assignBtn.addActionListener(e -> {
            Course c = (Course) courseBox.getSelectedItem();
            if (c == null) return;
            String path = String.format("/instructors/%d/courses/%d?semester=%s&year=%s", 
                    inst.getInstructorId(), c.getCourseId(), semester.getSelectedItem(), year.getText());
            apiClient.post(path, null, new TypeToken<ApiResponse<Object>>(){})
                    .thenRun(() -> {
                        JOptionPane.showMessageDialog(this, "Course assigned successfully!");
                        dialog.dispose();
                    });
        });

        dialog.add(assignBtn, "span 2, growx, gapy 20");
        dialog.setVisible(true);
    }
}
