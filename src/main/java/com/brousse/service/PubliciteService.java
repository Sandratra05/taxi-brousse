package com.brousse.service;

import com.brousse.model.PaiementPublicite;
import com.brousse.model.Publicite;
import com.brousse.repository.PaiementPubliciteRepository;
import com.brousse.repository.PubliciteRepository;
import com.brousse.repository.SocieteRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.brousse.model.Societe;

@Service
public class PubliciteService {
    private final PubliciteRepository publiciteRepository;
    private final SocieteRepository societeRepository;
    private final PaiementPubliciteRepository paiementPubliciteRepository;

    public PubliciteService(PubliciteRepository publiciteRepository,
                            SocieteRepository societeRepository,
                            PaiementPubliciteRepository paiementPubliciteRepository) {
        this.publiciteRepository = publiciteRepository;
        this.societeRepository = societeRepository;
        this.paiementPubliciteRepository = paiementPubliciteRepository;
    }

    public Publicite create(Publicite publicite) {
        return publiciteRepository.save(publicite);
    }

    public Optional<Publicite> findById(Integer id) {
        return publiciteRepository.findById(id);
    }

    public List<Publicite> findAll() {
        return publiciteRepository.findAll();
    }

    public List<Publicite> findBySocieteId(Integer idSociete) {
        return publiciteRepository.findBySociete_Id(idSociete);
    }

    public List<Publicite> findByVehiculeId(Integer idVehicule) {
        return publiciteRepository.findByVehicule_Id(idVehicule);
    }

    public Publicite update(Integer id, Publicite details) {
        Publicite p = publiciteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicité non trouvée avec id: " + id));

        p.setDateDiffusion(details.getDateDiffusion());
        p.setVehicule(details.getVehicule());
        p.setSociete(details.getSociete());
        p.setEstPaye(details.getEstPaye());

        return publiciteRepository.save(p);
    }

    public void delete(Integer id) {
        publiciteRepository.deleteById(id);
    }

    public List<Publicite> getByAnneeAndMonth(int year, int month) {
        return publiciteRepository.findAll().stream()
                .filter(p -> p.getDateDiffusion().getYear() == year && p.getDateDiffusion().getMonthValue() == month)
                .toList();
    }

    /**
     * Calcule le montant total payé pour une liste de publicités
     */
    public BigDecimal totalPaye(List<Publicite> publicites) {
        BigDecimal total = BigDecimal.ZERO;
        for (Publicite p : publicites) {
            BigDecimal montantPaye = paiementPubliciteRepository.sumMontantByPubliciteId(p.getId());
            total = total.add(montantPaye != null ? montantPaye : BigDecimal.ZERO);
        }
        return total;
    }

    /**
     * Calcule le montant total payé pour une société donnée
     */
    public BigDecimal totalPayeBySociete(Integer societeId) {
        BigDecimal result = paiementPubliciteRepository.sumMontantBySocieteId(societeId);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Calcule le montant total payé pour une société par année et mois
     */
    public BigDecimal totalPayeBySocieteAndAnneeMois(Integer societeId, Integer annee, Integer mois) {
        BigDecimal result = paiementPubliciteRepository.sumMontantBySocieteIdAndAnneeMois(societeId, annee, mois);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Retourne une map avec le total payé par société pour une année/mois donnés
     */
    public Map<Integer, BigDecimal> totalPayeParSociete(Integer annee, Integer mois) {
        Map<Integer, BigDecimal> result = new HashMap<>();
        List<Societe> societes = societeRepository.findAll();
        for (Societe s : societes) {
            if (annee != null && mois != null) {
                result.put(s.getId(), totalPayeBySocieteAndAnneeMois(s.getId(), annee, mois));
            } else {
                result.put(s.getId(), totalPayeBySociete(s.getId()));
            }
        }
        return result;
    }

    /**
     * Retourne une map avec le total payé par société (sans filtre)
     */
    public Map<Integer, BigDecimal> totalPayeParSociete() {
        return totalPayeParSociete(null, null);
    }

}
