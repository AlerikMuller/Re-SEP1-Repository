package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import model.TripPlanningModelManager;

import java.io.IOException;

public class ViewHandler
{
  private Stage stage;
  private TripPlanningModelManager modelManager;

  private MainViewController mainViewController;
  private CustomerViewController customerViewController;

  public ViewHandler(Stage stage, TripPlanningModelManager modelManager)
  {
    this.stage = stage;
    this.modelManager = modelManager;
  }

  public void start()
  {
    openView("MainView");
  }

  public void openView(String id)
  {
    Region root = null;
    switch (id)
    {
      case "MainView":
        root = loadMainView();
        break;
      case "CustomerView":
        root = loadCustomerView();
        break;
      // Teammates' views (BusView, ChauffeurView, TripView...) will be added here later.
    }

    if (root != null)
    {
      stage.setTitle("Horsens Tours");
      stage.show();
    }
  }

  private Region loadMainView()
  {
    Region root = null;
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("MainView.fxml"));
      root = loader.load();
      mainViewController = loader.getController();
      Scene scene = new Scene(root);
      mainViewController.init(this, scene, modelManager);
      stage.setScene(scene);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return root;
  }

  private Region loadCustomerView()
  {
    Region root = null;
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("CustomerView.fxml"));
      root = loader.load();
      customerViewController = loader.getController();
      Scene scene = new Scene(root);
      customerViewController.init(this, scene, modelManager);
      stage.setScene(scene);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return root;
  }
}
