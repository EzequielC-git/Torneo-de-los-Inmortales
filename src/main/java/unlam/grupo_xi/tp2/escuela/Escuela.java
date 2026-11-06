package unlam.grupo_xi.tp2.escuela;

import lombok.Getter;
import unlam.grupo_xi.tp2.equipamiento.Equipamiento;
import unlam.grupo_xi.tp2.tecnica.Tecnica;
import java.util.Collections;
import java.util.List;

@Getter
public class Escuela {
    public List<Tecnica> getTecnicasIniciales() {
        return Collections.emptyList();
    }

    public List<Equipamiento> getEquipamientosIniciales() {
        return Collections.emptyList();
    }
}
