package unlam.grupo_xi.tp2.combate;

import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.observador.*;
import unlam.grupo_xi.tp2.pareja.Pareja;
import unlam.grupo_xi.tp2.tecnica.Tecnica;

import java.util.ArrayList;
import java.util.List;


public class Combate {
    public static final int ASALTOS_MAXIMOS_DEFAULT = 20;
    public static final int ENERGIA_POR_ASALTO_DEFAULT = 10;

    private final Pareja pareja1;
    private final Pareja pareja2;
    private int asaltoActual;
    private final int asaltosMaximos;
    private final int energiaPorAsalto;
    private final int ronda;
    private final List<ObservadorTorneo> observadores;

    public Combate(Pareja pareja1, Pareja pareja2) {
        this(pareja1, pareja2, ASALTOS_MAXIMOS_DEFAULT, ENERGIA_POR_ASALTO_DEFAULT, 1);
    }

    public Combate(Pareja pareja1, Pareja pareja2, int asaltosMaximos, int energiaPorAsalto) {
        this(pareja1, pareja2, asaltosMaximos, energiaPorAsalto, 1);
    }

    public Combate(Pareja pareja1, Pareja pareja2, int asaltosMaximos, int energiaPorAsalto, int ronda) {
        if (pareja1 == null || pareja2 == null) {
            throw new IllegalArgumentException("Las parejas participantes no pueden ser nulas.");
        }
        if (pareja1.equals(pareja2)) {
            throw new IllegalArgumentException("Un combate no puede enfrentar a la misma pareja.");
        }
        this.pareja1 = pareja1;
        this.pareja2 = pareja2;
        this.asaltosMaximos = asaltosMaximos > 0 ? asaltosMaximos : ASALTOS_MAXIMOS_DEFAULT;
        this.energiaPorAsalto = energiaPorAsalto >= 0 ? energiaPorAsalto : ENERGIA_POR_ASALTO_DEFAULT;
        this.ronda = ronda;
        this.asaltoActual = 1;
        this.observadores = new ArrayList<>();
    }

    public Pareja getPareja1() {
        return pareja1;
    }

    public Pareja getPareja2() {
        return pareja2;
    }

    public int getAsaltoActual() {
        return asaltoActual;
    }

    public int getAsaltosMaximos() {
        return asaltosMaximos;
    }

    public int getEnergiaPorAsalto() {
        return energiaPorAsalto;
    }

    public int getRonda() {
        return ronda;
    }

    public void agregarObservador(ObservadorTorneo observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void notificarAcontecimiento(Acontecimiento evento) {
        if (evento != null) {
            for (ObservadorTorneo obs : observadores) {
                obs.actualizar(evento);
            }
        }
    }

    /**
     * Desarrolla el combate en asaltos y turnos hasta que una pareja quede derrotada
     * o se alcance el límite de asaltos.
     * Retorna la pareja ganadora.
     */
    public Pareja ejecutarCombate() {
        prepararCombatientesParaNuevoCombate();

        notificarAcontecimiento(new AcontecimientoBasico(
                TipoAcontecimiento.INICIO_COMBATE,
                "Comienza el combate de Ronda " + ronda + " entre " + pareja1 + " y " + pareja2,
                ronda
        ));

        // Empieza la pareja de menor identificador
        Pareja primera = (pareja1.getId() < pareja2.getId()) ? pareja1 : pareja2;
        Pareja segunda = (primera == pareja1) ? pareja2 : pareja1;

        for (asaltoActual = 1; asaltoActual <= asaltosMaximos; asaltoActual++) {
            // Al comenzar cada asalto, cada combatiente en pie recupera una cantidad fija de energía
            recuperarEnergiaAlComienzoDelAsalto();

            // Turno de la primera pareja
            ejecutarTurnoPareja(primera, segunda);
            if (segunda.estaDerrotada()) {
                break;
            }

            // Turno de la segunda pareja
            ejecutarTurnoPareja(segunda, primera);
            if (primera.estaDerrotada()) {
                break;
            }
        }

        Pareja ganadora = determinarParejaGanadora(primera, segunda);
        Pareja perdedora = ganadora.equals(pareja1) ? pareja2 : pareja1;

        notificarAcontecimiento(new AcontecimientoResultado(
                "Fin del combate en asalto " + Math.min(asaltoActual, asaltosMaximos) + ". Ganadora: " + ganadora
                        + " con vida total " + ganadora.getVidaTotal() + " vs " + perdedora + " con vida " + perdedora.getVidaTotal(),
                ronda,
                ganadora,
                ganadora.getVidaTotal(),
                perdedora,
                perdedora.getVidaTotal()
        ));

        return ganadora;
    }

    private void prepararCombatientesParaNuevoCombate() {
        // Al comenzar cada combate, todos los integrantes recuperan vida y energía al máximo
        pareja1.getIntegrante1().restablecerParaNuevoCombate();
        pareja1.getIntegrante2().restablecerParaNuevoCombate();
        pareja2.getIntegrante1().restablecerParaNuevoCombate();
        pareja2.getIntegrante2().restablecerParaNuevoCombate();

        // Se reinician las restricciones de técnicas y muros del combate
        pareja1.reiniciarParaNuevoCombate();
        pareja2.reiniciarParaNuevoCombate();
        pareja1.getIntegrante1().reiniciarDefensa();
        pareja1.getIntegrante2().reiniciarDefensa();
        pareja2.getIntegrante1().reiniciarDefensa();
        pareja2.getIntegrante2().reiniciarDefensa();
    }

    private void recuperarEnergiaAlComienzoDelAsalto() {
        for (Combatiente c : pareja1.getIntegrantesEnPie()) {
            c.recuperarEnergiaPorAsalto(energiaPorAsalto);
        }
        for (Combatiente c : pareja2.getIntegrantesEnPie()) {
            c.recuperarEnergiaPorAsalto(energiaPorAsalto);
        }
    }

    /**
     * En cada asalto, cada pareja juega su turno completo: cada integrante en pie realiza una acción.
     * Dentro de la pareja actúa primero el de menor identificador.
     */
    private void ejecutarTurnoPareja(Pareja atacante, Pareja defensora) {
        List<Combatiente> integrantesEnPie = atacante.getIntegrantesOrdenadosParaElTurno();

        for (Combatiente combatiente : integrantesEnPie) {
            // Si el combatiente cayó durante el turno o la pareja rival ya fue derrotada, no actúa
            if (!combatiente.estaEnPie() || defensora.estaDerrotada()) {
                break;
            }

            ejecutarAccionCombatiente(combatiente, atacante, defensora);
        }
    }

    private void ejecutarAccionCombatiente(Combatiente ejecutor, Pareja parejaEjecutor, Pareja parejaRival) {
        Combatiente objetivoOfensivo = ejecutor.elegirObjetivo(parejaRival.getIntegrantesEnPie());
        if (objetivoOfensivo == null && !parejaRival.estaDerrotada()) {
            return;
        }

        Tecnica tecnicaElegida = ejecutor.decidirAccion(parejaEjecutor);

        if (tecnicaElegida != null) {
            // Las técnicas defensivas y de recuperación se aplican al ejecutor
            Combatiente objetivoFinal = (tecnicaElegida.esCuracion() || tecnicaElegida.esDefensiva())
                    ? ejecutor
                    : objetivoOfensivo;

            // Ejecución de la técnica
            ejecutor.gastarEnergia(tecnicaElegida.getCosto());
            ejecutor.registrarUsoTecnica(tecnicaElegida);
            parejaEjecutor.registrarUsoTecnica(tecnicaElegida, ejecutor);
            tecnicaElegida.ejecutar(ejecutor, objetivoFinal);

            notificarAcontecimiento(new AcontecimientoAccion(
                    ejecutor.getNombre() + " (ID: " + ejecutor.getId() + ") ejecutó " + tecnicaElegida.getNombre()
                            + " sobre " + objetivoFinal.getNombre() + " (ID: " + objetivoFinal.getId() + ")",
                    ronda,
                    ejecutor,
                    objetivoFinal
            ));

            if (objetivoFinal != ejecutor && !objetivoFinal.estaEnPie()) {
                notificarAcontecimiento(new AcontecimientoEliminacion(
                        objetivoFinal.getNombre() + " (ID: " + objetivoFinal.getId() + ") ha quedado fuera de combate.",
                        ronda,
                        objetivoFinal
                ));
            }
        } else {
            ejecutor.realizarAtaqueBasico(objetivoOfensivo);

            notificarAcontecimiento(new AcontecimientoAccion(
                    ejecutor.getNombre() + " (ID: " + ejecutor.getId() + ") realizó un ataque básico sobre "
                            + objetivoOfensivo.getNombre() + " (ID: " + objetivoOfensivo.getId() + ")",
                    ronda,
                    ejecutor,
                    objetivoOfensivo
            ));

            if (!objetivoOfensivo.estaEnPie()) {
                notificarAcontecimiento(new AcontecimientoEliminacion(
                        objetivoOfensivo.getNombre() + " (ID: " + objetivoOfensivo.getId() + ") ha quedado fuera de combate.",
                        ronda,
                        objetivoOfensivo
                ));
            }
        }
    }

    /**
     * Determina el ganador del combate:
     * 1. Si una pareja quedó derrotada (ambos caídos), gana la otra.
     * 2. Si se alcanzó el límite de asaltos, gana la que tenga más vida total.
     * 3. En caso de empate en vida total, gana la de menor identificador.
     */
    private Pareja determinarParejaGanadora(Pareja primera, Pareja segunda) {
        if (primera.estaDerrotada()) {
            return segunda;
        }
        if (segunda.estaDerrotada()) {
            return primera;
        }

        // Límite de asaltos alcanzado: desempate por vida total
        int vida1 = pareja1.getVidaTotal();
        int vida2 = pareja2.getVidaTotal();

        if (vida1 > vida2) {
            return pareja1;
        } else if (vida2 > vida1) {	
            return pareja2;
        }

        // Empate en vida total: desempate por menor identificador
        return (pareja1.getId() < pareja2.getId()) ? pareja1 : pareja2;
    }
}
