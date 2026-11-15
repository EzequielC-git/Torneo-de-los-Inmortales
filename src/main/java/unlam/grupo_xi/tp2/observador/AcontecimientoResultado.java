package unlam.grupo_xi.tp2.observador;

import unlam.grupo_xi.tp2.pareja.Pareja;

public class AcontecimientoResultado extends Acontecimiento {
    private final Pareja parejaGanadora;
    private final int vidaTotalGanadora;
    private final Pareja parejaPerdedora;
    private final int vidaTotalPerdedora;

    public AcontecimientoResultado(String descripcion, int ronda,
                                   Pareja parejaGanadora, int vidaTotalGanadora,
                                   Pareja parejaPerdedora, int vidaTotalPerdedora) {
        
        super(TipoAcontecimiento.RESULTADO_COMBATE, descripcion, ronda);
        
        this.parejaGanadora = parejaGanadora;
        this.vidaTotalGanadora = vidaTotalGanadora;
        this.parejaPerdedora = parejaPerdedora;
        this.vidaTotalPerdedora = vidaTotalPerdedora;
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
}