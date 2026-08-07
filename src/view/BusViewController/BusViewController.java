package view.BusViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Bus;
import model.TripPlanningModelManager;
import view.ViewHandler;

/**
 * Controller for viewing and managing buses in a table.
 * Provides functionality to view, update, and delete buses.
 *
 * @author Kelsang Sherpa
 * @version 1.0
 */
public class BusViewController
{

  private TripPlanningModelManager modelManager;
  private ViewHandler viewHandler;
  private Scene scene;

  @FXML private TableView<Bus> bussTableView;
  @FXML private TableColumn<Bus, String> regNoColumn;
  @FXML private TableColumn<Bus, String> typeColumn;
  @FXML private TableColumn<Bus, Float> rentPricePerDayColumn;
  @FXML private TableColumn<Bus, Integer> seatCapacityColumn;
  @FXML private TableColumn<Bus, Boolean> availabilityColumn;

  @FXML private Button refreshButton;
  @FXML private Button removeButton;
  @FXML private Button updateButton;
  @FXML private Button backButton;


  /**
   * Initializes the controller with scene, view handler, and model manager.
   * Sets up the table and populates it with bus data.
   *
   * @param viewHandler the view handler for navigation
   * @param scene the scene for this view
   * @param modelManager the model manager for data access
   */
  public void init(ViewHandler viewHandler, Scene scene,  TripPlanningModelManager modelManager)
  {
    this.modelManager = modelManager;
    this.scene = scene;
    this.viewHandler = viewHandler;

    setUpTable();
    reset();
  }

  /**
   * Configures table columns to display bus properties.
   * Maps each column to the corresponding Bus object attribute.
   */
  private void setUpTable()
  {
    //Connect columns with Bus attributes
   regNoColumn.setCellValueFactory(new PropertyValueFactory<>("regNo"));
   typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
   rentPricePerDayColumn.setCellValueFactory(new PropertyValueFactory<>("rentPricePerDay"));
   seatCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("seatCapacity"));
   availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
   bussTableView.getItems().setAll(modelManager.getAllBuses().getAllBuses());
  }

  /**
   * Refreshes the table with current bus data from the model manager.
   */
  public void reset()
  {
    if(modelManager!=null)
    {
      bussTableView.getItems().setAll(modelManager.getAllBuses().getAllBuses());
    }
  }
  /**
   * Returns the scene of this view.
   *
   * @return the Scene for this controller
   */
  public Scene getScene()
  {
    return scene;
  }
  /**
   * Handles button actions including refresh, delete, update, and back navigation.
   *
   * @param e the ActionEvent triggered by user interaction
   */
  @FXML
  public void handleActions(ActionEvent e)
  {
    if(e.getSource() == refreshButton){
      // refresh button
      refresh();
    }
    else if(e.getSource() == removeButton){
      //remove button
      deleteBus();
    }
    else if(e.getSource() == backButton){
      // back button
      viewHandler.openView("MainView");
    }
    else if(e.getSource() == updateButton){
      //update button
      updateBus();
    }
  }

  /**
   * Clears and reloads the bus table with fresh data.
   */
  @FXML
  private void refresh()
  {
    bussTableView.getItems().clear();
    reset();
  }

  /**
   * Opens an edit dialog for the selected bus.
   * Updates the bus data if confirmed by the user.
   */
  @FXML
  private void updateBus()
  {
    Bus selectedBus =
        bussTableView.getSelectionModel()
            .getSelectedItem();

    if(selectedBus == null)
    {
      showMessage("Please select a bus.");
      return;
    }
    // Open edit dialog
    Dialog<Bus> dialog = new Dialog<>();

    dialog.setTitle("Update Bus");


    ButtonType updateButton =
        new ButtonType(
            "Update",
            ButtonBar.ButtonData.OK_DONE);

    dialog.getDialogPane()
        .getButtonTypes()
        .addAll(updateButton, ButtonType.CANCEL);



    TextField regNo =
        new TextField(selectedBus.getRegNo());

    TextField type =
        new TextField(selectedBus.getType());

    TextField price =
        new TextField(
            String.valueOf(
                selectedBus.getRentPricePerDay()));

    TextField seats =
        new TextField(
            String.valueOf(
                selectedBus.getSeatCapacity()));

    TextField availability =
        new TextField(
            String.valueOf(
                selectedBus.getAvailability()
            )
        );


    VBox box = new VBox(10,
        new Label("Registration Number"),
        regNo,

        new Label("Type"),
        type,

        new Label("Rent price"),
        price,

        new Label("Seats"),
        seats,

        new Label("Availability"),
        availability
    );


    dialog.getDialogPane()
        .setContent(box);



    dialog.setResultConverter(button ->
    {
      if(button == updateButton)
      {
        try
        {
          selectedBus.setRegNo(regNo.getText());
          selectedBus.setType(type.getText());
          selectedBus.setRentPricePerDay(
              Float.parseFloat(price.getText()));

          selectedBus.setSeatCapacity(
              Integer.parseInt(seats.getText()));
          selectedBus.setAvailability(
              Boolean.parseBoolean(availability.getText()));


          return selectedBus;

        }
        catch(Exception e)
        {
          showMessage("Invalid data.");
        }
      }

      return null;
    });



    dialog.showAndWait()
        .ifPresent(bus ->
        {
          modelManager.updateBus(bus);
          modelManager.saveCompany();

          bussTableView.refresh();

          showMessage(
              "Bus updated successfully.");
        });
  }


  /**
   * Removes the selected bus from the system.
   * Displays a message if no bus is selected.
   */
  @FXML
  private void deleteBus()
  {
    Bus selectedBus =  bussTableView.getSelectionModel().getSelectedItem();
    if(selectedBus != null){
      modelManager.removeBus(selectedBus);
      bussTableView.getItems().remove(selectedBus);
    }else{
      showMessage("Please select a bus.");
    }
  }

  /**
   * Displays an information message dialog to the user.
   *
   * @param message the message to display
   */
  private void showMessage(String message)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
