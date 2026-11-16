package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public class MeditacionDelInmortal implements Tecnica{
	private static final int COSTO_BASE = 15;
	private static final int VIDA_RECUPERADA = 25;
	
	@Override
	public String getNombre() {
		return "Meditacion del Inmortal";
	}

	@Override
	public int getCosto() {
		return COSTO_BASE;
	}

	@Override
	public boolean esCuracion() {
		return true;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		ejecutor.recuperarVida(VIDA_RECUPERADA);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		return o instanceof MeditacionDelInmortal;
	}

	@Override
	public int hashCode() {
		return getNombre().hashCode();
	}
}
