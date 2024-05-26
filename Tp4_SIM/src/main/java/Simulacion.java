import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Simulacion {
    // Parametros
    private double tiempoSimulacion;
    private List<Integer> iteracionesAMostrar;
    private double frecuenciaLlegada;
    private List<Double> probabilidadesCasos;
    private List<Double> tiemposAtencionConsulta;
    private double probabilidadLectura;
    private double tiempoPromedioLectura;

    // Variables
    private List<Cliente> capacidadTotal;
    private List<Bibliotecario> bibliotecarios;
    private Cola cola;
    private String evento;
    private int nroIteracion;
    private double reloj;
    private int llegadasTotales;
    private int llegadasFallidas;

    // Constructor con valores por defecto
    public Simulacion(Optional<Double> frecuenciaLlegada,
                      Optional<Double> tiempoSimulacion,
                      Optional<List<Integer>> iteracionesAMostrar,
                      Optional<List<Double>> probabilidadesCasos,
                      Optional<List<Double>> tiemposAtencionConsulta,
                      Optional<Double> probabilidadLectura,
                      Optional<Double> tiempoPromedioLectura) {
        this.frecuenciaLlegada = frecuenciaLlegada.orElse(4.0);
        this.iteracionesAMostrar = iteracionesAMostrar.orElse(getDefaultIteracionesAMostrar());
        this.tiempoSimulacion = tiempoSimulacion.orElse(100.0);
        this.probabilidadesCasos = probabilidadesCasos.orElse(getDefaultProbabilidadesCasos());
        this.tiemposAtencionConsulta = tiemposAtencionConsulta.orElse(getDefaultTiemposAtencionConsulta());
        this.probabilidadLectura = probabilidadLectura.orElse(0.4);
        this.tiempoPromedioLectura = tiempoPromedioLectura.orElse(30.0);
        inicializarVariables();
    }

    // Método para inicializar las variables comunes
    private void inicializarVariables() {
        capacidadTotal = new ArrayList<Cliente>();
        bibliotecarios = new ArrayList<Bibliotecario>();
        Bibliotecario s1 = new Bibliotecario();
        Bibliotecario s2 = new Bibliotecario();
        bibliotecarios.add(s1);
        bibliotecarios.add(s2);
        cola = new Cola();
        evento = "";
        reloj = 0;
        llegadasTotales = 0;
        llegadasFallidas = 0;
    }

    private static List<Double> getDefaultProbabilidadesCasos() {
        List<Double> defaultProbabilidades = new ArrayList<>();
        defaultProbabilidades.add(0.45);
        defaultProbabilidades.add(0.45);
        defaultProbabilidades.add(0.1);
        return defaultProbabilidades;
    }

    private static List<Double> getDefaultTiemposAtencionConsulta() {
        List<Double> defaultTiempos = new ArrayList<>();
        defaultTiempos.add(2.0);
        defaultTiempos.add(5.0);
        return defaultTiempos;
    }

    private static List<Integer> getDefaultIteracionesAMostrar() {
        List<Integer> defaultIteraciones = new ArrayList<>();
        defaultIteraciones.add(1);
        defaultIteraciones.add(100);
        return defaultIteraciones;
    }
    // Inicializar
    public void inicializar() {
        cola.setProximaLlegada(1.6);
        imprimirResultados();
        nroIteracion++;
        while (reloj < tiempoSimulacion && nroIteracion < 100000) {
            switch (evento) {
                case "llegadaCliente":
                    llegadaCliente();
                    break;
                case "finAtencionS1":
                    finAtencionS1();
                    break;
                case "finAtencionS2":
                    finAtencionS2();
                    break;
                case "finLectura":
                    finLectura();
                    break;
            }
            imprimirResultados();
            calcularProximoEvento();
            nroIteracion++;
        }
        System.out.println(nroIteracion);

    }

    private void imprimirResultados() {
    }

    // Calcular próximo evento
    public void calcularProximoEvento() {
    }

    // Eventos
    public void llegadaCliente() {
    }

    public void finAtencionS1() {
    }

    public void finAtencionS2() {
    }

    public void finLectura() {
    }
}
