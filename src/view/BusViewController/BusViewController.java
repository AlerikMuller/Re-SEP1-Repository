package view.BusViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    /*
   regNoColumn.setCellValueFactory(new PropertyValueFactory<>("regNo"));
   typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
   rentPricePerDayColumn.setCellValueFactory(new PropertyValueFactory<>("rentPricePerDay"));
   seatCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("seatCapacity"));
   availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
   bussTableView.getItems().setAll(modelManager.getAllBuses().getAllBuses());
  */
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
      refreshBusTableView();
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
      updateBusAvailability();
    }
  }

  @FXML
  private void refreshBusTableView()
  {
    bussTableView.getItems().clear();
    reset();
  }

  @FXML
  private void updateBusAvailability()
  {
    Bus selectedBus = bussTableView.getSelectionModel().getSelectedItem();
    if(selectedBus != null){
      modelManager.updateBusAvailability(selectedBus.getAvailability(), selectedBus);
      reset();
    }else{
      showMessage("Please select a bus or choose availability.");
    }
  }


  @FXML
  private void deleteBus(){
    Bus selectedBus =  bussTableView.getSelectionModel().getSelectedItem();
    if(selectedBus != null){
      modelManager.removeBus(selectedBus);
      reset();
    }else{
      showMessage("Please select a bus.");
    }
  }

  private void showMessage(String message)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
