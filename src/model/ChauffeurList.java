package model;

import java.util.ArrayList;

/**
 * A container class that stores and manages a collection of {@link Chauffeur} objects.
 * <p>
 * The {@code ChauffeurList} wraps an {@link ArrayList} of chauffeurs and provides
 * operations for adding, removing, searching and filtering the chauffeurs held by the
 * trip-planning company. It supports filtering by work-schedule availability and by
 * chauffeur preference, which is used when a suitable chauffeur has to be selected for
 * a trip.
 *
 * @author Ghiyath
 * @version 1.0
 */
public class ChauffeurList {

    private ArrayList<Chauffeur> chauffeurs;

    /**
     * Creates an empty {@code ChauffeurList}.
     */
    public ChauffeurList() {
        this.chauffeurs = new ArrayList<>();
    }

    /**
     * Adds a chauffeur to the list.
     *
     * @param chauffeur the chauffeur to add
     * @throws IllegalArgumentException if the chauffeur is {@code null}
     */
    public void addChauffeur(Chauffeur chauffeur) {
        if (chauffeur == null) {
            throw new IllegalArgumentException("Chauffeur cannot be null.");
        }
        chauffeurs.add(chauffeur);
    }

    /**
     * Removes the given chauffeur from the list.
     *
     * @param chauffeur the chauffeur to remove
     */
    public void removeChauffeur(Chauffeur chauffeur) {
        chauffeurs.remove(chauffeur);
    }

    /**
     * Returns the chauffeur at the given position in the list.
     *
     * @param index the position of the chauffeur in the list
     * @return the chauffeur at the specified index
     */
    public Chauffeur getChauffeur(int index) {
        return chauffeurs.get(index);
    }

    /**
     * Searches for a chauffeur by phone number, ignoring case.
     *
     * @param phone the phone number to search for
     * @return the matching chauffeur, or {@code null} if no chauffeur has that phone number
     */
    public Chauffeur getChauffeurByPhone(String phone) {
        for (Chauffeur chauffeur : chauffeurs) {
            if (chauffeur.getPhone().equalsIgnoreCase(phone)) {
                return chauffeur;
            }
        }
        return null;
    }

    /**
     * Checks whether the list contains a chauffeur with the given phone number.
     *
     * @param phone the phone number to look for
     * @return {@code true} if a chauffeur with that phone number exists, otherwise {@code false}
     */
    public boolean containsPhone(String phone) {
        return getChauffeurByPhone(phone) != null;
    }

    /**
     * Returns a new list containing only the chauffeurs who are available for the given
     * date and time interval, according to their work schedules.
     *
     * @param dateInterval the date interval the chauffeur must be available for
     * @param timeInterval the time interval the chauffeur must be available for
     * @return a {@code ChauffeurList} of available chauffeurs
     */
    public ChauffeurList getAvailableChauffeurs(DateInterval dateInterval, TimeInterval timeInterval) {
        ChauffeurList available = new ChauffeurList();
        for (Chauffeur chauffeur : chauffeurs) {
            if (chauffeur.isAvailableFor(dateInterval, timeInterval)) {
                available.addChauffeur(chauffeur);
            }
        }
        return available;
    }

    /**
     * Returns a new list containing only the chauffeurs who are available for the given
     * date and time interval and whose preference matches the requested preference.
     * If no preference is given, only availability is considered.
     *
     * @param dateInterval the date interval the chauffeur must be available for
     * @param timeInterval the time interval the chauffeur must be available for
     * @param preference   the preference to match, or {@code null}/empty to ignore preference
     * @return a {@code ChauffeurList} of suitable chauffeurs
     */
    public ChauffeurList getSuitableChauffeurs(DateInterval dateInterval, TimeInterval timeInterval, String preference) {
        ChauffeurList suitable = new ChauffeurList();
        for (Chauffeur chauffeur : chauffeurs) {
            boolean isAvailable = chauffeur.isAvailableFor(dateInterval, timeInterval);
            boolean matchesPreference = preference == null || preference.isEmpty()
                    || chauffeur.getPreferenceNotes().equalsIgnoreCase(preference);
            if (isAvailable && matchesPreference) {
                suitable.addChauffeur(chauffeur);
            }
        }
        return suitable;
    }

    /**
     * Returns the number of chauffeurs in the list.
     *
     * @return the number of chauffeurs
     */
    public int size() {
        return chauffeurs.size();
    }

    /**
     * Checks whether the list contains no chauffeurs.
     *
     * @return {@code true} if the list is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return chauffeurs.isEmpty();
    }

    /**
     * Returns a string representation of the list, containing the string
     * representation of every chauffeur it holds.
     *
     * @return a string describing all chauffeurs in the list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ChauffeurList{\n");
        for (Chauffeur chauffeur : chauffeurs) {
            sb.append("  ").append(chauffeur.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}