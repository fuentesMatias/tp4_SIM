
import lombok.Data;

@Data
public class Cliente {
    private String estado = "";
    private double rndDesicion;
    private double rndTiempoLectura;
    private double tiempoEntrada;
    private double tiempoSalida;

    public Cliente() {
        rndDesicion = 0;
        rndTiempoLectura = 0;
        tiempoEntrada = 0;
        tiempoSalida = 0;
    }

    public void calcularTiempoSalida(double reloj) {
        rndTiempoLectura = Math.random();
        tiempoSalida = reloj - 30 * Math.log(1 - rndTiempoLectura);
    }

    public boolean decidirSiPermaneceLeyendo() {
        rndDesicion = Math.random();
        return rndDesicion >= 0.6;
    }

    public double calcularTiempoPermancencia() {
        return tiempoSalida - tiempoEntrada;
    }
}
