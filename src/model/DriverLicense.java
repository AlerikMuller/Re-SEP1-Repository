package model;

/**
 * Represents the driver license belonging to a registered chauffeur.
 * The class stores the license number and the supported bus license
 * type used when managing chauffeur qualifications in the system.
 *
 * @author Alerik Muller
 * @version 1.0
 */
public class DriverLicense {
    private String licenseNo;
    private String licenseType;

    /**
     * Creates a driver license with the given number and license type.
     *
     * @param licenseNo the driver license number
     * @param licenseType the type or combination of supported license types
     */
    public DriverLicense(String licenseNo, String licenseType) {
        setDriverLicense(licenseNo, licenseType);
    }

    /**
     * Sets the driver license number.
     *
     * @param licenseNo the license number to store
     * @throws IllegalArgumentException if the license number is null or empty
     */
    public void setLicenseNo(String licenseNo) {
        if (licenseNo == null || licenseNo.trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be empty.");
        }
        this.licenseNo = licenseNo.trim();
    }

    /**
     * Returns the driver license number.
     *
     * @return the stored license number
     */
    public String getLicenseNo() {
        return licenseNo;
    }

    /**
     * Sets and normalizes one or more supported bus license types.
     *
     * @param type the license type to store
     * @throws IllegalArgumentException if the value is empty or contains unsupported types
     */
    public void setLicenseType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("License type cannot be empty.");
        }

        String normalized = type.trim().toUpperCase();

        String[] parts = normalized.split("[, /]+");
        for (String part : parts) {
            if (!part.equals("MINI_BUS") && !part.equals("LARGE_BUS")) {
                throw new IllegalArgumentException("License type must be MINI_BUS or LARGE_BUS or a combination of them.");
            }
        }
        this.licenseType = normalized;
    }

    /**
     * Returns the stored driver license type.
     *
     * @return the driver license type
     */
    public String getLicenseType() {
        return licenseType;
    }

    /**
     * Updates both the license number and license type using validated setters.
     *
     * @param number the license number to store
     * @param type the license type to store
     */
    public void setDriverLicense(String number, String type) {
        setLicenseNo(number);
        setLicenseType(type);
    }

    /**
     * Returns a string containing the stored driver license information.
     *
     * @return a string representation of the driver license
     */
    @Override
    public String toString() {
        return "DriverLicense{" +
                "licenseNo='" + licenseNo + '\'' +
                ", licenseType='" + licenseType + '\'' +
                '}';
    }
}