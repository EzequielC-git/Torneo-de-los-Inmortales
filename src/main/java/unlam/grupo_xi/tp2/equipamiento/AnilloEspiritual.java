package unlam.grupo_xi.tp2.equipamiento;

public class AnilloEspiritual implements Equipamiento{

	final int BONUS_ENERGIA = 20;
	final CategoriaEquipamiento categoria = CategoriaEquipamiento.ANILLO;
	
	@Override
	public CategoriaEquipamiento getCategoria() {
		return categoria;
	}
	
	@Override
	public int getBonusEnergiaMaxima(int energiaBase) {
		return energiaBase + BONUS_ENERGIA;
	}
}
