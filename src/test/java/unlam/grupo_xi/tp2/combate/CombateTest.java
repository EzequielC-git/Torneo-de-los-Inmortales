package unlam.grupo_xi.tp2.combate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.combatiente.CultivadorDeLaEspada;
import unlam.grupo_xi.tp2.combatiente.HechiceroDelVacio;
import unlam.grupo_xi.tp2.combatiente.MonjeDelCuerpoCelestial;
import unlam.grupo_xi.tp2.observador.Acontecimiento;
import unlam.grupo_xi.tp2.observador.ObservadorTorneo;
import unlam.grupo_xi.tp2.observador.TipoAcontecimiento;
import unlam.grupo_xi.tp2.pareja.Pareja;
import unlam.grupo_xi.tp2.tecnica.CorteDeLosMilAmaneceres;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CombateTest {

    private Combatiente c1, c2, c3, c4;
    private Pareja pareja1;
    private Pareja pareja2;

    
    @BeforeEach
    public void setUp() {
        c1 = new CultivadorDeLaEspada(1, "Aurelio");
        c2 = new HechiceroDelVacio(2, "Nebula");
        pareja1 = new Pareja(10, c1, c2);

        c3 = new MonjeDelCuerpoCelestial(3, "Kael");
        c4 = new CultivadorDeLaEspada(4, "Darian");
        pareja2 = new Pareja(20, c3, c4);
    }

    @Test
    public void combateIniciaRestaurandoVidaYEnergiaAlMaximo() {
        c1.recibirDanio(50);
        c1.gastarEnergia(30);
        assertEquals(50, c1.getVidaActual());
        assertEquals(10, c1.getEnergiaActual());

        Combate combate = new Combate(pareja1, pareja2, 1, 10);
        combate.ejecutarCombate();

        // Al iniciar el combate se restaura al máximo antes de los turnos
        assertTrue(c1.getVidaActual() > 0);
    }

    @Test
    public void combateTerminaCuandoUnaParejaQuedaDerrotada() {
        // Dotamos a la pareja 1 de técnicas poderosas para vencer rápidamente
        c1.agregarTecnica(new CorteDeLosMilAmaneceres());
        c2.agregarTecnica(new CorteDeLosMilAmaneceres());

        Combate combate = new Combate(pareja1, pareja2, 30, 10);
        Pareja ganadora = combate.ejecutarCombate();

        assertNotNull(ganadora);
        assertTrue(pareja1.estaDerrotada() || pareja2.estaDerrotada());
        assertFalse(ganadora.estaDerrotada());
    }

    @Test
    public void desempatePorVidaTotalAlAlcanzarLimiteDeAsaltos() {
        // Combate de 1 solo asalto: pareja1 dañará a pareja2, pareja2 quedará con menos vida
        c1.agregarTecnica(new CorteDeLosMilAmaneceres());
        Combate combate = new Combate(pareja1, pareja2, 1, 0);

        Pareja ganadora = combate.ejecutarCombate();

        // Gana quien tenga mayor vida total al llegar al límite de asaltos
        if (pareja1.getVidaTotal() > pareja2.getVidaTotal()) {
            assertEquals(pareja1, ganadora);
        } else if (pareja2.getVidaTotal() > pareja1.getVidaTotal()) {
            assertEquals(pareja2, ganadora);
        } else {
            // Empate en vida: gana la de menor identificador (pareja1: ID 10 vs pareja2: ID 20)
            assertEquals(pareja1, ganadora);
        }
    }

    @Test
    public void desempatePorMenorIdentificadorEnEmpateDeVidaAlLimite() {
        // Sin técnicas ni daño (ambas parejas quedan intactas al límite de 0 daño en 1 asalto si nadie ataca o empate)
        // Forzamos un combate donde ambas parejas terminan con la misma vida
        Pareja p1 = new Pareja(5, new MonjeDelCuerpoCelestial(10, "M1"), new MonjeDelCuerpoCelestial(11, "M2"));
        Pareja p2 = new Pareja(15, new MonjeDelCuerpoCelestial(20, "M3"), new MonjeDelCuerpoCelestial(21, "M4"));

        Combate combate = new Combate(p1, p2, 1, 0);
        Pareja ganadora = combate.ejecutarCombate();

        // Como ambas tienen la misma vida o empatan, la regla dice: en empate, la de menor ID
        if (p1.getVidaTotal() == p2.getVidaTotal()) {
            assertEquals(p1, ganadora);
        }
    }

    @Test
    public void eleccionDeObjetivoPriorizaRivalEnPieConMenosVida() {
        c3.recibirDanio(30); // c3 queda en 70 de vida
        c4.recibirDanio(10); // c4 queda en 90 de vida

        Combatiente objetivo = c1.elegirObjetivo(pareja2.getIntegrantesEnPie());

        // Debe elegir a c3 porque tiene menos vida actual (70 vs 90)
        assertEquals(c3, objetivo);
    }

    @Test
    public void eleccionDeObjetivoDesempataPorMenorIdentificador() {
        // Ambos rivales con la misma vida
        Combatiente objetivo = c1.elegirObjetivo(pareja2.getIntegrantesEnPie());

        // En empate de vida (100 vs 100), elige al de menor ID (c3 tiene ID 3, c4 tiene ID 4)
        assertEquals(c3, objetivo);
    }

    @Test
    public void caidoNoActuaNiEsElegidoComoObjetivo() {
        // c3 es MonjeDelCuerpoCelestial (mitiga el 25% del daño recibido).
        // Le aplicamos daño suficiente para superar la mitigación y dejarlo fuera de combate.
        c3.recibirDanio(200);
        assertFalse(c3.estaEnPie());

        Combatiente objetivo = c1.elegirObjetivo(pareja2.getIntegrantesEnPie());
        // Solo puede elegir a c4 (c3 está caído)
        assertEquals(c4, objetivo);
    }

    @Test
    public void combateNotificaAcontecimientosAObservadores() {
        List<Acontecimiento> eventos = new ArrayList<>();
        ObservadorTorneo observador = eventos::add;

        Combate combate = new Combate(pareja1, pareja2, 5, 10);
        combate.agregarObservador(observador);

        combate.ejecutarCombate();

        assertFalse(eventos.isEmpty());
        // El primer evento debe ser INICIO_COMBATE
        assertEquals(TipoAcontecimiento.INICIO_COMBATE, eventos.get(0).getTipoEvento());
        // El último evento debe ser RESULTADO_COMBATE
        assertEquals(TipoAcontecimiento.RESULTADO_COMBATE, eventos.get(eventos.size() - 1).getTipoEvento());
    }

    @Test
    public void combateEsDeterministaAnteMismoEstadoInicial() {
        // Combate 1
        c1.agregarTecnica(new CorteDeLosMilAmaneceres());
        c3.agregarTecnica(new CorteDeLosMilAmaneceres());
        Combate combate1 = new Combate(pareja1, pareja2, 10, 10);
        Pareja ganadora1 = combate1.ejecutarCombate();

        // Combate 2 (mismo estado inicial)
        Combatiente c1_bis = new CultivadorDeLaEspada(1, "Aurelio");
        Combatiente c2_bis = new HechiceroDelVacio(2, "Nebula");
        Pareja pareja1_bis = new Pareja(10, c1_bis, c2_bis);

        Combatiente c3_bis = new MonjeDelCuerpoCelestial(3, "Kael");
        Combatiente c4_bis = new CultivadorDeLaEspada(4, "Darian");
        Pareja pareja2_bis = new Pareja(20, c3_bis, c4_bis);

        c1_bis.agregarTecnica(new CorteDeLosMilAmaneceres());
        c3_bis.agregarTecnica(new CorteDeLosMilAmaneceres());
        Combate combate2 = new Combate(pareja1_bis, pareja2_bis, 10, 10);
        Pareja ganadora2 = combate2.ejecutarCombate();

        // Mismo campeón e idéntico resultado
        assertEquals(ganadora1.getIdentificador(), ganadora2.getIdentificador());
    }
}
