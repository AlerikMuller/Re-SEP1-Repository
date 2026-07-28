package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.TripPlanningModelManager;
import view.BusViewController.AddBusViewController;
import view.BusViewController.BusViewController;

import java.io.IOException;

public class ViewHandler
{
  private final Stage stage;
  private BusViewController busViewController;
  private AddBusViewController addBusViewController;

  private final TripPlanningModelManager modelManager;

  public ViewHandler(Stage stage, TripPlanningModelManager modelManager)
  {
    this.stage = stage;
    this.modelManager = modelManager;
  }
  public void start()
  {
    //kelsang
   loadViewBus();
   loadViewAddBus();
   openView("BusMainView");
   //Giyath
    //Alerik
  }

  public  void openView(String id)
  {
    switch (id)
    {
      case "AddBusView":
        if(addBusViewController == null)
        {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("ErrorMessage");
          alert.setHeaderText("Error");
          alert.setContentText("Please enter a valid Bus information");
          alert.showAndWait();
        }
        stage.setScene(addBusViewController.getScene());
        addBusViewController.reset();
        break;
      case "BusMainView":
        if(busViewController == null){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("ErrorMessage");
          alert.setHeaderText("Error");
          alert.setContentText("Please enter a valid Bus information");
          alert.showAndWait();
        }
        stage.setScene(busViewController.getScene());
        busViewController.reset();
        break;
        //cases....
    }
    String title = "";
    if(stage.getScene().getRoot().getUserData()!=null)
    {
      title = stage.getScene().getRoot().getUserData().toString();
    }
    stage.setTitle(title);
    stage.show();
  }

  private void loadViewBus()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/BusViewController/BusViewController.fxml"));
      Region root = loader.load();
      busViewController = loader.getController();
      busViewController.init(this,new Scene(root),modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }
  private void loadViewAddBus()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/BusViewController/AddBusViewController.fxml"));
      Region root = loader.load();
      addBusViewController = loader.getController();
      addBusViewController.init(this, new Scene(root), modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }
}
