package view.BusViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
  @FXML private TextField availabilityTextField;

  public void init(ViewHandler viewHandler, Scene scene,
      TripPlanningModelManager modelManager)
  {
    this.viewHandler = viewHandler;
    this.scene = scene;
    this.modelManager = modelManager;
  }

  public void reset()
  {
  }

  public Scene getScene()
  {
    return scene;
  }

  public void handleActions(ActionEvent e)
  {

  }
}
