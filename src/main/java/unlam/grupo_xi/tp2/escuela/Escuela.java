package unlam.grupo_xi.tp2.escuela;

import unlam.grupo_xi.tp2.equipamiento.Equipamiento;
import unlam.grupo_xi.tp2.tecnica.Tecnica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Escuela {
    private final String nombre;
    private final List<Tecnica> tecnicasIniciales;
    private final List<Equipamiento> equipamientosIniciales;

    public Escuela(String nombre, List<Tecnica> tecnicasIniciales, List<Equipamiento> equipamientosIniciales) {
        this.nombre = nombre;
        this.tecnicasIniciales = tecnicasIniciales != null ? new ArrayList<>(tecnicasIniciales) : Collections.emptyList();
        this.equipamientosIniciales = equipamientosIniciales != null ? new ArrayList<>(equipamientosIniciales) : Collections.emptyList();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Tecnica> getTecnicasIniciales() {
        return Collections.unmodifiableList(tecnicasIniciales);
    }

    public List<Equipamiento> getEquipamientosIniciales() {
        return Collections.unmodifiableList(equipamientosIniciales);
    }
}
