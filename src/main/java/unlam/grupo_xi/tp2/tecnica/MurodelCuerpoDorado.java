package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public class MurodelCuerpoDorado implements Tecnica{
	private static final int COSTO_BASE = 15;
	private static final double REDUCCION_DANIO = 0.30;
	
	@Override
	public String getNombre() {
		return "Muro del Cuerpo Doardo";
	}

	@Override
	public int getCosto() {
		return COSTO_BASE;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		ejecutor.activarMuro(REDUCCION_DANIO);
	}
}
