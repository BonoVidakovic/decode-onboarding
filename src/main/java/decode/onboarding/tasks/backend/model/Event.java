package decode.onboarding.tasks.backend.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    @Column(name = "start")
    private LocalDateTime startTime;

    @Column(name = "end")
    private LocalDateTime endTime;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "owner")
    private User owner;

}
