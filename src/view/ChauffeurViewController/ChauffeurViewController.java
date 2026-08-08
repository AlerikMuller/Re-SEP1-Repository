package view.ChauffeurViewController;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Chauffeur;
import model.ChauffeurList;
import model.DateInterval;
import model.DriverLicense;
import model.Time;
import model.TimeInterval;
import model.TripPlanningModelManager;
import model.WorkSchedule;
import view.ViewHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controls the view used for managing existing chauffeurs in the system.
 * The controller displays chauffeur information, handles chauffeur editing
 * and removal, manages work schedule creation, validates entered values,
 * refreshes the chauffeur table, and saves changes persistently.
 *
 * @author Alerik Muller
 * @version 1.0
 */

//Controls chauffeur viewing, editing, removal, schedules, and table interaction.
public class ChauffeurViewController
{
  @FXML private TextField nameField;
  @FXML private TextField phoneField;
  @FXML private TextField experienceField;
  @FXML private ComboBox<String> preferenceBox;
  @FXML private CheckBox availableCheckBox;
  @FXML private CheckBox suitableCheckBox;
  @FXML private TextField licenseNoField;
  @FXML private ComboBox<String> licenseTypeBox;

  @FXML private ComboBox<String> dayBox;
  @FXML private ComboBox<String> scheduleStatusBox;
  @FXML private DatePicker scheduleStartDatePicker;
  @FXML private DatePicker scheduleEndDatePicker;
  @FXML private TextField scheduleStartTimeField;
  @FXML private TextField scheduleEndTimeField;

  @FXML private TableView<Chauffeur> chauffeurTableView;
  @FXML private TableColumn<Chauffeur, String> nameColumn;
  @FXML private TableColumn<Chauffeur, String> phoneColumn;
  @FXML private TableColumn<Chauffeur, String> experienceColumn;
  @FXML private TableColumn<Chauffeur, String> preferenceColumn;
  @FXML private TableColumn<Chauffeur, String> availableColumn;
  @FXML private TableColumn<Chauffeur, String> suitableColumn;
  @FXML private TableColumn<Chauffeur, String> licenseColumn;

  private TripPlanningModelManager modelManager;
  private ViewHandler viewHandler;
  private Scene scene;
  private final ObservableList<Chauffeur> chauffeurRows = FXCollections.observableArrayList();

  //Connects controller dependencies and prepares table controls for use.
  public void init(ViewHandler viewHandler, Scene scene, TripPlanningModelManager modelManager)
  {
    this.modelManager = modelManager;
    this.scene = scene;
    this.viewHandler = viewHandler;
    setupComboBoxes();
    setupTableColumns();
    setupChauffeurSelection();
    refreshChauffeurs();
  }

  public Scene getScene()
  {
    return scene;
  }

  public void reset()
  {
    refreshChauffeurs();
  }

  private void setupComboBoxes()
  {
    if (preferenceBox != null)
    {
      preferenceBox.setItems(FXCollections.observableArrayList("Shorter trips", "Longer trips", "Customer wishes"));
    }

    if (licenseTypeBox != null)
    {
      licenseTypeBox.setItems(FXCollections.observableArrayList("MINI_BUS", "LARGE_BUS"));
    }

    if (dayBox != null)
    {
      dayBox.setItems(FXCollections.observableArrayList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
    }

    if (scheduleStatusBox != null)
    {
      scheduleStatusBox.setItems(FXCollections.observableArrayList("Active", "OFF"));
    }

    if (availableCheckBox != null)
    {
      availableCheckBox.setSelected(true);
    }

    if (suitableCheckBox != null)
    {
      suitableCheckBox.setSelected(true);
    }
  }

  //Maps chauffeur properties to their corresponding table display columns.
  private void setupTableColumns()
  {
    if (nameColumn != null)
    {
      nameColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
    }

    if (phoneColumn != null)
    {
      phoneColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPhone()));
    }

    if (experienceColumn != null)
    {
      experienceColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getExperienceYears())));
    }

    if (preferenceColumn != null)
    {
      preferenceColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPreferenceNotes()));
    }

    if (availableColumn != null)
    {
      availableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().isAvailable())));
    }

    if (suitableColumn != null)
    {
      suitableColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().isSuitable())));
    }

    if (licenseColumn != null)
    {
      licenseColumn.setCellValueFactory(data ->
      {
        DriverLicense license = data.getValue().getDrivingLicense();
        return new ReadOnlyStringWrapper(license == null ? "" : license.getLicenseNo() + " " + license.getLicenseType());
      });
    }

    if (chauffeurTableView != null)
    {
      chauffeurTableView.setItems(chauffeurRows);
      chauffeurTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
  }

  //Loads selected chauffeur details whenever the table selection changes.
  private void setupChauffeurSelection()
  {
    if (chauffeurTableView == null)
    {
      return;
    }

    chauffeurTableView.getSelectionModel().selectedItemProperty().addListener(
            (observableValue, oldChauffeur, selectedChauffeur) ->
            {
              if (selectedChauffeur != null)
              {
                fillFieldsFromChauffeur(selectedChauffeur);
              }
            });
  }

  //Reloads all chauffeur records into the management table display.
  private void refreshChauffeurs()
  {
    if (chauffeurTableView == null || modelManager == null)
    {
      return;
    }

    Chauffeur selectedChauffeur = chauffeurTableView.getSelectionModel().getSelectedItem();
    ChauffeurList chauffeurList = modelManager.getAllChauffeurs();
    chauffeurRows.clear();

    for (int i = 0; i < chauffeurList.size(); i++)
    {
      chauffeurRows.add(chauffeurList.getChauffeur(i));
    }

    if (selectedChauffeur != null && chauffeurRows.contains(selectedChauffeur))
    {
      chauffeurTableView.getSelectionModel().select(selectedChauffeur);
    }
    else
    {
      chauffeurTableView.getSelectionModel().clearSelection();
    }

    chauffeurTableView.refresh();
  }

  //Validates fields and saves changes to the selected chauffeur.
  @FXML
  private void editChauffeur()
  {
    Chauffeur selectedChauffeur = getSelectedChauffeur();

    if (selectedChauffeur == null)
    {
      showError("Please select a chauffeur to edit.");
      return;
    }

    try
    {
      DriverLicense driverLicense = createDriverLicenseFromFields();
      selectedChauffeur.setName(getText(nameField, "Name"));
      selectedChauffeur.setPhone(getText(phoneField, "Phone"));
      selectedChauffeur.setExperienceYears(parseExperienceYears());
      selectedChauffeur.setPreferenceNotes(getComboValue(preferenceBox, "Preference"));
      selectedChauffeur.setAvailable(availableCheckBox == null || availableCheckBox.isSelected());
      selectedChauffeur.setDriverLicense(driverLicense);
      selectedChauffeur.setSuitable(suitableCheckBox == null || suitableCheckBox.isSelected(), selectedChauffeur.getPreferenceNotes(), driverLicense);
      modelManager.updateChauffeur(selectedChauffeur);
      modelManager.saveCompany();
      refreshChauffeurs();
      chauffeurTableView.getSelectionModel().select(selectedChauffeur);
      fillFieldsFromChauffeur(selectedChauffeur);
      chauffeurTableView.refresh();
      showConfirmation("Chauffeur updated successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  //Confirms and attempts removal of the currently selected chauffeur.
  @FXML
  private void removeChauffeur()
  {
    Chauffeur selectedChauffeur = getSelectedChauffeur();

    if (selectedChauffeur == null)
    {
      showError("Please select a chauffeur to remove.");
      return;
    }

    if (!confirm("Remove Chauffeur", "Are you sure you want to remove the selected chauffeur?"))
    {
      return;
    }

    try
    {
      modelManager.removeChauffeur(selectedChauffeur);
      modelManager.saveCompany();
      refreshChauffeurs();
      clearFields();
      showConfirmation("Chauffeur removed successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  //Creates and appends a validated schedule to selected chauffeur.
  @FXML
  private void addWorkSchedule()
  {
    Chauffeur selectedChauffeur = getSelectedChauffeur();

    if (selectedChauffeur == null)
    {
      showError("Please select a chauffeur before adding a work schedule.");
      return;
    }

    try
    {
      WorkSchedule schedule = createRequiredScheduleFromFields();
      selectedChauffeur.addSchedule(schedule);
      modelManager.updateChauffeur(selectedChauffeur);
      modelManager.saveCompany();
      refreshChauffeurs();
      chauffeurTableView.getSelectionModel().select(selectedChauffeur);
      fillFieldsFromChauffeur(selectedChauffeur);
      chauffeurTableView.refresh();
      showConfirmation("Work schedule added successfully.");
    }
    catch (Exception e)
    {
      showError(e.getMessage());
    }
  }

  @FXML
  private void clearFields()
  {
    if (nameField != null)
    {
      nameField.clear();
    }

    if (phoneField != null)
    {
      phoneField.clear();
    }

    if (experienceField != null)
    {
      experienceField.clear();
    }

    if (preferenceBox != null)
    {
      preferenceBox.setValue(null);
    }

    if (availableCheckBox != null)
    {
      availableCheckBox.setSelected(true);
    }

    if (suitableCheckBox != null)
    {
      suitableCheckBox.setSelected(true);
    }

    if (licenseNoField != null)
    {
      licenseNoField.clear();
    }

    if (licenseTypeBox != null)
    {
      licenseTypeBox.setValue(null);
    }

    clearScheduleFields();

    if (chauffeurTableView != null)
    {
      chauffeurTableView.getSelectionModel().clearSelection();
    }
  }

  private void clearScheduleFields()
  {
    if (dayBox != null)
    {
      dayBox.setValue(null);
    }

    if (scheduleStatusBox != null)
    {
      scheduleStatusBox.setValue(null);
    }

    if (scheduleStartDatePicker != null)
    {
      scheduleStartDatePicker.setValue(null);
    }

    if (scheduleEndDatePicker != null)
    {
      scheduleEndDatePicker.setValue(null);
    }

    if (scheduleStartTimeField != null)
    {
      scheduleStartTimeField.clear();
    }

    if (scheduleEndTimeField != null)
    {
      scheduleEndTimeField.clear();
    }
  }

  @FXML
  private void back()
  {
    viewHandler.openView("MainView");
  }

  private DriverLicense createDriverLicenseFromFields()
  {
    String licenseNo = getText(licenseNoField, "License number");
    String licenseType = getComboValue(licenseTypeBox, "License type");
    return new DriverLicense(licenseNo, licenseType);
  }

  //Builds a schedule from required date, day, and status values.
  private WorkSchedule createRequiredScheduleFromFields()
  {
    String day = getComboValue(dayBox, "Day");
    String status = getComboValue(scheduleStatusBox, "Schedule status");
    DateInterval dateInterval = createScheduleDateInterval();
    boolean hasStartTime = hasText(scheduleStartTimeField);
    boolean hasEndTime = hasText(scheduleEndTimeField);

    if (hasStartTime || hasEndTime)
    {
      TimeInterval timeInterval = createScheduleTimeInterval(dateInterval);
      return new WorkSchedule(day, status, dateInterval, timeInterval);
    }

    return new WorkSchedule(day, status, dateInterval);
  }

  private DateInterval createScheduleDateInterval()
  {
    if (scheduleStartDatePicker == null || scheduleStartDatePicker.getValue() == null)
    {
      throw new IllegalArgumentException("Please select a schedule start date.");
    }

    if (scheduleEndDatePicker == null || scheduleEndDatePicker.getValue() == null)
    {
      throw new IllegalArgumentException("Please select a schedule end date.");
    }

    model.Date startDate = convertLocalDate(scheduleStartDatePicker.getValue());
    model.Date endDate = convertLocalDate(scheduleEndDatePicker.getValue());
    return new DateInterval(startDate, endDate);
  }

  //Validates optional times and prevents invalid same-day schedule order.
  private TimeInterval createScheduleTimeInterval(DateInterval dateInterval)
  {
    Time startTime = parseTime(getText(scheduleStartTimeField, "Schedule start time"));
    Time endTime = parseTime(getText(scheduleEndTimeField, "Schedule end time"));

    if (dateInterval.getStartDate().equals(dateInterval.getEndDate()) && !startTime.isBefore(endTime))
    {
      throw new IllegalArgumentException("Start time must be before end time when the schedule starts and ends on the same date.");
    }

    return new TimeInterval(startTime, endTime);
  }

  private int parseExperienceYears()
  {
    String experienceText = getText(experienceField, "Experience years");

    try
    {
      int experienceYears = Integer.parseInt(experienceText);

      if (experienceYears < 0)
      {
        throw new IllegalArgumentException("Experience years cannot be negative.");
      }

      return experienceYears;
    }
    catch (NumberFormatException e)
    {
      throw new IllegalArgumentException("Experience years must be a whole number.");
    }
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

  private model.Date convertLocalDate(LocalDate localDate)
  {
    return new model.Date(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
  }

  private LocalDate convertModelDate(model.Date date)
  {
    return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
  }

  private String getText(TextField field, String fieldName)
  {
    if (field == null || field.getText() == null || field.getText().trim().isEmpty())
    {
      throw new IllegalArgumentException(fieldName + " cannot be empty.");
    }

    return field.getText().trim();
  }

  private String getComboValue(ComboBox<String> comboBox, String fieldName)
  {
    if (comboBox == null || comboBox.getValue() == null || comboBox.getValue().trim().isEmpty())
    {
      throw new IllegalArgumentException(fieldName + " must be selected.");
    }

    return comboBox.getValue().trim();
  }

  private boolean hasText(TextField field)
  {
    return field != null && field.getText() != null && !field.getText().trim().isEmpty();
  }

  private Chauffeur getSelectedChauffeur()
  {
    if (chauffeurTableView == null)
    {
      return null;
    }

    return chauffeurTableView.getSelectionModel().getSelectedItem();
  }

  //Copies selected chauffeur information into editable interface controls.
  private void fillFieldsFromChauffeur(Chauffeur chauffeur)
  {
    if (chauffeur == null)
    {
      return;
    }

    if (nameField != null)
    {
      nameField.setText(chauffeur.getName());
    }

    if (phoneField != null)
    {
      phoneField.setText(chauffeur.getPhone());
    }

    if (experienceField != null)
    {
      experienceField.setText(String.valueOf(chauffeur.getExperienceYears()));
    }

    if (preferenceBox != null)
    {
      preferenceBox.setValue(chauffeur.getPreferenceNotes());
    }

    if (availableCheckBox != null)
    {
      availableCheckBox.setSelected(chauffeur.isAvailable());
    }

    if (suitableCheckBox != null)
    {
      suitableCheckBox.setSelected(chauffeur.isSuitable());
    }

    DriverLicense license = chauffeur.getDrivingLicense();

    if (license != null)
    {
      if (licenseNoField != null)
      {
        licenseNoField.setText(license.getLicenseNo());
      }

      if (licenseTypeBox != null)
      {
        licenseTypeBox.setValue(license.getLicenseType());
      }
    }

    fillScheduleFieldsFromFirstSchedule(chauffeur);
  }

  //Displays the first stored schedule for the selected chauffeur.
  private void fillScheduleFieldsFromFirstSchedule(Chauffeur chauffeur)
  {
    ArrayList<WorkSchedule> schedules = chauffeur.getAllWorkSchedules();

    if (schedules.isEmpty())
    {
      clearScheduleFields();
      return;
    }

    WorkSchedule schedule = schedules.get(0);

    if (dayBox != null)
    {
      dayBox.setValue(schedule.getDay());
    }

    if (scheduleStatusBox != null)
    {
      scheduleStatusBox.setValue(schedule.getStatus());
    }

    if (scheduleStartDatePicker != null && schedule.getDateInterval() != null)
    {
      scheduleStartDatePicker.setValue(convertModelDate(schedule.getDateInterval().getStartDate()));
    }

    if (scheduleEndDatePicker != null && schedule.getDateInterval() != null)
    {
      scheduleEndDatePicker.setValue(convertModelDate(schedule.getDateInterval().getEndDate()));
    }

    if (schedule.getTimeInterval() != null)
    {
      if (scheduleStartTimeField != null)
      {
        scheduleStartTimeField.setText(schedule.getTimeInterval().getStartTime().toString());
      }

      if (scheduleEndTimeField != null)
      {
        scheduleEndTimeField.setText(schedule.getTimeInterval().getEndTime().toString());
      }
    }
    else
    {
      if (scheduleStartTimeField != null)
      {
        scheduleStartTimeField.clear();
      }

      if (scheduleEndTimeField != null)
      {
        scheduleEndTimeField.clear();
      }
    }
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