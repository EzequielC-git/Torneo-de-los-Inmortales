package unlam.grupo_xi.tp2.combatiente;

public class CultivadorDeLaEspada extends Combatiente {

    private static final double MULTIPLICADOR_DANIO = 1.20;

    public CultivadorDeLaEspada(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public int calcularDanioProducido(int danioBase) {
        int danioPotenciado = (int) Math.round(danioBase * MULTIPLICADOR_DANIO);
        return danioPotenciado + getBonusDanioEquipamiento();
    }

    @Override
    public int calcularDanioRecibido(int danioEntrante) {
        return Math.max(0, danioEntrante - getReduccionDanioEquipamiento());
    }

    @Override
    public int calcularGastoEnergia(int costoBase) {
        return costoBase;
    }
}