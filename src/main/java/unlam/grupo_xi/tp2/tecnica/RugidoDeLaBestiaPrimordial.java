package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public class RugidoDeLaBestiaPrimordial implements Tecnica{
	private static final int COSTO_BASE = 25;
	private static final int DANIO_BASE = 35;
	
	@Override
	public String getNombre() {
		return "Rugido de la Bestia Primordial";
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
