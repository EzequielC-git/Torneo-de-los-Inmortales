package unlam.grupo_xi.tp2.observador;

import unlam.grupo_xi.tp2.combatiente.Combatiente; 

public class AcontecimientoAccion extends Acontecimiento {
    private final Combatiente ejecutor;
    private final Combatiente objetivo;

    public AcontecimientoAccion(String descripcion, int ronda, Combatiente ejecutor, Combatiente objetivo) {
        super(TipoAcontecimiento.ACCION_REALIZADA, descripcion, ronda);
        this.ejecutor = ejecutor;
        this.objetivo = objetivo;
    }

    public Combatiente getEjecutor() {
        return ejecutor;
    }

    public Combatiente getObjetivo() {
        return objetivo;
    }
}