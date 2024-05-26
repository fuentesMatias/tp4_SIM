import lombok.Data;

@Data
public class Cliente {
    private String estado = ""; // Esperando, Leyendo, Atendido
    private static double probabilidadPermanecerLeyendo = 0.4;
    private static double tiempoPromedioLectura = 30.0;
    private double rndDesicion;
    private double rndTiempoLectura;
    private double tiempoEntrada;
    private double tiempoSalida;

    public Cliente(double reloj) {
        rndDesicion = 0;
        rndTiempoLectura = 0;
        tiempoEntrada = reloj;
        tiempoSalida = 0;
    }

    public static void setProbabilidadPermanecerLeyendo(Double prob) {
        probabilidadPermanecerLeyendo = prob;

    }

    public static void setTiempoPromedioLectura(Double tiempo) {
        tiempoPromedioLectura = tiempo;
    }
    public void calcularTiempoSalida(double reloj) {
        rndDesicion = Math.random();
        if (rndDesicion < probabilidadPermanecerLeyendo){ // decidir si permanece leyendo
            rndTiempoLectura = Math.random();
            tiempoSalida = reloj - 30 * Math.log(1 - rndTiempoLectura);
        }
        else {
            tiempoSalida = reloj;
        }
    }

    public double calcularTiempoPermancencia() {
        return tiempoSalida - tiempoEntrada;
    }
}
