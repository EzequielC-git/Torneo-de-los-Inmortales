package unlam.grupo_xi.tp2.combatiente;

import unlam.grupo_xi.tp2.TorneoDeLosInmortales;
import unlam.grupo_xi.tp2.escuela.Escuela;

public class CreadorCombatientes {
    private int proximoId;

    public CreadorCombatientes() {
        this.proximoId = 1;
    }

    //Creación centralizada a partir del nombre del tipo de combatiente (según UML y ejemplo de main).
    public Combatiente crear(String tipo, String nombre, Escuela escuela) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de combatiente no puede ser nulo o vacío.");
        }

        CategoriaCombatiente categoria = parsearTipo(tipo);
        return crear(categoria, nombre, escuela);
    }


    // Sobrecarga para crear a partir del enum CategoriaCombatiente.

    public Combatiente crear(CategoriaCombatiente tipo, String nombre, Escuela escuela) {
        int idAsignado = this.proximoId++;
        // Mantener sincronizada la variable estática si algún test la resetea
        TorneoDeLosInmortales.SIGUIENTE_ID = this.proximoId;

        Combatiente combatiente = instanciarPorTipo(tipo, idAsignado, nombre);

        if (escuela != null && escuela.getTecnicasIniciales() != null) {
            escuela.getTecnicasIniciales().forEach(combatiente::agregarTecnica);
        }

        if (escuela != null && escuela.getEquipamientosIniciales() != null) {
            escuela.getEquipamientosIniciales().forEach(combatiente::equipar);
        }

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

    private CategoriaCombatiente parsearTipo(String tipo) {
        String normalizado = tipo.trim().toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");

        if (normalizado.contains("espada")) {
            return CategoriaCombatiente.CULTIVADOR_DE_LA_ESPADA;
        } else if (normalizado.contains("monje") || normalizado.contains("celestial")) {
            return CategoriaCombatiente.MONJE_DEL_CUERPO_CELESTIAL;
        } else if (normalizado.contains("vacio") || normalizado.contains("hechicero")) {
            return CategoriaCombatiente.HECHICERO_DEL_VACIO;
        } else {
            throw new IllegalArgumentException("Tipo de combatiente no reconocido: " + tipo);
        }
    }
}
