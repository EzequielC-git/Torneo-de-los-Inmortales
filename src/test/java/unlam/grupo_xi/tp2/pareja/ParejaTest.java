package unlam.grupo_xi.tp2.pareja;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.combatiente.CultivadorDeLaEspada;
import unlam.grupo_xi.tp2.combatiente.HechiceroDelVacio;
import unlam.grupo_xi.tp2.tecnica.CorteDeLosMilAmaneceres;
import unlam.grupo_xi.tp2.tecnica.Tecnica;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParejaTest {

    private Combatiente combatiente1;
    private Combatiente combatiente2;
    private Pareja pareja;
    private Tecnica corte;

    @BeforeEach
    public void setUp() {
        combatiente1 = new CultivadorDeLaEspada(1, "Aurelio");
        combatiente2 = new HechiceroDelVacio(2, "Nebula");
        pareja = new Pareja(1, combatiente1, combatiente2);
        corte = new  CorteDeLosMilAmaneceres();

        combatiente1.agregarTecnica(corte);
        combatiente2.agregarTecnica(corte);
    }

    @Test
    public void crearParejaAsignaCorrectamenteIdentificadorEIntegrantes() {
        assertEquals(1, pareja.getIdentificador());
        assertEquals(1, pareja.getId());
        assertEquals(combatiente1, pareja.getIntegrante1());
        assertEquals(combatiente2, pareja.getIntegrante2());
        assertTrue(pareja.contiene(combatiente1));
        assertTrue(pareja.contiene(combatiente2));
        assertEquals(combatiente2, pareja.getCompaniero(combatiente1));
        assertEquals(combatiente1, pareja.getCompaniero(combatiente2));
    }

    @Test
    public void crearParejaConIntegrantesNulosLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Pareja(1, null, combatiente2));
        assertThrows(IllegalArgumentException.class, () -> new Pareja(1, combatiente1, null));
    }

    @Test
    public void crearParejaConMismoCombatienteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Pareja(1, combatiente1, combatiente1));
    }

    @Test
    public void getIntegrantesEnPieRetornaSoloLosVivos() {
        assertEquals(2, pareja.getIntegrantesEnPie().size());

        // Dañamos a uno hasta dejarlo fuera de combate
        combatiente1.recibirDanio(combatiente1.getVidaMaxima());
        assertFalse(combatiente1.estaEnPie());

        List<Combatiente> enPie = pareja.getIntegrantesEnPie();
        assertEquals(1, enPie.size());
        assertEquals(combatiente2, enPie.get(0));

        // Dañamos al segundo
        combatiente2.recibirDanio(combatiente2.getVidaMaxima());
        assertTrue(pareja.getIntegrantesEnPie().isEmpty());
    }

    @Test
    public void getVidaTotalSumaVidaDeIntegrantesEnPieYCaidosSumanCero() {
        assertEquals(200, pareja.getVidaTotal()); // 100 + 100

        combatiente1.recibirDanio(40); // queda en 60
        assertEquals(160, pareja.getVidaTotal()); // 60 + 100

        combatiente1.recibirDanio(100); // cae a 0
        assertEquals(100, pareja.getVidaTotal()); // 0 + 100
    }

    @Test
    public void estaDerrotadaRetornaTrueSoloCuandoAmbosEstanCaidos() {
        assertFalse(pareja.estaDerrotada());

        combatiente1.recibirDanio(100);
        assertFalse(pareja.estaDerrotada());

        combatiente2.recibirDanio(100);
        assertTrue(pareja.estaDerrotada());
    }

    @Test
    public void integrantesOrdenadosParaElTurnoOrdenaPorMenorIdentificador() {
        // Creamos una pareja donde el primer parámetro tiene ID mayor
        Combatiente idCinco = new CultivadorDeLaEspada(5, "Mayor");
        Combatiente idDos = new HechiceroDelVacio(2, "Menor");
        Pareja parejaDesordenada = new Pareja(10, idCinco, idDos);

        List<Combatiente> turno = parejaDesordenada.getIntegrantesOrdenadosParaElTurno();
        assertEquals(2, turno.size());
        assertEquals(2, turno.get(0).getId());
        assertEquals(5, turno.get(1).getId());
    }

    @Test
    public void integrantesOrdenadosParaElTurnoExcluyeIntegrantesCaidos() {
        combatiente1.recibirDanio(combatiente1.getVidaMaxima()); // Caído el ID 1

        List<Combatiente> turno = pareja.getIntegrantesOrdenadosParaElTurno();
        assertEquals(1, turno.size());
        assertEquals(combatiente2.getId(), turno.get(0).getId());
    }

    // REGLA DEL COMPAÑERO:
    // "cuando un integrante usa una técnica en un combate, su compañero no puede usarla
    // mientras el primero conserve usos de ella y siga en pie; si los agota o queda fuera
    // de combate, el compañero puede usarla. La restricción termina con el combate."

    @Test
    public void companieroNoPuedeUsarTecnicaMientrasElPrimeroConserveUsosYSigaEnPie() {
        // Inicialmente ambos pueden usarla
        assertTrue(pareja.puedeUsarTecnica(corte, combatiente1));
        assertTrue(pareja.puedeUsarTecnica(corte, combatiente2));

        // Combatiente 1 usa la técnica
        pareja.registrarUsoTecnica(corte, combatiente1);
        combatiente1.registrarUsoTecnica(corte); // le quedan 4 usos

        // Combatiente 1 sí puede seguir usándola
        assertTrue(pareja.puedeUsarTecnica(corte, combatiente1));

        // Su compañero (combatiente 2) NO puede usarla
        assertFalse(pareja.puedeUsarTecnica(corte, combatiente2));
    }

    @Test
    public void companieroPuedeUsarTecnicaSiElPrimeroAgotoSusUsos() {
        // Combatiente 1 registra uso y agota sus 5 usos
        pareja.registrarUsoTecnica(corte, combatiente1);
        for (int i = 0; i < 5; i++) {
            combatiente1.registrarUsoTecnica(corte);
        }
        assertEquals(0, combatiente1.getUsosRestantes(corte));

        // Combatiente 1 ya no puede usarla (por falta de usos)
        assertFalse(pareja.puedeUsarTecnica(corte, combatiente1));

        // Su compañero ahora SÍ puede usarla porque el primero agotó sus usos
        assertTrue(pareja.puedeUsarTecnica(corte, combatiente2));
    }

    @Test
    public void companieroPuedeUsarTecnicaSiElPrimeroQuedaFueraDeCombate() {
        // Combatiente 1 registra uso y le quedan usos
        pareja.registrarUsoTecnica(corte, combatiente1);
        combatiente1.registrarUsoTecnica(corte); // le quedan 4 usos

        // Bloqueado para el compañero
        assertFalse(pareja.puedeUsarTecnica(corte, combatiente2));

        // Combatiente 1 es derrotado
        combatiente1.recibirDanio(combatiente1.getVidaMaxima());
        assertFalse(combatiente1.estaEnPie());

        // Su compañero ahora SÍ puede usarla porque el primero cayó
        assertTrue(pareja.puedeUsarTecnica(corte, combatiente2));
    }

    @Test
    public void restriccionDelCompanieroTerminaConElCombate() {
        pareja.registrarUsoTecnica(corte, combatiente1);
        combatiente1.registrarUsoTecnica(corte);

        // Bloqueado para el compañero en este combate
        assertFalse(pareja.puedeUsarTecnica(corte, combatiente2));

        // Finaliza el combate y se reinicia para el siguiente
        pareja.reiniciarParaNuevoCombate();

        // En el nuevo combate, ambos pueden volver a usarla
        assertTrue(pareja.puedeUsarTecnica(corte, combatiente1));
        assertTrue(pareja.puedeUsarTecnica(corte, combatiente2));
    }

    @Test
    public void dosParejasSonIgualesSiTienenElMismoIdentificador() {
        Pareja pareja1 = new Pareja(10, combatiente1, combatiente2);
        Combatiente otro1 = new CultivadorDeLaEspada(3, "Otro1");
        Combatiente otro2 = new HechiceroDelVacio(4, "Otro2");
        Pareja pareja2 = new Pareja(10, otro1, otro2);
        Pareja pareja3 = new Pareja(20, combatiente1, combatiente2);

        assertEquals(pareja1, pareja2);
        assertNotEquals(pareja1, pareja3);
        assertEquals(pareja1.hashCode(), pareja2.hashCode());
    }
}
