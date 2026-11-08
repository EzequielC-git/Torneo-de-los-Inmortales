package unlam.grupo_xi.tp2.equipamiento;

public interface Equipamiento{
	
	public CategoriaEquipamiento getCategoria();
	
	public default int getBonusVidaMaxima(int vidaBase) {
		return vidaBase;
	}
	
	public default int getBonusEnergiaMaxima(int energiaBase) {
		return energiaBase;
	}
	
	public default int modificarDanioProducido(int dañoBase) {
		return dañoBase;
	}
	
	public default int modificarDanioRecibido(int dañoBruto) {
		return dañoBruto;
	}
}
