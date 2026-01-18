package com.brousse.service;

import com.brousse.model.*;
import com.brousse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

@Service
@Transactional
public class BilletService {
    
    private final BilletRepository billetRepository;

    private final PlaceRepository placeRepository;

    private final ClientRepository clientRepository;

    private final VoyageRepository voyageRepository;

    private final TarifRepository tarifRepository;

    private final PaiementRepository paiementRepository;

    private final StatutPaiementRepository statutPaiementRepository;

    private final PaiementStatutRepository paiementStatutRepository;

    private final MethodePaiementRepository methodePaiementRepository;

    private final PlaceService placeService;

    private final CommandeRepository commandeRepository;

    private final DetailsCommandeRepository detailsCommandeRepository;

    private final VehiculeService vehiculeService;

    private final PlaceTarifRepository placeTarifRepository;

    private final PlaceTarifService placeTarifService;

    public BilletService(BilletRepository billetRepository,
                         PlaceRepository placeRepository,
                         ClientRepository clientRepository,
                         VoyageRepository voyageRepository,
                         TarifRepository tarifRepository,
                         PaiementRepository paiementRepository,
                         StatutPaiementRepository statutPaiementRepository,
                         PaiementStatutRepository paiementStatutRepository,
                         MethodePaiementRepository methodePaiementRepository,
                         PlaceService placeService,
                         CommandeRepository commandeRepository,
                         DetailsCommandeRepository detailsCommandeRepository,
                         VehiculeService vehiculeService,
                         PlaceTarifRepository placeTarifRepository,
                         PlaceTarifService placeTarifService) {
        this.billetRepository = billetRepository;
        this.placeRepository = placeRepository;
        this.clientRepository = clientRepository;
        this.voyageRepository = voyageRepository;
        this.tarifRepository = tarifRepository;
        this.paiementRepository = paiementRepository;
        this.statutPaiementRepository = statutPaiementRepository;
        this.paiementStatutRepository = paiementStatutRepository;
        this.methodePaiementRepository = methodePaiementRepository;
        this.placeService = placeService;
        this.commandeRepository = commandeRepository;
        this.detailsCommandeRepository = detailsCommandeRepository;
        this.vehiculeService = vehiculeService;
        this.placeTarifRepository = placeTarifRepository;
        this.placeTarifService = placeTarifService;
    }

    /**
     * Récupère un billet par place et voyage
     */
    public Optional<Billet> getBilletByPlaceAndVoyage(Integer idPlace, Integer idVoyage) {
        List<Billet> billets = billetRepository.findByVoyage_Id(idVoyage);
        return billets.stream()
                .filter(b -> b.getPlace().getId().equals(idPlace))
                .findFirst();
    }

    /**
     * Acheter un billet pour une place donnée
     */
    public Billet acheterBillet(Integer idVoyage, Integer idPlace, Integer idClient) {
        // Vérifications
        Voyage voyage = voyageRepository.findById(idVoyage)
                .orElseThrow(() -> new IllegalArgumentException("Voyage introuvable"));

        Place place = placeRepository.findById(idPlace)
                .orElseThrow(() -> new IllegalArgumentException("Place introuvable"));
        
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));
        
        // Vérifier que la place est disponible
        if (!placeService.isPlaceDisponible(idPlace, idVoyage)) {
            throw new IllegalStateException("Cette place est déjà réservée pour ce voyage");
        }
        
        // Calculer le montant (tarif du trajet selon catégorie et type client adulte)
        Trajet trajet = voyage.getTrajet();
        BigDecimal montantTotal = placeTarifService.getTarif(trajet.getId(), place.getCategorie().getId(), 1, voyage.getDateDepart());
        if (montantTotal.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Aucun tarif défini pour ce trajet, catégorie et type client");
        }

        // Créer le billet
        Billet billet = new Billet();
        billet.setVoyage(voyage);
        billet.setPlace(place);
        billet.setMontantTotal(montantTotal);
        billet.setStatut("Non Payé");
        billet.setCodeBillet(genererCodeBillet());
        
        return billetRepository.save(billet);
    }

    /**
     * Récupère tous les billets d'un voyage
     */
    public List<Billet> getBilletsByVoyage(Integer idVoyage) {
        return billetRepository.findByVoyage_Id(idVoyage);
    }

    /**
     * Génère un code unique pour le billet
     */
    private String genererCodeBillet() {
        return "BIL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Récupère tous les billets
     */
    public List<Billet> findAll() {
        return billetRepository.findAll();
    }

    /**
     * Récupère un billet par ID
     */
    public Optional<Billet> findById(Integer id) {
        return billetRepository.findById(id);
    }

    /**
     * Payer un billet
     */
    public void payerBillet(Integer idBillet, String datePaiement, Integer idMethodePaiement) {
        Billet billet = billetRepository.findById(idBillet)
                .orElseThrow(() -> new IllegalArgumentException("Billet introuvable"));

        if ("Payé".equals(billet.getStatut())) {
            throw new IllegalStateException("Ce billet est déjà payé");
        }

        // Parser la date de paiement
        Instant datePaiementInstant;
        try {
            datePaiementInstant = Instant.parse(datePaiement + "T00:00:00Z"); // Assuming date format is YYYY-MM-DD
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide");
        }

        // Mettre à jour le statut du billet
        billet.setStatut("Payé");
        billetRepository.save(billet);

        // Créer le paiement
        Paiement paiement = new Paiement();
        paiement.setMontant(billet.getMontantTotal());
        paiement.setDatePaiement(datePaiementInstant);
        MethodePaiement methode = methodePaiementRepository.findById(idMethodePaiement)
                .orElseThrow(() -> new IllegalArgumentException("Méthode de paiement introuvable"));
        paiement.setMethodePaiement(methode);

        paiement = paiementRepository.save(paiement);

        // Créer le statut de paiement
        StatutPaiement statutPaiement = new StatutPaiement();
        statutPaiement.setPaiement(paiement);
        statutPaiement.setPaiementStatut(paiementStatutRepository.findById(1).orElseThrow());
        statutPaiement.setDateStatut(Instant.now());

        statutPaiementRepository.save(statutPaiement);
    }

    /**
     * Acheter plusieurs billets en une commande
     */
    public Integer acheterBilletsEnCommande(Integer idVoyage, List<Integer> placesIds, Integer idClient, Integer nbEnfant, Integer nbSenior) {
        // Vérifications
        Voyage voyage = voyageRepository.findById(idVoyage)
                .orElseThrow(() -> new IllegalArgumentException("Voyage introuvable"));

        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        Trajet trajet = voyage.getTrajet();
        BigDecimal montantTotal = BigDecimal.ZERO;

        // Créer la commande
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setDate(Instant.now());

        // Calculer le montant total et vérifier disponibilité
        int enfantCount = 0;
        int seniorCount = 0;
        for (Integer idPlace : placesIds) {
            Place place = placeRepository.findById(idPlace)
                    .orElseThrow(() -> new IllegalArgumentException("Place introuvable: " + idPlace));

            // Vérifier que la place est disponible
            if (!placeService.isPlaceDisponible(idPlace, idVoyage)) {
                throw new IllegalStateException("La place " + place.getNumero() + " n'est plus disponible");
            }

            // Déterminer le type client
            Integer typeClientId;
            if (enfantCount < nbEnfant) {
                typeClientId = 2; // Enfant
                enfantCount++;
            } else if (seniorCount < nbSenior) {
                typeClientId = 3; // Senior
                seniorCount++;
            } else {
                typeClientId = 1; // Adulte
            }

            // Trouver le tarif
            BigDecimal tarif = placeTarifService.getTarif(trajet.getId(), place.getCategorie().getId(), typeClientId, voyage.getDateDepart());
            if (tarif.equals(BigDecimal.ZERO)) {
                throw new IllegalArgumentException("Aucun tarif défini pour ce trajet, catégorie et type client");
            }

            montantTotal = montantTotal.add(tarif);
        }

        commande.setMontantTotal(montantTotal);
        commande = commandeRepository.save(commande);

        // Créer les billets et les détails de commande
        enfantCount = 0;
        seniorCount = 0;
        for (Integer idPlace : placesIds) {
            Place place = placeRepository.findById(idPlace).orElseThrow();

            // Déterminer le type client
            Integer typeClientId;
            if (enfantCount < nbEnfant) {
                typeClientId = 2; // Enfant
                enfantCount++;
            } else if (seniorCount < nbSenior) {
                typeClientId = 3; // Senior
                seniorCount++;
            } else {
                typeClientId = 1; // Adulte
            }

            // Trouver le tarif pour ce billet
            BigDecimal tarif = placeTarifService.getTarif(trajet.getId(), place.getCategorie().getId(), typeClientId, voyage.getDateDepart());

            // Créer le billet
            Billet billet = new Billet();
            billet.setVoyage(voyage);
            billet.setPlace(place);
            billet.setMontantTotal(tarif);
            billet.setStatut("Non Payé");
            billet.setCodeBillet(genererCodeBillet());
            billet = billetRepository.save(billet);

            // Créer le détail de commande
            DetailsCommande detail = new DetailsCommande();
            detail.setCommande(commande);
            detail.setBillet(billet);
            detailsCommandeRepository.save(detail);
        }

        return commande.getId();
    }

    /**
     * Payer une commande (tous les billets de la commande)
     */
    public void payerCommande(Integer idCommande, String datePaiement, Integer idMethodePaiement) {
        Commande commande = commandeRepository.findById(idCommande)
                .orElseThrow(() -> new IllegalArgumentException("Commande introuvable"));

        // Parser la date de paiement
        Instant datePaiementInstant;
        try {
            datePaiementInstant = Instant.parse(datePaiement + "T00:00:00Z"); // Assuming date format is YYYY-MM-DD
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide");
        }

        // Récupérer tous les détails de la commande
        List<DetailsCommande> details = detailsCommandeRepository.findByCommande_Id(idCommande);

        if (details.isEmpty()) {
            throw new IllegalStateException("Aucun billet dans cette commande");
        }

        // Vérifier que tous les billets ne sont pas déjà payés
        for (DetailsCommande detail : details) {
            if ("Payé".equals(detail.getBillet().getStatut())) {
                throw new IllegalStateException("Certains billets de cette commande sont déjà payés");
            }
        }

        // Payer chaque billet
        for (DetailsCommande detail : details) {
            Billet billet = detail.getBillet();

            // Mettre à jour le statut du billet
            billet.setStatut("Payé");
            billetRepository.save(billet);

            // Créer le paiement pour ce billet
            Paiement paiement = new Paiement();
            paiement.setMontant(billet.getMontantTotal());
            paiement.setDatePaiement(datePaiementInstant);
            paiement.setCommande(commande);  // Ajouter la référence à la commande
            MethodePaiement methode = methodePaiementRepository.findById(idMethodePaiement)
                    .orElseThrow(() -> new IllegalArgumentException("Méthode de paiement introuvable"));
            paiement.setMethodePaiement(methode);

            paiement = paiementRepository.save(paiement);

            // Créer le statut de paiement
            StatutPaiement statutPaiement = new StatutPaiement();
            statutPaiement.setPaiement(paiement);
            statutPaiement.setPaiementStatut(paiementStatutRepository.findById(1).orElseThrow());
            statutPaiement.setDateStatut(Instant.now());

            statutPaiementRepository.save(statutPaiement);
        }
    }
}
