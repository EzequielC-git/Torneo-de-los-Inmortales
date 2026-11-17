package unlam.grupo_xi.tp2;

import unlam.grupo_xi.tp2.combatiente.Combatiente;
import unlam.grupo_xi.tp2.combatiente.CreadorCombatientes;
import unlam.grupo_xi.tp2.equipamiento.AnilloEspiritual;
import unlam.grupo_xi.tp2.equipamiento.ArmaduraDorada;
import unlam.grupo_xi.tp2.equipamiento.EspadaCelestial;
import unlam.grupo_xi.tp2.equipamiento.SelloDelFenix;
import unlam.grupo_xi.tp2.escuela.Escuela;
import unlam.grupo_xi.tp2.observador.CronistaDeLaSecta;
import unlam.grupo_xi.tp2.observador.HeraldoImperial;
import unlam.grupo_xi.tp2.pareja.Pareja;
import unlam.grupo_xi.tp2.tecnica.CorteDeLosMilAmaneceres;
import unlam.grupo_xi.tp2.tecnica.DominioDeLaNada;
import unlam.grupo_xi.tp2.tecnica.MeditacionDelInmortal;
import unlam.grupo_xi.tp2.tecnica.MurodelCuerpoDorado;
import unlam.grupo_xi.tp2.tecnica.RugidoDeLaBestiaPrimordial;
import unlam.grupo_xi.tp2.torneo.Torneo;

import java.util.List;

public final class TorneoDeLosInmortales {

    public static void main(String[] args) {

        // Escuela = configuración: técnicas y equipamiento iniciales.
        Escuela escuelaDeLaEspada = new Escuela(
                "Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres(), new RugidoDeLaBestiaPrimordial()),
                List.of(new EspadaCelestial())
        );
        Escuela palacioDelVacio = new Escuela("Palacio del Vacío",
                List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
                List.of(new AnilloEspiritual())
        );
        Escuela monasterioDelCuerpoDorado = new Escuela("Monasterio del Cuerpo Dorado",
                List.of(new MurodelCuerpoDorado(), new MeditacionDelInmortal(), new CorteDeLosMilAmaneceres()),
                List.of(new ArmaduraDorada(), new SelloDelFenix())
        );

        // La creación de combatientes está centralizada.
        CreadorCombatientes creador = new CreadorCombatientes();

        Combatiente aurelio = creador.crear("Cultivador de la Espada", "Aurelio", escuelaDeLaEspada);
        Combatiente nebula = creador.crear("Hechicero del Vacío", "Nébula", palacioDelVacio);
        Combatiente kael = creador.crear("Monje del Cuerpo Celestial", "Kael", palacioDelVacio);
        Combatiente darian = creador.crear("Cultivador de la Espada", "Darian", escuelaDeLaEspada);
        Combatiente lian = creador.crear("Monje del Cuerpo Celestial", "Lian", monasterioDelCuerpoDorado);
        Combatiente sora = creador.crear("Hechicero del Vacío", "Sora", monasterioDelCuerpoDorado);
        Combatiente ishan = creador.crear("Cultivador de la Espada", "Ishan", monasterioDelCuerpoDorado);
        Combatiente yue = creador.crear("Hechicero del Vacío", "Yue", escuelaDeLaEspada);

        Torneo torneo = new Torneo();

        // Registrar al Heraldo y al Cronista como interesados.
        HeraldoImperial heraldo = new HeraldoImperial();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarObservador(heraldo);
        torneo.agregarObservador(cronista);

        Pareja primera = new Pareja(1, aurelio, nebula);
        torneo.inscribir(primera);

        // Crear los demás combatientes e inscribir las otras tres parejas.
        torneo.inscribir(new Pareja(2, kael, darian));
        torneo.inscribir(new Pareja(3, lian, sora));
        torneo.inscribir(new Pareja(4, ishan, yue));

        // El Heraldo anuncia a los Dos Santos Supremos.
        torneo.iniciar();

        // El main le pide la tabla al Cronista y el Heraldo la presenta.
        heraldo.presentarTablaPosiciones(cronista.generarTablaPosiciones());
    }
}