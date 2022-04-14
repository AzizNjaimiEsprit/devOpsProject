package tn.esprit.spring.services;

import tn.esprit.spring.entities.Departement;

import java.util.List;

public interface IDepartmentService {
    public List<Departement> getAllDepartements();
    public Departement addOrUpdateDepartement(Departement departement);
    public String getDepartementName(int departementId);
    public Departement getDepartement(int departementId);
    public void deletedepartementById(int departementId);
}
