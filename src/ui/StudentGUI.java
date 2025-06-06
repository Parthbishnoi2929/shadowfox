package ui;

import model.Student;
import service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class StudentGUI extends JFrame {
    private final StudentService studentService = new StudentService();

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField courseField = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Course"}, 0);
    private final JTable studentTable = new JTable(tableModel);

    public StudentGUI() {
        setTitle("🎓 Student Information System");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Course:"));
        formPanel.add(courseField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        formPanel.add(buttonPanel);
        formPanel.add(new JLabel("")); // filler

        // Center Panel - Table
        studentTable.setFillsViewportHeight(true);
        studentTable.setRowHeight(25);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(studentTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Student Records"));

        // Bottom Hint Panel
        JLabel hintLabel = new JLabel("💡 Shortcuts: [Enter] Add | [Ctrl+U] Update | [Delete] Delete | [Esc] Clear");
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));

        //  Add Panels to Frame
        add(formPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(hintLabel, BorderLayout.SOUTH);

        // Event Listeners
        addBtn.addActionListener(e -> handleAdd());
        updateBtn.addActionListener(e -> handleUpdate());
        deleteBtn.addActionListener(e -> handleDelete());

        studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row != -1) {
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    courseField.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });

        // Table style
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Keyboard Shortcuts
        JRootPane rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "addStudent");
        actionMap.put("addStudent", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), "updateStudent");
        actionMap.put("updateStudent", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleUpdate();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteStudent");
        actionMap.put("deleteStudent", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleDelete();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clearFields");
        actionMap.put("clearFields", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Center the window
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleAdd() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String course = courseField.getText().trim();

            Student s = new Student(id, name, course);
            studentService.addStudent(s);
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            showError("ID must be a valid number.");
        }
    }

    private void handleUpdate() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String course = courseField.getText().trim();

            studentService.updateStudent(id, name, course);
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            showError("ID must be a valid number.");
        }
    }

    private void handleDelete() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            studentService.deleteStudent(id);
            refreshTable();
            clearFields();
        } catch (NumberFormatException ex) {
            showError("ID must be a valid number.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing
        for (Student s : studentService.getAllStudents()) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getCourse()});
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        studentTable.clearSelection();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
