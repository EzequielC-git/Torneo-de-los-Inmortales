package unlam.grupo_xi.tp2.observador;

import java.util.Comparator;

public class OrdenarResultados implements Comparator<AcontecimientoResultado>{

	@Override
	public int compare(AcontecimientoResultado o1, AcontecimientoResultado o2) {
		Integer difRondas = o2.getRonda() - o1.getRonda();
		
		if(difRondas == 0){
			Integer difVidas = o2.getVidaTotalPerdedora() - o1.getVidaTotalPerdedora();
			if(difVidas == 0) {
				return o1.getParejaPerdedora().getId() - o2.getParejaPerdedora().getId();
			}else
				return difVidas;
		}
		
		return difRondas;
	}

}
