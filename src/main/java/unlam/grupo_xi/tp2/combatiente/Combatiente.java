package unlam.grupo_xi.tp2.combatiente;

import unlam.grupo_xi.tp2.equipamiento.CategoriaEquipamiento;
import unlam.grupo_xi.tp2.equipamiento.Equipamiento;
import unlam.grupo_xi.tp2.tecnica.Tecnica;
import java.util.*;

public abstract class Combatiente {
    protected static final int VIDA_BASE = 100;
    protected static final int ENERGIA_BASE = 40;
    protected static final int MAX_USOS_TECNICA = 5;
    protected static final int MAX_EQUIPAMIENTOS = 3;

    private final int id;
    private final String nombre;

    private int vidaMaxima, vidaActual;
    private int energiaMaxima, energiaActual;
    
    private double reduccionMuro;

    private final Map<CategoriaEquipamiento, Equipamiento> equipamientos;
    private final Map<Tecnica, Integer> usosTecnicas;

    public Combatiente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.vidaMaxima = VIDA_BASE;
        this.vidaActual = VIDA_BASE;
        this.energiaMaxima = ENERGIA_BASE;
        this.energiaActual = ENERGIA_BASE;
        this.equipamientos = new HashMap<>();
        this.usosTecnicas = new HashMap<>();
    }

    public abstract int calcularDanioProducido(int danioBase);
    public abstract int calcularDanioRecibido(int danioEntrante);
    public abstract int calcularGastoEnergia(int costoBase);
    
    public void activarMuro(double reduccion) {
    	this.reduccionMuro = reduccion;
    }
    
    public void recibirDanio(int danioEntrante) {
        if (!estaEnPie() || danioEntrante <= 0) return;

        int danioFinal = calcularDanioRecibido(danioEntrante);
        
        danioFinal = (int)(danioFinal * (1 - reduccionMuro));
        
        this.vidaActual = Math.max(0, this.vidaActual - danioFinal);
    }

    public void recuperarVida(int cantidad) {
        if (cantidad > 0 && estaEnPie())
            this.vidaActual = Math.min(this.vidaMaxima, this.vidaActual + cantidad);
    }

    public void recuperarEnergia(int cantidad) {
        if (cantidad > 0 && estaEnPie())
            this.energiaActual = Math.min(this.energiaMaxima, this.energiaActual + cantidad);
    }

    public void gastarEnergia(int costoBase) {
        int costoFinal = calcularGastoEnergia(costoBase);
        this.energiaActual = Math.max(0, this.energiaActual - costoFinal);
    }
    
    public void perderEnergia(int cantidad) {
    	if (cantidad > 0) {
    		this.energiaActual = Math.max(0, this.energiaActual - cantidad);
    	}
    }

    public void restablecerParaNuevoCombate() {
        this.vidaActual = this.vidaMaxima;
        this.energiaActual = this.energiaMaxima;
    }

    public boolean estaEnPie() {
        return this.vidaActual > 0;
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getVidaActual() {
        return this.vidaActual;
    }

    public int getVidaMaxima() {
        return this.vidaMaxima;
    }

    public int getEnergiaActual() {
        return this.energiaActual;
    }

    public int getEnergiaMaxima() {
        return this.energiaMaxima;
    }



    // Equipamientos
    // —————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    public boolean equipar(Equipamiento equipamiento) {
        if (equipamiento == null) return false;

        CategoriaEquipamiento categoria = equipamiento.getCategoria();
        if (equipamientos.size() >= MAX_EQUIPAMIENTOS || equipamientos.containsKey(categoria))
            return false; // máximo 3 o categoría repetida

        equipamientos.put(categoria, equipamiento);
        recalcularMaximos();
        return true;
    }

    public boolean desequipar(CategoriaEquipamiento categoria) {
        if (!equipamientos.containsKey(categoria)) return false;

        equipamientos.remove(categoria);
        recalcularMaximos();

        if (this.vidaActual > this.vidaMaxima)
            this.vidaActual = this.vidaMaxima;

        if (this.energiaActual > this.energiaMaxima)
            this.energiaActual = this.energiaMaxima;
        return true;
    }

    private void recalcularMaximos() {
        int bonusVida = 0, bonusEnergia = 0;

        for (Equipamiento eq : equipamientos.values()) {
            // to-do: modificadores
        }

        this.vidaMaxima = VIDA_BASE + bonusVida;
        this.energiaMaxima = ENERGIA_BASE + bonusEnergia;
    }

    protected int getBonusDanioEquipamiento() {
        int total = 0;
        for (Equipamiento eq : equipamientos.values()) {
            // to-do: modificadores
        }
        return total;
    }

    protected int getReduccionDanioEquipamiento() {
        int total = 0;
        for (Equipamiento eq : equipamientos.values()) {
            // to-do: modificadores
        }
        return total;
    }

    public List<Equipamiento> getEquipamientos() {
        return new ArrayList<>(equipamientos.values());
    }



    // Técnicas
    // —————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    public void agregarTecnica(Tecnica tecnica) {
        if (tecnica != null && !usosTecnicas.containsKey(tecnica))
            usosTecnicas.put(tecnica, MAX_USOS_TECNICA);
    }

    public boolean tieneUsosRestantes(Tecnica tecnica) {
        return usosTecnicas.getOrDefault(tecnica, 0) > 0;
    }

    public int getUsosRestantes(Tecnica tecnica) {
        return usosTecnicas.getOrDefault(tecnica, 0);
    }

    public void registrarUsoTecnica(Tecnica tecnica) {
        int restantes = getUsosRestantes(tecnica);
        if (restantes > 0)
            usosTecnicas.put(tecnica, restantes - 1);
    }

    public List<Tecnica> getTecnicas() {
        return new ArrayList<>(usosTecnicas.keySet());
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Combatiente that)) return false;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
