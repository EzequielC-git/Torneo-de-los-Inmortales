package unlam.grupo_xi.tp2.equipamiento;

public class AnilloEspiritual implements Equipamiento {

	public static final int BONUS_ENERGIA = 20;
	private final CategoriaEquipamiento categoria = CategoriaEquipamiento.ANILLO;
	
	@Override
	public CategoriaEquipamiento getCategoria() {
		return categoria;
	}
	
	@Override
	public int getBonusEnergiaMaxima() {
		return BONUS_ENERGIA;
	}
}
