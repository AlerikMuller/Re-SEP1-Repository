package view.TripViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

//Controls trip registration, resource selection, validation, and navigation actions.
public class AddTripViewController
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

    @FXML private Button registerTripButton;
    @FXML private Button clearButton;
    @FXML private Button backButton;
    @FXML private Button deselectCustomerButton;
    @FXML private Button deselectBusButton;
    @FXML private Button deselectChauffeurButton;

    private TripPlanningModelManager modelManager;
    private ViewHandler viewHandler;
    private Scene scene;

    //Connects dependencies and loads selectable trip resources into controls.
    public void init(ViewHandler viewHandler, Scene scene, TripPlanningModelManager modelManager)
    {
        this.viewHandler = viewHandler;
        this.scene = scene;
        this.modelManager = modelManager;
        setupStatusBox();
        refreshAll();
    }

    public Scene getScene()
    {
        return scene;
    }

    public void reset()
    {
        refreshAll();
        clearFields();
    }

    //Routes button actions to registration, clearing, deselection, or navigation.
    @FXML
    public void handleActions(ActionEvent e)
    {
        if (e.getSource() == registerTripButton)
        {
            registerTrip();
        }
        else if (e.getSource() == clearButton)
        {
            clearFields();
        }
        else if (e.getSource() == backButton)
        {
            viewHandler.openView("MainView");
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

    //Refreshes every selectable resource list used during trip registration.
    private void refreshAll()
    {
        refreshCustomers();
        refreshBuses();
        refreshChauffeurs();
    }

    private void refreshCustomers()
    {
        if (customerBox == null || modelManager == null)
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
        if (busBox == null || modelManager == null)
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

    //Loads suitable available chauffeurs, falling back to available chauffeurs.
    private void refreshChauffeurs()
    {
        if (chauffeurBox == null || modelManager == null)
        {
            return;
        }

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

        chauffeurBox.setItems(chauffeurs);
    }

    //Validates, checks conflicts, registers, saves, and confirms the trip.
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
            clearFields();
            refreshAll();
            showConfirmation("Trip registered successfully.");
        }
        catch (Exception e)
        {
            showError(e.getMessage());
        }
    }

    //Builds a complete trip from validated registration form values.
    private Trip createTripFromFields()
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

    //Converts selected calendar dates into a validated model interval.
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

    //Validates trip times and enforces chronological same-day ordering.
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

    //Parses accepted twenty-four-hour text into a validated model time.
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

    private model.Date convertLocalDate(LocalDate localDate)
    {
        return new model.Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
    }

    private String getText(TextField field, String fieldName)
    {
        if (field == null || field.getText() == null || field.getText().trim().isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }

        return field.getText().trim();
    }

    //Prevents buses or chauffeurs from being double-booked on overlapping trips.
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
    }

    private void deselectComboBox(ComboBox<?> comboBox)
    {
        if (comboBox != null)
        {
            comboBox.getSelectionModel().clearSelection();
            comboBox.setValue(null);
        }
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