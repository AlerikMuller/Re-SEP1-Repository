package model;

import java.util.ArrayList;

public class Chauffeur {
    private String name;
    private String phone;
    private int experienceYears;
    private String preferenceNotes;
    private boolean isAvailable;
    private boolean isSuitable;
    private ArrayList<WorkSchedule> workSchedules;
    private DriverLicense driverLicense;

    public Chauffeur(String name, String phone, int experienceYears, String preferenceNotes,
                     boolean isAvailable, boolean isSuitable, DriverLicense driverLicense) {
        this.workSchedules = new ArrayList<>();
        setChauffeur(name, phone, experienceYears, preferenceNotes, isAvailable, isSuitable, driverLicense);
    }

   /* public Chauffeur(String name, String phone, int experienceYears, String preferenceNotes,
                     boolean isAvailable, boolean isSuitable, DriverLicense driverLicense) {
        this(name, phone, experienceYears, preferenceNotes, isAvailable, isSuitable);
        this.driverLicense = driverLicense;
    }*/

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Chauffeur name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }
        if (!phone.trim().matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must contain digits only.");
        }
        this.phone = phone.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative.");
        }
        this.experienceYears = experienceYears;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setPreferenceNotes(String preferenceNotes) {
        if (preferenceNotes == null || preferenceNotes.trim().isEmpty()) {
            this.preferenceNotes = "";
        } else if((preferenceNotes.equals("Shorter trips") || preferenceNotes.equals("Longer trips") || preferenceNotes.equals("Customer wishes"))) {
            this.preferenceNotes = preferenceNotes.trim();
        }
    }

    public String getPreferenceNotes() {
        return preferenceNotes;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setSuitable(boolean suitable, String preferenceNotes, DriverLicense driverLicense) {
       if((preferenceNotes.equals("Shorter trips") || preferenceNotes.equals("Longer trips")) && (driverLicense.equals("MINI_BUS") || driverLicense.equals("LARGE_BUS")))
        {
            this.isSuitable = suitable;
        }
    }

    public boolean isSuitable() {
        return isSuitable;
    }

    public void setChauffeur(String name, String phone, int experienceYears, String preferenceNotes,
                             boolean isAvailable, boolean isSuitable, DriverLicense driverLicense) {
        setName(name);
        setPhone(phone);
        setExperienceYears(experienceYears);
        setPreferenceNotes(preferenceNotes);
        setAvailable(isAvailable);
        setDriverLicense(driverLicense);
        setSuitable(isSuitable, preferenceNotes, driverLicense);
    }

    public void setDriverLicense(DriverLicense driverLicense)
    {
        this.driverLicense = driverLicense;
    }
    public void addSchedule(WorkSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Work schedule cannot be empty.");
        }
        workSchedules.add(schedule);
    }

    public ArrayList<WorkSchedule> getAllWorkSchedules() {
        return new ArrayList<>(workSchedules);
    }

    public DriverLicense getDrivingLicense() {
        return driverLicense;
    }

    public void addDriverLicense(DriverLicense driverLicense) {
        if (driverLicense == null) {
            throw new IllegalArgumentException("Driver license cannot be empty.");
        }
        this.driverLicense = driverLicense;
    }

    public boolean isAvailableFor(DateInterval dateInterval, TimeInterval timeInterval) {
        if (!isAvailable) {
            return false;
        }

        if (workSchedules.isEmpty()) {
            return true;
        }

        for (WorkSchedule schedule : workSchedules) {
            if (schedule.isAvailableFor(dateInterval, timeInterval)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Chauffeur{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", experienceYears=" + experienceYears +
                ", preferenceNotes='" + preferenceNotes + '\'' +
                ", isAvailable=" + isAvailable +
                ", isSuitable=" + isSuitable +
                ", workSchedules=" + workSchedules +
                ", driverLicense=" + driverLicense +
                '}';
    }
}