package model;

import java.util.ArrayList;

/**
 * Represents a chauffeur employed by Horsens Tours and stores the
 * information needed for planning and trip assignments. The class
 * manages personal details, experience, preferences, availability,
 * suitability, driver license information, and work schedules.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class Chauffeur
{
    private String name;
    private String phone;
    private int experienceYears;
    private String preferenceNotes;
    private boolean isAvailable;
    private boolean isSuitable;
    private ArrayList<WorkSchedule> workSchedules;
    private DriverLicense driverLicense;

    /**
     * Creates a chauffeur with the given information and an empty schedule list.
     *
     * @param name the chauffeur's name
     * @param phone the chauffeur's phone number
     * @param experienceYears the chauffeur's years of experience
     * @param preferenceNotes the chauffeur's trip preference
     * @param isAvailable whether the chauffeur is currently available
     * @param isSuitable whether the chauffeur is currently suitable
     * @param driverLicense the chauffeur's driver license
     */
    public Chauffeur(String name, String phone, int experienceYears, String preferenceNotes, boolean isAvailable, boolean isSuitable, DriverLicense driverLicense)
    {
        this.workSchedules = new ArrayList<>();
        setChauffeur(name, phone, experienceYears, preferenceNotes, isAvailable, isSuitable, driverLicense);
    }

    /**
     * Sets the chauffeur's name.
     *
     * @param name the name to store
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Chauffeur name cannot be empty.");
        }

        this.name = name.trim();
    }

    /**
     * Returns the chauffeur's name.
     *
     * @return the chauffeur's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the chauffeur's phone number and requires digits only.
     *
     * @param phone the phone number to store
     * @throws IllegalArgumentException if the phone is empty or contains non-digits
     */
    public void setPhone(String phone)
    {
        if (phone == null || phone.trim().isEmpty())
        {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }

        if (!phone.trim().matches("\\d+"))
        {
            throw new IllegalArgumentException("Phone number must contain digits only.");
        }

        this.phone = phone.trim();
    }

    /**
     * Returns the chauffeur's phone number.
     *
     * @return the chauffeur's phone number
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * Sets the chauffeur's number of years of driving experience.
     *
     * @param experienceYears the years of experience to store
     * @throws IllegalArgumentException if the experience value is negative
     */
    public void setExperienceYears(int experienceYears)
    {
        if (experienceYears < 0)
        {
            throw new IllegalArgumentException("Experience years cannot be negative.");
        }

        this.experienceYears = experienceYears;
    }

    /**
     * Returns the chauffeur's number of years of experience.
     *
     * @return the chauffeur's years of experience
     */
    public int getExperienceYears()
    {
        return experienceYears;
    }

    /**
     * Sets the chauffeur's trip preference using one of the supported values.
     *
     * @param preferenceNotes the preference to store
     * @throws IllegalArgumentException if the preference is empty or unsupported
     */
    public void setPreferenceNotes(String preferenceNotes)
    {
        if (preferenceNotes == null || preferenceNotes.trim().isEmpty())
        {
            throw new IllegalArgumentException("Preference notes cannot be empty.");
        }

        String value = preferenceNotes.trim();

        if (!value.equals("Shorter trips") && !value.equals("Longer trips") && !value.equals("Customer wishes"))
        {
            throw new IllegalArgumentException("Preference must be Shorter trips, Longer trips, or Customer wishes.");
        }

        this.preferenceNotes = value;
    }

    /**
     * Returns the chauffeur's stored trip preference.
     *
     * @return the chauffeur's preference notes
     */
    public String getPreferenceNotes()
    {
        return preferenceNotes;
    }

    /**
     * Sets whether the chauffeur is generally available.
     *
     * @param available the new availability value
     */
    public void setAvailable(boolean available)
    {
        this.isAvailable = available;
    }

    /**
     * Checks whether the chauffeur is generally marked as available.
     *
     * @return {@code true} if available, otherwise {@code false}
     */
    public boolean isAvailable()
    {
        return isAvailable;
    }

    /**
     * Sets the chauffeur's suitability after validating required planning information.
     *
     * @param suitable the new suitability value
     * @param preferenceNotes the chauffeur's preference information
     * @param driverLicense the chauffeur's driver license
     * @throws IllegalArgumentException if preference or license information is missing
     */
    public void setSuitable(boolean suitable, String preferenceNotes, DriverLicense driverLicense)
    {
        if (preferenceNotes == null || preferenceNotes.trim().isEmpty())
        {
            throw new IllegalArgumentException("Preference notes cannot be empty.");
        }

        if (driverLicense == null)
        {
            throw new IllegalArgumentException("Driver license cannot be empty.");
        }

        this.isSuitable = suitable;
    }

    /**
     * Checks whether the chauffeur is currently marked as suitable.
     *
     * @return {@code true} if suitable, otherwise {@code false}
     */
    public boolean isSuitable()
    {
        return isSuitable;
    }

    /**
     * Updates all main chauffeur information using the individual setters.
     *
     * @param name the chauffeur's name
     * @param phone the chauffeur's phone number
     * @param experienceYears the chauffeur's years of experience
     * @param preferenceNotes the chauffeur's trip preference
     * @param isAvailable whether the chauffeur is available
     * @param isSuitable whether the chauffeur is suitable
     * @param driverLicense the chauffeur's driver license
     */
    public void setChauffeur(String name, String phone, int experienceYears, String preferenceNotes, boolean isAvailable, boolean isSuitable, DriverLicense driverLicense)
    {
        setName(name);
        setPhone(phone);
        setExperienceYears(experienceYears);
        setPreferenceNotes(preferenceNotes);
        setAvailable(isAvailable);
        setDriverLicense(driverLicense);
        setSuitable(isSuitable, preferenceNotes, driverLicense);
    }

    /**
     * Sets the chauffeur's driver license.
     *
     * @param driverLicense the driver license to store
     * @throws IllegalArgumentException if the driver license is {@code null}
     */
    public void setDriverLicense(DriverLicense driverLicense)
    {
        if (driverLicense == null)
        {
            throw new IllegalArgumentException("Driver license cannot be empty.");
        }

        this.driverLicense = driverLicense;
    }

    /**
     * Adds a work schedule to the chauffeur's collection of schedules.
     *
     * @param schedule the work schedule to add
     * @throws IllegalArgumentException if the schedule is {@code null}
     */
    public void addSchedule(WorkSchedule schedule)
    {
        if (schedule == null)
        {
            throw new IllegalArgumentException("Work schedule cannot be empty.");
        }

        workSchedules.add(schedule);
    }

    /**
     * Returns a copy of all work schedules assigned to the chauffeur.
     *
     * @return an {@link ArrayList} containing the chauffeur's schedules
     */
    public ArrayList<WorkSchedule> getAllWorkSchedules()
    {
        return new ArrayList<>(workSchedules);
    }

    /**
     * Returns the chauffeur's current driver license.
     *
     * @return the chauffeur's driver license
     */
    public DriverLicense getDrivingLicense()
    {
        return driverLicense;
    }

    /**
     * Adds or replaces the chauffeur's current driver license.
     *
     * @param driverLicense the driver license to assign
     */
    public void addDriverLicense(DriverLicense driverLicense)
    {
        setDriverLicense(driverLicense);
    }

    /**
     * Checks whether the chauffeur is available for the requested intervals.
     * General availability is checked first, followed by stored work schedules.
     *
     * @param dateInterval the requested date interval
     * @param timeInterval the requested time interval
     * @return {@code true} if the chauffeur is available, otherwise {@code false}
     */
    public boolean isAvailableFor(DateInterval dateInterval, TimeInterval timeInterval)
    {
        if (!isAvailable)
        {
            return false;
        }

        if (workSchedules.isEmpty())
        {
            return true;
        }

        for (WorkSchedule schedule : workSchedules)
        {
            if (schedule.isAvailableFor(dateInterval, timeInterval))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a short string containing the chauffeur's name and phone number.
     *
     * @return a string representation of the chauffeur
     */
    @Override
    public String toString()
    {
        return name + " (" + phone + ")";
    }
}