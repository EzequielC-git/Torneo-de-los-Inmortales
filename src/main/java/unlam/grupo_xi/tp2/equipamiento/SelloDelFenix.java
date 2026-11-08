package unlam.grupo_xi.tp2.equipamiento;

public class SelloDelFenix implements Equipamiento{
	
	final int BONUS_VIDA = 30;
	final CategoriaEquipamiento categoria = CategoriaEquipamiento.SELLO;
	
	@Override
	public CategoriaEquipamiento getCategoria() {
		return categoria;
	}
	
	@Override
	public int getBonusVidaMaxima(int vidaBase) {
		return vidaBase+BONUS_VIDA;
	}
}
