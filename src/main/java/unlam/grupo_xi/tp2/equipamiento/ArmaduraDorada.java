package unlam.grupo_xi.tp2.equipamiento;

public class ArmaduraDorada implements Equipamiento{

	final int REDUCCION_DANIO = 10;
	final CategoriaEquipamiento categoria = CategoriaEquipamiento.ARMADURA;
	
	@Override
	public CategoriaEquipamiento getCategoria() {
		return categoria;
	}

	@Override
	public int modificarDanioRecibido(int dañoBruto) {
		return Math.max(0, dañoBruto - REDUCCION_DANIO);
	}
	
}
