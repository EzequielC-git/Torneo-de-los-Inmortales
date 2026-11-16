package unlam.grupo_xi.tp2.comportamiento;

public class DefensaPorAgotamiento implements ComportamientoReactivo {
	private boolean activa;
	
	private static final double REDUCCION_DANIO = 0.20;
	
	@Override
	public void actualizarEnergia(int energiaAnterior, int energiaActual) {
		if (energiaAnterior > 0 && energiaActual == 0) {
			this.activa = true;
		}

	}

	@Override
	public int modificarDanioRecibido(int danio) {
		if (this.activa ) {
			return (int) Math.round(danio * (1 - REDUCCION_DANIO));
		}
			
		return danio;
	}
	
	@Override
	public void reiniciar() {
		this.activa = false;
	}
	
}
