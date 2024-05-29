import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        Simulacion simulacion = new Simulacion(Optional.empty(),
                Optional.of(1000.0),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
        simulacion.simular();
        Simulador simulador = new Simulador();
    }
}
