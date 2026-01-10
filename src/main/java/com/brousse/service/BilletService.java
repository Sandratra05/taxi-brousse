package com.brousse.service;

import com.brousse.model.*;
import com.brousse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BilletService {
    
    @Autowired
    private BilletRepository billetRepository;
    
    @Autowired
    private PlaceRepository placeRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private TarifRepository tarifRepository;
    
    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private StatutPaiementRepository statutPaiementRepository;

    @Autowired
    private PaiementStatutRepository paiementStatutRepository;

    @Autowired
    private MethodePaiementRepository methodePaiementRepository;

    @Autowired
    private PlaceService placeService;  

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
        
        // Calculer le montant (tarif du trajet)
        Trajet trajet = voyage.getTrajet();
        List<Tarif> tarifs = tarifRepository.findAll();
        Tarif tarif = tarifs.stream()
                .filter(t -> t.getTrajet().getId().equals(trajet.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucun tarif défini pour ce trajet"));
        
        BigDecimal montantTotal = tarif.getPrixBase();
        
        // Créer le billet
        Billet billet = new Billet();
        billet.setVoyage(voyage);
        billet.setPlace(place);
        billet.setClient(client);
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

        // Mettre à jour le statut du billet
        billet.setStatut("Payé");
        billetRepository.save(billet);

        // Créer le paiement
        Paiement paiement = new Paiement();
        paiement.setBillet(billet);
        paiement.setMontant(billet.getMontantTotal());
        MethodePaiement methode = methodePaiementRepository.findById(idMethodePaiement)
                .orElseThrow(() -> new IllegalArgumentException("Méthode de paiement introuvable"));
        paiement.setMethodePaiement(methode);

        paiement = paiementRepository.save(paiement);

        // Créer le statut de paiement
        StatutPaiement statutPaiement = new StatutPaiement();
        statutPaiement.setId(new StatutPaiementId(paiement.getId(), 1)); // 1 = Effectué
        statutPaiement.setPaiement(paiement);
        statutPaiement.setPaiementStatut(paiementStatutRepository.findById(1).orElseThrow());
        statutPaiement.setDateStatut(java.time.LocalDate.now());

        statutPaiementRepository.save(statutPaiement);
    }
}
