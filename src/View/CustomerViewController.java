package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Customer;



public class CustomerViewController {

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button removeButton;
    @FXML private Button clearButton;
    @FXML private Label messageLabel;

    // TODO: replace this local list with a reference passed in from ViewHandler /
    //       TripPlanningModelManager once those exist.
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    // Called automatically by the FXMLLoader after the FXML is loaded.
    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTable.setItems(customers);

        // When a row is selected, load its values into the fields for editing.
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        nameField.setText(newSelection.getName());
                        phoneField.setText(newSelection.getPhone());
                    }
                });
    }

    @FXML
    private void addCustomer() {
        try {
            Customer customer = new Customer(nameField.getText(), phoneField.getText());
            customers.add(customer);   // TODO: modelManager.addCustomer(customer);
            showConfirmation("Customer added.");
            reset();
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    @FXML
    private void editCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorMessage("Select a customer to edit.");
            return;
        }
        try {
            selected.setName(nameField.getText());
            selected.setPhone(phoneField.getText());
            customerTable.refresh();   // TODO: modelManager.updateCustomer(selected);
            showConfirmation("Customer updated.");
            reset();
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    // Kept to match the class diagram (updateCustomer); delegates to editCustomer.
    @FXML
    private void updateCustomer() {
        editCustomer();
    }

    @FXML
    private void removeCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorMessage("Select a customer to remove.");
            return;
        }
        customers.remove(selected);    // TODO: modelManager.removeCustomer(selected);
        showConfirmation("Customer removed.");
        reset();
    }

    @FXML
    private void reset() {
        nameField.clear();
        phoneField.clear();
        customerTable.getSelectionModel().clearSelection();
    }

    private void showErrorMessage(String message) {
        messageLabel.setStyle("-fx-text-fill: #b00020;");
        messageLabel.setText(message);
    }

    private void showConfirmation(String message) {
        messageLabel.setStyle("-fx-text-fill: #1b7a1b;");
        messageLabel.setText(message);
    }
}