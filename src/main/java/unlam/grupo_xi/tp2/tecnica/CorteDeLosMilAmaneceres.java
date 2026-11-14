package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public class CorteDeLosMilAmaneceres implements Tecnica {
	private static final int COSTO_BASE = 10;
	private static final int DANIO_BASE = 25;
	
	@Override
	public String getNombre() {
		return "Corte de los Mil Amaneceres";
	}

	@Override
	public int getCosto() {
		return COSTO_BASE;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		objetivo.recibirDanio(ejecutor.calcularDanioProducido(DANIO_BASE));
	}
}
