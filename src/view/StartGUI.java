package view;

import javafx.application.Application;
import javafx.stage.Stage;
import model.TripPlanningModelManager;

public class StartGUI extends Application
{
  public void start(Stage window)
  {
    TripPlanningModelManager modelManager = new TripPlanningModelManager("company.json");
    modelManager.loadCompany();
    ViewHandler viewHandler = new ViewHandler(window, modelManager);
    viewHandler.start();
  }
}
