import lombok.Data;

@Data
public class Cliente {
    private static int ultimoId = 0; // Atributo estático para el último ID usado
    private int id; // ID único para cada cliente
    private String estado = ""; // Esperando, Leyendo, Atendido

    private static double probabilidadPermanecerLeyendo = 0.4;
    private static double tiempoPromedioLectura = 30.0;

    private double rndDesicion;
    private double rndTiempoLectura;
    private double tiempoLectura;
    private double tiempoEntrada;
    private double tiempoSalida;

    public Cliente(double reloj) {
        id = ultimoId++;
        rndDesicion = 0;
        rndTiempoLectura = 0;
        tiempoEntrada = reloj;
        tiempoSalida = -1;
    }

    public static void setProbabilidadPermanecerLeyendo(Double prob) {
        probabilidadPermanecerLeyendo = prob;

    }

    public static void setTiempoPromedioLectura(Double tiempo) {
        tiempoPromedioLectura = tiempo;
    }
    public boolean calcularTiempoSalidaSiPidio(double reloj) {
        rndDesicion = Math.random();
        if (rndDesicion < probabilidadPermanecerLeyendo){ // decidir si permanece leyendo
            rndTiempoLectura = Math.random();
            tiempoLectura = -tiempoPromedioLectura * Math.log(1 - rndTiempoLectura);
            tiempoSalida = reloj + tiempoLectura;
            return true;
        }
        else {
            tiempoSalida = reloj;
            return false;
        }
    }

    public double calcularTiempoPermancencia() {
        return tiempoSalida - tiempoEntrada;
    }
}
