package unlam.grupo_xi.tp2.equipamiento;

public class ArmaduraDorada implements Equipamiento {

	public static final int REDUCCION_DANIO = 10;
	private final CategoriaEquipamiento categoria = CategoriaEquipamiento.ARMADURA;
	
	@Override
	public CategoriaEquipamiento getCategoria() {
		return categoria;
	}

	@Override
	public int modificarDanioRecibido(int danioBruto) {
		return Math.max(0, danioBruto - REDUCCION_DANIO);
	}
}
