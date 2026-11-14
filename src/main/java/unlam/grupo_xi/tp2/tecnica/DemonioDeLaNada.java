package unlam.grupo_xi.tp2.tecnica;

import unlam.grupo_xi.tp2.combatiente.Combatiente;

public class DemonioDeLaNada implements Tecnica{
	private static final int COSTO_BASE = 15;
	private static final int DANIO_BASE = 10;
	private static final int ENERGIA_QUITADA = 8;
	
	@Override
	public String getNombre() {
		return "Demonio de la Nada";
	}

	@Override
	public int getCosto() {
		return COSTO_BASE;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		objetivo.recibirDanio(ejecutor.calcularDanioProducido(DANIO_BASE));
		objetivo.perderEnergia(ENERGIA_QUITADA);
	}
}
