package unlam.grupo_xi.tp2.observador;
import unlam.grupo_xi.tp2.pareja.*;
import java.util.*;

public class CronistaDeLaSecta implements ObservadorTorneo{

	private final List<Acontecimiento> historialEventos;
	
	public CronistaDeLaSecta(){
		this.historialEventos = new ArrayList<>();
	}

	@Override
    public void actualizar(Acontecimiento evento) {
        historialEventos.add(evento);
    }
	
	public List<Acontecimiento> getHistorialEventos() {
        return historialEventos;
    }
	
	public List<Pareja> generarTablaPosiciones() {
		
		List<AcontecimientoResultado> historialResultados = new ArrayList<>();
		
		for(Acontecimiento a : historialEventos) {
			if(a.getTipoEvento() == TipoAcontecimiento.RESULTADO_COMBATE) {
				historialResultados.add((AcontecimientoResultado) a);
            }
		}
		
		historialResultados.sort(new OrdenarResultados());
		
        List<Pareja> tablaFinal = new ArrayList<>();
        
        for(AcontecimientoResultado resultado : historialResultados) {
            Pareja ganadora = resultado.getParejaGanadora();
            Pareja perdedora = resultado.getParejaPerdedora();
         
            if(!tablaFinal.contains(ganadora)) {
                tablaFinal.add(ganadora);
            }
            if(!tablaFinal.contains(perdedora)) {
                tablaFinal.add(perdedora);
            }
        }
        
        return tablaFinal; 
    }
}
