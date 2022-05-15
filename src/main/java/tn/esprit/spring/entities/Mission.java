package tn.esprit.spring.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Mission implements Serializable {

	private static final long serialVersionUID = -5369734855993305723L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String name;

	private String description;
	
	@ManyToOne
	private Departement departement;
	
	@OneToMany(mappedBy="mission")
	private  List<Timesheet> timesheets;

}
