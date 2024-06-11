import lombok.Data;

@Data
public class Cliente {
    private static int ultimoId = 0; // Atributo estático para el último ID usado
    private int id; // ID único para cada cliente
    private String estado = ""; // Esperando, Leyendo, Atendido,EsperandoParaDevolver


    private static double probabilidadPermanecerLeyendo = 0.4;
    private static double tiempoPromedioLectura = 30.0;

    private double rndDesicion;
    private double rndTiempoLectura;
    private double tiempoLectura;
    private double tiempoFinLectura;
    private double tiempoEntrada;
    private double tiempoSalida;

    public Cliente(double reloj) {
        id = ultimoId++;
        rndDesicion = 0;
        rndTiempoLectura = 0;
        tiempoEntrada = reloj;
        tiempoFinLectura = -1;
        tiempoSalida = -1;
    }

    public static void setProbabilidadPermanecerLeyendo(Double prob) {
        probabilidadPermanecerLeyendo = prob;

    }

    // resetear el id
    public static void resetId() {
        ultimoId = 0;
    }

    public static void setTiempoPromedioLectura(Double tiempo) {
        tiempoPromedioLectura = tiempo;
    }

    public boolean calcularSiHayLectura(double reloj) {
        rndDesicion = Math.random();
        if (rndDesicion < probabilidadPermanecerLeyendo){ // decidir si permanece leyendo
            rndTiempoLectura = Math.random();
            tiempoLectura = -tiempoPromedioLectura * Math.log(1 - rndTiempoLectura);
            tiempoFinLectura = reloj + tiempoLectura;
            return true;
        }
        else {
            return false;
        }
    }

    public double calcularTiempoPermancencia() {
        return tiempoSalida - tiempoEntrada;
    }
}
