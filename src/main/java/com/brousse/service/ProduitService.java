package com.brousse.service;

import org.springframework.stereotype.Service;

import com.brousse.model.Produit;
import com.brousse.repository.ProduitRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    // Create
    public Produit create(Produit produit) {
        return produitRepository.save(produit);
    }

    // Read - Get by ID
    public Optional<Produit> findById(Integer id) {
        return produitRepository.findById(id);
    }

    // Read - Get all
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    // Update
    public Produit update(Integer id, Produit produitDetails) {
        Produit produit = produitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + id));
        
        produit.setLibelle(produitDetails.getLibelle());
        produit.setPrixUnitaire(produitDetails.getPrixUnitaire());
        
        return produitRepository.save(produit);
    }

    // Delete
    public void delete(Integer id) {
        Produit produit = produitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + id));
        produitRepository.delete(produit);
    }
}
