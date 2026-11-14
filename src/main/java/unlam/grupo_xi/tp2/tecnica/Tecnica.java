package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public interface Tecnica {
	
	public String getNombre();
	
	public int getCosto();
	
	public default int getCostoBase() {
		return getCosto();
	}
	
	public default boolean esCuracion() {
		return false;
	}
	
	public default boolean esDefensiva() {
		return false;
	}
	
	public default boolean esOfensiva() {
		return !esCuracion() && !esDefensiva();
	}
	
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo);
}

