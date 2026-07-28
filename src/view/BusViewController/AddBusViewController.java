package view.BusViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.Bus;
import model.TripPlanningModelManager;
import view.ViewHandler;

public class AddBusViewController
{
  private Scene scene;
  private TripPlanningModelManager modelManager;
  private ViewHandler viewHandler;

  @FXML private Button addButton;
  @FXML private Button backButton;

  @FXML private TextField regNoTextField;
  @FXML private TextField typeTextField;
  @FXML private TextField rentPricePerDayTextField;
  @FXML private TextField seatCapacityTextField;
  @FXML private RadioButton availableRadioButton;
  @FXML private RadioButton unavailableRadioButton;
  @FXML private ToggleGroup availabilityGroup;
  @FXML private MenuItem exitMenuItem;
  @FXML private MenuItem aboutMenuItem;

  @FXML public void initialize()
  {
    availabilityGroup = new ToggleGroup();
    availableRadioButton.setToggleGroup(availabilityGroup);
    unavailableRadioButton.setToggleGroup(availabilityGroup);
  }
  public void init(ViewHandler viewHandler, Scene scene,
      TripPlanningModelManager modelManager)
  {
    this.viewHandler = viewHandler;
    this.scene = scene;
    this.modelManager = modelManager;
  }
  public void reset()
  {
    regNoTextField.clear();
    typeTextField.clear();
    rentPricePerDayTextField.clear();
    seatCapacityTextField.clear();

    if (availabilityGroup != null)
    {
      availabilityGroup.selectToggle(null);
    }
  }

  public Scene getScene()
  {
    return scene;
  }

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
  private void addBus()
  {
    if (regNoTextField.getText().isBlank()
        || typeTextField.getText().isBlank()
        || rentPricePerDayTextField.getText().isBlank()
        || seatCapacityTextField.getText().isBlank())
    {
      showMessage("Please fill in all fields.");
      return;
    }
    if(availabilityGroup.getSelectedToggle() == null)
    {
      showMessage("Please select availability.");
      return;
    }
    try
    {
      String regNo = regNoTextField.getText().trim();
      String type = typeTextField.getText().trim();

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
  private void showAbout()
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("About");
    alert.setHeaderText(null);
    alert.setContentText("This is a JavaFX bus management applications.");
    alert.showAndWait();
  }
  private void showMessage(String message)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
