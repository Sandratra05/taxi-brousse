package com.brousse.service;

import com.brousse.model.PlaceTarif;
import com.brousse.repository.PlaceTarifRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceTarifService {

	private final PlaceTarifRepository placeTarifRepository;

	public PlaceTarifService(PlaceTarifRepository placeTarifRepository) {
		this.placeTarifRepository = placeTarifRepository;
	}

	// Récupère tous les tarifs de places pour un trajet donné
	public List<PlaceTarif> findByTrajetId(Integer trajetId) {
		if (trajetId == null) return java.util.Collections.emptyList();
		return placeTarifRepository.findByTrajet_Id(trajetId);
	}

}
