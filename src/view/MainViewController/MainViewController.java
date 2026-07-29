package view.MainViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import model.TripPlanningModelManager;
import view.ViewHandler;

public class MainViewController
{
  private Scene scene;
  private TripPlanningModelManager modelManager;
  private ViewHandler viewHandler;

  @FXML private Button createNewTripButton;
  @FXML private Button manageTripButton;
  @FXML private Button addBusButton;
  @FXML private Button manageBusButton;
  @FXML private Button addCustomerButton;
  @FXML private Button manageCustomerButton;
  @FXML private Button addChauffeurButton;
  @FXML private Button manageChauffeurButton;
  @FXML private MenuItem exitMenuItem;
  @FXML private MenuItem aboutMenuItem;

  public void init(ViewHandler viewHandler, Scene scene, TripPlanningModelManager modelManager)
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

  @FXML
  public void handleActions(ActionEvent e)
  {
    if (e.getSource() == addCustomerButton || e.getSource() == manageCustomerButton)
    {
      // Both open the Customer view (it handles add + manage in one screen).
      viewHandler.openView("CustomerView");
    }
    else if (e.getSource() == manageBusButton)
    {
      viewHandler.openView("BusMainView");
    }
    else if(e.getSource() == addChauffeurButton || e.getSource() == manageChauffeurButton)
    {
      viewHandler.openView("ChauffeurView");
    }
    else if(e.getSource() == addBusButton)
    {
      viewHandler.openView("AddBusView");
    }
    else if(e.getSource() == createNewTripButton || e.getSource() == manageTripButton)
    {
      viewHandler.openView("TripView");
    }
   /*   // These views belong to teammates and aren't on this branch yet.
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not available yet");
      alert.setHeaderText(null);
      alert.setContentText("This section is still being implemented by another team member.");
      alert.showAndWait();
    }
    */
    else if (e.getSource() == exitMenuItem)
    {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
          "Do you really want to exit the program?",
          ButtonType.YES, ButtonType.NO);
      alert.setTitle("Exit");
      alert.setHeaderText("Trip Planning Company");
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES)
      {
        System.exit(0);
      }
    }
    else if (e.getSource() == aboutMenuItem)
    {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText("Trip Planning Company");
      alert.setTitle("About");
      alert.setContentText("Trip planning system for Horsens Tours — manage buses, "
          + "chauffeurs, customers and trips.");
      alert.showAndWait();
    }
  }
}
