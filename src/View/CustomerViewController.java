package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Customer;
import model.CustomerList;
import model.TripPlanningModelManager;

public class CustomerViewController
{
    private Scene scene;
    private TripPlanningModelManager modelManager;
    private ViewHandler viewHandler;

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button removeButton;
    @FXML private Button clearButton;
    @FXML private Button backButton;
    @FXML private Label messageLabel;

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    public void init(ViewHandler viewHandler, Scene scene, TripPlanningModelManager modelManager)
    {
        this.viewHandler = viewHandler;
        this.scene = scene;
        this.modelManager = modelManager;

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTable.setItems(customers);

        // When a row is selected, load its values into the fields for editing.
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null)
                    {
                        nameField.setText(newSelection.getName());
                        phoneField.setText(newSelection.getPhone());
                    }
                });

        refreshTable();
    }

    public Scene getScene()
    {
        return scene;
    }

    public void reset()
    {
        refreshTable();
        clearFields();
    }

    // Reloads the table from the model manager so it always reflects the company data.
    private void refreshTable()
    {
        customers.clear();
        CustomerList list = modelManager.getAllCustomers();
        for (int i = 0; i < list.size(); i++)
        {
            customers.add(list.getCustomer(i));
        }
    }

    @FXML
    public void handleActions(ActionEvent e)
    {
        if (e.getSource() == addButton)
        {
            addCustomer();
        }
        else if (e.getSource() == editButton)
        {
            editCustomer();
        }
        else if (e.getSource() == removeButton)
        {
            removeCustomer();
        }
        else if (e.getSource() == clearButton)
        {
            clearFields();
        }
        else if (e.getSource() == backButton)
        {
            viewHandler.openView("MainView");
        }
    }

    private void addCustomer()
    {
        try
        {
            Customer customer = new Customer(nameField.getText(), phoneField.getText());
            modelManager.addCustomer(customer);
            modelManager.saveCompany();
            showConfirmation("Customer added.");
            reset();
        }
        catch (IllegalArgumentException e)
        {
            showError(e.getMessage());
        }
    }

    private void editCustomer()
    {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            showError("Select a customer to edit.");
            return;
        }
        try
        {
            selected.setName(nameField.getText());
            selected.setPhone(phoneField.getText());
            modelManager.updateCustomer(selected);
            showConfirmation("Customer updated.");
            reset();
        }
        catch (IllegalArgumentException e)
        {
            showError(e.getMessage());
        }
    }

    private void removeCustomer()
    {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null)
        {
            showError("Select a customer to remove.");
            return;
        }
        modelManager.removeCustomer(selected);
        modelManager.saveCompany();
        showConfirmation("Customer removed.");
        reset();
    }

    private void clearFields()
    {
        nameField.clear();
        phoneField.clear();
        customerTable.getSelectionModel().clearSelection();
    }

    public void showError(String message)
    {
        messageLabel.setStyle("-fx-text-fill: #b00020;");
        messageLabel.setText(message);
    }

    public void showConfirmation(String message)
    {
        messageLabel.setStyle("-fx-text-fill: #1b7a1b;");
        messageLabel.setText(message);
    }
}