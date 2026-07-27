package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TripPlanningModelManager;

public class ViewHandler
{
  private Stage stage;

  private TripPlanningModelManager modelManager;

  public ViewHandler(Stage stage, TripPlanningModelManager modelManager)
  {
    this.stage = stage;
    this.modelManager = modelManager;
  }
  public void start()
  {

  }
}
