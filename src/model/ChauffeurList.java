package model;

import java.util.ArrayList;

public class ChauffeurList {

    private ArrayList<Chauffeur> chauffeurs;

    public ChauffeurList() {
        this.chauffeurs = new ArrayList<>();
    }

    public void addChauffeur(Chauffeur chauffeur) {
        if (chauffeur == null) {
            throw new IllegalArgumentException("Chauffeur cannot be null.");
        }
        chauffeurs.add(chauffeur);
    }

    public void removeChauffeur(Chauffeur chauffeur) {
        chauffeurs.remove(chauffeur);
    }

    public Chauffeur getChauffeur(int index) {
        return chauffeurs.get(index);
    }

    public Chauffeur getChauffeurByPhone(String phone) {
        for (Chauffeur chauffeur : chauffeurs) {
            if (chauffeur.getPhone().equalsIgnoreCase(phone)) {
                return chauffeur;
            }
        }
        return null;
    }

    public boolean containsPhone(String phone) {
        return getChauffeurByPhone(phone) != null;
    }

    // Filters by work-schedule availability for the given interval.
    public ChauffeurList getAvailableChauffeurs(DateInterval dateInterval, TimeInterval timeInterval) {
        ChauffeurList available = new ChauffeurList();
        for (Chauffeur chauffeur : chauffeurs) {
            if (chauffeur.isAvailableFor(dateInterval, timeInterval)) {
                available.addChauffeur(chauffeur);
            }
        }
        return available;
    }

    // Filters by availability AND stated preference (e.g. "shorter trips").
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

    public int size() {
        return chauffeurs.size();
    }

    public boolean isEmpty() {
        return chauffeurs.isEmpty();
    }

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
