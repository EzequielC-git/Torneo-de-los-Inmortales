package unlam.grupo_xi.tp2.comportamiento;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DefensaPorAgotamientoTest {

	@Test
	void mantieneDanioCuandoEstaDesactivada() {

	    DefensaPorAgotamiento defensa = new DefensaPorAgotamiento();

	    int danioOriginal = 100;

	    int danioFinal = defensa.modificarDanioRecibido(danioOriginal);

	    assertEquals(100, danioFinal);
	}
	
	@Test
	void reduceDanioCuandoLaEnergiaEsCero() {

	    DefensaPorAgotamiento defensa = new DefensaPorAgotamiento();

	    defensa.actualizarEnergia(10, 0);

	    int danioFinal = defensa.modificarDanioRecibido(100);

	    assertEquals(80, danioFinal);
	}
	
	@Test
	void mantieneDefensaActivaAlRecupereEnergia() {

	    DefensaPorAgotamiento defensa = new DefensaPorAgotamiento();

	    defensa.actualizarEnergia(10, 0);
	    defensa.actualizarEnergia(0, 10);

	    int danioFinal = defensa.modificarDanioRecibido(100);

	    assertEquals(80, danioFinal);
	}
	
	@Test
	void reiniciaDefensaEnUnNuevoCombate() {

	    DefensaPorAgotamiento defensa = new DefensaPorAgotamiento();

	    defensa.actualizarEnergia(10, 0);

	    defensa.reiniciar();

	    int danioFinal = defensa.modificarDanioRecibido(100);

	    assertEquals(100, danioFinal);
	}
}

