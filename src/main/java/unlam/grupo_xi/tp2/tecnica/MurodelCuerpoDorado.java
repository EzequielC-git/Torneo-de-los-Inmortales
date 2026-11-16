package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public class MurodelCuerpoDorado implements Tecnica{
	private static final int COSTO_BASE = 15;
	private static final double REDUCCION_DANIO = 0.30;
	
	@Override
	public String getNombre() {
		return "Muro del Cuerpo Dorado";
	}

	@Override
	public int getCosto() {
		return COSTO_BASE;
	}

	@Override
	public boolean esDefensiva() {
		return true;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		ejecutor.activarMuro(REDUCCION_DANIO);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		return o instanceof MurodelCuerpoDorado;
	}

	@Override
	public int hashCode() {
		return getNombre().hashCode();
	}
}
