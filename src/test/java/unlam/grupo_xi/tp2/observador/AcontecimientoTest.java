package unlam.grupo_xi.tp2.observador;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.combatiente.CultivadorDeLaEspada;
import unlam.grupo_xi.tp2.combatiente.HechiceroDelVacio;
import unlam.grupo_xi.tp2.combatiente.MonjeDelCuerpoCelestial;
import unlam.grupo_xi.tp2.pareja.Pareja;

class AcontecimientoTest {

    private Combatiente aurelio;
    private Combatiente nebula;
    private Combatiente kael;
    private Combatiente darian;
    private Pareja pareja1;
    private Pareja pareja2;

    @BeforeEach
    void setUp() {
        aurelio = new CultivadorDeLaEspada(1, "Aurelio");
        nebula = new HechiceroDelVacio(2, "Nébula");
        kael = new MonjeDelCuerpoCelestial(3, "Kael");
        darian = new CultivadorDeLaEspada(4, "Darian");

        pareja1 = new Pareja(1, aurelio, nebula);
        pareja2 = new Pareja(2, kael, darian);
    }

    @Test
    @DisplayName("AcontecimientoBasico guarda tipo, descripción, ronda, momento y formatea toString()")
    void testAcontecimientoBasico() {
        Date antes = new Date();
        Acontecimiento evento = new AcontecimientoBasico(
                TipoAcontecimiento.INICIO_COMBATE,
                "Comienza el combate de Ronda 1",
                1
        );
        Date despues = new Date();

        assertEquals(TipoAcontecimiento.INICIO_COMBATE, evento.getTipoEvento());
        assertEquals("Comienza el combate de Ronda 1", evento.getDescripcion());
        assertEquals(1, evento.getRonda());
        assertNotNull(evento.getMomento());
        assertFalse(evento.getMomento().before(antes));
        assertFalse(evento.getMomento().after(despues));
        assertEquals("[INICIO_COMBATE] Comienza el combate de Ronda 1", evento.toString());
    }

    @Test
    @DisplayName("AcontecimientoAccion asigna ACCION_REALIZADA y guarda ejecutor y objetivo")
    void testAcontecimientoAccion() {
        AcontecimientoAccion accion = new AcontecimientoAccion(
                "Aurelio ejecutó Corte de los Mil Amaneceres sobre Kael",
                1,
                aurelio,
                kael
        );

        assertEquals(TipoAcontecimiento.ACCION_REALIZADA, accion.getTipoEvento());
        assertEquals("Aurelio ejecutó Corte de los Mil Amaneceres sobre Kael", accion.getDescripcion());
        assertEquals(1, accion.getRonda());
        assertEquals(aurelio, accion.getEjecutor());
        assertEquals(kael, accion.getObjetivo());
        assertNotNull(accion.getMomento());
    }

    @Test
    @DisplayName("AcontecimientoAccion admite objetivo nulo para técnicas propias")
    void testAcontecimientoAccionSinObjetivo() {
        AcontecimientoAccion accionPropia = new AcontecimientoAccion(
                "Nébula usó Meditación del Inmortal",
                2,
                nebula,
                null
        );

        assertEquals(TipoAcontecimiento.ACCION_REALIZADA, accionPropia.getTipoEvento());
        assertEquals(nebula, accionPropia.getEjecutor());
        assertNull(accionPropia.getObjetivo());
    }

    @Test
    @DisplayName("AcontecimientoEliminacion asigna ELIMINACION y registra al combatiente caído")
    void testAcontecimientoEliminacion() {
        AcontecimientoEliminacion eliminacion = new AcontecimientoEliminacion(
                "Kael ha quedado fuera de combate",
                1,
                kael
        );

        assertEquals(TipoAcontecimiento.ELIMINACION, eliminacion.getTipoEvento());
        assertEquals("Kael ha quedado fuera de combate", eliminacion.getDescripcion());
        assertEquals(1, eliminacion.getRonda());
        assertEquals(kael, eliminacion.getCaido());
        assertEquals("[ELIMINACION] Kael ha quedado fuera de combate", eliminacion.toString());
    }

    @Test
    @DisplayName("AcontecimientoResultado asigna RESULTADO_COMBATE y almacena parejas y vidas totales")
    void testAcontecimientoResultado() {
        AcontecimientoResultado resultado = new AcontecimientoResultado(
                "Fin del combate en asalto 8",
                2,
                pareja1,
                115,
                pareja2,
                0
        );

        assertEquals(TipoAcontecimiento.RESULTADO_COMBATE, resultado.getTipoEvento());
        assertEquals("Fin del combate en asalto 8", resultado.getDescripcion());
        assertEquals(2, resultado.getRonda());
        assertEquals(pareja1, resultado.getParejaGanadora());
        assertEquals(115, resultado.getVidaTotalGanadora());
        assertEquals(pareja2, resultado.getParejaPerdedora());
        assertEquals(0, resultado.getVidaTotalPerdedora());
    }

    @Test
    @DisplayName("Polimorfismo: todas las subclases pueden tratarse como Acontecimiento")
    void testPolimorfismoAcontecimientos() {
        List<Acontecimiento> eventos = List.of(
                new AcontecimientoBasico(TipoAcontecimiento.INICIO_COMBATE, "Inicio", 1),
                new AcontecimientoAccion("Ataque", 1, aurelio, kael),
                new AcontecimientoEliminacion("Caído", 1, kael),
                new AcontecimientoResultado("Resultado", 1, pareja1, 100, pareja2, 0),
                new AcontecimientoBasico(TipoAcontecimiento.PROCLAMACION, "Campeones", 2)
        );

        assertEquals(5, eventos.size());
        assertInstanceOf(AcontecimientoBasico.class, eventos.get(0));
        assertInstanceOf(AcontecimientoAccion.class, eventos.get(1));
        assertInstanceOf(AcontecimientoEliminacion.class, eventos.get(2));
        assertInstanceOf(AcontecimientoResultado.class, eventos.get(3));
        assertEquals(TipoAcontecimiento.PROCLAMACION, eventos.get(4).getTipoEvento());
    }

    @Test
    @DisplayName("CronistaDeLaSecta guarda todos los acontecimientos en orden cronológico")
    void testCronistaRegistraHistorialCompleto() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();

        Acontecimiento inicio = new AcontecimientoBasico(TipoAcontecimiento.INICIO_COMBATE, "Inicio combate", 1);
        Acontecimiento accion = new AcontecimientoAccion("Aurelio ataca a Kael", 1, aurelio, kael);
        Acontecimiento eliminacion = new AcontecimientoEliminacion("Kael cae", 1, kael);
        Acontecimiento resultado = new AcontecimientoResultado("Gana Pareja 1", 1, pareja1, 90, pareja2, 0);
        Acontecimiento proclamacion = new AcontecimientoBasico(TipoAcontecimiento.PROCLAMACION, "Dos Santos Supremos", 2);

        cronista.actualizar(inicio);
        cronista.actualizar(accion);
        cronista.actualizar(eliminacion);
        cronista.actualizar(resultado);
        cronista.actualizar(proclamacion);

        List<Acontecimiento> historial = cronista.getHistorialEventos();
        assertEquals(5, historial.size());
        assertEquals(List.of(inicio, accion, eliminacion, resultado, proclamacion), historial);
    }

    @Test
    @DisplayName("CronistaDeLaSecta filtra resultados y arma la tabla de posiciones sin duplicados")
    void testCronistaGeneraTablaPosiciones() {
        Combatiente c5 = new CultivadorDeLaEspada(5, "Lyra");
        Combatiente c6 = new MonjeDelCuerpoCelestial(6, "Toren");
        Combatiente c7 = new HechiceroDelVacio(7, "Vesper");
        Combatiente c8 = new CultivadorDeLaEspada(8, "Orin");

        Pareja pareja3 = new Pareja(3, c5, c6);
        Pareja pareja4 = new Pareja(4, c7, c8);

        CronistaDeLaSecta cronista = new CronistaDeLaSecta();

        // Ronda 1 y Cruce 1: Pareja1 le gana a Pareja2
        cronista.actualizar(new AcontecimientoBasico(TipoAcontecimiento.INICIO_COMBATE, "R1 C1", 1));
        cronista.actualizar(new AcontecimientoAccion("Ataque en R1 C1", 1, aurelio, kael));
        cronista.actualizar(new AcontecimientoResultado("Fin R1 C1", 1, pareja1, 80, pareja2, 0));

        // Ronda 1 y Cruce 2: Pareja3 le gana a Pareja4
        cronista.actualizar(new AcontecimientoBasico(TipoAcontecimiento.INICIO_COMBATE, "R1 C2", 1));
        cronista.actualizar(new AcontecimientoResultado("Fin R1 C2", 1, pareja3, 60, pareja4, 0));

        // Ronda 2 - Final: Pareja3 le gana a Pareja1
        cronista.actualizar(new AcontecimientoBasico(TipoAcontecimiento.INICIO_COMBATE, "Final", 2));
        cronista.actualizar(new AcontecimientoResultado("Fin Final", 2, pareja3, 40, pareja1, 0));
        cronista.actualizar(new AcontecimientoBasico(TipoAcontecimiento.PROCLAMACION, "Campeones: Pareja 3", 2));

        List<Pareja> tabla = cronista.generarTablaPosiciones();

        assertEquals(List.of(pareja3, pareja1, pareja2, pareja4), tabla);
    }

    @Test
    @DisplayName("HeraldoImperial relata acontecimientos y presenta la tabla de posiciones")
    void testHeraldoImperialActualizaYPresentaTabla() {
        HeraldoImperial heraldo = new HeraldoImperial();
        assertInstanceOf(ObservadorTorneo.class, heraldo);

        PrintStream salidaOriginal = System.out;
        ByteArrayOutputStream capturaSalida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturaSalida));

        try {
            Acontecimiento evento = new AcontecimientoBasico(
                    TipoAcontecimiento.PROCLAMACION,
                    "Aurelio y Nébula son los Dos Santos Supremos",
                    2
            );
            heraldo.actualizar(evento);
            heraldo.presentarTablaPosiciones(List.of(pareja1, pareja2));
        } finally {
            System.setOut(salidaOriginal);
        }

        String textoImpreso = capturaSalida.toString();
        assertTrue(textoImpreso.contains("[PROCLAMACION] Aurelio y Nébula son los Dos Santos Supremos"));
        assertTrue(textoImpreso.contains("TABLA DE POSICIONES FINAL"));
        assertTrue(textoImpreso.contains("1. " + pareja1));
        assertTrue(textoImpreso.contains("2. " + pareja2));
    }
}