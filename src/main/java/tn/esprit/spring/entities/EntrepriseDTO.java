package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntrepriseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -547388177630010669L;

	private int idDTO;
	private String nameDTO;
	private String raisonSocialDTO;
	private List<Departement> departementsDTO = new ArrayList<>();

	public EntrepriseDTO() {
		super();
		this.getEntreprise();
	}

	public Entreprise getEntreprise() {
		Entreprise entreprise = new Entreprise();
		entreprise.setId(this.idDTO);
		entreprise.setName(this.nameDTO);
		entreprise.setRaisonSocial(this.raisonSocialDTO);
		entreprise.setDepartements(this.departementsDTO);
		return entreprise;

	}

	public int getId() {
		return idDTO;
	}

	public void setId(int id) {
		this.idDTO = id;
	}

	public String getName() {
		return nameDTO;
	}

	public void setName(String name) {
		this.nameDTO = name;
	}

	public String getRaisonSocial() {
		return raisonSocialDTO;
	}

	public void setRaisonSocial(String raisonSocial) {
		this.raisonSocialDTO = raisonSocial;
	}

	public List<Departement> getDepartements() {
		return departementsDTO;
	}

	public void setDepartements(List<Departement> departements) {
		this.departementsDTO = departements;
	}

	public void addDepartement(Departement departement) {
		departement.setEntreprise(this.getEntreprise());
		this.departementsDTO.add(departement);
	}

}
