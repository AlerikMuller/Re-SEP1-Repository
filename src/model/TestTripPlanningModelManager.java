package model;

public class TestTripPlanningModelManager
{
  public static void main(String[] args)
  {
    TripPlanningModelManager manager = new TripPlanningModelManager(
        "company.json");

    // Load existing data (if any)
    manager.loadCompany();

    // ---------------- Create Objects ----------------

    Bus bus1 = new Bus("AB12345", "Mini bus", 20, 5, true);
    Bus bus2 = new Bus("CD67890", "Large bus", 15, 10, true);

    DriverLicense license1 = new DriverLicense("A101", "MINI_BUS");
    DriverLicense license2 = new DriverLicense("A102", "LARGE_BUS");

    Chauffeur chauffeur1 = new Chauffeur("John", "12345678", 10, "Longer trips",
        true, true, license1);

    Chauffeur chauffeur2 = new Chauffeur("Peter", "87654321", 5,
        "Shorter trips", true, true, license2);

    Customer customer1 = new Customer("Alice", "11111111");

    Customer customer2 = new Customer("Bob", "22222222");

    Date date1 = new Date(28, 7, 2026);
    Date date2 = new Date(6, 8, 2026);
    DateInterval dateInterval1 = new DateInterval(date1, date1);
    DateInterval dateInterval2 = new DateInterval(date2, date2);

    Time time1 = new Time(6, 0, 0);
    Time time2 = new Time(7, 20, 0);
    Time time3 = new Time(10, 0, 0);

    TimeInterval timeInterval1 = new TimeInterval(time1, time2);
    TimeInterval timeInterval2 = new TimeInterval(time2, time3);

    Trip trip1 = new Trip("Odense", "Copenhagen", "Not Started",dateInterval1, timeInterval1);

    Trip trip2 = new Trip("Aarhus", "Aalborg", "Not Started", dateInterval2, timeInterval2);

    // ---------------- Add Objects ----------------

    manager.addBus(bus1);
    manager.addBus(bus2);

    manager.addChauffeur(chauffeur1);
    manager.addChauffeur(chauffeur2);

    manager.addCustomer(customer1);
    manager.addCustomer(customer2);

    manager.registerTrip(trip1);
    manager.registerTrip(trip2);

    // Save all data
    manager.saveCompany();

    // ---------------- Display Data ----------------

    System.out.println("===== ALL BUSES =====");
    for (int i = 0; i < manager.getAllBuses().size(); i++)
    {
      System.out.println(manager.getAllBuses().getBus(i));
    }

    System.out.println("\n===== AVAILABLE BUSES =====");
    for (int i = 0; i < manager.getAllAvailableBuses(true).size(); i++)
    {
      System.out.println(manager.getAllAvailableBuses(true).getBus(i));
    }

    System.out.println("\n===== ALL CHAUFFEURS =====");
    for (int i = 0; i < manager.getAllChauffeurs().size(); i++)
    {
      System.out.println(manager.getAllChauffeurs().getChauffeur(i));
    }

    System.out.println("\n===== SUITABLE CHAUFFEURS =====");
    for (int i = 0;
         i < manager.getAllSuitableChauffeurs(true, true).size(); i++)
    {
      System.out.println(
          manager.getAllSuitableChauffeurs(true, true).getChauffeur(i));
    }

    System.out.println("\n===== ALL CUSTOMERS =====");
    for (int i = 0; i < manager.getAllCustomers().size(); i++)
    {
      System.out.println(manager.getAllCustomers().getCustomer(i));
    }

    System.out.println("\n===== ALL TRIPS =====");
    for (int i = 0; i < manager.getAllTrips().size(); i++)
    {
      System.out.println(manager.getAllTrips().getTrip(i));
    }
    if(trip1.getAssignedBus() == null || trip1.getAssignedChauffeur() == null)
    {
      trip1.assignBus(bus1);
      trip1.assignChauffeur(chauffeur1);
    }
    manager.assignBusToTrip(trip1);
    manager.assignChauffeurToTrip(trip1);

    if(trip2.getAssignedBus() == null || trip2.getAssignedChauffeur() == null)
    {
      trip2.assignBus(bus2);
      trip2.assignChauffeur(chauffeur2);
    }
    manager.assignBusToTrip(trip2);
    manager.assignChauffeurToTrip(trip2);
    manager.saveCompany();
    System.out.println("\n===== ALL TRIPS =====");
    for (int i = 0; i < manager.getAllTrips().size(); i++)
    {
      System.out.println(manager.getAllTrips().getTrip(i));
    }
    // ---------------- Test Remove ----------------

    manager.removeCustomer(customer2);
    manager.removeChauffeur(chauffeur2);
    manager.removeBus(bus2);

    // Save changes
    manager.saveCompany();

    System.out.println("\n===== AFTER REMOVAL =====");
    System.out.println("Customers: "
        + manager.getAllCustomers().size());

    System.out.println("Chauffeurs: "
        + manager.getAllChauffeurs().size());

    System.out.println("Buses: "
        + manager.getAllBuses().size());

    System.out.println("\nTest completed successfully.");
  }
      }
