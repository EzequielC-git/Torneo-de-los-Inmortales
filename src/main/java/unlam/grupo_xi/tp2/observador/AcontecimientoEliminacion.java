package unlam.grupo_xi.tp2.observador;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public class AcontecimientoEliminacion extends Acontecimiento {
    private final Combatiente caido;

    public AcontecimientoEliminacion(String descripcion, int ronda, Combatiente caido) {
        super(TipoAcontecimiento.ELIMINACION, descripcion, ronda);
        this.caido = caido;
    }

    public Combatiente getCaido() {
        return caido;
    }
}	