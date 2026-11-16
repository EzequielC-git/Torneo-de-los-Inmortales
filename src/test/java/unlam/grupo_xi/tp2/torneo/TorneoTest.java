package unlam.grupo_xi.tp2.torneo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.combatiente.CreadorCombatientes;
import unlam.grupo_xi.tp2.equipamiento.*;
import unlam.grupo_xi.tp2.escuela.Escuela;
import unlam.grupo_xi.tp2.observador.*;
import unlam.grupo_xi.tp2.pareja.InscripcionInvalidaException;
import unlam.grupo_xi.tp2.pareja.Pareja;
import unlam.grupo_xi.tp2.tecnica.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TorneoTest {

    private CreadorCombatientes creador;
    private Escuela espada, vacio, monasterio;

    @BeforeEach
    public void setUp() {
        creador = new CreadorCombatientes();
        espada = new Escuela("Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres(), new RugidoDeLaBestiaPrimordial()),
                List.of(new EspadaCelestial()));
        vacio = new Escuela("Palacio del Vacío",
                List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
                List.of(new AnilloEspiritual()));
        monasterio = new Escuela("Monasterio del Cuerpo Dorado",
                List.of(new MurodelCuerpoDorado(), new MeditacionDelInmortal(), new CorteDeLosMilAmaneceres()),
                List.of(new ArmaduraDorada(), new SelloDelFenix()));
    }

    private Pareja nuevaPareja(int id) {
        String[] tipos = {"Cultivador de la Espada", "Hechicero del Vacío", "Monje del Cuerpo Celestial"};
        Escuela[] escuelas = {espada, vacio, monasterio};
        Combatiente a = creador.crear(tipos[id % 3], "Combatiente " + id + "A", escuelas[id % 3]);
        Combatiente b = creador.crear(tipos[(id + 1) % 3], "Combatiente " + id + "B", escuelas[(id + 2) % 3]);
        return new Pareja(id, a, b);
    }

    private List<Pareja> nuevasParejas(int cantidad) {
        List<Pareja> parejas = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) parejas.add(nuevaPareja(i));
        return parejas;
    }

    private Torneo torneoCon(List<Pareja> parejas, CronistaDeLaSecta cronista) {
        Torneo torneo = new Torneo();
        torneo.agregarObservador(cronista);
        parejas.forEach(torneo::inscribir);
        return torneo;
    }

    private List<AcontecimientoResultado> resultados(CronistaDeLaSecta cronista) {
        List<AcontecimientoResultado> r = new ArrayList<>();
        for (Acontecimiento a : cronista.getHistorialEventos()) {
            if (a instanceof AcontecimientoResultado res) r.add(res);
        }
        return r;
    }

    private Set<Pareja> participantes(AcontecimientoResultado r) {
        return Set.of(r.getParejaGanadora(), r.getParejaPerdedora());
    }


    @Test
    public void inscribirDosVecesLaMismaParejaSeRechazaSinCambios() {
        Torneo torneo = new Torneo();
        Pareja pareja = nuevaPareja(1);
        torneo.inscribir(pareja);

        assertThrows(InscripcionInvalidaException.class, () -> torneo.inscribir(pareja));
        assertEquals(1, torneo.getParejasInscriptas().size());
    }

    @Test
    public void inscribirUnCombatienteQueYaEstaEnOtraParejaSeRechaza() {
        Torneo torneo = new Torneo();
        Pareja pareja = nuevaPareja(1);
        torneo.inscribir(pareja);
        Combatiente nuevo = creador.crear("Monje del Cuerpo Celestial", "Nuevo", monasterio);

        assertThrows(InscripcionInvalidaException.class,
                () -> torneo.inscribir(new Pareja(2, pareja.getIntegrante1(), nuevo)));
        assertEquals(1, torneo.getParejasInscriptas().size());
    }

    @Test
    public void noSeAdmitenMasDeOchoParejas() {
        Torneo torneo = new Torneo();
        nuevasParejas(8).forEach(torneo::inscribir);

        assertThrows(InscripcionInvalidaException.class, () -> torneo.inscribir(nuevaPareja(9)));
    }

    @Test
    public void elTorneoSoloSeJuegaConCuatroUOchoParejas() {
        for (int cantidad : new int[]{0, 2, 3, 5, 6, 7}) {
            Torneo torneo = new Torneo();
            nuevasParejas(cantidad).forEach(torneo::inscribir);

            assertThrows(IllegalStateException.class, torneo::iniciar, "Con " + cantidad + " parejas");
            assertNull(torneo.getCampeona());
        }
    }

    @Test
    public void noSePuedeInscribirNiVolverAIniciarUnTorneoYaJugado() {
        Torneo torneo = torneoCon(nuevasParejas(4), new CronistaDeLaSecta());
        torneo.iniciar();

        assertThrows(IllegalStateException.class, () -> torneo.inscribir(nuevaPareja(5)));
        assertThrows(IllegalStateException.class, torneo::iniciar);
    }


    @Test
    public void torneoDeCuatroParejasJuegaDosRondasYTresCombates() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Torneo torneo = torneoCon(nuevasParejas(4), cronista);
        torneo.iniciar();

        List<AcontecimientoResultado> r = resultados(cronista);
        assertEquals(3, r.size());
        assertEquals(List.of(1, 1, 2), r.stream().map(Acontecimiento::getRonda).toList());
        assertEquals(2, torneo.getCantidadDeRondas());
    }

    @Test
    public void torneoDeOchoParejasJuegaTresRondasYSieteCombates() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Torneo torneo = torneoCon(nuevasParejas(8), cronista);
        torneo.iniciar();

        List<AcontecimientoResultado> r = resultados(cronista);
        assertEquals(7, r.size());
        assertEquals(List.of(1, 1, 1, 1, 2, 2, 3), r.stream().map(Acontecimiento::getRonda).toList());
        assertEquals(3, torneo.getCantidadDeRondas());
    }

    @Test
    public void losCrucesDeLaPrimeraRondaSiguenElOrdenDeInscripcion() {
        Pareja p1 = nuevaPareja(1), p2 = nuevaPareja(2), p3 = nuevaPareja(3), p4 = nuevaPareja(4);
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneoCon(List.of(p3, p1, p4, p2), cronista).iniciar();

        List<AcontecimientoResultado> r = resultados(cronista);
        assertEquals(Set.of(p3, p1), participantes(r.get(0)));
        assertEquals(Set.of(p4, p2), participantes(r.get(1)));
    }

    @Test
    public void lasRondasSiguientesEnfrentanALasGanadorasEnElOrdenDeLosCruces() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneoCon(nuevasParejas(8), cronista).iniciar();

        List<AcontecimientoResultado> r = resultados(cronista);
        assertEquals(Set.of(r.get(0).getParejaGanadora(), r.get(1).getParejaGanadora()), participantes(r.get(4)));
        assertEquals(Set.of(r.get(2).getParejaGanadora(), r.get(3).getParejaGanadora()), participantes(r.get(5)));
        assertEquals(Set.of(r.get(4).getParejaGanadora(), r.get(5).getParejaGanadora()), participantes(r.get(6)));
    }

    @Test
    public void alTerminarSeProclamaALaParejaGanadoraDeLaFinal() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Torneo torneo = torneoCon(nuevasParejas(4), cronista);
        Pareja campeona = torneo.iniciar();

        List<Acontecimiento> historial = cronista.getHistorialEventos();
        List<AcontecimientoResultado> r = resultados(cronista);
        assertEquals(TipoAcontecimiento.PROCLAMACION, historial.get(historial.size() - 1).getTipoEvento());
        assertEquals(r.get(r.size() - 1).getParejaGanadora(), campeona);
    }

    @Test
    public void laTablaDePosicionesQuedaOrdenadaPorUltimaRondaJugadaYVidaTotalFinal() {
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        List<Pareja> inscriptas = nuevasParejas(4);
        Torneo torneo = torneoCon(inscriptas, cronista);
        Pareja campeona = torneo.iniciar();

        List<Pareja> tabla = cronista.generarTablaPosiciones();
        List<AcontecimientoResultado> r = resultados(cronista);
        AcontecimientoResultado finalTorneo = r.get(2);

        assertEquals(4, tabla.size());
        assertEquals(campeona, tabla.get(0));
        assertEquals(finalTorneo.getParejaPerdedora(), tabla.get(1));

        // Las posiciones 3 y 4 corresponden a las perdedoras de ronda 1, ordenadas por su vida final y luego por ID
        AcontecimientoResultado semi1 = r.get(0);
        AcontecimientoResultado semi2 = r.get(1);
        if (semi1.getVidaTotalPerdedora() > semi2.getVidaTotalPerdedora()
                || (semi1.getVidaTotalPerdedora() == semi2.getVidaTotalPerdedora()
                && semi1.getParejaPerdedora().getId() < semi2.getParejaPerdedora().getId())) {
            assertEquals(semi1.getParejaPerdedora(), tabla.get(2));
            assertEquals(semi2.getParejaPerdedora(), tabla.get(3));
        } else {
            assertEquals(semi2.getParejaPerdedora(), tabla.get(2));
            assertEquals(semi1.getParejaPerdedora(), tabla.get(3));
        }
    }


    @Test
    public void dosTorneosArmadosIgualProducenElMismoCampeonYElMismoHistorial() {
        CronistaDeLaSecta cronista1 = new CronistaDeLaSecta();
        Pareja campeona1 = torneoCon(nuevasParejas(8), cronista1).iniciar();

        setUp();
        CronistaDeLaSecta cronista2 = new CronistaDeLaSecta();
        Pareja campeona2 = torneoCon(nuevasParejas(8), cronista2).iniciar();

        assertEquals(campeona1, campeona2);
        assertEquals(textos(cronista1), textos(cronista2));
    }

    private List<String> textos(CronistaDeLaSecta c) {
        return c.getHistorialEventos().stream().map(Acontecimiento::toString).toList();
    }
}