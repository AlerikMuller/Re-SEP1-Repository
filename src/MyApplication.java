import javafx.application.Application;
import javafx.stage.Stage;

import model.TripPlanningModelManager;
import View.ViewHandler;

public class MyApplication extends Application
{
  @Override
  public void start(Stage primaryStage)
  {
    // Create the model manager (loads company data from the JSON file).
    TripPlanningModelManager modelManager =
        new TripPlanningModelManager("tripPlanningCompany.json");
    modelManager.loadCompany();

    // Start navigation on the dashboard (MainView).
    ViewHandler viewHandler = new ViewHandler(primaryStage, modelManager);
    viewHandler.start();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
