package unlam.grupo_xi.tp2.comportamiento;

public interface ComportamientoReactivo {

	default void actualizarEnergia(int energiaAnterior, int energiaActual) {
	}
	
	default int modificarDanioRecibido(int danio){
		return danio;
	}
	
	void reiniciar();
}
