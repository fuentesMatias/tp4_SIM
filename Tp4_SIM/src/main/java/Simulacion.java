import lombok.Data;

import java.util.*;

@Data
public class Simulacion {
    // Parametros
    private double tiempoSimulacion;
    private double mostrarDesde;
    private Integer iteracionesAMostrar;
    private double frecuenciaLlegada;
    private List<Double> probabilidadesCasos;
    private List<Double> tiemposAtencionConsulta;
    private double probabilidadLectura;
    private double tiempoPromedioLectura;

    // Variables
    //matriz de resultados
    private List<List<Object>> matriz = new ArrayList<>();
    private static final int CAPACIDAD_TOTAL = 20;
    private List<Cliente> capacidadTotal;
    private int clientesActuales = 0; // Seguimiento del número actual de clientes en la biblioteca
    private List<Bibliotecario> bibliotecarios;
    private Cola cola;
    private String evento;
    private int nroIteracion;
    private double reloj;
    private int llegadasTotales;
    private int llegadasFallidas;
    private List<Double> tiemposDePermanencia;
    boolean imprimirTiempoPermanencia = false;

    // Constructor con valores por defecto
    public Simulacion(Optional<Double> frecuenciaLlegada,
                      Optional<Double> tiempoSimulacion,
                      Optional<Double> mostrarDesde,
                      Optional<Integer> iteracionesAMostrar,
                      Optional<List<Double>> probabilidadesCasos,
                      Optional<List<Double>> tiemposAtencionConsulta,
                      Optional<Double> probabilidadLectura,
                      Optional<Double> tiempoPromedioLectura) {
        this.frecuenciaLlegada = frecuenciaLlegada.orElse(4.0);
        Cola.setMediaLlegada(frecuenciaLlegada.orElse(4.0));

        this.iteracionesAMostrar = iteracionesAMostrar.orElse(100);
        this.tiempoSimulacion = tiempoSimulacion.orElse(100.0);
        this.mostrarDesde = mostrarDesde.orElse(0.0);

        this.probabilidadesCasos = probabilidadesCasos.orElse(getDefaultProbabilidadesCasos());
        Bibliotecario.setProbabilidadesCasos(probabilidadesCasos.orElse(getDefaultProbabilidadesCasos()));

        this.tiemposAtencionConsulta = tiemposAtencionConsulta.orElse(getDefaultTiemposAtencionConsulta());
        Bibliotecario.setTiemposAtencionConsulta(tiemposAtencionConsulta.orElse(getDefaultTiemposAtencionConsulta()));

        this.probabilidadLectura = probabilidadLectura.orElse(0.4);
        Cliente.setProbabilidadPermanecerLeyendo(probabilidadLectura.orElse(0.4));

        this.tiempoPromedioLectura = tiempoPromedioLectura.orElse(30.0);
        Cliente.setTiempoPromedioLectura(tiempoPromedioLectura.orElse(30.0));


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
        tiemposDePermanencia = new ArrayList<>();
        //imprimir el vector capacidadTotal
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            capacidadTotal.add(null);
        }


    }

    // Método para agregar un cliente a la biblioteca
    private void agregarCliente(Cliente cliente) {
        // Buscar el primer hueco vacío en la capacidadTotal
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (capacidadTotal.get(i) == null) {
                capacidadTotal.set(i, cliente);
                clientesActuales++;
                break;
            }
        }
    }
    // Método para eliminar un cliente de la biblioteca
    private void eliminarCliente(int idCliente) {
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (capacidadTotal.get(i) != null && capacidadTotal.get(i).getId() == idCliente) {
                capacidadTotal.set(i, null); // Marcar el lugar como vacío
                clientesActuales--;
                break;
            }
        }
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


    // Inicializar
    public List<List<Object>> simular() {
        Cliente.resetId();

        cola.setProximaLlegada(1.6);
        matriz.add(imprimirResultados());
        calcularProximoEvento();
        nroIteracion++;
        int mostradas = 0;

        while (reloj < tiempoSimulacion && nroIteracion < 100000) {
            imprimirTiempoPermanencia = false;
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
            //imprimir los resultados si el reloj es mayor o igual a mostrarDesde y crear un contador para imprimir solo las iteracionesAMostrar y mostrar la ultima
            if (reloj >= mostrarDesde && mostradas < iteracionesAMostrar || reloj >= tiempoSimulacion) {
                mostradas++;
                matriz.add(imprimirResultados());
            }

            calcularProximoEvento();
            nroIteracion++;
        }
        //mostrar la ultima fila
        matriz.add(imprimirResultados());


        return matriz;
    }

    private List<Object> imprimirResultados() {
        //crea una lista de Objetos para guardar los resultados de longitud fija

        List<Object> resultados = new ArrayList<>(124);
        //agregar todos estos datos:
        //"evento", "reloj", "Llegada_rnd1", "tiempo", "proxLlegada",
        //                "Cola Atención", "Ocupacion Actual", "S1_estado", "S1_cliente",
        //                "S1_rndCaso", "S1_caso", "S1_rndTiempo", "S1_duracionA",
        //                "S1_finAtencion", "S2_estado", "S2_cliente", "S2_rndCaso",
        //                "S2_caso", "S2_rndTiempo", "S2_duracionA", "S2_finAtencion",

        resultados.add(0, evento);
        resultados.add(1, String.format("%.2f", reloj));
        resultados.add(2, evento.equals("llegadaCliente") ? String.format("%.2f", cola.getRnd()) : "-");
        resultados.add(3, evento.equals("llegadaCliente") ? String.format("%.2f", cola.getTiempoEntre()) : "-");
        resultados.add(4, String.format("%.2f", cola.getProximaLlegada()));
        resultados.add(5, cola.getColaAtencion().size());
        resultados.add(6, clientesActuales);

        resultados.add(7,llegadasTotales);
        resultados.add(8,llegadasFallidas);
        //si el evento es finAtencionS1 o finAtencionS2, agregar el ultimo timepo de permanencia
        resultados.add(9, imprimirTiempoPermanencia ? String.format("%.2f", tiemposDePermanencia.get(tiemposDePermanencia.size()-1)) : "-");

        resultados.add(10, bibliotecarios.get(0).getEstado().equals("Libre") ? "Libre" : bibliotecarios.get(0).getEstado());
        resultados.add(11, bibliotecarios.get(0).getEstado().equals("Libre") ? "-" : String.valueOf(bibliotecarios.get(0).getCliente().getId()));
        resultados.add(12, bibliotecarios.get(0).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(0).getRndCaso()));
        resultados.add(13, bibliotecarios.get(0).getEstado().equals("Libre") ? "-" : bibliotecarios.get(0).getTipoConsulta());
        resultados.add(14, bibliotecarios.get(0).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(0).getRndTiempoAtencion()));
        resultados.add(15, bibliotecarios.get(0).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(0).getDuracionAtencion()));
        resultados.add(16, bibliotecarios.get(0).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(0).getTiempoFinAtencion()));

        resultados.add(17, bibliotecarios.get(1).getEstado().equals("Libre") ? "Libre" : bibliotecarios.get(1).getEstado());
        resultados.add(18, bibliotecarios.get(1).getEstado().equals("Libre") ? "-" : String.valueOf(bibliotecarios.get(1).getCliente().getId()));
        resultados.add(19, bibliotecarios.get(1).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(1).getRndCaso()));
        resultados.add(20, bibliotecarios.get(1).getEstado().equals("Libre") ? "-" : bibliotecarios.get(1).getTipoConsulta());
        resultados.add(21, bibliotecarios.get(1).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(1).getRndTiempoAtencion()));
        resultados.add(22, bibliotecarios.get(1).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(1).getDuracionAtencion()));
        resultados.add(23, bibliotecarios.get(1).getEstado().equals("Libre") ? "-" : String.format("%.2f", bibliotecarios.get(1).getTiempoFinAtencion()));

        int i = 24;
        for (Cliente cliente : capacidadTotal) {
            if (cliente == null) {
                resultados.add(i, "-");
                resultados.add(i + 1, "-");
                resultados.add(i + 2, "-");
                resultados.add(i + 3, "-");
                resultados.add(i + 4, "-");
                resultados.add(i + 5, "-");
                resultados.add(i + 6, "-");
                i += 7;
                continue;
            }
            resultados.add(i, cliente.getId());
            resultados.add(i + 1, cliente.getEstado());
            resultados.add(i + 2, String.format("%.2f", cliente.getTiempoEntrada()));
            resultados.add(i + 3, String.format("%.2f", cliente.getRndDesicion()));
            resultados.add(i + 4, String.format("%.2f", cliente.getRndTiempoLectura()));
            resultados.add(i + 5, String.format("%.2f", cliente.getTiempoLectura()));
            resultados.add(i + 6, cliente.getTiempoFinLectura() == -1 ? "-" : String.format("%.2f", cliente.getTiempoFinLectura()));
            i += 7;
        }
        return resultados;
    }

    // Calcular próximo evento
    public void calcularProximoEvento() {
        double minimo = cola.getProximaLlegada();
        evento = "llegadaCliente";
        if (bibliotecarios.get(0).getTiempoFinAtencion() < minimo && bibliotecarios.get(0).getTiempoFinAtencion() != -1){
            minimo = bibliotecarios.get(0).getTiempoFinAtencion();
            evento = "finAtencionS1";
        }
        if (bibliotecarios.get(1).getTiempoFinAtencion() < minimo && bibliotecarios.get(1).getTiempoFinAtencion() != -1){
            minimo = bibliotecarios.get(1).getTiempoFinAtencion();
            evento = "finAtencionS2";
        }
        //para cada cliente que este en estado leyendo se fija si es el proximo en terminar
        for (Cliente cliente : capacidadTotal) {
            if  (cliente == null) {
                continue;
            }
            if (cliente.getTiempoFinLectura() < minimo && Objects.equals(cliente.getEstado(), "leyendo") && cliente.getTiempoFinLectura() != -1) {
                minimo = cliente.getTiempoFinLectura();
                evento = "finLectura";
            }
        }
        reloj = minimo;
    }

    // Eventos
    public void llegadaCliente() {
        if (clientesActuales < 20) {
            //si la capacidad es menor a 20 se agrega un cliente
            Cliente cliente = new Cliente(reloj);
            //se agrega el cliente a la biblioteca, dsp se decide a donde va
            agregarCliente(cliente);
            llegadasTotales++;

            // Si hay bibliotecarios libres se atiende al cliente en ese bibliotecario, sino lo pone en la cola
            if (bibliotecarios.get(0).getEstado().equals("Libre")) {
                bibliotecarios.get(0).ocuparBibliotecario(cliente, reloj); // setea y calcula el tiempo de fin de atencion
                cliente.setEstado("Atendido");
            } else if (bibliotecarios.get(1).getEstado().equals("Libre")) {
                bibliotecarios.get(1).ocuparBibliotecario(cliente, reloj); // setea y calcula el tiempo de fin de atencion
                cliente.setEstado("Atendido");
            } else {
                cola.getColaAtencion().add(cliente);
                cliente.setEstado("Esperando");
            }
            cola.calcularProximaLlegada(reloj);
        } else {
            llegadasFallidas++;
            llegadasTotales++;
            cola.calcularProximaLlegada(reloj);
        }
    }

    public void finAtencionS1() {
        Cliente cliente = bibliotecarios.get(0).getCliente();
        //cuando termina de antender a un cliente, primero se verifica si ese cliente pidio un libro
        if(Objects.equals(bibliotecarios.get(0).getTipoConsulta(), "Pedir")){
            //si pidio un libro, se calcula si se queda o se va
            if (cliente.calcularSiHayLectura(reloj)){ // En este metodo se setea el tiempo ya sea si se va o se queda
                //si se queda, se cambia el estado a leyendo, y se libera al bibliotecario
                cliente.setEstado("leyendo");
                bibliotecarios.get(0).liberarBibliotecario();
            }
            else{
                //si se va, se libera al bibliotecario y se elimina al cliente de la biblioteca y se guarda el tiempo de permanencia
                cliente.setTiempoSalida(reloj); //Seteo el tiempo de salida en el actual
                tiemposDePermanencia.add(cliente.calcularTiempoPermancencia()); //Guardo el tiempo de permanencia
                imprimirTiempoPermanencia = true;
                eliminarCliente(cliente.getId()); //Saco al cliente de la biblioteca
                bibliotecarios.get(0).liberarBibliotecario(); //Libero al bibliotecario
            }
        }
        else{
            //si no pidio un libro, se libera al bibliotecario, y se saca al cliente de la biblioteca y se guarda el tiempo de permanencia
            cliente.setTiempoSalida(reloj); //Seteo el tiempo de salida en el actual
            tiemposDePermanencia.add(cliente.calcularTiempoPermancencia()); //Guardo el tiempo de permanencia
            imprimirTiempoPermanencia = true;
            eliminarCliente(cliente.getId()); //Saco al cliente de la biblioteca
            bibliotecarios.get(0).liberarBibliotecario(); //Libero al bibliotecario
        }

        //Ahora si hay gente en la cola, se atiende al proximo
        if (!cola.getColaAtencion().isEmpty()) { //Si hay gente en la cola
            Cliente next = cola.getColaAtencion().get(0);
            cola.getColaAtencion().remove(0);
            bibliotecarios.get(0).ocuparBibliotecario(next, reloj);
            next.setEstado("Atendido");
        }

    }

    public void finAtencionS2() {
        Cliente cliente = bibliotecarios.get(1).getCliente();
        //cuando termina de antender a un cliente, primero se verifica si ese cliente pidio un libro
        if(Objects.equals(bibliotecarios.get(1).getTipoConsulta(), "Pedir")){
            //si pidio un libro, se calcula si se queda o se va
            if (cliente.calcularSiHayLectura(reloj)){ // En este metodo se setea el tiempo de fin Lectura
                //si se queda, se cambia el estado a leyendo, y se libera al bibliotecario
                cliente.setEstado("leyendo");
                bibliotecarios.get(1).liberarBibliotecario();
            }
            else{
                //si se va, se libera al bibliotecario y se elimina al cliente de la biblioteca y se guarda el tiempo de permanencia
                cliente.setTiempoSalida(reloj); //Seteo el tiempo de salida en el actual
                tiemposDePermanencia.add(cliente.calcularTiempoPermancencia()); //Guardo el tiempo de permanencia
                imprimirTiempoPermanencia = true;
                eliminarCliente(cliente.getId()); //Saco al cliente de la biblioteca
                bibliotecarios.get(1).liberarBibliotecario(); //Libero al bibliotecario
            }
        }
        else{
            //si no pidio un libro, se libera al bibliotecario, y se saca al cliente de la biblioteca y se guarda el tiempo de permanencia
            cliente.setTiempoSalida(reloj); //Seteo el tiempo de salida en el actual
            tiemposDePermanencia.add(cliente.calcularTiempoPermancencia()); //Guardo el tiempo de permanencia
            imprimirTiempoPermanencia = true;
            eliminarCliente(cliente.getId()); //Saco al cliente de la biblioteca
            bibliotecarios.get(1).liberarBibliotecario(); //Libero al bibliotecario
        }

        //Ahora si hay gente en la cola, se atiende al proximo
        if (!cola.getColaAtencion().isEmpty()) { //Si hay gente en la cola
            Cliente next = cola.getColaAtencion().get(0);
            cola.getColaAtencion().remove(0);
            bibliotecarios.get(1).ocuparBibliotecario(next, reloj);
            next.setEstado("Atendido");
        }
    }

    public void finLectura() {
        //Busca al cliente que termino de leer por el tiempo del reloj actual
        Cliente cliente = null;
        for (Cliente c : capacidadTotal) {
            if (c == null) {
                continue;
            }
            if (c.getTiempoFinLectura() == reloj && Objects.equals(c.getEstado(), "leyendo")) {
                cliente = c;
                break;
            }
        }
        // Si termino de leer, verifica si hay un bibliotecario libre para atenderlo, sino lo pone en la cola
        assert cliente != null;
        cliente.setEstado("EsperandoParaDevolver");
        if (bibliotecarios.get(0).getEstado().equals("Libre")) {
            bibliotecarios.get(0).ocuparBibliotecario(cliente, reloj); // setea y calcula el tiempo de fin de atencion
            cliente.setEstado("Atendido");
        } else if (bibliotecarios.get(1).getEstado().equals("Libre")) {
            bibliotecarios.get(1).ocuparBibliotecario(cliente, reloj); // setea y calcula el tiempo de fin de atencion
            cliente.setEstado("Atendido");
        } else {
            cola.getColaAtencion().add(cliente);
            cliente.setEstado("EsperandoParaDevolver");
        }
    }
}
