package service;

import model.InventoryItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventoryManager {
    private List<InventoryItem> inventory;
    private static final String DATA_FILE = "inventory.dat";

    public InventoryManager() {
        inventory = new ArrayList<>();
        loadInventory(); // Load data when manager is initialized
    }

    public boolean addItem(InventoryItem item) {
        // Check for duplicate ID
        if (inventory.stream().anyMatch(i -> i.getId().equals(item.getId()))) {
            return false; // Item with this ID already exists
        }
        inventory.add(item);
        saveInventory();
        return true;
    }

    public boolean updateItem(String id, String newName, int newQuantity, double newPrice) {
        Optional<InventoryItem> itemOptional = inventory.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (itemOptional.isPresent()) {
            InventoryItem item = itemOptional.get();
            item.setName(newName);
            item.setQuantity(newQuantity);
            item.setPrice(newPrice);
            saveInventory();
            return true;
        }
        return false; // Item not found
    }

    public boolean deleteItem(String id) {
        boolean removed = inventory.removeIf(item -> item.getId().equals(id));
        if (removed) {
            saveInventory();
        }
        return removed;
    }

    public List<InventoryItem> getAllItems() {
        return new ArrayList<>(inventory); // Return a copy to prevent external modification
    }

    public Optional<InventoryItem> getItemById(String id) {
        return inventory.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public List<InventoryItem> searchItems(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return inventory.stream()
                .filter(item -> item.getName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getId().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    private void saveInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(inventory);
            System.out.println("Inventory saved to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadInventory() {
        File file = new File(DATA_FILE);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList) {
                    inventory = (ArrayList<InventoryItem>) obj;
                    System.out.println("Inventory loaded from " + DATA_FILE);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading inventory: " + e.getMessage());
                // If loading fails, start with an empty inventory
                inventory = new ArrayList<>();
            }
        } else {
            System.out.println("No existing inventory data found. Starting with empty inventory.");
        }
    }
}