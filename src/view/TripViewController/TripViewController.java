package view.TripViewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Bus;
import model.BusList;
import model.Chauffeur;
import model.ChauffeurList;
import model.Customer;
import model.CustomerList;
import model.DateInterval;
import model.Time;
import model.TimeInterval;
import model.Trip;
import model.TripList;
import model.TripPlanningModelManager;
import view.ViewHandler;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controls the view used for managing registered trips in the system.
 * The controller displays trip information, allows supported trip editing
 * and removal, manages resource and customer selections, validates date
 * and time intervals, detects overlapping assignments, and filters ended trips.
 *
 * @author Alerik Muller
 * @version 1.0
 */

//Controls trip overview, editing, removal, filtering, and resource reassignment.
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

  @FXML private Button editTripButton;
  @FXML private Button removeTripButton;
  @FXML private Button viewPastTripButton;
  @FXML private Button refreshTripsButton;
  @FXML private Button clearButton;
  @FXML private Button backButton;
  @FXML private Button deselectCustomerButton;
  @FXML private Button deselectBusButton;
  @FXML private Button deselectChauffeurButton;

  private TripPlanningModelManager modelManager;
  private Scene scene;
  private boolean showingPastTrips;
  private ViewHandler viewHandler;
  private final ObservableList<Trip> tripRows = FXCollections.observableArrayList();

  //Connects controller dependencies and prepares the management interface.
  public void init(ViewHandler viewHandler, Scene scene, TripPlanningModelManager modelManager)
  {
    this.modelManager = modelManager;
    this.scene = scene;
    this.viewHandler = viewHandler;
    showingPastTrips = false;
    setupStatusBox();
    setupTableColumns();
    setupTripSelection();
    refreshAll();
  }

  public Scene getScene()
  {
    return scene;
  }

  public void reset()
  {
    refreshAll();
  }

  //Routes interface actions to editing, removal, filtering, and navigation.
  @FXML
  public void handleActions(ActionEvent e)
  {
    if (e.getSource() == editTripButton)
    {
      editTrip();
    }
    else if (e.getSource() == removeTripButton)
    {
      removeTrip();
    }
    else if (e.getSource() == backButton)
    {
      viewHandler.openView("MainView");
    }
    else if (e.getSource() == viewPastTripButton)
    {
      viewPastTrips();
    }
    else if (e.getSource() == refreshTripsButton)
    {
      showAllTrips();
    }
    else if (e.getSource() == clearButton)
    {
      clearFields();
    }
    else if (e.getSource() == deselectCustomerButton)
    {
      deselectComboBox(customerBox);
    }
    else if (e.getSource() == deselectBusButton)
    {
      deselectComboBox(busBox);
    }
    else if (e.getSource() == deselectChauffeurButton)
    {
      deselectComboBox(chauffeurBox);
    }
  }

  private void setupStatusBox()
  {
    if (statusBox != null)
    {
      statusBox.setItems(FXCollections.observableArrayList("Not Started", "Started", "Cancelled", "Ended"));
      statusBox.setValue("Not Started");
    }
  }

  //Maps trip properties and assignments into their displayed table columns.
  private void setupTableColumns()
  {
    if (originColumn != null)
    {
      originColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getOrigin()));
    }

    if (destinationColumn != null)
    {
      destinationColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDestination()));
    }

    if (statusColumn != null)
    {
      statusColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getStatus()));
    }

    if (dateIntervalColumn != null)
    {
      dateIntervalColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getDateIntervalString()));
    }

    if (timeIntervalColumn != null)
    {
      timeIntervalColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTimeIntervalString()));
    }

    if (customerColumn != null)
    {
      customerColumn.setCellValueFactory(data ->
      {
        Customer customer = data.getValue().getCustomer();
        return new ReadOnlyStringWrapper(customer == null ? "" : customer.getName() + " (" + customer.getPhone() + ")");
      });
    }

    if (busColumn != null)
    {
      busColumn.setCellValueFactory(data ->
      {
        Bus bus = data.getValue().getAssignedBus();
        return new ReadOnlyStringWrapper(bus == null ? "" : bus.getRegNo());
      });
    }

    if (chauffeurColumn != null)
    {
      chauffeurColumn.setCellValueFactory(data ->
      {
        Chauffeur chauffeur = data.getValue().getAssignedChauffeur();
        return new ReadOnlyStringWrapper(chauffeur == null ? "" : chauffeur.getName());
      });
    }

    if (tripTableView != null)
    {
      tripTableView.setItems(tripRows);
      tripTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
  }

  //Loads selected trip details whenever the table selection changes.
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

  //Reloads either all trips or only ended trip records.
  private void refreshTrips()
  {
    if (tripTableView == null || modelManager == null)
    {
      return;
    }

    Trip selectedTrip = tripTableView.getSelectionModel().getSelectedItem();
    TripList tripList = showingPastTrips ? modelManager.getAllTripsByStatus("Ended") : modelManager.getAllTrips();
    tripRows.clear();

    for (int i = 0; i < tripList.size(); i++)
    {
      tripRows.add(tripList.getTrip(i));
    }

    if (selectedTrip != null && tripRows.contains(selectedTrip))
    {
      tripTableView.getSelectionModel().select(selectedTrip);
    }
    else
    {
      tripTableView.getSelectionModel().clearSelection();
    }

    tripTableView.refresh();
  }

  private void refreshCustomers()
  {
    if (customerBox == null || modelManager == null)
    {
      return;
    }

    Customer selectedCustomer = customerBox.getValue();
    CustomerList customerList = modelManager.getAllCustomers();
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    for (int i = 0; i < customerList.size(); i++)
    {
      customers.add(customerList.getCustomer(i));
    }

    customerBox.setItems(customers);

    if (selectedCustomer != null && !customers.contains(selectedCustomer))
    {
      customers.add(selectedCustomer);
    }

    customerBox.setValue(selectedCustomer);
  }

  private void refreshBuses()
  {
    if (busBox == null || modelManager == null)
    {
      return;
    }

    Bus selectedBus = busBox.getValue();
    BusList busList = modelManager.getAllAvailableBuses(true);
    ObservableList<Bus> buses = FXCollections.observableArrayList();

    for (int i = 0; i < busList.size(); i++)
    {
      buses.add(busList.getBus(i));
    }

    if (selectedBus != null && !buses.contains(selectedBus))
    {
      buses.add(selectedBus);
    }

    busBox.setItems(buses);
    busBox.setValue(selectedBus);
  }

  //Loads suitable available chauffeurs while preserving current assignments.
  private void refreshChauffeurs()
  {
    if (chauffeurBox == null || modelManager == null)
    {
      return;
    }

    Chauffeur selectedChauffeur = chauffeurBox.getValue();
    ChauffeurList chauffeurList = modelManager.getAllSuitableChauffeurs(true, true);

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

    if (selectedChauffeur != null && !chauffeurs.contains(selectedChauffeur))
    {
      chauffeurs.add(selectedChauffeur);
    }

    chauffeurBox.setItems(chauffeurs);
    chauffeurBox.setValue(selectedChauffeur);
  }

  //Validates proposed changes, prevents conflicts, then saves the trip.
  @FXML
  private void editTrip()
  {
    Trip selectedTrip = getSelectedTrip();

    if (selectedTrip == null)
    {
      showError("Please select a trip to edit.");
      return;
    }

    if (!selectedTrip.canBeEdited())
    {
      showError("The selected trip cannot be edited with its current status.");
      return;
    }

    try
    {
      Trip proposedTrip = createTripFromFieldsForEdit();

      if (hasOverlappingAssignment(proposedTrip, selectedTrip))
      {
        showError("The selected bus or chauffeur is already assigned to another overlapping trip.");
        return;
      }

      selectedTrip.setOrigin(proposedTrip.getOrigin());
      selectedTrip.setDestination(proposedTrip.getDestination());
      selectedTrip.setStatus(proposedTrip.getStatus());
      selectedTrip.setDateInterval(proposedTrip.getDateInterval());
      selectedTrip.setTimeInterval(proposedTrip.getTimeInterval());
      selectedTrip.assignBus(proposedTrip.getAssignedBus());
      selectedTrip.assignChauffeur(proposedTrip.getAssignedChauffeur());
      selectedTrip.setCustomer(proposedTrip.getCustomer());

      modelManager.updateTrip(selectedTrip);
      modelManager.saveCompany();
      showingPastTrips = false;
      refreshAll();
      tripTableView.getSelectionModel().select(selectedTrip);
      fillFieldsFromTrip(selectedTrip);
      tripTableView.refresh();
      showConfirmation("Trip updated successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  //Checks status, confirms deletion, removes, and persists selected trip.
  @FXML
  private void removeTrip()
  {
    Trip selectedTrip = getSelectedTrip();

    if (selectedTrip == null)
    {
      showError("Please select a trip to remove.");
      return;
    }

    if (!selectedTrip.canBeRemoved())
    {
      showError("The selected trip cannot be removed with its current status.");
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
      showingPastTrips = false;
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
  private void viewPastTrips()
  {
    showingPastTrips = true;
    refreshTrips();
    showConfirmation("Past trips loaded.");
  }

  @FXML
  private void showAllTrips()
  {
    showingPastTrips = false;
    refreshAll();
    showConfirmation("All trips loaded.");
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

    deselectComboBox(customerBox);
    deselectComboBox(busBox);
    deselectComboBox(chauffeurBox);

    if (tripTableView != null)
    {
      tripTableView.getSelectionModel().clearSelection();
    }
  }

  private void deselectComboBox(ComboBox<?> comboBox)
  {
    if (comboBox != null)
    {
      comboBox.getSelectionModel().clearSelection();
      comboBox.setValue(null);
    }
  }

  //Builds proposed trip data from the current editable form values.
  private Trip createTripFromFieldsForEdit()
  {
    Bus bus = busBox == null ? null : busBox.getValue();
    Chauffeur chauffeur = chauffeurBox == null ? null : chauffeurBox.getValue();
    Customer customer = customerBox == null ? null : customerBox.getValue();

    if (bus == null)
    {
      throw new IllegalArgumentException("Please select an assigned bus.");
    }

    if (chauffeur == null)
    {
      throw new IllegalArgumentException("Please select an assigned chauffeur.");
    }

    String origin = getText(originField, "Origin");
    String destination = getText(destinationField, "Destination");
    String status = statusBox == null || statusBox.getValue() == null ? "Not Started" : statusBox.getValue();
    DateInterval dateInterval = createDateInterval();
    TimeInterval timeInterval = createTimeInterval(dateInterval);

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

  //Validates entered times and same-day start-before-end requirements.
  private TimeInterval createTimeInterval(DateInterval dateInterval)
  {
    Time startTime = parseTime(getText(startTimeField, "Start time"));
    Time endTime = parseTime(getText(endTimeField, "End time"));

    if (dateInterval.getStartDate().equals(dateInterval.getEndDate()) && !startTime.isBefore(endTime))
    {
      throw new IllegalArgumentException("Start time must be before end time when the trip starts and ends on the same date.");
    }

    return new TimeInterval(startTime, endTime);
  }

  private model.Date convertLocalDate(LocalDate localDate)
  {
    return new model.Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
  }

  private LocalDate convertModelDate(model.Date date)
  {
    return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
  }

  private Time parseTime(String text)
  {
    String trimmed = text.trim();

    if (!trimmed.matches("^([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?$"))
    {
      throw new IllegalArgumentException("Time must use 24-hour format HH:mm or HH:mm:ss.");
    }

    String[] parts = trimmed.split(":");
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

  //Copies selected trip information and assignments into editing controls.
  private void fillFieldsFromTrip(Trip trip)
  {
    if (trip == null)
    {
      return;
    }

    refreshCustomers();
    refreshBuses();
    refreshChauffeurs();

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
      ensureCustomerIsSelectable(trip.getCustomer());
      customerBox.setValue(trip.getCustomer());
    }

    if (busBox != null)
    {
      ensureBusIsSelectable(trip.getAssignedBus());
      busBox.setValue(trip.getAssignedBus());
    }

    if (chauffeurBox != null)
    {
      ensureChauffeurIsSelectable(trip.getAssignedChauffeur());
      chauffeurBox.setValue(trip.getAssignedChauffeur());
    }
  }

  private void ensureCustomerIsSelectable(Customer customer)
  {
    if (customer != null && customerBox != null && !customerBox.getItems().contains(customer))
    {
      customerBox.getItems().add(customer);
    }
  }

  private void ensureBusIsSelectable(Bus bus)
  {
    if (bus != null && busBox != null && !busBox.getItems().contains(bus))
    {
      busBox.getItems().add(bus);
    }
  }

  private void ensureChauffeurIsSelectable(Chauffeur chauffeur)
  {
    if (chauffeur != null && chauffeurBox != null && !chauffeurBox.getItems().contains(chauffeur))
    {
      chauffeurBox.getItems().add(chauffeur);
    }
  }

  //Detects conflicting bus or chauffeur assignments while ignoring edited trip.
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