package unlam.grupo_xi.tp2.combatiente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unlam.grupo_xi.tp2.TorneoDeLosInmortales;
import unlam.grupo_xi.tp2.tecnica.Tecnica;
import static org.junit.jupiter.api.Assertions.*;

public class CombatienteTest {

    @BeforeEach
    public void setUp() {
        TorneoDeLosInmortales.SIGUIENTE_ID = 1;
    }

    // Verifica que Cultivador de la Espada potencie el daño base (20% adicional)
    @Test
    public void cultivadorDeLaEspadaPotenciaDanioEmitido() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        int danioBase = 100;

        int danioResultante = cultivador.calcularDanioProducido(danioBase);

        assertEquals(120, danioResultante);
    }

    // Verifica que Monje del Cuerpo Celestial reduzca el daño recibido (25% de mitigación)
    @Test
    public void monjeDelCuerpoCelestialReduceDanioRecibido() {
        Combatiente monje = new MonjeDelCuerpoCelestial(1, "Kael");
        int danioEntrante = 100;

        int danioRecibido = monje.calcularDanioRecibido(danioEntrante);

        assertEquals(75, danioRecibido);
    }

    // Verifica que Hechicero del Vacio consuma menos energía al ejecutar técnicas (30% de ahorro)
    @Test
    public void hechiceroDelVacioReduceCostoEnergia() {
        Combatiente hechicero = new HechiceroDelVacio(1, "Nebula");
        int costoBase = 20;

        int costoFinal = hechicero.calcularGastoEnergia(costoBase);

        assertEquals(14, costoFinal);
    }

    // Verifica que el daño reduzca la vida y que no sea inferior a cero
    @Test
    public void recibirDanioReduceVidaHastaCeroYQuedaFueraDeCombate() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio");

        cultivador.recibirDanio(40);
        assertEquals(60, cultivador.getVidaActual());
        assertTrue(cultivador.estaEnPie());

        cultivador.recibirDanio(100);
        assertEquals(0, cultivador.getVidaActual());
        assertFalse(cultivador.estaEnPie());

        // Un combatiente caído no debe recibir más daño ni alterar su estado
        cultivador.recibirDanio(50);
        assertEquals(0, cultivador.getVidaActual());
    }

    // Verifica que la recuperación de vida y energía no supere los máximos configurados
    @Test
    public void recuperacionDeVidaYEnergiaNoSuperaMaximos() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio");

        cultivador.recibirDanio(30);
        cultivador.gastarEnergia(20);

        cultivador.recuperarVida(50);
        cultivador.recuperarEnergia(50);

        assertEquals(cultivador.getVidaMaxima(), cultivador.getVidaActual());
        assertEquals(cultivador.getEnergiaMaxima(), cultivador.getEnergiaActual());
    }

    // Verifica el restablecimiento de estadísticas para un nuevo combate
    @Test
    public void restablecerParaNuevoCombateRestauraVidaYEnergiaAlMaximo() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio");

        cultivador.recibirDanio(100);
        cultivador.gastarEnergia(40);
        assertFalse(cultivador.estaEnPie());

        cultivador.restablecerParaNuevoCombate();

        assertEquals(100, cultivador.getVidaActual());
        assertEquals(40, cultivador.getEnergiaActual());
        assertTrue(cultivador.estaEnPie());
    }

    // Verifica que la igualdad dependa exclusivamente del identificador inmutable
    @Test
    public void dosCombatientesSonIgualesSiTienenMismoIdSinImportarNombreONaturaleza() {
        Combatiente combatiente1 = new CultivadorDeLaEspada(10, "Aurelio");
        Combatiente combatiente2 = new HechiceroDelVacio(10, "Darian");
        Combatiente combatiente3 = new CultivadorDeLaEspada(11, "Aurelio");

        assertEquals(combatiente1, combatiente2);
        assertNotEquals(combatiente1, combatiente3);
        assertEquals(combatiente1.hashCode(), combatiente2.hashCode());
    }

    // Verifica la regla de los 5 usos iniciales por técnica
    @Test
    public void tecnicaIniciaConCincoUsosYSeAgotaAlConsumirlos() {
        Combatiente cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        Tecnica tecnica = new Tecnica();

        cultivador.agregarTecnica(tecnica);

        assertEquals(5, cultivador.getUsosRestantes(tecnica));
        assertTrue(cultivador.tieneUsosRestantes(tecnica));

        for (int i = 0; i < 5; i++)
            cultivador.registrarUsoTecnica(tecnica);

        assertEquals(0, cultivador.getUsosRestantes(tecnica));
        assertFalse(cultivador.tieneUsosRestantes(tecnica));
    }

    // Verifica que CreadorCombatientes asigne IDs consecutivos de forma centralizada
    @Test
    public void creadorCombatientesAsignaIdentificadoresConsecutivos() {
        CreadorCombatientes creador = new CreadorCombatientes();

        Combatiente primero = creador.crear(CategoriaCombatiente.CULTIVADOR_DE_LA_ESPADA, "Aurelio", null);
        Combatiente segundo = creador.crear(CategoriaCombatiente.MONJE_DEL_CUERPO_CELESTIAL, "Kael", null);
        Combatiente tercero = creador.crear(CategoriaCombatiente.HECHICERO_DEL_VACIO, "Nebula", null);

        assertEquals(1, primero.getId());
        assertEquals(2, segundo.getId());
        assertEquals(3, tercero.getId());
    }
}