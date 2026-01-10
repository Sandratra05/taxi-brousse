package com.brousse.controller;

import com.brousse.dto.ClientFilterDTO;
import com.brousse.model.Client;
import com.brousse.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {
    
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Liste des clients avec filtres optionnels
    @GetMapping
    public String list(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String email,
            Model model
    ) {
        List<Client> clients;
        
        // Si au moins un filtre est fourni, utiliser la recherche filtrée
        if ((nom != null && !nom.isBlank()) || 
            (prenom != null && !prenom.isBlank()) || 
            (telephone != null && !telephone.isBlank()) || 
            (email != null && !email.isBlank())) {
            
            ClientFilterDTO filtres = new ClientFilterDTO();
            filtres.setNom(nom);
            filtres.setPrenom(prenom);
            filtres.setTelephone(telephone);
            filtres.setEmail(email);
            clients = clientService.findWithFilter(filtres);
            
            // Conserver les valeurs des filtres pour l'affichage
            model.addAttribute("nom", nom);
            model.addAttribute("prenom", prenom);
            model.addAttribute("telephone", telephone);
            model.addAttribute("email", email);
        } else {
            clients = clientService.findAll();
        }
        
        model.addAttribute("clients", clients);
        return "clients/list"; // templates/clients/list.html
    }

    // Détail d'un client
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Client client = clientService.findById(id).orElse(null);
        if (client == null) {
            return "redirect:/clients";
        }
        model.addAttribute("client", client);
        return "clients/detail"; // templates/clients/detail.html
    }

    // Formulaire de création
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        return "clients/create"; // templates/clients/create.html
    }

    // Soumission création
    @PostMapping
    public String create(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String email,
            Model model
    ) {
        // Validations basiques
        boolean hasError = false;
        if (nom == null || nom.isBlank()) {
            model.addAttribute("nomError", "Nom obligatoire");
            hasError = true;
        }
        if (prenom == null || prenom.isBlank()) {
            model.addAttribute("prenomError", "Prénom obligatoire");
            hasError = true;
        }
        
        if (hasError) {
            // Repasser les valeurs saisies
            model.addAttribute("nom", nom);
            model.addAttribute("prenom", prenom);
            model.addAttribute("telephone", telephone);
            model.addAttribute("email", email);
            return "clients/create";
        }
        
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setTelephone(telephone);
        client.setEmail(email);
        
        Client saved = clientService.create(client);
        return "redirect:/clients/" + saved.getId();
    }

    // Formulaire de modification
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Client client = clientService.findById(id).orElse(null);
        if (client == null) {
            return "redirect:/clients";
        }
        model.addAttribute("clientId", id);
        model.addAttribute("nom", client.getNom());
        model.addAttribute("prenom", client.getPrenom());
        model.addAttribute("telephone", client.getTelephone());
        model.addAttribute("email", client.getEmail());
        return "clients/edit"; // templates/clients/edit.html
    }

    // Soumission modification
    @PostMapping("/{id}")
    public String update(
            @PathVariable Integer id,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String email,
            Model model
    ) {
        boolean hasError = false;
        if (nom != null && nom.isBlank()) {
            model.addAttribute("nomError", "Nom obligatoire");
            hasError = true;
        }
        if (prenom != null && prenom.isBlank()) {
            model.addAttribute("prenomError", "Prénom obligatoire");
            hasError = true;
        }
        
        if (hasError) {
            model.addAttribute("clientId", id);
            model.addAttribute("nom", nom);
            model.addAttribute("prenom", prenom);
            model.addAttribute("telephone", telephone);
            model.addAttribute("email", email);
            return "clients/edit";
        }
        
        Client clientDetails = new Client();
        clientDetails.setNom(nom);
        clientDetails.setPrenom(prenom);
        clientDetails.setTelephone(telephone);
        clientDetails.setEmail(email);
        
        clientService.update(id, clientDetails);
        return "redirect:/clients/" + id;
    }

    // Suppression
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        clientService.delete(id);
        return "redirect:/clients";
    }
}
