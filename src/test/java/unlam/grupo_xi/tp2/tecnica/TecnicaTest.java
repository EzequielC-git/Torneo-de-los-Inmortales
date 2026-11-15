package unlam.grupo_xi.tp2.tecnica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unlam.grupo_xi.tp2.combate.Combate;
import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.combatiente.CultivadorDeLaEspada;
import unlam.grupo_xi.tp2.combatiente.HechiceroDelVacio;
import unlam.grupo_xi.tp2.combatiente.MonjeDelCuerpoCelestial;
import unlam.grupo_xi.tp2.pareja.Pareja;

import static org.junit.jupiter.api.Assertions.*;

public class TecnicaTest {

    private Combatiente objetivo;

    @BeforeEach
    public void setUp() {
        objetivo = new HechiceroDelVacio(99, "Objetivo");
    }

    @Test
    public void corteDeLosMilAmaneceresDaniaAlAdversario() {
        Combatiente ejecutor = new HechiceroDelVacio(1, "Nebula");
        new CorteDeLosMilAmaneceres().ejecutar(ejecutor, objetivo);
        assertEquals(75, objetivo.getVidaActual());
    }

    @Test
    public void dominioDeLaNadaDaniaYQuitaEnergiaAlAdversario() {
        Combatiente ejecutor = new HechiceroDelVacio(1, "Nebula");
        new DominioDeLaNada().ejecutar(ejecutor, objetivo);
        assertEquals(90, objetivo.getVidaActual());
        assertEquals(32, objetivo.getEnergiaActual());
    }

    @Test
    public void rugidoDeLaBestiaPrimordialEsUnAtaquePoderoso() {
        Combatiente ejecutor = new HechiceroDelVacio(1, "Nebula");
        new RugidoDeLaBestiaPrimordial().ejecutar(ejecutor, objetivo);
        assertEquals(65, objetivo.getVidaActual());
    }

    @Test
    public void muroDelCuerpoDoradoReduceElDanioQueRecibeElEjecutor() {
        Combatiente ejecutor = new HechiceroDelVacio(1, "Nebula");
        assertFalse(ejecutor.tieneDefensaActiva());

        new MurodelCuerpoDorado().ejecutar(ejecutor, ejecutor);
        ejecutor.recibirDanio(50);

        assertTrue(ejecutor.tieneDefensaActiva());
        assertEquals(65, ejecutor.getVidaActual());
    }

    @Test
    public void meditacionDelInmortalRecuperaVidaSinSuperarElMaximo() {
        Combatiente ejecutor = new HechiceroDelVacio(1, "Nebula");
        ejecutor.recibirDanio(50);

        new MeditacionDelInmortal().ejecutar(ejecutor, ejecutor);
        assertEquals(75, ejecutor.getVidaActual());

        new MeditacionDelInmortal().ejecutar(ejecutor, ejecutor);
        new MeditacionDelInmortal().ejecutar(ejecutor, ejecutor);
        assertEquals(100, ejecutor.getVidaActual());
    }

    @Test
    public void cadaTecnicaSeClasificaComoOfensivaDefensivaODeRecuperacion() {
        assertTrue(new CorteDeLosMilAmaneceres().esOfensiva());
        assertTrue(new DominioDeLaNada().esOfensiva());
        assertTrue(new RugidoDeLaBestiaPrimordial().esOfensiva());
        assertTrue(new MurodelCuerpoDorado().esDefensiva());
        assertTrue(new MeditacionDelInmortal().esCuracion());
        assertFalse(new MurodelCuerpoDorado().esOfensiva());
        assertFalse(new MeditacionDelInmortal().esOfensiva());
    }

    @Test
    public void mismaTecnicaDaniaDiferenteSegunElTipoDelEjecutor() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Combatiente objetivoA = new HechiceroDelVacio(10, "Objetivo A");
        Combatiente objetivoB = new HechiceroDelVacio(11, "Objetivo B");

        corte.ejecutar(new CultivadorDeLaEspada(1, "Aurelio"), objetivoA);
        corte.ejecutar(new HechiceroDelVacio(2, "Nebula"), objetivoB);

        assertEquals(70, objetivoA.getVidaActual());
        assertEquals(75, objetivoB.getVidaActual());
    }

    @Test
    public void mismaTecnicaDaniaMenosSiElObjetivoEsUnMonje() {
        Combatiente monje = new MonjeDelCuerpoCelestial(10, "Kael");
        new CorteDeLosMilAmaneceres().ejecutar(new HechiceroDelVacio(1, "Nebula"), monje);
        assertEquals(81, monje.getVidaActual());
    }

    @Test
    public void elHechiceroGastaMenosEnergiaAlEjecutarLaMismaTecnica() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula");
        Combatiente cultivador = new CultivadorDeLaEspada(2, "Aurelio");

        hechicero.gastarEnergia(corte.getCosto());
        cultivador.gastarEnergia(corte.getCosto());

        assertEquals(33, hechicero.getEnergiaActual());
        assertEquals(30, cultivador.getEnergiaActual());
    }

    
    @Test
    public void noSeEjecutaUnaTecnicaSinEnergiaSuficiente() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        Tecnica corte = new CorteDeLosMilAmaneceres();
        cultivador.agregarTecnica(corte);
        cultivador.gastarEnergia(35);

        assertTrue(cultivador.tieneUsosRestantes(corte));
        assertFalse(cultivador.puedeEjecutarTecnica(corte, null));
        assertNull(cultivador.decidirAccion());
    }

    @Test
    public void noSeEjecutaUnaTecnicaSinUsosAunqueHayaEnergia() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        Tecnica corte = new CorteDeLosMilAmaneceres();
        cultivador.agregarTecnica(corte);
        for (int i = 0; i < 5; i++) {
            cultivador.registrarUsoTecnica(corte);
        }

        assertEquals(cultivador.getEnergiaMaxima(), cultivador.getEnergiaActual());
        assertFalse(cultivador.puedeEjecutarTecnica(corte, null));
        assertNull(cultivador.decidirAccion());
    }

    @Test
    public void losUsosSeConservanEntreCombates() {
        Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio");
        Combatiente nebula = new HechiceroDelVacio(2, "Nebula");
        Combatiente kael = new MonjeDelCuerpoCelestial(3, "Kael");
        Combatiente darian = new MonjeDelCuerpoCelestial(4, "Darian");
        Pareja pareja1 = new Pareja(1, aurelio, nebula);
        Pareja pareja2 = new Pareja(2, kael, darian);
        Tecnica corte = new CorteDeLosMilAmaneceres();
        aurelio.agregarTecnica(corte);

        new Combate(pareja1, pareja2, 3, 10).ejecutarCombate();
        assertEquals(2, aurelio.getUsosRestantes(corte));

        new Combate(pareja1, pareja2, 3, 10).ejecutarCombate();
        assertEquals(0, aurelio.getUsosRestantes(corte));
    }
}