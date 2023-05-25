package hu.gde.runnersdemo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SponsorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long sponsorId;

    private String sponsorName;

    @JsonIgnore
    @OneToMany
    private List<RunnerEntity>runners=new ArrayList<>();

    public List<RunnerEntity> getRunners() {
        return runners;
    }

    public long getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(long sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }
}
