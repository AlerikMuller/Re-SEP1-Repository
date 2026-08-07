package view.BusViewController;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.Bus;
import model.TripPlanningModelManager;
import view.ViewHandler;

/**
 * Controller for adding new buses to the system.
 * Handles user input validation, bus creation, and persistence.
 *
 * @author Kelsang Sherpa
 * @version 1.0
 */
public class AddBusViewController
{
  private Scene scene;
  private TripPlanningModelManager modelManager;
  private ViewHandler viewHandler;

  @FXML private Button addButton;
  @FXML private Button backButton;

  @FXML private ComboBox<String> busTypeBox;

  @FXML private TextField regNoTextField;
  @FXML private TextField rentPricePerDayTextField;
  @FXML private TextField seatCapacityTextField;
  @FXML private RadioButton availableRadioButton;
  @FXML private RadioButton unavailableRadioButton;
  @FXML private ToggleGroup availabilityGroup;
  @FXML private MenuItem exitMenuItem;
  @FXML private MenuItem aboutMenuItem;

  /**
   * Initializes toggle group for availability radio buttons.
   */
  @FXML public void initialize()
  {
    availabilityGroup = new ToggleGroup();
    availableRadioButton.setToggleGroup(availabilityGroup);
    unavailableRadioButton.setToggleGroup(availabilityGroup);
  }
  /**
   * Initializes the controller with scene, view handler, and model manager.
   * Populates the bus type combo box.
   *
   * @param viewHandler the view handler for navigation
   * @param scene the scene for this view
   * @param modelManager the model manager for data access
   */
  public void init(ViewHandler viewHandler, Scene scene,
      TripPlanningModelManager modelManager)
  {
    this.viewHandler = viewHandler;
    this.scene = scene;
    this.modelManager = modelManager;
    setupComboBox();
  }
  /**
   * Clears all input fields and resets selections to default state.
   */
  public void reset()
  {
    regNoTextField.clear();
    if(busTypeBox!=null)
    {
      busTypeBox.setValue(null);
    }
    rentPricePerDayTextField.clear();
    seatCapacityTextField.clear();

    if (availabilityGroup != null)
    {
      availabilityGroup.selectToggle(null);
    }
  }

  /**
   * Returns the scene of this view.
   *
   * @return the Scene for this controller
   */
  public Scene getScene()
  {
    return scene;
  }

  /**
   * Handles button and menu item actions.
   * Routes to appropriate methods based on event source.
   *
   * @param e the ActionEvent triggered by user interaction
   */
  @FXML
  public void handleActions(ActionEvent e)
  {
    if(e.getSource() == addButton)
    {
        addBus();
    }
    else if (e.getSource() == backButton)
    {
      viewHandler.openView("MainView");
    }
    else if (e.getSource() == exitMenuItem)
    {
      exitProgram();
    }
    else if (e.getSource() == aboutMenuItem)
    {
      showAbout();
    }
  }
  /**
   * Validates input and adds a new bus to the system.
   * Checks for empty fields, duplicate registration numbers, and valid data types.
   */
  private void addBus()
  {
    if (regNoTextField.getText().isBlank()
        || busTypeBox.getValue().isBlank()
        || rentPricePerDayTextField.getText().isBlank()
        || seatCapacityTextField.getText().isBlank())
    {
      showMessage("Please fill in all fields.");
      return;
    }
    else if(availabilityGroup.getSelectedToggle() == null)
    {
      showMessage("Please select availability.");
      return;
    }
    try
    {
      String regNo = regNoTextField.getText().trim();
      String type = getComboValue(busTypeBox);
          //typeTextField.getText().trim();

      float rentPricePerDay = Float.parseFloat(
          rentPricePerDayTextField.getText().trim());
      int seatCapacity = Integer.parseInt(seatCapacityTextField.getText().trim());

      boolean availability = availableRadioButton.isSelected();

      //Check duplicate registration number
      for(Bus existingBus :
                  modelManager.getAllBuses().getAllBuses())
      {
        if(existingBus.getRegNo().equalsIgnoreCase(regNo))
        {
          showMessage("A bus with this registration number already exists.");
          return;
        }
      }

      Bus bus = new Bus(regNo, type, rentPricePerDay, seatCapacity,
          availability);

      modelManager.addBus(bus);
      modelManager.saveCompany();

      showMessage("Bus successfully added.");

      reset();
    }
    catch (NumberFormatException ex)
    {
      showMessage("Rent price and seat capacity must be valid numbers.");
    }
    catch (IllegalArgumentException ex)
    {
      showMessage(ex.getMessage());
    }
  }
  /**
   * Retrieves and validates the selected value from a combo box.
   *
   * @param comboBox the ComboBox to extract value from
   * @return the trimmed value from the combo box
   * @throws IllegalArgumentException if the value is null or empty
   */
  private String getComboValue(ComboBox<String> comboBox)
  {
    if (comboBox == null || comboBox.getValue() == null || comboBox.getValue().trim().isEmpty())
    {
      throw new IllegalArgumentException("Type" + " must be selected.");
    }

    return comboBox.getValue().trim();
  }
  /**
   * Populates the bus type combo box with available options.
   */
  private void setupComboBox()
  {
    if(busTypeBox!=null)
    {
      busTypeBox.setItems(FXCollections.observableArrayList(
          "Mini bus","Large bus"
      ));
    }
  }
  /**
   * Displays a confirmation dialog and exits the program if confirmed.
   */
  private void exitProgram()
  {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
        "Do you really want to exit the program?",
        ButtonType.YES, ButtonType.NO);
    alert.setTitle("Exit");
    alert.setHeaderText(null);

    alert.showAndWait();

    if (alert.getResult() == ButtonType.YES)
    {
      System.exit(0);
    }
  }
  /**
   * Displays an information dialog with details about the application.
   */
  private void showAbout()
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("About");
    alert.setHeaderText(null);
    alert.setContentText("This is a JavaFX bus management applications.");
    alert.showAndWait();
  }
  /**
   * Displays an information message dialog to the user.
   *
   * @param message the message to display
   */
  private void showMessage(String message)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
