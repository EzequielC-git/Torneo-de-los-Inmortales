package unlam.grupo_xi.tp2.equipamiento;

public class EspadaCelestial implements Equipamiento {

	public static final int BONUS_DANIO = 10;
	private final CategoriaEquipamiento categoria = CategoriaEquipamiento.ARMA;
	
	@Override
	public CategoriaEquipamiento getCategoria() {
		return categoria;
	}

	@Override
	public int getBonusDanioProducido() {
		return BONUS_DANIO;
	}
}
