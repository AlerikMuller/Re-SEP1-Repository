package model;

public class DriverLicense {
    private String licenseNo;
    private String licenseType;

    public DriverLicense(String licenseNo, String licenseType) {
        setDriverLicense(licenseNo, licenseType);
    }

    public void setLicenseNo(String licenseNo) {
        if (licenseNo == null || licenseNo.trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be empty.");
        }
        this.licenseNo = licenseNo.trim();
    }

    public String getLicenseNo() {
        return licenseNo;
    }

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

    public String getLicenseType() {
        return licenseType;
    }

    public void setDriverLicense(String number, String type) {
        setLicenseNo(number);
        setLicenseType(type);
    }

    @Override
    public String toString() {
        return "DriverLicense{" +
                "licenseNo='" + licenseNo + '\'' +
                ", licenseType='" + licenseType + '\'' +
                '}';
    }
}