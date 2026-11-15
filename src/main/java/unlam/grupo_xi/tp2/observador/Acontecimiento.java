package unlam.grupo_xi.tp2.observador;

import java.util.Date;

public abstract class Acontecimiento{
    private final TipoAcontecimiento tipoEvento;
    private final String descripcion;
    private final Date momento;
    private final int ronda;

    public Acontecimiento(TipoAcontecimiento tipoEvento, String descripcion, int ronda) {
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.ronda = ronda;
        this.momento = new Date();
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

    @Override
    public String toString() {
        return "[" + tipoEvento + "] " + descripcion;
    }
}