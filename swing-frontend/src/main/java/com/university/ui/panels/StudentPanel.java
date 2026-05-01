package com.university.ui.panels;

import com.google.gson.reflect.TypeToken;
import com.university.model.ApiResponse;
import com.university.model.Person;
import com.university.model.Student;
import com.university.service.ApiClient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class StudentPanel extends JPanel {
    private final ApiClient apiClient;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;

    public StudentPanel(ApiClient apiClient) {
        this.apiClient = apiClient;
        setLayout(new MigLayout("inset 40, fill, wrap 1", "[grow]", "[]20[]10[grow]"));

        // Header
        JPanel headerPanel = new JPanel(new MigLayout("inset 0, fillx", "[grow][]"));
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Students Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(title);

        JButton addButton = new JButton("+ Add Student");
        addButton.setBackground(new Color(37, 99, 235));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showStudentDialog(null));
        headerPanel.add(addButton);

        add(headerPanel, "growx");

        // Search Bar
        JPanel searchPanel = new JPanel(new MigLayout("inset 0, fillx", "[grow][]"));
        searchPanel.setOpaque(false);
        
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Search by name, SSN, or major...");
        searchPanel.add(searchField, "growx");
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);
        
        add(searchPanel, "growx");

        // Table
        String[] columns = {"ID", "First Name", "Last Name", "Email", "Major", "Year"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add row selection listener for edit/delete placeholder
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                // Future: Show floating menu or actions
            }
        });

        // Action Buttons Panel
        JPanel actionPanel = new JPanel(new MigLayout("inset 0", "[]10[]"));
        actionPanel.setOpaque(false);
        
        JButton editBtn = new JButton("Edit Selected");
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1 && currentStudents != null) {
                Student selected = currentStudents.get(row);
                showStudentDialog(selected);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to edit.");
            }
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

    private List<Student> currentStudents;

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        
        Integer id = (Integer) tableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student ID " + id + "?") == JOptionPane.YES_OPTION) {
            apiClient.delete("/students/" + id)
                    .thenRun(() -> {
                        JOptionPane.showMessageDialog(this, "Student deleted successfully");
                        refreshData();
                    });
        }
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshData();
            return;
        }

        apiClient.get("/students/search?q=" + query, new TypeToken<ApiResponse<List<Student>>>(){})
                .thenAccept(res -> updateTable(res.getData()))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private void refreshData() {
        apiClient.get("/students", new TypeToken<ApiResponse<List<Student>>>(){})
                .thenAccept(res -> updateTable(res.getData()))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private void updateTable(List<Student> students) {
        this.currentStudents = students;
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            for (Student s : students) {
                Vector<Object> row = new Vector<>();
                row.add(s.getStudentId());
                row.add(s.getPerson().getFirstName());
                row.add(s.getPerson().getLastName());
                row.add(s.getPerson().getEmail());
                row.add(s.getMajor());
                row.add(s.getEnrollmentYear());
                tableModel.addRow(row);
            }
        });
    }

    private void showStudentDialog(Student student) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                student != null ? "Edit Student" : "Add New Student", true);
        dialog.setLayout(new MigLayout("wrap 2, inset 20", "[][grow]"));
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JTextField fn = new JTextField(student != null ? student.getPerson().getFirstName() : "");
        JTextField ln = new JTextField(student != null ? student.getPerson().getLastName() : "");
        JTextField ssn = new JTextField(student != null ? student.getPerson().getSsn() : "");
        JTextField email = new JTextField(student != null ? student.getPerson().getEmail() : "");
        JTextField major = new JTextField(student != null ? student.getMajor() : "");
        JTextField year = new JTextField(student != null ? String.valueOf(student.getEnrollmentYear()) : "2024");

        dialog.add(new JLabel("First Name:")); dialog.add(fn, "growx");
        dialog.add(new JLabel("Last Name:")); dialog.add(ln, "growx");
        dialog.add(new JLabel("SSN:")); dialog.add(ssn, "growx");
        dialog.add(new JLabel("Email:")); dialog.add(email, "growx");
        dialog.add(new JLabel("Major:")); dialog.add(major, "growx");
        dialog.add(new JLabel("Enrollment Year:")); dialog.add(year, "growx");

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            Student s = student != null ? student : new Student();
            if (s.getPerson() == null) s.setPerson(new Person());
            
            s.getPerson().setFirstName(fn.getText());
            s.getPerson().setLastName(ln.getText());
            s.getPerson().setSsn(ssn.getText());
            s.getPerson().setEmail(email.getText());
            s.setMajor(major.getText());
            s.setEnrollmentYear(Integer.parseInt(year.getText()));

            if (s.getStudentId() != null) {
                apiClient.put("/students/" + s.getStudentId(), s, new TypeToken<ApiResponse<Student>>(){})
                        .thenRun(() -> { dialog.dispose(); refreshData(); });
            } else {
                apiClient.post("/students", s, new TypeToken<ApiResponse<Student>>(){})
                        .thenRun(() -> { dialog.dispose(); refreshData(); });
            }
        });

        dialog.add(saveBtn, "span 2, growx, gapy 20");
        dialog.setVisible(true);
    }
}
