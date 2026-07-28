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

    availabilityGroup.selectToggle(null);
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
      try
      {
        String regNo = regNoTextField.getText();
        String type = typeTextField.getText();
        float rentPricePerDay = Float.parseFloat(
            rentPricePerDayTextField.getText());
        int seatCapacity = Integer.parseInt(seatCapacityTextField.getText());
        boolean availability = availableRadioButton.isSelected();

        Bus bus = new Bus(regNo, type, rentPricePerDay, seatCapacity,
            availability);
        modelManager.addBus(bus);
        reset();
      }
      catch (NumberFormatException ex)
      {
        showMessage("Rent price and seat capacity must be valid numbers.");
      }
    }
    else if (e.getSource() == backButton)
    {
      viewHandler.openView("MainView");
    }
    else if (e.getSource() == exitMenuItem)
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
    else if (e.getSource() == aboutMenuItem)
    {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText(null);
      alert.setTitle("About");
      alert.setContentText("This is just a little program that demonstrates some of the GUI features in Java");
      alert.showAndWait();
    }
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
