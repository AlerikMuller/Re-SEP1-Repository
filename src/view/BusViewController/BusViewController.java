package view.BusViewController;

import javafx.fxml.FXML;
import javafx.scene.Scene;
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

  @FXML private TableView<Bus> bussTableView;
  @FXML private TableColumn<Bus, String> regNoColumn;
  @FXML private TableColumn<Bus, String> typeColumn;
  @FXML private TableColumn<Bus, Float> rentPricePerDayColumn;
  @FXML private TableColumn<Bus, Integer> seatCapacityColumn;
  @FXML private TableColumn<Bus, Boolean> availabilityColumn;
  @FXML private Button refreshButton;
  @FXML private Button backButton;

  public void init(TripPlanningModelManager modelManager, ViewHandler viewHandler)
  {
    this.modelManager = modelManager;
    this.viewHandler = viewHandler;
    setUpTable();
    reset();
  }

  private void setUpTable()
  {
   regNoColumn.setCellValueFactory(new PropertyValueFactory<>("regNo"));
   typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
   rentPricePerDayColumn.setCellValueFactory(new PropertyValueFactory<>("rentPricePerDay"));
   seatCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("seatCapacity"));
   availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
  }

  public void reset()
  {

  }

}
