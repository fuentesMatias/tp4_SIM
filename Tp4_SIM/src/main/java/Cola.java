import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Cola {
    private List<Cliente> colaAtencion;
    private double rnd;
    private double tiempoEntre;
    private double proximaLlegada;

    public Cola() {
        colaAtencion = new ArrayList<Cliente>();
        rnd = 0;
        tiempoEntre = 0;
        proximaLlegada = 0;
    }
}
