package unlam.grupo_xi.tp2.equipamiento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.combatiente.CreadorCombatientes;
import unlam.grupo_xi.tp2.combatiente.CultivadorDeLaEspada;
import unlam.grupo_xi.tp2.escuela.Escuela;
import unlam.grupo_xi.tp2.tecnica.CorteDeLosMilAmaneceres;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EquipamientoTest {

    private Combatiente combatiente;
    private Equipamiento espada;
    private Equipamiento armadura;
    private Equipamiento anillo;
    private Equipamiento sello;

    @BeforeEach
    public void setUp() {
        combatiente = new CultivadorDeLaEspada(1, "Aurelio");
        espada = new EspadaCelestial();
        armadura = new ArmaduraDorada();
        anillo = new AnilloEspiritual();
        sello = new SelloDelFenix();
    }

    @Test
    public void combatientePuedeLlevarHastaTresEquipamientosDistintos() {
        assertTrue(combatiente.equipar(espada));
        assertTrue(combatiente.equipar(armadura));
        assertTrue(combatiente.equipar(anillo));

        assertEquals(3, combatiente.getEquipamientos().size());

        // Intentar equipar un cuarto objeto se rechaza sin cambios
        assertFalse(combatiente.equipar(sello));
        assertEquals(3, combatiente.getEquipamientos().size());
    }

    @Test
    public void rechazarEquipamientoDeCategoriaRepetida() {
        assertTrue(combatiente.equipar(espada)); // Categoría ARMA

        Equipamiento otraEspada = new EspadaCelestial();
        // Repetir categoría ARMA debe ser rechazado
        assertFalse(combatiente.equipar(otraEspada));
        assertEquals(1, combatiente.getEquipamientos().size());
    }

    @Test
    public void equiparObjetoAumentaMaximoPeroNoAumentaValorActual() {
        int vidaMaximaOriginal = combatiente.getVidaMaxima(); // 100
        int vidaActualOriginal = combatiente.getVidaActual(); // 100

        assertTrue(combatiente.equipar(sello)); // Sello del Fenix da +30 de vida máxima

        assertEquals(vidaMaximaOriginal + 30, combatiente.getVidaMaxima()); // 130
        assertEquals(vidaActualOriginal, combatiente.getVidaActual()); // Sigue en 100
    }

    @Test
    public void alDesequiparObjetoElValorActualSeRecortaAlNuevoMaximoSiLoSupera() {
        combatiente.equipar(sello); // Vida máxima sube a 130
        combatiente.recuperarVida(30); // Llevamos vida actual al nuevo tope (130)
        assertEquals(130, combatiente.getVidaActual());

        // Quitamos el sello (vida máxima vuelve a 100)
        combatiente.desequipar(sello);
        assertEquals(100, combatiente.getVidaMaxima());

        // La vida actual debe recortarse a 100
        assertEquals(100, combatiente.getVidaActual());
    }

    @Test
    public void espadaCelestialAumentaDanioProducido() {
        int danioBase = 50;
        int danioSinEspada = combatiente.calcularDanioProducido(danioBase);

        combatiente.equipar(espada); // +10 daño
        int danioConEspada = combatiente.calcularDanioProducido(danioBase);

        assertEquals(danioSinEspada + EspadaCelestial.BONUS_DANIO, danioConEspada);
    }

    @Test
    public void armaduraDoradaReduceDanioRecibido() {
        int danioEntrante = 50;
        int danioSinArmadura = combatiente.calcularDanioRecibido(danioEntrante);

        combatiente.equipar(armadura); // -10 daño
        int danioConArmadura = combatiente.calcularDanioRecibido(danioEntrante);

        assertEquals(danioSinArmadura - ArmaduraDorada.REDUCCION_DANIO, danioConArmadura);
    }

    @Test
    public void anilloEspiritualAumentaEnergiaMaxima() {
        int energiaMaxOriginal = combatiente.getEnergiaMaxima();
        combatiente.equipar(anillo);

        assertEquals(energiaMaxOriginal + AnilloEspiritual.BONUS_ENERGIA, combatiente.getEnergiaMaxima());
    }

    @Test
    public void creadorCombatientesPreparaSegunEscuelaPorNombre() {
        Escuela escuelaEspada = new Escuela(
                "Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres()),
                List.of(espada)
        );

        CreadorCombatientes creador = new CreadorCombatientes();
        Combatiente cultivador = creador.crear("Cultivador de la Espada", "Aurelio", escuelaEspada);

        assertEquals(1, cultivador.getId());
        assertEquals("Aurelio", cultivador.getNombre());
        assertEquals(1, cultivador.getTecnicas().size());
        assertEquals(1, cultivador.getEquipamientos().size());
        assertTrue(cultivador.getEquipamientos().contains(espada));
    }
}
