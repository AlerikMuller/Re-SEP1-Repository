package view.CustomerViewController;

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
import view.ViewHandler;

/**
 * The controller for the customer view of the graphical user interface.
 * <p>
 * The {@code CustomerViewController} lets the employee add, edit, remove and view
 * customers. It displays all customers in a table, loads a selected customer's details
 * into the input fields for editing, and forwards add, update and remove operations to
 * the {@link TripPlanningModelManager}. It follows the same {@code init(...)} pattern as
 * the other view controllers and uses the {@link ViewHandler} to navigate back to the
 * main view.
 *
 * @author Ghiyath
 * @version 1.0
 */
public class CustomerViewController {

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

    /**
     * Initialises the controller with the objects it needs to operate. This method sets up
     * the table columns, links the table to the customer data, adds a listener that loads a
     * selected customer's details into the input fields, and loads the current customers.
     *
     * @param viewHandler  the view handler used for navigation between screens
     * @param scene        the scene associated with this view
     * @param modelManager the model manager used to read and modify customer data
     */
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

    /**
     * Returns the scene associated with this view.
     *
     * @return the scene of the customer view
     */
    public Scene getScene()
    {
        return scene;
    }

    /**
     * Resets the view by reloading the customer table and clearing the input fields.
     */
    public void reset()
    {
        refreshTable();
        clearFields();
    }

    /**
     * Reloads the customer table from the model manager so that it always reflects the
     * current company data.
     */
    private void refreshTable()
    {
        customers.clear();
        CustomerList list = modelManager.getAllCustomers();
        for (int i = 0; i < list.size(); i++)
        {
            customers.add(list.getCustomer(i));
        }
    }

    /**
     * Handles the button actions of the customer view and dispatches each action to the
     * matching operation. The add, edit, remove and clear buttons trigger their respective
     * operations, while the back button returns the employee to the main view.
     *
     * @param e the action event fired by one of the view's buttons
     */
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

    /**
     * Creates a new customer from the values in the input fields, adds it through the model
     * manager, saves the company data, and refreshes the view. Any validation error is shown
     * as an error message.
     */
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

    /**
     * Updates the currently selected customer with the values in the input fields, saves the
     * change through the model manager, and refreshes the view. If no customer is selected or
     * the input is invalid, an error message is shown instead.
     */
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

    /**
     * Removes the currently selected customer through the model manager, saves the company
     * data, and refreshes the view. If no customer is selected, an error message is shown.
     */
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

    /**
     * Clears the input fields and removes the current selection from the customer table.
     */
    private void clearFields()
    {
        nameField.clear();
        phoneField.clear();
        customerTable.getSelectionModel().clearSelection();
    }

    /**
     * Displays an error message to the employee in red.
     *
     * @param message the error message to display
     */
    public void showError(String message)
    {
        messageLabel.setStyle("-fx-text-fill: #b00020;");
        messageLabel.setText(message);
    }

    /**
     * Displays a confirmation message to the employee in green.
     *
     * @param message the confirmation message to display
     */
    public void showConfirmation(String message)
    {
        messageLabel.setStyle("-fx-text-fill: #1b7a1b;");
        messageLabel.setText(message);
    }
}