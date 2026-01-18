package com.brousse.service;

import com.brousse.model.PlaceTarif;
import com.brousse.model.Reduction;
import com.brousse.repository.PlaceTarifRepository;
import com.brousse.repository.ReductionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceTarifService {

	private final PlaceTarifRepository placeTarifRepository;
	private final ReductionRepository reductionRepository;

	public PlaceTarifService(PlaceTarifRepository placeTarifRepository, ReductionRepository reductionRepository) {
		this.placeTarifRepository = placeTarifRepository;
		this.reductionRepository = reductionRepository;
	}

	// Récupère tous les tarifs de places pour un trajet donné
	public List<PlaceTarif> findByTrajetId(Integer trajetId) {
		if (trajetId == null) return java.util.Collections.emptyList();
		return placeTarifRepository.findByTrajet_Id(trajetId);
	}

	public void saveAll(List<PlaceTarif> placeTarifs) {
		placeTarifRepository.saveAll(placeTarifs);
	}

	// Récupère le tarif pour un trajet, catégorie, type client et date donnée
	public BigDecimal getTarif(Integer trajetId, Integer categorieId, Integer typeClientId, LocalDateTime dateDepart) {
		List<PlaceTarif> tarifs = placeTarifRepository.findByTrajet_Id(trajetId);

		// Trouver le tarif de base 
		BigDecimal baseTarif = tarifs.stream()
			.filter(pt -> pt.getCategorie().getId().equals(categorieId) &&
					  pt.getTypeClient().getId().equals(1) &&
					  !pt.getDateTarif().isAfter(dateDepart))
			.max((a, b) -> a.getDateTarif().compareTo(b.getDateTarif()))
			.map(PlaceTarif::getTarif)
			.orElse(BigDecimal.ZERO);

		if (typeClientId == 1) {
			return baseTarif;
		} else if(typeClientId == 3) {
			// Appliquer la réduction pour le typeClientId donné
			Optional<Reduction> reductionOpt = reductionRepository.findByTypeClient_Id(typeClientId);
			if (reductionOpt.isPresent()) {
				BigDecimal reductionPercent = reductionOpt.get().getReduction();
				BigDecimal reductionFactor = BigDecimal.ONE.subtract(reductionPercent.divide(BigDecimal.valueOf(100)));
				return baseTarif.multiply(reductionFactor);
			} else {
				// Pas de réduction définie, retourner le tarif de base
				return baseTarif;
			}
		} else {
			BigDecimal baseTarifEnfant = tarifs.stream()
			.filter(pt -> pt.getCategorie().getId().equals(categorieId) &&
					  pt.getTypeClient().getId().equals(2) &&
					  !pt.getDateTarif().isAfter(dateDepart))
			.max((a, b) -> a.getDateTarif().compareTo(b.getDateTarif()))
			.map(PlaceTarif::getTarif)
			.orElse(BigDecimal.ZERO);

			return baseTarifEnfant;
		}
	}
}
