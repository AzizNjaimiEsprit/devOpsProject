package tn.esprit.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DepartmentServiceImp implements IDepartmentService {

    @Autowired
    DepartementRepository deptRepoistory;

    @Autowired
    EmployeRepository employeRepository;

    @Autowired
    IEntrepriseService entrepriseService;

    public List<Departement> getAllDepartements() {
        return (List<Departement>) deptRepoistory.findAll();
    }

    @Override
    public Departement addOrUpdateDepartement(Departement departement) {
        return deptRepoistory.save(departement);
    }

    @Override
    public String getDepartementName(int departementId) {
        Optional<Departement> optionaldepratement = deptRepoistory.findById(departementId);
        return optionaldepratement.isPresent() ? optionaldepratement.get().getName()
                : new NoSuchElementException("Department with the id " + departementId + " doesn't exist in the db")
                .getMessage();
    }

    @Override
    public void deletedepartementById(int departementId) {
        Optional<Departement> optionaldepratement = deptRepoistory.findById(departementId);
        optionaldepratement.ifPresent(o -> {
            Entreprise entreprise = entrepriseService.getEntrepriseById(o.getEntreprise().getId());
            entreprise.getDepartements().remove(o);
            entrepriseService.updateEntreprise(entreprise);
            o.setEntreprise(null);
            deptRepoistory.save(o);
            deptRepoistory.deleteById(departementId);
        });
    }

    @Override
    public Departement getDepartement(int departementId) {
        Optional<Departement> optionaldepratement = deptRepoistory.findById(departementId);
        if (optionaldepratement.isPresent()) return optionaldepratement.get();
        return null;
    }
}
