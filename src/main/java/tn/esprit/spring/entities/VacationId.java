package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Date;

public class VacationId implements Serializable {
    private int employeeId;
    private Date from;
    private Date to;
}
