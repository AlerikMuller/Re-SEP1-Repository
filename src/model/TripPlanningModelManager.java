package model;

import parser.ParserException;
import utility.MyFileHandler;

import java.util.ArrayList;
/**
 * Manages the TripPlanningCompany model and coordinates file persistence operations.
 * This class acts as a central access point between the UI/controller layer and the underlying
 * {@code TripPlanningCompany} model, as well as the {@code MyFileHandler} for file I/O operations.
 * It provides methods to load and save company data from/to JSON files, and offers a comprehensive
 * API for managing buses, chauffeurs, customers, and trips within the company.
 *
 * @author Kelsang Sherpa
 * @version 1.0
 */
public class TripPlanningModelManager
{
  private final String fileName;
  private TripPlanningCompany company;
  private final MyFileHandler fileHandler;

  /**
   * Constructs a TripPlanningModelManager with the specified file name.
   * Initializes the MyFileHandler and creates an empty TripPlanningCompany instance.
   * The company data is not loaded from the file in this constructor; {@link #loadCompany()} must be called separately.
   *
   * @param fileName the name of the JSON file to access for reading and writing company data
   */
  public TripPlanningModelManager(String fileName)
  {
    this.fileName = fileName;
    fileHandler = new MyFileHandler();
    company = createEmptyCompany();
  }

  /**
   * Loads the company's data from the configured JSON file.
   * If the file does not exist or contains no company data, a new empty
   * {@code TripPlanningCompany} is created. If data is successfully loaded,
   * the company instance is restored and all required internal lists are
   * initialized if they are missing.
   * If an error occurs while parsing the file, an empty company is created
   * and an error message is printed to the console.
   *
   * @throws ParserException if an error occurs during JSON parsing (caught internally)
   */
  public void loadCompany()
  {
    try
    {
      TripPlanningCompany loadedCompany = fileHandler.loadFromJson(fileName, TripPlanningCompany.class);

      if (loadedCompany == null)
      {
        company = createEmptyCompany();
      }
      else
      {
        company = loadedCompany;
        ensureCompanyListsExist();
      }
    }
    catch (ParserException e)
    {
      company = createEmptyCompany();
      System.out.println("Error loading company file");
    }
  }

  /**
   * Saves the current company's data to the configured JSON file.
   * If an error occurs during the save operation, an error message is printed to the console.
   *
   * @throws ParserException if an error occurs during JSON serialization (caught internally)
   */
  public void saveCompany()
  {
    try
    {
      fileHandler.saveToJson(fileName, company);
    }
    catch (ParserException e)
    {
      System.out.println("Error saving company file");
    }
  }

  /**
   * Retrieves all buses registered in the company.
   *
   * @return a {@code BusList} containing all buses in the company
   */
  public BusList getAllBuses()
  {
    return company.getAllBuses();
  }

  /**
   * Retrieves buses filtered by their availability status.
   * Returns a new {@code BusList} containing only buses matching the specified availability status.
   *
   * @param availability the desired availability status ({@code true} for available, {@code false} for unavailable)
   * @return a {@code BusList} containing buses matching the specified availability status
   */
  public BusList getAllAvailableBuses(boolean availability)
  {
    BusList availableBuses = new BusList();
    BusList allBuses = company.getAllBuses();

    for (int i = 0; i < allBuses.size(); i++)
    {
      Bus bus = allBuses.getBus(i);

      if (bus.getAvailability() == availability)
      {
        availableBuses.getAllBuses().add(bus);
      }
    }

    return availableBuses;
  }

  /**
   * Retrieves all customers registered in the company.
   *
   * @return a {@code CustomerList} containing all customers in the company
   */
  public CustomerList getAllCustomers()
  {
    return company.getAllCustomers();
  }

  /**
   * Retrieves all trips registered in the company.
   *
   * @return a {@code TripList} containing all trips in the company
   */
  public TripList getAllTrips()
  {
    return company.getAllTrips();
  }

  /**
   * Retrieves trips filtered by their status.
   * Returns a new {@code TripList} containing only trips with the specified status.
   * The status comparison is case-insensitive.
   *
   * @param status the trip status to filter by (e.g., "Not Started", "In Progress", "Completed")
   * @return a {@code TripList} containing trips matching the specified status
   */
  public TripList getAllTripsByStatus(String status)
  {
    TripList allTripsByStatus = new TripList();
    TripList allTrips = company.getAllTrips();

    for (int i = 0; i < allTrips.size(); i++)
    {
      Trip trip = allTrips.getTrip(i);

      if (trip.getStatus().equalsIgnoreCase(status))
      {
        allTripsByStatus.addTrip(trip);
      }
    }

    return allTripsByStatus;
  }

  /**
   * Retrieves all chauffeurs registered in the company.
   *
   * @return a {@code ChauffeurList} containing all chauffeurs in the company
   */
  public ChauffeurList getAllChauffeurs()
  {
    return company.getAllChauffeurs();
  }

  /**
   * Retrieves the work schedule for a specific chauffeur.
   * Returns a copy of the chauffeur's work schedule to prevent external modifications.
   *
   * @param chauffeur the chauffeur whose work schedule is to be retrieved
   * @return an {@code ArrayList<WorkSchedule>} containing the chauffeur's work schedule
   */
  public ArrayList<WorkSchedule> getChauffeurWorkSchedule(Chauffeur chauffeur)
  {
    return new ArrayList<>(chauffeur.getAllWorkSchedules());
  }

  /**
   * Retrieves chauffeurs filtered by availability and suitability status.
   * Returns a new {@code ChauffeurList} containing only chauffeurs matching both criteria.
   *
   * @param availability the desired availability status ({@code true} for available, {@code false} for unavailable)
   * @param isSuitable the desired suitability status ({@code true} for suitable, {@code false} for unsuitable)
   * @return a {@code ChauffeurList} containing chauffeurs matching both the availability and suitability criteria
   */
  public ChauffeurList getAllSuitableChauffeurs(boolean availability, boolean isSuitable)
  {
    ChauffeurList allSuitableChauffeurs = new ChauffeurList();
    ChauffeurList allChauffeurs = company.getAllChauffeurs();

    for (int i = 0; i < allChauffeurs.size(); i++)
    {
      Chauffeur chauffeur = allChauffeurs.getChauffeur(i);

      if (chauffeur.isAvailable() == availability && chauffeur.isSuitable() == isSuitable)
      {
        allSuitableChauffeurs.addChauffeur(chauffeur);
      }
    }

    return allSuitableChauffeurs;
  }

  /**
   * Registers a new trip with the company.
   * Sets the trip status to "Not Started" upon registration.
   *
   * @param trip the trip to register
   */
  public void registerTrip(Trip trip)
  {
    trip.setStatus("Not Started");
    company.getAllTrips().addTrip(trip);
  }

  /**
   * Updates an existing trip with new information.
   * Finds the trip in the company's trip list and updates all its properties including
   * origin, destination, status, date interval, time interval, assigned bus, assigned chauffeur, and customer.
   *
   * @param tripToUpdate the trip object containing the updated information
   */
  public void updateTrip(Trip tripToUpdate)
  {
    TripList allTrips = company.getAllTrips();

    for (int i = 0; i < allTrips.size(); i++)
    {
      Trip trip = allTrips.getTrip(i);

      if (trip == tripToUpdate)
      {
        trip.setOrigin(tripToUpdate.getOrigin());
        trip.setDestination(tripToUpdate.getDestination());
        trip.setStatus(tripToUpdate.getStatus());
        trip.setDateInterval(tripToUpdate.getDateInterval());
        trip.setTimeInterval(tripToUpdate.getTimeInterval());
        trip.assignBus(tripToUpdate.getAssignedBus());
        trip.assignChauffeur(tripToUpdate.getAssignedChauffeur());
        trip.setCustomer(tripToUpdate.getCustomer());
        return;
      }
    }
  }

  /**
   * Adds a new bus to the company.
   *
   * @param bus the bus to add to the company
   */
  public void addBus(Bus bus)
  {
    company.getAllBuses().addBus(bus);
  }

  /**
   * Adds a new chauffeur to the company.
   *
   * @param chauffeur the chauffeur to add to the company
   */
  public void addChauffeur(Chauffeur chauffeur)
  {
    company.getAllChauffeurs().addChauffeur(chauffeur);
  }

  /**
   * Adds a new customer to the company.
   *
   * @param customer the customer to add to the company
   */
  public void addCustomer(Customer customer)
  {
    company.getAllCustomers().addCustomer(customer);
  }

  /**
   * Changes the status of a trip using the appropriate status update logic.
   * If the trip has no assigned customer, it uses the usual trip status update logic;
   * otherwise, it uses the customer trip status update logic.
   *
   * @param tripToUpdate the trip whose status is to be changed
   */
  public void changeTripStatus(Trip tripToUpdate)
  {
    if (tripToUpdate.getCustomer() == null)
    {
      company.getAllTrips().changeUsualTripStatus(tripToUpdate);
    }
    else
    {
      company.getAllTrips().changeCustomerTripStatus(tripToUpdate);
    }
  }

  /**
   * Removes a trip from the company.
   *
   * @param tripToRemove the trip to remove from the company
   */
  public void removeTrip(Trip tripToRemove)
  {
    company.getAllTrips().removeTrip(tripToRemove);
  }

  /**
   * Assigns a bus to a trip.
   * The bus assignment is performed via the trip list's assignment logic.
   *
   * @param tripToAssign the trip to which a bus will be assigned
   */
  public void assignBusToTrip(Trip tripToAssign)
  {
    company.getAllTrips().assignBusToTrip(tripToAssign);
  }

  /**
   * Assigns a chauffeur to a trip.
   * The chauffeur assignment is performed via the trip list's assignment logic.
   *
   * @param tripToAssign the trip to which a chauffeur will be assigned
   */
  public void assignChauffeurToTrip(Trip tripToAssign)
  {
    company.getAllTrips().assignChauffeurToTrip(tripToAssign);
  }

  /**
   * Removes a bus from the company and persists the changes to the file.
   *
   * @param busToRemove the bus to remove from the company
   */
  public void removeBus(Bus busToRemove)
  {
    company.getAllBuses().removeBus(busToRemove);
    saveCompany();
  }

  /**
   * Updates an existing bus with new information.
   * Finds the bus in the company's bus list and updates all its properties including
   * registration number, type, rent price per day, seat capacity, and availability status.
   *
   * @param busToUpdate the bus object containing the updated information
   */
  public void updateBus(Bus busToUpdate)
  {
    for (int i = 0; i < company.getAllBuses().size(); i++)
    {
      Bus bus = company.getAllBuses().getBus(i);

      if (bus == busToUpdate)
      {
        bus.setRegNo(busToUpdate.getRegNo());
        bus.setType(busToUpdate.getType());
        bus.setRentPricePerDay(busToUpdate.getRentPricePerDay());
        bus.setSeatCapacity(busToUpdate.getSeatCapacity());
        bus.setAvailability(busToUpdate.getAvailability());
        return;
      }
    }
  }

  /**
   * Updates an existing chauffeur with new information.
   * Finds the chauffeur in the company's chauffeur list and updates all their properties including
   * name, phone, experience years, preference notes, availability status, driving license, and suitability.
   *
   * @param chauffeurToUpdate the chauffeur object containing the updated information
   */
  public void updateChauffeur(Chauffeur chauffeurToUpdate)
  {
    for (int i = 0; i < company.getAllChauffeurs().size(); i++)
    {
      Chauffeur chauffeur = company.getAllChauffeurs().getChauffeur(i);

      if (chauffeur == chauffeurToUpdate)
      {
        chauffeur.setName(chauffeurToUpdate.getName());
        chauffeur.setPhone(chauffeurToUpdate.getPhone());
        chauffeur.setExperienceYears(chauffeurToUpdate.getExperienceYears());
        chauffeur.setPreferenceNotes(chauffeurToUpdate.getPreferenceNotes());
        chauffeur.setAvailable(chauffeurToUpdate.isAvailable());
        chauffeur.setDriverLicense(chauffeurToUpdate.getDrivingLicense());
        chauffeur.setSuitable(chauffeurToUpdate.isSuitable(), chauffeurToUpdate.getPreferenceNotes(), chauffeurToUpdate.getDrivingLicense());
        return;
      }
    }
  }

  /**
   * Removes a chauffeur from the company.
   * Prevents removal if the chauffeur is currently assigned to an active trip (unavailable status).
   * If removal is prevented, an error message is printed to the console.
   *
   * @param chauffeurToRemove the chauffeur to remove from the company
   */
  public void removeChauffeur(Chauffeur chauffeurToRemove)
  {
    for (int i = 0; i < company.getAllTrips().size(); i++)
    {
      Trip trip = company.getAllTrips().getTrip(i);

      if (trip.getAssignedChauffeur() == chauffeurToRemove && !chauffeurToRemove.isAvailable())
      {
        System.out.println("Chauffeur cannot be removed because it is assigned to an active trip.");
        return;
      }
    }

    company.getAllChauffeurs().removeChauffeur(chauffeurToRemove);
  }

  /**
   * Updates an existing customer with new information.
   * Finds the customer in the company's customer list and updates their name and phone number.
   *
   * @param customerToUpdate the customer object containing the updated information
   */
  public void updateCustomer(Customer customerToUpdate)
  {
    for (int i = 0; i < company.getAllCustomers().size(); i++)
    {
      Customer customer = company.getAllCustomers().getCustomer(i);

      if (customer == customerToUpdate)
      {
        customer.setName(customerToUpdate.getName());
        customer.setPhone(customerToUpdate.getPhone());
        return;
      }
    }
  }

  /**
   * Removes a customer from the company.
   *
   * @param customerToRemove the customer to remove from the company
   */
  public void removeCustomer(Customer customerToRemove)
  {
    company.getAllCustomers().removeCustomer(customerToRemove);
  }

  /**
   * Creates and returns a new empty TripPlanningCompany with initialized empty lists.
   * Used during initialization or when loading fails to ensure the model is never null.
   *
   * @return a new TripPlanningCompany with empty buses, chauffeurs, customers, and trips lists
   */
  private TripPlanningCompany createEmptyCompany()
  {
    return new TripPlanningCompany(new BusList(), new ChauffeurList(), new CustomerList(), new TripList());
  }

  /**
   * Ensures that all required lists in the company are initialized.
   * Checks each list (buses, chauffeurs, customers, and trips) and initializes them if they are null.
   * This method is called after loading company data to prevent NullPointerExceptions.
   */
  private void ensureCompanyListsExist()
  {
    if (company.getAllBuses() == null)
    {
      company.setAllBuses(new BusList());
    }

    if (company.getAllChauffeurs() == null)
    {
      company.setAllChauffeurs(new ChauffeurList());
    }

    if (company.getAllCustomers() == null)
    {
      company.setAllCustomers(new CustomerList());
    }

    if (company.getAllTrips() == null)
    {
      company.setAllTrips(new TripList());
    }
  }
}