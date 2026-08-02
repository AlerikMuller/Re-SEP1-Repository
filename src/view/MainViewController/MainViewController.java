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

  @FXML private Button addNewTripButton;
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
      viewHandler.openView("CustomerView");
    }
    else if (e.getSource() == manageBusButton)
    {
      viewHandler.openView("BusMainView");
    }
    else if (e.getSource() == addBusButton)
    {
      viewHandler.openView("AddBusView");
    }
    else if (e.getSource() == addNewTripButton)
    {
      viewHandler.openView("AddTripView");
    }
    else if (e.getSource() == manageTripButton)
    {
      viewHandler.openView("TripView");
    }
    else if (e.getSource() == addChauffeurButton)
    {
      viewHandler.openView("AddChauffeurView");
    }
    else if (e.getSource() == manageChauffeurButton)
    {
      viewHandler.openView("ChauffeurView");
    }
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
      alert.setContentText("Trip planning system for Horsens Tours — manage buses, chauffeurs, customers and trips.");
      alert.showAndWait();
    }
  }
}