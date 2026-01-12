package com.brousse.service;

import com.brousse.model.*;
import com.brousse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;
import java.util.List;

import com.brousse.dto.VoyageDTO;
import com.brousse.dto.VoyageFilterDTO;
import java.util.ArrayList;

@Service
@Transactional
public class VoyageService {

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private VoyageStatutRepository voyageStatutRepository;

    @Autowired
    private StatutVoyageRepository statutVoyageRepository;

    @Autowired
    private StatutVehiculeRepository statutVehiculeRepository;

    @Autowired
    private VehiculeStatutRepository vehiculeStatutRepository;

    private static final Duration MARGE_TURNAROUND = Duration.ofMinutes(15);

    public List<VoyageDTO> getVoyageChiffreAffaire() {

        List<Object[]> results = voyageRepository.findVoyageChiffreAffaire();

        List<VoyageDTO> dtos = new ArrayList<>();

        for (Object[] record : results) {

            VoyageDTO dto = new VoyageDTO();

            dto.setId(((Integer) record[0]));                 // id du voyage
            // dateDepart peut être renvoyé comme Timestamp, java.util.Date, LocalDateTime ou LocalDate
            Object dateObj = record[1];
            LocalDateTime dateDepart = null;
            if (dateObj instanceof Timestamp) {
                dateDepart = ((Timestamp) dateObj).toLocalDateTime();
            } else if (dateObj instanceof java.time.LocalDateTime) {
                dateDepart = (LocalDateTime) dateObj;
            } else if (dateObj instanceof java.sql.Date) {
                dateDepart = ((java.sql.Date) dateObj).toLocalDate().atStartOfDay();
            } else if (dateObj instanceof java.util.Date) {
                dateDepart = ((java.util.Date) dateObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else if (dateObj instanceof java.time.LocalDate) {
                dateDepart = ((java.time.LocalDate) dateObj).atStartOfDay();
            }
            dto.setDateDepart(dateDepart); // date départ

            // Mapping Chauffeur si présent (vérifie la longueur du record)
            Chauffeur chauffeur = chauffeurRepository.findById((Integer) record[2]).orElse(null);
            dto.setChauffeur(chauffeur);

            // Mapping Vehicule minimal pour DTO
            Vehicule vehicule = vehiculeRepository.findById((Integer) record[3]).orElse(null);
            dto.setVehicule(vehicule);

            Trajet trajet = trajetRepository.findById((Integer) record[4]).orElse(null);
            dto.setTrajet(trajet);

            // Champ calculé : chiffre d’affaires
            dto.setChiffreAffaire(record[5] != null
                ? ((Number) record[5]).doubleValue()
                : 0.0);
            dtos.add(dto);
        }

        return dtos;
    }

    public Voyage creerVoyage(LocalDateTime dateDepart,
                             Integer idChauffeur,
                             Integer idVehicule,
                             Integer idTrajet,
                             Integer idVoyageStatut) {

        if (dateDepart == null) {
            throw new IllegalArgumentException("La date de départ est obligatoire.");
        }
        if (dateDepart.isBefore(LocalDateTime.now().minusMinutes(2))) {
            throw new IllegalArgumentException("La date de départ ne peut pas être dans le passé.");
        }

        Chauffeur chauffeur = chauffeurRepository.findById(idChauffeur)
                .orElseThrow(() -> new IllegalArgumentException("Chauffeur introuvable."));

        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable."));

        String vehiculeStatus = getCurrentVehiculeStatusLibelle(vehicule);
        if (vehiculeStatus == null || !vehiculeStatus.equalsIgnoreCase("Disponible")) {
            throw new IllegalArgumentException("Ce véhicule n'est pas disponible.");
        }

        Trajet trajet = trajetRepository.findById(idTrajet)
                .orElseThrow(() -> new IllegalArgumentException("Trajet introuvable."));

        verifierDisponibiliteVehiculeEtChauffeur(chauffeur, vehicule, trajet, dateDepart, null);

        LocalDateTime from = dateDepart.minusHours(2);
        LocalDateTime to = dateDepart.plusHours(2);
        boolean conflitVehicule = voyageRepository.existsByVehicule_IdAndDateDepartBetween(vehicule.getId(), from, to);
        if (conflitVehicule) {
            throw new IllegalArgumentException("Ce véhicule a déjà une voyage programmée sur ce créneau.");
        }

        Voyage voyage = new Voyage();
        voyage.setDateDepart(dateDepart);
        voyage.setChauffeur(chauffeur);
        voyage.setVehicule(vehicule);
        voyage.setTrajet(trajet);

        voyage = voyageRepository.save(voyage);

        appliquerStatut(voyage, idVoyageStatut, "Création de la voyage");

        return voyageRepository.save(voyage);
    }

    @Transactional(readOnly = true)
    public List<Voyage> listerVoyages() {
        return voyageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Voyage trouverParId(Integer id) {
        return voyageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voyage introuvable."));
    }

    public List<Voyage> listerVoyagesAvecFiltre(VoyageFilterDTO filtres) {
        List<Voyage> voyages = voyageRepository.findAll();

        // Filter in memory
        if (filtres.getTrajetId() != null) {
            voyages = voyages.stream()
                    .filter(v -> v.getTrajet().getId().equals(filtres.getTrajetId()))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (filtres.getChauffeurId() != null) {
            voyages = voyages.stream()
                    .filter(v -> v.getChauffeur().getId().equals(filtres.getChauffeurId()))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (filtres.getVehiculeId() != null) {
            voyages = voyages.stream()
                    .filter(v -> v.getVehicule().getId().equals(filtres.getVehiculeId()))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (filtres.getStatutId() != null) {
            voyages = voyages.stream()
                    .filter(v -> {
                        Integer currentStatusId = getCurrentStatusId(v);
                        return currentStatusId != null && currentStatusId.equals(filtres.getStatutId());
                    })
                    .collect(java.util.stream.Collectors.toList());
        }

        if (filtres.getDateDebut() != null) {
            voyages = voyages.stream()
                    .filter(v -> v.getDateDepart().isAfter(filtres.getDateDebut()) || v.getDateDepart().equals(filtres.getDateDebut()))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (filtres.getDateFin() != null) {
            voyages = voyages.stream()
                    .filter(v -> v.getDateDepart().isBefore(filtres.getDateFin()) || v.getDateDepart().equals(filtres.getDateFin()))
                    .collect(java.util.stream.Collectors.toList());
        }

        return voyages;
    }

    public List<Voyage> listerVoyagesPrevus() {
        List<Voyage> voyages = voyageRepository.findAll();
        return voyages.stream()
                .filter(v -> {
                    String statut = getCurrentStatusLibelle(v);
                    return !"En cours".equalsIgnoreCase(statut) && !"Terminée".equalsIgnoreCase(statut) && !"Annulée".equalsIgnoreCase(statut);
                })
                .collect(java.util.stream.Collectors.toList());
    }

    public Voyage modifierVoyage(Integer idVoyage,
                                 LocalDateTime nouvelleDateDepart,
                                 Integer idChauffeur,
                                 Integer idVehicule,
                                 Integer idTrajet,
                                 Integer idVoyageStatut) {

        Voyage voyage = voyageRepository.findById(idVoyage)
                .orElseThrow(() -> new IllegalArgumentException("Voyage introuvable."));

        String currentLibelle = getCurrentStatusLibelle(voyage);
        if ("Terminée".equalsIgnoreCase(currentLibelle)
                || "Annulée".equalsIgnoreCase(currentLibelle)) {
            throw new IllegalStateException("Ce voyage est terminé ou annulé et ne peut plus être modifiée.");
        }

        boolean dejaPartie = voyage.getDateDepart() != null
                && voyage.getDateDepart().isBefore(LocalDateTime.now().minusMinutes(2));

        // Si déjà partie : on autorise seulement changement de statut
        if (dejaPartie) {
            appliquerStatut(voyage, idVoyageStatut, "Changement de statut (voyage déjà partie)");
            return voyageRepository.save(voyage);
        }

        if (nouvelleDateDepart == null) {
            throw new IllegalArgumentException("La date de départ est obligatoire.");
        }
        if (nouvelleDateDepart.isBefore(LocalDateTime.now().minusMinutes(2))) {
            throw new IllegalArgumentException("La date de départ ne peut pas être dans le passé.");
        }

        Chauffeur chauffeur = chauffeurRepository.findById(idChauffeur)
                .orElseThrow(() -> new IllegalArgumentException("Chauffeur introuvable."));

        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable."));

        String vehiculeStatus = getCurrentVehiculeStatusLibelle(vehicule);
        if (vehiculeStatus == null || !vehiculeStatus.equalsIgnoreCase("Disponible")) {
            throw new IllegalArgumentException("Ce véhicule n'est pas disponible.");
        }

        Trajet trajet = trajetRepository.findById(idTrajet)
                .orElseThrow(() -> new IllegalArgumentException("Trajet introuvable."));

        verifierDisponibiliteVehiculeEtChauffeur(chauffeur, vehicule, trajet, nouvelleDateDepart, voyage.getId());

        LocalDateTime from = nouvelleDateDepart.minusHours(2);
        LocalDateTime to = nouvelleDateDepart.plusHours(2);

        boolean conflitVehicule = voyageRepository.existsByVehicule_IdAndDateDepartBetweenAndIdNot(
                vehicule.getId(), from, to, voyage.getId()
        );
        if (conflitVehicule) {
            throw new IllegalArgumentException("Ce véhicule a déjà une voyage programmée sur ce créneau.");
        }

        voyage.setDateDepart(nouvelleDateDepart);
        voyage.setChauffeur(chauffeur);
        voyage.setVehicule(vehicule);
        voyage.setTrajet(trajet);

        appliquerStatut(voyage, idVoyageStatut, "Modification de la voyage");

        return voyageRepository.save(voyage);
    }

    private void appliquerStatut(Voyage voyage, Integer idVoyageStatut, String commentaire) {
        String currentLibelle = getCurrentStatusLibelle(voyage);
        if (estTerminal(currentLibelle)) {
            throw new IllegalStateException("La voyage est terminée, statut non modifiable.");
        }
        if (idVoyageStatut == null) {
            throw new IllegalArgumentException("Le statut est obligatoire.");
        }

        VoyageStatut ref = voyageStatutRepository.findById(idVoyageStatut)
                .orElseThrow(() -> new IllegalArgumentException("Statut invalide."));

        if (currentLibelle != null && currentLibelle.equalsIgnoreCase(ref.getLibelle())) {
            return;
        }

        StatutVoyage hist = new StatutVoyage();
        hist.setVoyage(voyage);
        hist.setVoyageStatut(ref);
        hist.setDateStatut(Instant.now());

        statutVoyageRepository.save(hist);
    }

    // -------------------- TEMPS / FIN ESTIMEE --------------------

    private LocalDateTime finEstimee(LocalDateTime depart, Trajet trajet) {
        BigDecimal d = trajet.getDureeEstimeeMinutes();
        long minutes = (d == null ? 0L : d.longValue());
        return depart.plusMinutes(minutes);
    }

    private LocalDateTime finEstimee(Voyage c) {
        return finEstimee(c.getDateDepart(), c.getTrajet());
    }

    // -------------------- STATUTS --------------------

    private boolean estTerminal(String statut) {
        return statut != null && (
                "Terminée".equalsIgnoreCase(statut) ||
                "Annulée".equalsIgnoreCase(statut)
        );
    }

    private boolean estAnnulee(Voyage c) {
        String statut = getCurrentStatusLibelle(c);
        return statut != null && "Annulée".equalsIgnoreCase(statut);
    }

    // -------------------- REQUETES AVANT/APRES --------------------

    private Voyage derniereVoyageVehiculeAvant(Integer vehiculeId, LocalDateTime date, Integer excludeId) {
        return voyageRepository
                .findByVehicule_IdAndDateDepartLessThanOrderByDateDepartDesc(
                        vehiculeId, date, org.springframework.data.domain.Pageable.ofSize(1))
                .stream()
                .filter(c -> excludeId == null || !c.getId().equals(excludeId))
                .findFirst()
                .orElse(null);
    }

    private Voyage prochaineVoyageVehiculeApres(Integer vehiculeId, LocalDateTime date, Integer excludeId) {
        return voyageRepository
                .findByVehicule_IdAndDateDepartGreaterThanOrderByDateDepartAsc(
                        vehiculeId, date, org.springframework.data.domain.Pageable.ofSize(1))
                .stream()
                .filter(c -> excludeId == null || !c.getId().equals(excludeId))
                .findFirst()
                .orElse(null);
    }

    private Voyage derniereVoyageChauffeurAvant(Integer chauffeurId, LocalDateTime date, Integer excludeId) {
        return voyageRepository
                .findByChauffeur_IdAndDateDepartLessThanOrderByDateDepartDesc(
                        chauffeurId, date, org.springframework.data.domain.Pageable.ofSize(1))
                .stream()
                .filter(c -> excludeId == null || !c.getId().equals(excludeId))
                .findFirst()
                .orElse(null);
    }

    private Voyage prochaineVoyageChauffeurApres(Integer chauffeurId, LocalDateTime date, Integer excludeId) {
        return voyageRepository
                .findByChauffeur_IdAndDateDepartGreaterThanOrderByDateDepartAsc(
                        chauffeurId, date, org.springframework.data.domain.Pageable.ofSize(1))
                .stream()
                .filter(c -> excludeId == null || !c.getId().equals(excludeId))
                .findFirst()
                .orElse(null);
    }

    // -------------------- REGLE CHAINAGE --------------------

    private void verifierChaine(
            String qui,
            Voyage prev,
            LocalDateTime newStart,
            LocalDateTime newEnd,
            Integer newDepartGareId,
            Integer newArriveeGareId,
            Voyage next
    ) {
        // ---- CONTRAINTE AVANT ----
        if (prev != null && !estAnnulee(prev)) {
            LocalDateTime prevEnd = finEstimee(prev).plus(MARGE_TURNAROUND);
            Integer prevArriveeId = prev.getTrajet().getGareArrivee().getId();

            if (newStart.isBefore(prevEnd)) {
                throw new IllegalArgumentException(qui + " indisponible : chevauchement avec la voyage précédente.");
            }
            if (!newDepartGareId.equals(prevArriveeId)) {
                throw new IllegalArgumentException(
                        qui + " est à une autre gare à cette heure (doit partir de : "
                                + prev.getTrajet().getGareArrivee().getNom() + ")."
                );
            }
        }

        // ---- CONTRAINTE APRES ----
        if (next != null && !estAnnulee(next)) {
            LocalDateTime nextStart = next.getDateDepart();
            Integer nextDepartId = next.getTrajet().getGareDepart().getId();

            // ta nouvelle voyage doit finir + marge AVANT la prochaine
            if (newEnd.plus(MARGE_TURNAROUND).isAfter(nextStart)) {
                throw new IllegalArgumentException(qui + " indisponible : ta modification casse la voyage suivante.");
            }
            // et finir à la gare d'où part la prochaine
            if (!newArriveeGareId.equals(nextDepartId)) {
                throw new IllegalArgumentException(
                        qui + " ne sera pas à la bonne gare pour la voyage suivante (doit arriver à : "
                                + next.getTrajet().getGareDepart().getNom() + ")."
                );
            }
        }
    }

    private void verifierDisponibiliteVehiculeEtChauffeur(
            Chauffeur chauffeur,
            Vehicule vehicule,
            Trajet trajet,
            LocalDateTime dateDepart,
            Integer excludeVoyageId
    ) {
        LocalDateTime dateArrivee = finEstimee(dateDepart, trajet);
        Integer departGareId = trajet.getGareDepart().getId();
        Integer arriveeGareId = trajet.getGareArrivee().getId();

        // VEHICULE
        Voyage prevV = derniereVoyageVehiculeAvant(vehicule.getId(), dateDepart, excludeVoyageId);
        Voyage nextV = prochaineVoyageVehiculeApres(vehicule.getId(), dateDepart, excludeVoyageId);
        verifierChaine("Véhicule", prevV, dateDepart, dateArrivee, departGareId, arriveeGareId, nextV);

        // CHAUFFEUR
        Voyage prevC = derniereVoyageChauffeurAvant(chauffeur.getId(), dateDepart, excludeVoyageId);
        Voyage nextC = prochaineVoyageChauffeurApres(chauffeur.getId(), dateDepart, excludeVoyageId);
        verifierChaine("Chauffeur", prevC, dateDepart, dateArrivee, departGareId, arriveeGareId, nextC);
    }

    @Transactional(readOnly = true)
    public Integer getCurrentStatusId(Voyage voyage) {
        List<StatutVoyage> statuts = statutVoyageRepository.findByVoyage_IdOrderByDateStatutDesc(voyage.getId());
        if (statuts.isEmpty()) {
            return null;
        }
        return statuts.get(0).getVoyageStatut().getId();
    }

    @Transactional(readOnly = true)
    public String getCurrentStatusLibelle(Voyage voyage) {
        Integer statusId = getCurrentStatusId(voyage);
        if (statusId == null) {
            return null;
        }
        return voyageStatutRepository.findById(statusId).map(VoyageStatut::getLibelle).orElse(null);
    }

    @Transactional(readOnly = true)
    public String getCurrentVehiculeStatusLibelle(Vehicule vehicule) {
        List<StatutVehicule> statuts = statutVehiculeRepository.findByVehiculeIdOrderByDateStatutDesc(vehicule.getId());
        if (statuts.isEmpty()) {
            return null;
        }
        Integer statusId = statuts.get(0).getVehiculeStatut().getId();
        return vehiculeStatutRepository.findById(statusId).map(VehiculeStatut::getLibelle).orElse(null);
    }
}
