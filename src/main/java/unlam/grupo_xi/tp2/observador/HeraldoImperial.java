package unlam.grupo_xi.tp2.observador;

import unlam.grupo_xi.tp2.pareja.Pareja;
import java.util.List;

public class HeraldoImperial implements ObservadorTorneo {

    @Override
    public void actualizar(Acontecimiento evento) {
        System.out.println(evento.toString());
    }

    public void presentarTablaPosiciones(List<Pareja> tabla) {
        System.out.println("\nTABLA DE POSICIONES FINAL");
        int posicion = 1;
        for (Pareja p : tabla) {
            System.out.println(posicion + ". " + p);
            posicion++;
        }
    }
}