package unlam.grupo_xi.tp2.pareja;

import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.tecnica.Tecnica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Pareja {
    private final int id;
    private final Combatiente integrante1;
    private final Combatiente integrante2;

    // Registra qué combatiente usó una técnica en el combate en curso (para la regla del compañero)
    private final Map<Tecnica, Combatiente> tecnicaUsadaPor;

    public Pareja(int id, Combatiente integrante1, Combatiente integrante2) {
        if (integrante1 == null || integrante2 == null) {
            throw new IllegalArgumentException("Los integrantes de la pareja no pueden ser nulos.");
        }
        if (integrante1.equals(integrante2)) {
            throw new IllegalArgumentException("Una pareja debe estar compuesta por dos combatientes distintos.");
        }
        this.id = id;
        this.integrante1 = integrante1;
        this.integrante2 = integrante2;
        this.tecnicaUsadaPor = new HashMap<>();
    }


    public int getIdentificador() {
        return this.id;
    }

    public int getId() {
        return this.id;
    }

    public Combatiente getIntegrante1() {
        return this.integrante1;
    }

    public Combatiente getIntegrante2() {
        return this.integrante2;
    }

    //Retorna una lista con los integrantes que aún están en pie (vida > 0).
    public List<Combatiente> getIntegrantesEnPie() {
        List<Combatiente> enPie = new ArrayList<>();
        if (integrante1.estaEnPie()) {
            enPie.add(integrante1);
        }
        if (integrante2.estaEnPie()) {
            enPie.add(integrante2);
        }
        return enPie;
    }

    //Calcula la suma de la vida actual de los integrantes en pie. Los caídos suman cero.
    public int getVidaTotal() {
        int vida1 = integrante1.estaEnPie() ? integrante1.getVidaActual() : 0;
        int vida2 = integrante2.estaEnPie() ? integrante2.getVidaActual() : 0;
        return vida1 + vida2;
    }


   //Indica si la pareja ha sido derrotada (ambos integrantes fuera de combate).
    public boolean estaDerrotada() {
        return !integrante1.estaEnPie() && !integrante2.estaEnPie();
    }

    
	//Verifica si el combatiente dado pertenece a la pareja.
    public boolean contiene(Combatiente combatiente) {
        if (combatiente == null) return false;
        return integrante1.equals(combatiente) || integrante2.equals(combatiente);
    }

    
    //Retorna el compañero del combatiente especificado.
    public Combatiente getCompaniero(Combatiente combatiente) {
        if (integrante1.equals(combatiente)) {
            return integrante2;
        } else if (integrante2.equals(combatiente)) {
            return integrante1;
        }
        return null;
    }

    /**
     * Evalúa si un combatiente puede ejecutar una técnica respetando la regla del compañero:
     * Cuando un integrante usa una técnica en un combate, su compañero no puede usarla mientras
     * el primero conserve usos de ella y siga en pie; si los agota o queda fuera de combate,
     * el compañero puede usarla.
     */
    public boolean puedeUsarTecnica(Tecnica tecnica, Combatiente usuario) {
        if (tecnica == null || usuario == null) {
            return false;
        }

        if (!contiene(usuario) || !usuario.estaEnPie()) {
            return false;
        }

        if (!usuario.tieneUsosRestantes(tecnica)) {
            return false;
        }

        Combatiente companiero = getCompaniero(usuario);
        if (companiero != null) {
            Combatiente usuarioRegistrado = tecnicaUsadaPor.get(tecnica);

            // Si el compañero fue quien usó primero la técnica en este combate
            if (companiero.equals(usuarioRegistrado)) {
                // La técnica está bloqueada si el compañero sigue en pie Y aún conserva usos
                if (companiero.estaEnPie() && companiero.tieneUsosRestantes(tecnica)) {
                    return false;
                }
            }
        }

        return true;
    }

    //Registra el uso de una técnica por parte de un integrante durante el combate actual.
    public void registrarUsoTecnica(Tecnica tecnica, Combatiente usuario) {
        if (tecnica == null || usuario == null) {
            throw new IllegalArgumentException("La técnica y el usuario no pueden ser nulos.");
        }
        if (!contiene(usuario)) {
            throw new IllegalArgumentException("El combatiente no pertenece a esta pareja.");
        }

        // Si no estaba registrada o si el usuario anterior era el compañero (que ya no la bloquea),
        // se registra/actualiza el usuario actual.
        Combatiente usuarioPrevio = tecnicaUsadaPor.get(tecnica);
        if (usuarioPrevio == null || !usuarioPrevio.equals(usuario)) {
            tecnicaUsadaPor.put(tecnica, usuario);
        }
    }

    /**
     * Retorna los integrantes en pie ordenados por menor identificador, según la regla
     * de turnos del combate (actúa primero el de menor identificador).
     */
    public List<Combatiente> getIntegrantesOrdenadosParaElTurno() {
        List<Combatiente> ordenados = getIntegrantesEnPie();
        ordenados.sort(Comparator.comparingInt(Combatiente::getId));
        return ordenados;
    }

    /**
     * Reinicia el registro de técnicas del combate.
     * La restricción del compañero termina con el combate; en el siguiente, cualquiera
     * de los dos puede volver a usarla.
     */
    public void reiniciarParaNuevoCombate() {
        this.tecnicaUsadaPor.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pareja pareja)) return false;
        return this.id == pareja.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pareja #" + id + " [" + integrante1.getNombre() + " (ID: " + integrante1.getId() + "), "
                + integrante2.getNombre() + " (ID: " + integrante2.getId() + ")]";
    }
}
