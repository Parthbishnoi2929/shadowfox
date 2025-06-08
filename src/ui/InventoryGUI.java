package ui;

import service.InventoryManager;
import model.InventoryItem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent; // For keyboard events
import java.util.List;

public class InventoryGUI extends JFrame {
    private InventoryManager inventoryManager;

    // GUI Components
    private JTextField idField, nameField, quantityField, priceField, searchField;
    private JButton addButton, updateButton, deleteButton, clearButton, searchButton;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JLabel statusBar;
    private JTabbedPane tabbedPane; // For tabbed interface

    public InventoryGUI() {
        inventoryManager = new InventoryManager();
        setTitle("Shadow Fox Inventory Management System");
        setSize(900, 650); // Slightly increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        initComponents();
        addEventHandlers();
        addKeyboardActions(); // New: Add keyboard shortcuts
        refreshTable(); // Load initial data into the table
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Add some spacing

        tabbedPane = new JTabbedPane();

        // --- Manage Inventory Tab ---
        JPanel managePanel = new JPanel(new BorderLayout(10, 10));

        // Input Panel (using GridBagLayout for better control)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Item Details",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Item ID
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Item ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        idField = new JTextField(20); // Wider text field
        inputPanel.add(idField, gbc);

        // Item Name
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Item Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);

        // Quantity
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        quantityField = new JTextField(20);
        inputPanel.add(quantityField, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        priceField = new JTextField(20);
        inputPanel.add(priceField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // More spacing between buttons
        addButton = new JButton("Add Item");
        addButton.setToolTipText("Add a new item to inventory (Ctrl+A)");
        updateButton = new JButton("Update Item");
        updateButton.setToolTipText("Update details of selected item (Ctrl+U)");
        deleteButton = new JButton("Delete Item");
        deleteButton.setToolTipText("Delete selected item from inventory (Ctrl+D)");
        clearButton = new JButton("Clear Fields");
        clearButton.setToolTipText("Clear all input fields (Ctrl+C)");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        managePanel.add(inputPanel, BorderLayout.NORTH);
        managePanel.add(buttonPanel, BorderLayout.CENTER);

        // --- Search and Table Panel (now part of the main layout, or within its own tab) ---
        // For better organization, let's keep search separate in a tab.
        JPanel searchBrowsePanel = new JPanel(new BorderLayout(10, 10));
        searchBrowsePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Inventory List",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14)));

        JPanel searchControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchField = new JTextField(30); // Wider search field
        searchButton = new JButton("Search");
        searchButton.setToolTipText("Search items by name or ID (Ctrl+S)");
        searchControlPanel.add(new JLabel("Search:"));
        searchControlPanel.add(searchField);
        searchControlPanel.add(searchButton);

        String[] columnNames = {"ID", "Name", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        inventoryTable = new JTable(tableModel);
        inventoryTable.setFillsViewportHeight(true); // Table fills the viewport
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row selectable
        inventoryTable.setFont(new Font("SansSerif", Font.PLAIN, 13)); // Slightly larger font for table
        inventoryTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13)); // Bold header

        JScrollPane scrollPane = new JScrollPane(inventoryTable);

        searchBrowsePanel.add(searchControlPanel, BorderLayout.NORTH);
        searchBrowsePanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to the tabbed pane
        tabbedPane.addTab("Manage Items", managePanel);
        tabbedPane.addTab("Browse & Search", searchBrowsePanel);

        add(tabbedPane, BorderLayout.CENTER); // Add tabbed pane to the frame

        // Status Bar
        statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Add internal padding
        ));
        statusBar.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusBar.setForeground(new Color(50, 100, 150)); // A calmer blue

        add(statusBar, BorderLayout.SOUTH);
    }

    private void addEventHandlers() {
        addButton.addActionListener(e -> addItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> deleteItem());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> searchItems());

        inventoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && inventoryTable.getSelectedRow() != -1) {
                displaySelectedItemDetails();
                tabbedPane.setSelectedComponent(tabbedPane.getComponentAt(0)); // Switch to Manage Items tab
            }
        });

        // Trigger search when Enter is pressed in search field
        searchField.addActionListener(e -> searchItems());
    }

    private void addKeyboardActions() {
        // Define Action for Add Item
        InputMap inputMapAdd = addButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMapAdd = addButton.getActionMap();
        inputMapAdd.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), "addItemAction");
        actionMapAdd.put("addItemAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addItem();
            }
        });

        // Define Action for Update Item
        InputMap inputMapUpdate = updateButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMapUpdate = updateButton.getActionMap();
        inputMapUpdate.put(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK), "updateItemAction");
        actionMapUpdate.put("updateItemAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateItem();
            }
        });

        // Define Action for Delete Item
        InputMap inputMapDelete = deleteButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMapDelete = deleteButton.getActionMap();
        inputMapDelete.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), "deleteItemAction");
        actionMapDelete.put("deleteItemAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteItem();
            }
        });

        // Define Action for Clear Fields
        InputMap inputMapClear = clearButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMapClear = clearButton.getActionMap();
        inputMapClear.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "clearFieldsAction");
        actionMapClear.put("clearFieldsAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                clearFields();
            }
        });

        // Define Action for Search (can be linked to search button or search field)
        InputMap inputMapSearch = searchField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW); // Or searchButton.getInputMap
        ActionMap actionMapSearch = searchField.getActionMap(); // Or searchButton.getActionMap
        inputMapSearch.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "searchAction");
        actionMapSearch.put("searchAction", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                searchItems();
            }
        });
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        idField.setEditable(true); // Allow ID editing when adding new item
        inventoryTable.clearSelection(); // Clear table selection
        statusBar.setText("Fields cleared. Ready for new item entry.");
        idField.requestFocusInWindow(); // Put focus on ID field
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<InventoryItem> items = inventoryManager.getAllItems();
        for (InventoryItem item : items) {
            tableModel.addRow(new Object[]{item.getId(), item.getName(), item.getQuantity(), String.format("%.2f", item.getPrice())});
        }
        statusBar.setText("Inventory refreshed. Displaying " + items.size() + " items.");
    }

    private void addItem() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Item ID and Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (quantity < 0 || price < 0) {
                JOptionPane.showMessageDialog(this, "Quantity and Price cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            InventoryItem newItem = new InventoryItem(id, name, quantity, price);
            if (inventoryManager.addItem(newItem)) {
                JOptionPane.showMessageDialog(this, "Item '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
                statusBar.setText("Item '" + name + "' added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Item with ID '" + id + "' already exists. Please use a unique ID.", "Add Error", JOptionPane.ERROR_MESSAGE);
                statusBar.setText("Failed to add item: Duplicate ID.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid whole numbers for Quantity and valid decimal numbers for Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
            statusBar.setText("Input error: Invalid quantity or price format.");
        }
    }

    private void updateItem() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an item or enter an ID to update.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Item Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (quantity < 0 || price < 0) {
                JOptionPane.showMessageDialog(this, "Quantity and Price cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (inventoryManager.updateItem(id, name, quantity, price)) {
                JOptionPane.showMessageDialog(this, "Item '" + name + "' updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
                statusBar.setText("Item '" + name + "' updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Item with ID '" + id + "' not found for update.", "Update Error", JOptionPane.ERROR_MESSAGE);
                statusBar.setText("Failed to update item: Item not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid whole numbers for Quantity and valid decimal numbers for Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
            statusBar.setText("Input error: Invalid quantity or price format.");
        }
    }

    private void deleteItem() {
        String idToDelete = idField.getText().trim();
        if (idToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an item or enter an Item ID to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete item with ID: " + idToDelete + "?\nThis action cannot be undone.", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (inventoryManager.deleteItem(idToDelete)) {
                JOptionPane.showMessageDialog(this, "Item with ID '" + idToDelete + "' deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
                statusBar.setText("Item with ID '" + idToDelete + "' deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Item with ID '" + idToDelete + "' not found for deletion.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                statusBar.setText("Failed to delete item: Item not found.");
            }
        }
    }

    private void searchItems() {
        String query = searchField.getText().trim();
        List<InventoryItem> results;

        if (query.isEmpty()) {
            results = inventoryManager.getAllItems();
            statusBar.setText("Search cleared. Displaying all " + results.size() + " items.");
        } else {
            results = inventoryManager.searchItems(query);
            statusBar.setText("Search results for '" + query + "'. Found: " + results.size() + " items.");
        }

        tableModel.setRowCount(0); // Clear existing rows
        for (InventoryItem item : results) {
            tableModel.addRow(new Object[]{item.getId(), item.getName(), item.getQuantity(), String.format("%.2f", item.getPrice())});
        }
        tabbedPane.setSelectedComponent(tabbedPane.getComponentAt(1)); // Switch to Browse & Search tab
    }

    private void displaySelectedItemDetails() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            quantityField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            priceField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            idField.setEditable(false); // Prevent ID modification when updating
            statusBar.setText("Item details loaded for ID: " + idField.getText() + ". Ready for update or delete.");
        }
    }

    public static void main(String[] args) {
        // Set an aesthetic look and feel if available (e.g., Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to default
            System.out.println("Nimbus Look and Feel not found. Using default.");
        }

        // Ensure GUI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            InventoryGUI gui = new InventoryGUI();
            gui.setVisible(true);
        });
    }
}