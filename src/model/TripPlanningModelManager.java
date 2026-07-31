package model;

import parser.ParserException;
import utility.MyFileHandler;

import java.util.ArrayList;

public class TripPlanningModelManager
{
  private final String fileName;
  private TripPlanningCompany company;
  private final MyFileHandler fileHandler;

  public TripPlanningModelManager(String fileName)
  {
    this.fileName = fileName;
    fileHandler = new MyFileHandler();
    company = createEmptyCompany();
  }

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

  public BusList getAllBuses()
  {
    return company.getAllBuses();
  }

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

  public CustomerList getAllCustomers()
  {
    return company.getAllCustomers();
  }

  public TripList getAllTrips()
  {
    return company.getAllTrips();
  }

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

  public ChauffeurList getAllChauffeurs()
  {
    return company.getAllChauffeurs();
  }

  public ArrayList<WorkSchedule> getChauffeurWorkSchedule(Chauffeur chauffeur)
  {
    return new ArrayList<>(chauffeur.getAllWorkSchedules());
  }

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

  public void registerTrip(Trip trip)
  {
    trip.setStatus("Not Started");
    company.getAllTrips().addTrip(trip);
  }

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

  public void addBus(Bus bus)
  {
    company.getAllBuses().addBus(bus);
  }

  public void addChauffeur(Chauffeur chauffeur)
  {
    company.getAllChauffeurs().addChauffeur(chauffeur);
  }

  public void addCustomer(Customer customer)
  {
    company.getAllCustomers().addCustomer(customer);
  }

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

  public void removeTrip(Trip tripToRemove)
  {
    company.getAllTrips().removeTrip(tripToRemove);
  }

  public void assignBusToTrip(Trip tripToAssign)
  {
    company.getAllTrips().assignBusToTrip(tripToAssign);
  }

  public void assignChauffeurToTrip(Trip tripToAssign)
  {
    company.getAllTrips().assignChauffeurToTrip(tripToAssign);
  }

  public void removeBus(Bus busToRemove)
  {
    company.getAllBuses().removeBus(busToRemove);
    saveCompany();
  }

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

  public void removeCustomer(Customer customerToRemove)
  {
    company.getAllCustomers().removeCustomer(customerToRemove);
  }

  private TripPlanningCompany createEmptyCompany()
  {
    return new TripPlanningCompany(new BusList(), new ChauffeurList(), new CustomerList(), new TripList());
  }

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