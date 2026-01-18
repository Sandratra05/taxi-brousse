package com.brousse.service;

import com.brousse.model.Reduction;
import com.brousse.repository.ReductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReductionService {

    @Autowired
    private ReductionRepository reductionRepository;

    public List<Reduction> findAll() {
        return reductionRepository.findAll();
    }

    public Optional<Reduction> findById(Integer id) {
        return reductionRepository.findById(id);
    }

    public Reduction save(Reduction reduction) {
        return reductionRepository.save(reduction);
    }

    public void deleteById(Integer id) {
        reductionRepository.deleteById(id);
    }
}