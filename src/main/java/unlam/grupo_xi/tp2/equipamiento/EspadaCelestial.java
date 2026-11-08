package unlam.grupo_xi.tp2.equipamiento;

public class EspadaCelestial implements Equipamiento{

	final int BONUS_DANIO = 10;
	final CategoriaEquipamiento categoria = CategoriaEquipamiento.ARMA;
	
	@Override
	public CategoriaEquipamiento getCategoria() {
		return categoria;
	}
	
	@Override
	public int modificarDanioProducido(int dañoBase) {
		return dañoBase + BONUS_DANIO;
	}
	
}
