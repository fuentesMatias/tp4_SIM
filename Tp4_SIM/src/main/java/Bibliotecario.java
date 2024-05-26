import lombok.Data;

import java.util.List;

@Data
public class Bibliotecario {
    private String estado; // Libre, Ocupado
    private Cliente cliente;
    private String tipoConsulta; // pedir, devolver, socio
    private List<Double> tiemposAtencionConsulta;
    private double rnd;
    private double duracionAtencion;
    private double tiempoFinAtencion;

    public Bibliotecario() {
        estado = "Libre";
        cliente = null;
        tipoConsulta = "";
        rnd = 0;
        duracionAtencion = 0;
        tiempoFinAtencion = 0;
    }

    public void calcularTiempoFinAtencion(double reloj) {
        tiempoFinAtencion = reloj + duracionAtencion;
    }
}
