package decode.onboarding.tasks.backend.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;
}
