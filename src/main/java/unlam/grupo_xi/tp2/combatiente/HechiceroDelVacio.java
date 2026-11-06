package unlam.grupo_xi.tp2.combatiente;

public class HechiceroDelVacio extends Combatiente {

    private static final double FACTOR_COSTO_ENERGIA = 0.70;

    public HechiceroDelVacio(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public int calcularDanioProducido(int danioBase) {
        return danioBase + getBonusDanioEquipamiento();
    }

    @Override
    public int calcularDanioRecibido(int danioEntrante) {
        return Math.max(0, danioEntrante - getReduccionDanioEquipamiento());
    }

    @Override
    public int calcularGastoEnergia(int costoBase) {
        int costoReducido = (int) Math.round(costoBase * FACTOR_COSTO_ENERGIA);
        return Math.max(1, costoReducido);
    }
}