package unlam.grupo_xi.tp2.torneo;

import unlam.grupo_xi.tp2.combate.Combate;
import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.observador.Acontecimiento;
import unlam.grupo_xi.tp2.observador.AcontecimientoBasico;
import unlam.grupo_xi.tp2.observador.ObservadorTorneo;
import unlam.grupo_xi.tp2.observador.TipoAcontecimiento;
import unlam.grupo_xi.tp2.pareja.InscripcionInvalidaException;
import unlam.grupo_xi.tp2.pareja.Pareja;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Torneo eliminatorio por parejas (4 u 8).
 * Ronda 1: cruces por orden de inscripción. Rondas siguientes: por el orden de los cruces anteriores.
 */
public class Torneo {
    private static final int MAXIMO_PAREJAS = 8;

    private final List<Pareja> parejasInscriptas;// conserva el orden de inscripción
    private final Set<Combatiente> combatientesInscriptos;// evita duplicados
    private final List<ObservadorTorneo> observadores;
    private final int asaltosMaximos;
    private final int energiaPorAsalto;

    private Pareja campeona;
    private int cantidadDeRondas;

    public Torneo() {
        this(Combate.ASALTOS_MAXIMOS_DEFAULT, Combate.ENERGIA_POR_ASALTO_DEFAULT);
    }

    public Torneo(int asaltosMaximos, int energiaPorAsalto) {
        this.parejasInscriptas = new ArrayList<>();
        this.combatientesInscriptos = new HashSet<>();
        this.observadores = new ArrayList<>();
        this.asaltosMaximos = asaltosMaximos;
        this.energiaPorAsalto = energiaPorAsalto;
    }

    public void agregarObservador(ObservadorTorneo observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void inscribir(Pareja pareja) {
        if (pareja == null) {
            throw new IllegalArgumentException("La pareja no puede ser nula.");
        }
        if (campeona != null) {
            throw new IllegalStateException("El torneo ya se jugó.");
        }
        if (parejasInscriptas.contains(pareja)) {
            throw new InscripcionInvalidaException("La pareja #" + pareja.getId() + " ya está inscripta.");
        }
        validarNoInscripto(pareja.getIntegrante1());
        validarNoInscripto(pareja.getIntegrante2());
        if (parejasInscriptas.size() == MAXIMO_PAREJAS) {
            throw new InscripcionInvalidaException("El torneo admite como máximo " + MAXIMO_PAREJAS + " parejas.");
        }

        parejasInscriptas.add(pareja);
        combatientesInscriptos.add(pareja.getIntegrante1());
        combatientesInscriptos.add(pareja.getIntegrante2());
    }

    private void validarNoInscripto(Combatiente c) {
        if (combatientesInscriptos.contains(c)) {
            throw new InscripcionInvalidaException(c.getNombre() + " (ID: " + c.getId() + ") ya está inscripto.");
        }
    }

    public Pareja iniciar() {
        if (campeona != null) {
            throw new IllegalStateException("El torneo ya se jugó.");
        }
        int cantidad = parejasInscriptas.size();
        if (cantidad != 4 && cantidad != 8) {
            throw new IllegalStateException("El torneo se juega con 4 u 8 parejas (hay " + cantidad + ").");
        }

        List<Pareja> enCompetencia = new ArrayList<>(parejasInscriptas);
        int ronda = 0;
        while (enCompetencia.size() > 1) {
            ronda++;
            enCompetencia = jugarRonda(enCompetencia, ronda);
        }

        cantidadDeRondas = ronda;
        campeona = enCompetencia.get(0);

        notificar(new AcontecimientoBasico(TipoAcontecimiento.PROCLAMACION,
                "Los Dos Santos Supremos son " + campeona.getIntegrante1().getNombre() + " y "
                        + campeona.getIntegrante2().getNombre() + " (" + campeona + ")",
                ronda));

        return campeona;
    }

    // Cruces de a pares; las ganadoras quedan en el mismo orden para la ronda siguiente.
    private List<Pareja> jugarRonda(List<Pareja> parejas, int ronda) {
        List<Pareja> ganadoras = new ArrayList<>();
        for (int i = 0; i < parejas.size(); i += 2) {
            Combate combate = new Combate(parejas.get(i), parejas.get(i + 1), asaltosMaximos, energiaPorAsalto, ronda);
            for (ObservadorTorneo o : observadores) {
                combate.agregarObservador(o);
            }
            ganadoras.add(combate.ejecutarCombate());
        }
        return ganadoras;
    }

    private void notificar(Acontecimiento a) {
        for (ObservadorTorneo o : observadores) {
            o.actualizar(a);
        }
    }

    public List<Pareja> getParejasInscriptas() {
        return List.copyOf(parejasInscriptas);
    }

    public Pareja getCampeona() {
        return campeona;
    }

    public int getCantidadDeRondas() {
        return cantidadDeRondas;
    }
}