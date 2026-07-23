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

    company = new TripPlanningCompany(
        new BusList(),
        new ChauffeurList(),
        new CustomerList(),
        new TripList()
    );
  }

  //Load company data from JSON file
  public void loadCompany()
  {
    try
    {
      company = fileHandler.loadFromJson(
          fileName,
          TripPlanningCompany.class
      );
    }
    catch (ParserException e)
    {
      System.out.println("Error loading company file");
    }
  }

  //Save company data to JSON file
  public void saveCompany()
  {
    try
    {
      fileHandler.saveToJson(
          fileName,
          company
      );
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
    BusList allBuses = new BusList();
    BusList availableBuses = new BusList();
    allBuses =  company.getAllBuses();
    for(int i = 0; i < allBuses.size(); i++)
    {
      if(allBuses.getBus(i).getAvailability() == availability)
      {
        availableBuses.addBus(allBuses.getBus(i));
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
    TripList allTrips = new TripList();
    TripList allTripsByStatus = new TripList();
    allTrips = company.getAllTrips();
    for(int i = 0; i < allTrips.size(); i++)
    {
      if(allTrips.getTrip(i).getStatus().equalsIgnoreCase(status))
      {
        allTripsByStatus.addTrip(allTrips.getTrip(i));
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
    return new ArrayList<>(
        chauffeur.getAllWorkSchedules());
  }

  public ChauffeurList getAllSuitableChauffeurs(boolean availability, boolean isSuitable)
  {
    ChauffeurList allChauffeurs = new ChauffeurList();
    allChauffeurs = company.getAllChauffeurs();
    ChauffeurList allSuitableChauffeurs = new ChauffeurList();
    for(int i=0; i<allChauffeurs.size(); i++)
    {
      if(allChauffeurs.getChauffeur(i).isAvailable() == availability && allChauffeurs.getChauffeur(i).isSuitable() == isSuitable)
      {
        allSuitableChauffeurs.addChauffeur(allChauffeurs.getChauffeur(i));
      }
    }
    return allSuitableChauffeurs;
  }
  public void registerTrip(Trip trip)
  {
    trip.setStatus("Not Started");
    company.getAllTrips().addTrip(trip);
  }
  public void addBus(Bus bus)
  {
    company.getAllBuses().addBus(bus);
  }
  public void addChauffeur(Chauffeur chauffeur)
  {
    for(int i=0; i<company.getAllTrips().size(); i++)
    {
      Trip trip = company.getAllTrips().getTrip(i);
      if(trip.getAssignedChauffeur() == chauffeur)
      {
          System.out.println("Chauffeur already exists");
      }
    }
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
    else{
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
  public void updateBusAvailability(boolean availability, Bus busToUpdate)
  {
    company.getAllBuses().updateBusAvailability(availability, busToUpdate);
  }
  public void removeBus(Bus busToRemove)
  {
    company.getAllBuses().removeBus(busToRemove);
    saveCompany();
  }
  public void updateChauffeur(Chauffeur chauffeurToUpdate){
   for(int i=0; i<company.getAllChauffeurs().size(); i++)
   {
     Chauffeur chauffeur =  company.getAllChauffeurs().getChauffeur(i);
     if(chauffeur ==  chauffeurToUpdate)
     {
      chauffeur.setName(chauffeurToUpdate.getName());
      chauffeur.setPhone(chauffeurToUpdate.getPhone());
      chauffeur.setExperienceYears(chauffeurToUpdate.getExperienceYears());
      chauffeur.setPreferenceNotes(chauffeurToUpdate.getPreferenceNotes());
      chauffeur.setAvailable(chauffeurToUpdate.isAvailable());
      chauffeur.setSuitable(chauffeurToUpdate.isSuitable(), chauffeur.getPreferenceNotes(), chauffeurToUpdate.getDrivingLicense());
     }
   }
   saveCompany();
  }
  public void removeChauffeur(Chauffeur chauffeurToRemove)
  {
    for(int i=0; i<company.getAllTrips().size(); i++)
    {
      Trip trip = company.getAllTrips().getTrip(i);
      if(trip.getAssignedChauffeur() == chauffeurToRemove && !chauffeurToRemove.isAvailable())
      {
        System.out.println("Chauffeur cannot be removed because it is assigned to an active trip.");
        return;
      }
    }
    company.getAllChauffeurs().removeChauffeur(chauffeurToRemove);
  }

  public void updateCustomer(Customer customerToUpdate)
  {
    for(int i=0; i<company.getAllCustomers().size(); i++)
    {
      Customer customer = company.getAllCustomers().getCustomer(i);
      if(customer == customerToUpdate)
      {
        customer.setName(customerToUpdate.getName());
        customer.setPhone(customerToUpdate.getPhone());
      }
    }
    saveCompany();
  }
  public void removeCustomer(Customer customerToRemove)
  {
    company.getAllCustomers().removeCustomer(customerToRemove);
  }
}
