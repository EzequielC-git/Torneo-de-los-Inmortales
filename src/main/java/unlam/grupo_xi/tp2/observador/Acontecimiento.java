package unlam.grupo_xi.tp2.observador;

import unlam.grupo_xi.tp2.pareja.Pareja;

import java.util.Date;

public class Acontecimiento {
    private final TipoAcontecimiento tipoEvento;
    private final String descripcion;
    private final Date momento;
    private final int ronda;
    private final Pareja parejaGanadora;
    private final int vidaTotalGanadora;
    private final Pareja parejaPerdedora;
    private final int vidaTotalPerdedora;

    public Acontecimiento(TipoAcontecimiento tipoEvento, String descripcion, int ronda) {
        this(tipoEvento, descripcion, ronda, null, 0, null, 0);
    }
    

    public Acontecimiento(TipoAcontecimiento tipoEvento, String descripcion, int ronda,
                          Pareja parejaGanadora, int vidaTotalGanadora,
                          Pareja parejaPerdedora, int vidaTotalPerdedora) {
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.momento = new Date();
        this.ronda = ronda;
        this.parejaGanadora = parejaGanadora;
        this.vidaTotalGanadora = vidaTotalGanadora;
        this.parejaPerdedora = parejaPerdedora;
        this.vidaTotalPerdedora = vidaTotalPerdedora;
    }

    public TipoAcontecimiento getTipoEvento() {
        return tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getMomento() {
        return momento;
    }

    public int getRonda() {
        return ronda;
    }

    public Pareja getParejaGanadora() {
        return parejaGanadora;
    }

    public int getVidaTotalGanadora() {
        return vidaTotalGanadora;
    }

    public Pareja getParejaPerdedora() {
        return parejaPerdedora;
    }

    public int getVidaTotalPerdedora() {
        return vidaTotalPerdedora;
    }

    @Override
    public String toString() {
        return "[" + tipoEvento + "] " + descripcion;
    }
}
