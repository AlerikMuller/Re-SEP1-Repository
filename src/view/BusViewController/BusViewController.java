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

  private String tempRegNo;
  private String tempType;
  private String tempRentPrice;
  private String tempSeatCapacity;
  private String tempAvailability;


  public void init(ViewHandler viewHandler, Scene scene,  TripPlanningModelManager modelManager)
  {
    this.modelManager = modelManager;
    this.scene = scene;
    this.viewHandler = viewHandler;

    setUpTable();
    reset();
  }

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

  public void reset()
  {
    if(modelManager!=null)
    {
      bussTableView.getItems().setAll(modelManager.getAllBuses().getAllBuses());
    }
  }
  public Scene getScene()
  {
    return scene;
  }
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

  @FXML
  private void refresh()
  {
    bussTableView.getItems().clear();
    reset();
  }

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


    VBox box = new VBox(10,
        new Label("Registration Number"),
        regNo,

        new Label("Type"),
        type,

        new Label("Rent price"),
        price,

        new Label("Seats"),
        seats
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


  @FXML
  private void deleteBus(){
    Bus selectedBus =  bussTableView.getSelectionModel().getSelectedItem();
    if(selectedBus != null){
      modelManager.removeBus(selectedBus);
      bussTableView.getItems().remove(selectedBus);
    }else{
      showMessage("Please select a bus.");
    }
  }

  private void showMessage(String message)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
