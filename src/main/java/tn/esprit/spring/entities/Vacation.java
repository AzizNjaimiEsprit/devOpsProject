package tn.esprit.spring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(VacationId.class)
public class Vacation {
    @Id
    @ManyToOne
    @JoinColumn(name = "emolyee_id")
    private Employe employeeId;

    @Id
    @Column (name = "start_date")
    private Date from;
    @Id
    @Column (name = "end_date")
    private Date to;


    public Vacation(Employe employe, Date f, Date t) {
        employeeId = employe;
        from = f;
        to = t;
    }

}
