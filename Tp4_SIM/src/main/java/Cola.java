import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Cola {
    private List<Cliente> colaAtencion;
    private static double mediaLlegada = 4.0;
    private double rnd;
    private double tiempoEntre;
    private double proximaLlegada;

    public Cola() {
        colaAtencion = new ArrayList<Cliente>();
        rnd = 0;
        tiempoEntre = 0;
        proximaLlegada = 0;
    }

    public static void setMediaLlegada(Double aDouble) {
        mediaLlegada = aDouble;
    }

    public void calcularProximaLlegada(double reloj) {
        rnd = Math.random();
        tiempoEntre = -mediaLlegada * Math.log(1 - rnd);
        proximaLlegada = reloj + tiempoEntre;
    }
}
