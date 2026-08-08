package view.ChauffeurViewController;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Chauffeur;
import model.DateInterval;
import model.DriverLicense;
import model.Time;
import model.TimeInterval;
import model.TripPlanningModelManager;
import model.WorkSchedule;
import view.ViewHandler;

import java.time.LocalDate;

/**
 * Controls the view used for registering new chauffeurs in the system.
 * The controller handles user input, chauffeur and driver license
 * validation, optional work schedule creation, persistent saving, and
 * navigation between the chauffeur registration view and main menu.
 *
 * @author Alerik Muller
 * @version 1.0
 */

//Controls chauffeur registration, validation, optional schedules, and navigation actions.
public class AddChauffeurViewController
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

    private TripPlanningModelManager modelManager;
    private ViewHandler viewHandler;
    private Scene scene;

    //Connects view dependencies and prepares selectable values before interaction.
    public void init(ViewHandler viewHandler, Scene scene, TripPlanningModelManager modelManager)
    {
        this.viewHandler = viewHandler;
        this.scene = scene;
        this.modelManager = modelManager;
        setupComboBoxes();
    }

    public Scene getScene()
    {
        return scene;
    }

    public void reset()
    {
        clearFields();
    }

    //Populates supported preferences, license types, days, and schedule statuses.
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

    //Builds, saves, and confirms a new chauffeur from entered data.
    @FXML
    private void addChauffeur()
    {
        try
        {
            Chauffeur chauffeur = createChauffeurFromFields();
            WorkSchedule schedule = createOptionalScheduleFromFields();

            if (schedule != null)
            {
                chauffeur.addSchedule(schedule);
            }

            modelManager.addChauffeur(chauffeur);
            modelManager.saveCompany();
            clearFields();
            showConfirmation("Chauffeur added successfully.");
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

    //Converts validated interface values into a complete chauffeur object.
    private Chauffeur createChauffeurFromFields()
    {
        String name = getText(nameField, "Name");
        String phone = getText(phoneField, "Phone");
        int experienceYears = parseExperienceYears();
        String preferenceNotes = getComboValue(preferenceBox, "Preference");
        boolean available = availableCheckBox == null || availableCheckBox.isSelected();
        boolean suitable = suitableCheckBox == null || suitableCheckBox.isSelected();
        DriverLicense driverLicense = createDriverLicenseFromFields();

        return new Chauffeur(name, phone, experienceYears, preferenceNotes, available, suitable, driverLicense);
    }

    private DriverLicense createDriverLicenseFromFields()
    {
        String licenseNo = getText(licenseNoField, "License number");
        String licenseType = getComboValue(licenseTypeBox, "License type");

        return new DriverLicense(licenseNo, licenseType);
    }

    //Creates a work schedule only when schedule information was entered.
    private WorkSchedule createOptionalScheduleFromFields()
    {
        boolean hasAnyScheduleInput = hasComboValue(dayBox) || hasComboValue(scheduleStatusBox) || hasDateValue(scheduleStartDatePicker) || hasDateValue(scheduleEndDatePicker) || hasText(scheduleStartTimeField) || hasText(scheduleEndTimeField);

        if (!hasAnyScheduleInput)
        {
            return null;
        }

        return createRequiredScheduleFromFields();
    }

    //Builds a complete schedule after validating all mandatory schedule fields.
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

    //Validates optional schedule times and same-day chronological ordering.
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

    //Converts experience input to a required non-negative whole number.
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

    //Parses accepted twenty-four-hour time formats into model time objects.
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

    private boolean hasComboValue(ComboBox<String> comboBox)
    {
        return comboBox != null && comboBox.getValue() != null && !comboBox.getValue().trim().isEmpty();
    }

    private boolean hasDateValue(DatePicker datePicker)
    {
        return datePicker != null && datePicker.getValue() != null;
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