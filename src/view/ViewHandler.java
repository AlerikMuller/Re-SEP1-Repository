package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.TripPlanningModelManager;
import view.BusViewController.AddBusViewController;
import view.BusViewController.BusViewController;
import view.ChauffeurViewController.AddChauffeurViewController;
import view.ChauffeurViewController.ChauffeurViewController;
import view.CustomerViewController.CustomerViewController;
import view.MainViewController.MainViewController;
import view.TripViewController.AddTripViewController;
import view.TripViewController.TripViewController;

import java.io.IOException;

/**
 * Manages view transitions and loads all application views.
 * Acts as the central controller for switching between different scenes and initializing their controllers.
 *
 * @author Kelsang Sherpa
 * @version 1.0
 */
public class ViewHandler
{
  private final Stage stage;

  private MainViewController mainViewController;
  private TripViewController tripViewController;
  private AddTripViewController addTripViewController;
  private ChauffeurViewController chauffeurViewController;
  private AddChauffeurViewController addChauffeurViewController;
  private CustomerViewController customerViewController;
  private BusViewController busViewController;
  private AddBusViewController addBusViewController;

  private final TripPlanningModelManager modelManager;

  /**
   * Constructs a ViewHandler with the primary stage and model manager.
   *
   * @param stage the primary Stage for displaying scenes
   * @param modelManager the TripPlanningModelManager for data access
   */
  public ViewHandler(Stage stage, TripPlanningModelManager modelManager)
  {
    this.stage = stage;
    this.modelManager = modelManager;
  }

  /**
   * Initializes and loads all application views.
   * Then displays the MainView as the starting point.
   */
  public void start()
  {
    loadViewBus();
    loadViewAddBus();
    loadMainView();
    loadCustomerView();
    loadTripView();
    loadAddTripView();
    loadChauffeurView();
    loadAddChauffeurView();
    openView("MainView");
  }

  /**
   * Opens the view with the specified ID and displays it on the stage.
   * Resets the view controller before displaying.
   *
   * @param id the identifier of the view to open (e.g., "MainView", "BusMainView", "AddBusView")
   */
  public void openView(String id)
  {
    switch (id)
    {
      case "MainView":
        if (mainViewController == null)
        {
          showMessage("Main view could not be loaded.");
        }
        stage.setScene(mainViewController.getScene());
        mainViewController.reset();
        break;

      case "AddTripView":
        if (addTripViewController == null)
        {
          showMessage("Add trip view could not be loaded.");
        }
        stage.setScene(addTripViewController.getScene());
        addTripViewController.reset();
        break;

      case "TripView":
        if (tripViewController == null)
        {
          showMessage("Trip view could not be loaded.");
        }
        stage.setScene(tripViewController.getScene());
        tripViewController.reset();
        break;

      case "CustomerView":
        if (customerViewController == null)
        {
          showMessage("Customer view could not be loaded.");
        }
        stage.setScene(customerViewController.getScene());
        customerViewController.reset();
        break;

      case "AddBusView":
        if (addBusViewController == null)
        {
          showMessage("Add bus view could not be loaded.");
        }
        stage.setScene(addBusViewController.getScene());
        addBusViewController.reset();
        break;

      case "BusMainView":
        if (busViewController == null)
        {
          showMessage("Bus view could not be loaded.");
        }
        stage.setScene(busViewController.getScene());
        busViewController.reset();
        break;

      case "AddChauffeurView":
        if (addChauffeurViewController == null)
        {
          showMessage("Add chauffeur view could not be loaded.");
        }
        stage.setScene(addChauffeurViewController.getScene());
        addChauffeurViewController.reset();
        break;

      case "ChauffeurView":
        if (chauffeurViewController == null)
        {
          showMessage("Chauffeur view could not be loaded.");
        }
        stage.setScene(chauffeurViewController.getScene());
        chauffeurViewController.reset();
        break;
    }

    String title = "";

    if (stage.getScene().getRoot().getUserData() != null)
    {
      title = stage.getScene().getRoot().getUserData().toString();
    }

    stage.setTitle(title);
    stage.show();
  }

  /**
   * Loads the main view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
  private void loadMainView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/MainViewController/MainView.fxml"));
      Region root = loader.load();
      mainViewController = loader.getController();
      Scene scene = new Scene(root);
      mainViewController.init(this, scene, modelManager);
      stage.setScene(scene);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Loads the trip view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
  private void loadTripView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/TripViewController/TripView.fxml"));
      Region root = loader.load();
      tripViewController = loader.getController();
      Scene scene = new Scene(root);
      tripViewController.init(this, scene, modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Loads the add trip view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
  private void loadAddTripView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/TripViewController/AddTripView.fxml"));
      Region root = loader.load();
      addTripViewController = loader.getController();
      Scene scene = new Scene(root);
      addTripViewController.init(this, scene, modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Loads the chauffeur view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
  private void loadChauffeurView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/ChauffeurViewController/ChauffeurView.fxml"));
      Region root = loader.load();
      chauffeurViewController = loader.getController();
      Scene scene = new Scene(root);
      chauffeurViewController.init(this, scene, modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Loads the add chauffeur view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
  private void loadAddChauffeurView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/ChauffeurViewController/AddChauffeurView.fxml"));
      Region root = loader.load();
      addChauffeurViewController = loader.getController();
      Scene scene = new Scene(root);
      addChauffeurViewController.init(this, scene, modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Loads the bus view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
  private void loadViewBus()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/BusViewController/BusViewController.fxml"));
      Region root = loader.load();
      busViewController = loader.getController();
      busViewController.init(this, new Scene(root), modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Loads the add bus view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
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

  /**
   * Loads the customer view from FXML and initializes its controller.
   *
   * @throws RuntimeException if the FXML file cannot be loaded
   */
  private void loadCustomerView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/view/CustomerViewController/CustomerView.fxml"));
      Region root = loader.load();
      customerViewController = loader.getController();
      Scene scene = new Scene(root);
      customerViewController.init(this, scene, modelManager);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Displays an error alert dialog with the specified message.
   *
   * @param message the error message to display
   */
  private void showMessage(String message)
  {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("ErrorMessage");
    alert.setHeaderText("Error");
    alert.setContentText(message);
    alert.showAndWait();
  }
}