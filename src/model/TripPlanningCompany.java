package model;

public class TripPlanningCompany
{
  private BusList allBuses;
  private ChauffeurList allChauffeurs;
  private CustomerList allCustomers;
  private TripList allTrips;

  public TripPlanningCompany(BusList allBuses, ChauffeurList allChauffeurs, CustomerList allCustomers, TripList allTrips)
  {
    this.allBuses = allBuses;
    this.allChauffeurs = allChauffeurs;
    this.allCustomers = allCustomers;
    this.allTrips = allTrips;
  }

  public BusList getAllBuses()
  {
    return allBuses;
  }
  public ChauffeurList getAllChauffeurs()
  {
    return allChauffeurs;
  }
  public CustomerList getAllCustomers()
  {
    return allCustomers;
  }
  public TripList getAllTrips()
  {
    return allTrips;
  }
  public void setAllBuses(BusList allBuses)
  {
    this.allBuses = allBuses;
  }
  public void setAllCustomers(CustomerList allCustomers)
  {
    this.allCustomers = allCustomers;
  }
  public void setAllTrips(TripList allTrips)
  {
    this.allTrips = allTrips;
  }
  public void setAllChauffeurs(ChauffeurList allChauffeurs)
  {
    this.allChauffeurs = allChauffeurs;
  }
}
