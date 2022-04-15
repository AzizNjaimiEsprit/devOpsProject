package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class VacationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestReference;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private Employe supervisor;

    @Column (name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date from;

    @Column (name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date to;
    @Column
    private Boolean approved;
    @Transient
    private Boolean overlapTimeSheet;
    @Transient
    private List<Timesheet> overlappingTimeSheets;

    public VacationRequest (Employe employe, Employe supervisor, Date from, Date to) {
        employe = employe;
        supervisor = supervisor;
        from = from;
        to = to;
    }

    public VacationRequest() {

    }

}
