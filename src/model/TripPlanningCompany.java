package model;

/**
 * A model class that encapsulates all company data in separate list collections.
 * This class serves as a central data holder for buses, chauffeurs, customers, and trips,
 * allowing the TripPlanningModelManager to access and manage all company information through a single object.
 *
 * @author Kelsang Sherpa
 * @version 1.0
 */
public class TripPlanningCompany
{
  private BusList allBuses;
  private ChauffeurList allChauffeurs;
  private CustomerList allCustomers;
  private TripList allTrips;

  /**
   * Constructs a TripPlanningCompany with all company data collections.
   *
   * @param allBuses the BusList containing all buses in the company
   * @param allChauffeurs the ChauffeurList containing all chauffeurs in the company
   * @param allCustomers the CustomerList containing all customers in the company
   * @param allTrips the TripList containing all trips in the company
   */
  public TripPlanningCompany(BusList allBuses, ChauffeurList allChauffeurs, CustomerList allCustomers, TripList allTrips)
  {
    this.allBuses = allBuses;
    this.allChauffeurs = allChauffeurs;
    this.allCustomers = allCustomers;
    this.allTrips = allTrips;
  }

  /**
   * Retrieves the list of all buses in the company.
   *
   * @return the BusList containing all buses
   */
  public BusList getAllBuses()
  {
    return allBuses;
  }

  /**
   * Retrieves the list of all chauffeurs in the company.
   *
   * @return the ChauffeurList containing all chauffeurs
   */
  public ChauffeurList getAllChauffeurs()
  {
    return allChauffeurs;
  }

  /**
   * Retrieves the list of all customers in the company.
   *
   * @return the CustomerList containing all customers
   */
  public CustomerList getAllCustomers()
  {
    return allCustomers;
  }

  /**
   * Retrieves the list of all trips in the company.
   *
   * @return the TripList containing all trips
   */
  public TripList getAllTrips()
  {
    return allTrips;
  }

  /**
   * Sets the list of all buses for the company.
   *
   * @param allBuses the BusList to set as the company's buses
   */
  public void setAllBuses(BusList allBuses)
  {
    this.allBuses = allBuses;
  }

  /**
   * Sets the list of all customers for the company.
   *
   * @param allCustomers the CustomerList to set as the company's customers
   */
  public void setAllCustomers(CustomerList allCustomers)
  {
    this.allCustomers = allCustomers;
  }

  /**
   * Sets the list of all trips for the company.
   *
   * @param allTrips the TripList to set as the company's trips
   */
  public void setAllTrips(TripList allTrips)
  {
    this.allTrips = allTrips;
  }

  /**
   * Sets the list of all chauffeurs for the company.
   *
   * @param allChauffeurs the ChauffeurList to set as the company's chauffeurs
   */
  public void setAllChauffeurs(ChauffeurList allChauffeurs)
  {
    this.allChauffeurs = allChauffeurs;
  }
}
