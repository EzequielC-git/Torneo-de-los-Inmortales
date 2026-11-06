package unlam.grupo_xi.tp2.combatiente;

import unlam.grupo_xi.tp2.TorneoDeLosInmortales;
import unlam.grupo_xi.tp2.escuela.Escuela;

public class CreadorCombatientes {
    public Combatiente crear(CategoriaCombatiente tipo, String nombre, Escuela escuela) {
        int idAsignado = TorneoDeLosInmortales.SIGUIENTE_ID++;
        Combatiente combatiente = instanciarPorTipo(tipo, idAsignado, nombre);

        if (escuela != null && escuela.getTecnicasIniciales() != null)
            escuela.getTecnicasIniciales().forEach(combatiente::agregarTecnica);

        if (escuela != null && escuela.getEquipamientosIniciales() != null)
            escuela.getEquipamientosIniciales().forEach(combatiente::equipar);

        return combatiente;
    }

    private Combatiente instanciarPorTipo(CategoriaCombatiente tipo, int id, String nombre) {
        return switch (tipo) {
            case CULTIVADOR_DE_LA_ESPADA -> new CultivadorDeLaEspada(id, nombre);
            case MONJE_DEL_CUERPO_CELESTIAL -> new MonjeDelCuerpoCelestial(id, nombre);
            case HECHICERO_DEL_VACIO -> new HechiceroDelVacio(id, nombre);
            default -> throw new IllegalArgumentException("Tipo de combatiente no reconocido: " + tipo);
        };
    }
}