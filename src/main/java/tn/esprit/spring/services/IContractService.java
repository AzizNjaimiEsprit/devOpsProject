package tn.esprit.spring.services;

import tn.esprit.spring.entities.Contrat;

import java.util.List;

public interface IContractService {

    public List<Contrat> getAllContrats();
    public Contrat addOrUpdateContrat(Contrat contrat) ;
    public String getTypeContratById(int contratId);
    public void deleteContratById(int contratId);
}
