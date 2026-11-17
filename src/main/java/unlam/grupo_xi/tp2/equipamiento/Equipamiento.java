package unlam.grupo_xi.tp2.equipamiento;

public interface Equipamiento {
	
	CategoriaEquipamiento getCategoria();
	
	default int getBonusVidaMaxima() {
		return 0;
	}
	default int getBonusEnergiaMaxima() {
		return 0;
	}

	default int getBonusDanioProducido() {
		return 0;
	}
	default int getBonusDanioReducido() {
		return 0;
	}

	default int modificarDanioProducido(int danioBase) {
		return danioBase;
	}
	default int modificarDanioRecibido(int danioBruto) {
		return danioBruto;
	}
}
