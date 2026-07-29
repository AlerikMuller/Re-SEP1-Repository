package view.TripViewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import model.*;
import view.ViewHandler;

import java.time.LocalDate;
import java.util.Optional;

public class TripViewController
{
  @FXML private TextField originField;
  @FXML private TextField destinationField;
  @FXML private ComboBox<String> statusBox;
  @FXML private DatePicker startDatePicker;
  @FXML private DatePicker endDatePicker;
  @FXML private TextField startTimeField;
  @FXML private TextField endTimeField;
  @FXML private ComboBox<Customer> customerBox;
  @FXML private ComboBox<Bus> busBox;
  @FXML private ComboBox<Chauffeur> chauffeurBox;

  @FXML private TableView<Trip> tripTableView;
  @FXML private TableColumn<Trip, String> originColumn;
  @FXML private TableColumn<Trip, String> destinationColumn;
  @FXML private TableColumn<Trip, String> statusColumn;
  @FXML private TableColumn<Trip, String> dateIntervalColumn;
  @FXML private TableColumn<Trip, String> timeIntervalColumn;
  @FXML private TableColumn<Trip, String> customerColumn;
  @FXML private TableColumn<Trip, String> busColumn;
  @FXML private TableColumn<Trip, String> chauffeurColumn;

  @FXML private Button registerTripButton;
  @FXML private Button editTripButton;
  @FXML private Button removeTripButton;
  @FXML private Button assignBusChauffeurButton;
  @FXML private Button changeStatusButton;
  @FXML private Button viewPastTripButton;
  @FXML private Button clearButton;
  @FXML private Button backButton;

  private TripPlanningModelManager modelManager;
  private Scene scene;
  private boolean showingPastTrips;
  private ViewHandler viewHandler;

  public void init(ViewHandler viewHandler, Scene scene, TripPlanningModelManager modelManager)
  {
    this.modelManager = modelManager;
    this.scene = scene;
    this.viewHandler = viewHandler;

    setupStatusBox();
    setupTableColumns();
    setupTripSelection();
    refreshAll();
  }

  public Scene getScene(){
    return scene;
  }
  public void reset(){}
  @FXML
  public void handleActions(ActionEvent e)
  {
    if(e.getSource() == registerTripButton){
      //trip registration
      registerTrip();
    }
    else if(e.getSource() == editTripButton){
      //update trip
      editTrip();
    }
    else if(e.getSource() == removeTripButton){
      //delete any trip
      removeTrip();
    }
    else if(e.getSource() == backButton){
      viewHandler.openView("MainView");
    }
    else if(e.getSource() == assignBusChauffeurButton){
      //assign bus and chauffeur
      assignBusAndChauffeur();
    }
    else if(e.getSource()==changeStatusButton){
      //change status
      changeTripStatus();
    }
    else if(e.getSource() == viewPastTripButton){
      //view past trips
      viewPastTrips();
    }
    else if(e.getSource() == clearButton){
      //clear text fields
      clearFields();
    }
  }
  private void setupStatusBox()
  {
    if (statusBox == null)
    {
      return;
    }

    statusBox.setItems(FXCollections.observableArrayList(
        "Not Started",
        "Started",
        "Cancelled",
        "Ended"
    ));
    statusBox.setValue("Not Started");
  }

  private void setupTableColumns()
  {
    if (originColumn != null)
    {
      originColumn.setCellValueFactory(data ->
          new ReadOnlyStringWrapper(data.getValue().getOrigin()));
    }

    if (destinationColumn != null)
    {
      destinationColumn.setCellValueFactory(data ->
          new ReadOnlyStringWrapper(data.getValue().getDestination()));
    }

    if (statusColumn != null)
    {
      statusColumn.setCellValueFactory(data ->
          new ReadOnlyStringWrapper(data.getValue().getStatus()));
    }

    if (dateIntervalColumn != null)
    {
      dateIntervalColumn.setCellValueFactory(data ->
          new ReadOnlyStringWrapper(data.getValue().getDateIntervalString()));
    }

    if (timeIntervalColumn != null)
    {
      timeIntervalColumn.setCellValueFactory(data ->
          new ReadOnlyStringWrapper(data.getValue().getTimeIntervalString()));
    }

    if (customerColumn != null)
    {
      customerColumn.setCellValueFactory(data ->
      {
        Customer customer = data.getValue().getCustomer();
        return new ReadOnlyStringWrapper(customer == null ? "" : customer.toString());
      });
    }

    if (busColumn != null)
    {
      busColumn.setCellValueFactory(data ->
      {
        Bus bus = data.getValue().getAssignedBus();
        return new ReadOnlyStringWrapper(bus == null ? "" : bus.toString());
      });
    }

    if (chauffeurColumn != null)
    {
      chauffeurColumn.setCellValueFactory(data ->
      {
        Chauffeur chauffeur = data.getValue().getAssignedChauffeur();
        return new ReadOnlyStringWrapper(chauffeur == null ? "" : chauffeur.toString());
      });
    }
  }

  private void setupTripSelection()
  {
    if (tripTableView == null)
    {
      return;
    }

    tripTableView.getSelectionModel().selectedItemProperty().addListener(
        (observableValue, oldTrip, selectedTrip) ->
        {
          if (selectedTrip != null)
          {
            fillFieldsFromTrip(selectedTrip);
          }
        });
  }

  private void refreshAll()
  {
    refreshTrips();
    refreshCustomers();
    refreshBuses();
    refreshChauffeurs();
  }

  private void refreshTrips()
  {
    if (tripTableView == null)
    {
      return;
    }

    TripList tripList = showingPastTrips
        ? modelManager.getAllTripsByStatus("Ended")
        : modelManager.getAllTrips();

    ObservableList<Trip> trips = FXCollections.observableArrayList();

    for (int i = 0; i < tripList.size(); i++)
    {
      trips.add(tripList.getTrip(i));
    }

    tripTableView.setItems(trips);
  }

  private void refreshCustomers()
  {
    if (customerBox == null)
    {
      return;
    }

    CustomerList customerList = modelManager.getAllCustomers();
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    for (int i = 0; i < customerList.size(); i++)
    {
      customers.add(customerList.getCustomer(i));
    }

    customerBox.setItems(customers);
  }

  private void refreshBuses()
  {
    if (busBox == null)
    {
      return;
    }

    BusList busList = modelManager.getAllAvailableBuses(true);
    ObservableList<Bus> buses = FXCollections.observableArrayList();

    for (int i = 0; i < busList.size(); i++)
    {
      buses.add(busList.getBus(i));
    }

    busBox.setItems(buses);
  }

  private void refreshChauffeurs()
  {
    if (chauffeurBox == null)
    {
      return;
    }

    ChauffeurList chauffeurList = modelManager.getAllSuitableChauffeurs(true, true);

    // Fallback because the current Chauffeur.setSuitable(...) method in the model
    // may not correctly mark chauffeurs as suitable.
    if (chauffeurList.size() == 0)
    {
      chauffeurList = modelManager.getAllChauffeurs();
    }

    ObservableList<Chauffeur> chauffeurs = FXCollections.observableArrayList();

    for (int i = 0; i < chauffeurList.size(); i++)
    {
      Chauffeur chauffeur = chauffeurList.getChauffeur(i);
      if (chauffeur.isAvailable())
      {
        chauffeurs.add(chauffeur);
      }
    }

    chauffeurBox.setItems(chauffeurs);
  }

  @FXML
  private void registerTrip()
  {
    try
    {
      Trip trip = createTripFromFields();

      if (hasOverlappingAssignment(trip, null))
      {
        showError("The selected bus or chauffeur is already assigned to another overlapping trip.");
        return;
      }

      modelManager.registerTrip(trip);
      modelManager.saveCompany();

      showingPastTrips = false;
      refreshAll();
      clearFields();

      showConfirmation("Trip registered successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  @FXML
  private void viewTrip()
  {
    Trip selectedTrip = getSelectedTrip();

    if (selectedTrip == null)
    {
      showError("Please select a trip to view.");
      return;
    }

    fillFieldsFromTrip(selectedTrip);
    showConfirmation("Trip details loaded.");
  }

  @FXML
  private void editTrip()
  {
    Trip selectedTrip = getSelectedTrip();

    if (selectedTrip == null)
    {
      showError("Please select a trip to edit.");
      return;
    }

    try
    {
      selectedTrip.setOrigin(getText(originField, "Origin"));
      selectedTrip.setDestination(getText(destinationField, "Destination"));

      if (statusBox != null && statusBox.getValue() != null)
      {
        selectedTrip.setStatus(statusBox.getValue());
      }

      selectedTrip.setDateInterval(createDateInterval());
      selectedTrip.setTimeInterval(createTimeInterval());

      if (customerBox != null && customerBox.getValue() != null)
      {
        selectedTrip.setCustomer(customerBox.getValue());
      }

      if (busBox != null && busBox.getValue() != null)
      {
        selectedTrip.assignBus(busBox.getValue());
      }

      if (chauffeurBox != null && chauffeurBox.getValue() != null)
      {
        selectedTrip.assignChauffeur(chauffeurBox.getValue());
      }

      if (hasOverlappingAssignment(selectedTrip, selectedTrip))
      {
        showError("The selected bus or chauffeur is already assigned to another overlapping trip.");
        return;
      }

      modelManager.saveCompany();
      refreshAll();

      showConfirmation("Trip updated successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  @FXML
  private void removeTrip()
  {
    Trip selectedTrip = getSelectedTrip();

    if (selectedTrip == null)
    {
      showError("Please select a trip to remove.");
      return;
    }

    if (!confirm("Remove Trip", "Are you sure you want to remove the selected trip?"))
    {
      return;
    }

    try
    {
      modelManager.removeTrip(selectedTrip);
      modelManager.saveCompany();

      refreshAll();
      clearFields();

      showConfirmation("Trip removed successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  @FXML
  private void assignBusAndChauffeur()
  {
    Trip selectedTrip = getSelectedTrip();

    if (selectedTrip == null)
    {
      showError("Please select a trip first.");
      return;
    }

    if (busBox == null || busBox.getValue() == null)
    {
      showError("Please select a bus.");
      return;
    }

    if (chauffeurBox == null || chauffeurBox.getValue() == null)
    {
      showError("Please select a chauffeur.");
      return;
    }

    try
    {
      selectedTrip.assignBus(busBox.getValue());
      selectedTrip.assignChauffeur(chauffeurBox.getValue());

      if (hasOverlappingAssignment(selectedTrip, selectedTrip))
      {
        showError("The selected bus or chauffeur is already assigned to another overlapping trip.");
        return;
      }

      modelManager.saveCompany();
      refreshAll();

      showConfirmation("Bus and chauffeur assigned successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  @FXML
  private void displayAvailableBuses()
  {
    refreshBuses();
    showConfirmation("Available buses loaded.");
  }

  @FXML
  private void displaySuitableChauffeurs()
  {
    refreshChauffeurs();
    showConfirmation("Suitable chauffeurs loaded.");
  }

  @FXML
  private void changeTripStatus()
  {
    Trip selectedTrip = getSelectedTrip();

    if (selectedTrip == null)
    {
      showError("Please select a trip first.");
      return;
    }

    if (statusBox == null || statusBox.getValue() == null)
    {
      showError("Please select a status.");
      return;
    }

    try
    {
      selectedTrip.setStatus(statusBox.getValue());
      modelManager.changeTripStatus(selectedTrip);
      modelManager.saveCompany();

      refreshAll();
      showConfirmation("Trip status changed successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  @FXML
  private void viewPastTrips()
  {
    showingPastTrips = true;
    refreshTrips();
    showConfirmation("Past trips loaded.");
  }

  @FXML
  private void clearFields()
  {
    if (originField != null)
    {
      originField.clear();
    }

    if (destinationField != null)
    {
      destinationField.clear();
    }

    if (statusBox != null)
    {
      statusBox.setValue("Not Started");
    }

    if (startDatePicker != null)
    {
      startDatePicker.setValue(null);
    }

    if (endDatePicker != null)
    {
      endDatePicker.setValue(null);
    }

    if (startTimeField != null)
    {
      startTimeField.clear();
    }

    if (endTimeField != null)
    {
      endTimeField.clear();
    }

    if (customerBox != null)
    {
      customerBox.setValue(null);
    }

    if (busBox != null)
    {
      busBox.setValue(null);
    }

    if (chauffeurBox != null)
    {
      chauffeurBox.setValue(null);
    }

    if (tripTableView != null)
    {
      tripTableView.getSelectionModel().clearSelection();
    }
  }

  @FXML
  private void back()
  {
    showConfirmation("Back button pressed. View navigation can be connected later through ViewHandler.");
  }

  private Trip createTripFromFields()
  {
    String origin = getText(originField, "Origin");
    String destination = getText(destinationField, "Destination");
    String status = statusBox == null || statusBox.getValue() == null
        ? "Not Started"
        : statusBox.getValue();

    DateInterval dateInterval = createDateInterval();
    TimeInterval timeInterval = createTimeInterval();

    if (busBox == null || busBox.getValue() == null)
    {
      throw new IllegalArgumentException("Please select an available bus.");
    }

    if (chauffeurBox == null || chauffeurBox.getValue() == null)
    {
      throw new IllegalArgumentException("Please select a suitable chauffeur.");
    }

    Bus bus = busBox.getValue();
    Chauffeur chauffeur = chauffeurBox.getValue();

    Customer customer = customerBox == null ? null : customerBox.getValue();

    if (customer == null)
    {
      return new Trip(origin, destination, status, bus, chauffeur, dateInterval, timeInterval);
    }

    return new Trip(origin, destination, status, bus, chauffeur, dateInterval, timeInterval, customer);
  }

  private DateInterval createDateInterval()
  {
    if (startDatePicker == null || startDatePicker.getValue() == null)
    {
      throw new IllegalArgumentException("Please select a start date.");
    }

    if (endDatePicker == null || endDatePicker.getValue() == null)
    {
      throw new IllegalArgumentException("Please select an end date.");
    }

    model.Date startDate = convertLocalDate(startDatePicker.getValue());
    model.Date endDate = convertLocalDate(endDatePicker.getValue());

    return new DateInterval(startDate, endDate);
  }

  private TimeInterval createTimeInterval()
  {
    Time startTime = parseTime(getText(startTimeField, "Start time"));
    Time endTime = parseTime(getText(endTimeField, "End time"));

    return new TimeInterval(startTime, endTime);
  }

  private model.Date convertLocalDate(LocalDate localDate)
  {
    return new model.Date(
        localDate.getDayOfMonth(),
        localDate.getMonthValue(),
        localDate.getYear()
    );
  }

  private LocalDate convertModelDate(model.Date date)
  {
    return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
  }

  private Time parseTime(String text)
  {
    String[] parts = text.trim().split(":");

    if (parts.length != 2 && parts.length != 3)
    {
      throw new IllegalArgumentException("Time must be written as HH:mm or HH:mm:ss.");
    }

    int hour = Integer.parseInt(parts[0]);
    int minute = Integer.parseInt(parts[1]);
    int second = parts.length == 3 ? Integer.parseInt(parts[2]) : 0;

    return new Time(hour, minute, second);
  }

  private String getText(TextField field, String fieldName)
  {
    if (field == null || field.getText() == null || field.getText().trim().isEmpty())
    {
      throw new IllegalArgumentException(fieldName + " cannot be empty.");
    }

    return field.getText().trim();
  }

  private Trip getSelectedTrip()
  {
    if (tripTableView == null)
    {
      return null;
    }

    return tripTableView.getSelectionModel().getSelectedItem();
  }

  private void fillFieldsFromTrip(Trip trip)
  {
    if (trip == null)
    {
      return;
    }

    if (originField != null)
    {
      originField.setText(trip.getOrigin());
    }

    if (destinationField != null)
    {
      destinationField.setText(trip.getDestination());
    }

    if (statusBox != null)
    {
      statusBox.setValue(trip.getStatus());
    }

    if (startDatePicker != null && trip.getDateInterval() != null)
    {
      startDatePicker.setValue(convertModelDate(trip.getDateInterval().getStartDate()));
    }

    if (endDatePicker != null && trip.getDateInterval() != null)
    {
      endDatePicker.setValue(convertModelDate(trip.getDateInterval().getEndDate()));
    }

    if (startTimeField != null && trip.getTimeInterval() != null)
    {
      startTimeField.setText(trip.getTimeInterval().getStartTime().toString());
    }

    if (endTimeField != null && trip.getTimeInterval() != null)
    {
      endTimeField.setText(trip.getTimeInterval().getEndTime().toString());
    }

    if (customerBox != null)
    {
      customerBox.setValue(trip.getCustomer());
    }

    if (busBox != null)
    {
      busBox.setValue(trip.getAssignedBus());
    }

    if (chauffeurBox != null)
    {
      chauffeurBox.setValue(trip.getAssignedChauffeur());
    }
  }

  private boolean hasOverlappingAssignment(Trip tripToCheck, Trip tripToIgnore)
  {
    TripList allTrips = modelManager.getAllTrips();

    for (int i = 0; i < allTrips.size(); i++)
    {
      Trip existingTrip = allTrips.getTrip(i);

      if (existingTrip == tripToIgnore)
      {
        continue;
      }

      boolean sameBus = existingTrip.getAssignedBus() == tripToCheck.getAssignedBus();
      boolean sameChauffeur = existingTrip.getAssignedChauffeur() == tripToCheck.getAssignedChauffeur();

      if ((sameBus || sameChauffeur) && existingTrip.overlaps(tripToCheck))
      {
        return true;
      }
    }

    return false;
  }

  private boolean confirm(String title, String message)
  {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);

    Optional<ButtonType> result = alert.showAndWait();

    return result.isPresent() && result.get() == ButtonType.OK;
  }

  private void showError(String message)
  {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message == null ? "An unknown error occurred." : message);
    alert.showAndWait();
  }

  private void showConfirmation(String message)
  {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
