package it.uniroma3.siw.calcio.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Partita {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime dataOra;
    private String luogo;
    private int goalsHome;
    private int goalsAway;
    @Enumerated(EnumType.STRING)
    private StatoPartita stato;
    @ManyToOne
    private Torneo torneo;
    @ManyToOne
    private Squadra squadraHome;
    @ManyToOne
    private Squadra squadraAway;
    @ManyToOne
    private Arbitro arbitro;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getDataOra() {
        return dataOra;
    }
    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }
    public String getLuogo() {
        return luogo;
    }
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }
    public int getGoalsHome() {
        return goalsHome;
    }
    public void setGoalsHome(int goalsHome) {
        this.goalsHome = goalsHome;
    }
    public int getGoalsAway() {
        return goalsAway;
    }
    public void setGoalsAway(int goalsAway) {
        this.goalsAway = goalsAway;
    }
    public StatoPartita getStato() {
        return stato;
    }
    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }
    public Torneo getTorneo() {
        return torneo;
    }
    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }
    public Squadra getSquadraHome() {
        return squadraHome;
    }
    public void setSquadraHome(Squadra squadraHome) {
        this.squadraHome = squadraHome;
    }
    public Squadra getSquadraAway() {
        return squadraAway;
    }
    public void setSquadraAway(Squadra squadraAway) {
        this.squadraAway = squadraAway;
    }
    public Arbitro getArbitro() {
        return arbitro;
    }
    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataOra == null) ? 0 : dataOra.hashCode());
        result = prime * result + ((luogo == null) ? 0 : luogo.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Partita other = (Partita) obj;
        if (dataOra == null) {
            if (other.dataOra != null)
                return false;
        } else if (!dataOra.equals(other.dataOra))
            return false;
        if (luogo == null) {
            if (other.luogo != null)
                return false;
        } else if (!luogo.equals(other.luogo))
            return false;
        return true;
    }

    
}
