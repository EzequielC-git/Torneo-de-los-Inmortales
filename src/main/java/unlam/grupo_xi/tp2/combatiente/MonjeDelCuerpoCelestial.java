package unlam.grupo_xi.tp2.combatiente;

public class MonjeDelCuerpoCelestial extends Combatiente {

    private static final double FACTOR_MITIGACION = 0.75;

    public MonjeDelCuerpoCelestial(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public int calcularDanioProducido(int danioBase) {
        return danioBase + getBonusDanioEquipamiento();
    }

    @Override
    public int calcularDanioRecibido(int danioEntrante) {
        int mitigado = (int) Math.round(danioEntrante * FACTOR_MITIGACION);
        return Math.max(0, mitigado - getReduccionDanioEquipamiento());
    }

    @Override
    public int calcularGastoEnergia(int costoBase) {
        return costoBase;
    }
}