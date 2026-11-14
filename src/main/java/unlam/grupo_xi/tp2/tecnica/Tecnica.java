package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public interface Tecnica {
	
	public String getNombre();
	
	public int getCosto();
	
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo);
}

