import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Simulador extends JFrame {
    private JPanel Main;
    private JButton simularButton;
    private JTextField probSocio;
    private JTextField probDevolver;
    private JTextField probPedir;
    private JTextField tiempoMin;
    private JTextField frecLlegada;
    private JTextField tiempoMax;
    private JTextField probLectura;
    private JTable table1;
    private JTextField promLectura;
    private JTextField filas;
    private JTextField tiempo;
    private JScrollPane scrollPanel;

    public Simulador() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        add(Main);
        setTitle("Simulador");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicializar JTextFields con valores predeterminados
        frecLlegada.setText("5");
        probSocio.setText("0.1");
        probDevolver.setText("0.45");
        probPedir.setText("0.45");
        tiempoMin.setText("2");
        tiempoMax.setText("5");
        probLectura.setText("0.4");
        promLectura.setText("30");
        filas.setText("10");
        tiempo.setText("60");

        // Agregar DocumentListener a todos los JTextField
        addDocumentListener(probSocio);
        addDocumentListener(probDevolver);
        addDocumentListener(probPedir);
        addDocumentListener(tiempoMin);
        addDocumentListener(frecLlegada);
        addDocumentListener(tiempoMax);
        addDocumentListener(probLectura);
        addDocumentListener(promLectura);
        addDocumentListener(filas);
        addDocumentListener(tiempo);

        // Aplicar filtro de entrada a los JTextFields
        applyNumberFilter(probSocio);
        applyNumberFilter(probDevolver);
        applyNumberFilter(probPedir);
        applyNumberFilter(tiempoMin);
        applyNumberFilter(frecLlegada);
        applyNumberFilter(tiempoMax);
        applyNumberFilter(probLectura);
        applyNumberFilter(promLectura);
        applyNumberFilter(filas);
        applyNumberFilter(tiempo);

        // Configurar el tamaño del botón y agregar ActionListener
        simularButton.setPreferredSize(new Dimension(200, 100));
        simularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSimularButtonClick();
            }
        });

        // Crear la tabla con el encabezado doble
        String[] columnNames = {
                "evento", "reloj", "Llegada_rnd1", "tiempo", "proxLlegada",
                "Cola Atención", "Ocupacion Actual", "S1_estado", "S1_cliente",
                "S1_rndCaso", "S1_caso", "S1_rndTiempo", "S1_duracionA",
                "S1_finAtencion", "S2_estado", "S2_cliente", "S2_rndCaso",
                "S2_caso", "S2_rndTiempo", "S2_duracionA", "S2_finAtencion",
                "C1_estado", "C1_TiempoEntrada", "C1_rndDesicion", "C1_rndTiempoLectura","C1_TiempoLectura","C1_TiempoSalida",
                "C2_estado", "C2_TiempoEntrada", "C2_rndDesicion", "C2_rndTiempoLectura","C2_TiempoLectura","C2_TiempoSalida",
                "C3_estado", "C3_TiempoEntrada", "C3_rndDesicion", "C3_rndTiempoLectura","C3_TiempoLectura","C3_TiempoSalida",
                "C4_estado", "C4_TiempoEntrada", "C4_rndDesicion", "C4_rndTiempoLectura","C4_TiempoLectura","C4_TiempoSalida",
                "C5_estado", "C5_TiempoEntrada", "C5_rndDesicion", "C5_rndTiempoLectura","C5_TiempoLectura","C5_TiempoSalida",
                "C6_estado", "C6_TiempoEntrada", "C6_rndDesicion", "C6_rndTiempoLectura","C6_TiempoLectura","C6_TiempoSalida",
                "C7_estado", "C7_TiempoEntrada", "C7_rndDesicion", "C7_rndTiempoLectura","C7_TiempoLectura","C7_TiempoSalida",
                "C8_estado", "C8_TiempoEntrada", "C8_rndDesicion", "C8_rndTiempoLectura","C8_TiempoLectura","C8_TiempoSalida",
                "C9_estado", "C9_TiempoEntrada", "C9_rndDesicion", "C9_rndTiempoLectura","C9_TiempoLectura","C9_TiempoSalida",
                "C10_estado", "C10_TiempoEntrada", "C10_rndDesicion", "C10_rndTiempoLectura","C10_TiempoLectura","C10_TiempoSalida",
                "C11_estado", "C11_TiempoEntrada", "C11_rndDesicion", "C11_rndTiempoLectura","C11_TiempoLectura","C11_TiempoSalida",
                "C12_estado", "C12_TiempoEntrada", "C12_rndDesicion", "C12_rndTiempoLectura","C12_TiempoLectura","C12_TiempoSalida",
                "C13_estado", "C13_TiempoEntrada", "C13_rndDesicion", "C13_rndTiempoLectura","C13_TiempoLectura","C13_TiempoSalida",
                "C14_estado", "C14_TiempoEntrada", "C14_rndDesicion", "C14_rndTiempoLectura","C14_TiempoLectura","C14_TiempoSalida",
                "C15_estado", "C15_TiempoEntrada", "C15_rndDesicion", "C15_rndTiempoLectura","C15_TiempoLectura","C15_TiempoSalida",
                "C16_estado", "C16_TiempoEntrada", "C16_rndDesicion", "C16_rndTiempoLectura","C16_TiempoLectura","C16_TiempoSalida",
                "C17_estado", "C17_TiempoEntrada", "C17_rndDesicion", "C17_rndTiempoLectura","C17_TiempoLectura","C17_TiempoSalida",
                "C18_estado", "C18_TiempoEntrada", "C18_rndDesicion", "C18_rndTiempoLectura","C18_TiempoLectura","C18_TiempoSalida",
                "C19_estado", "C19_TiempoEntrada", "C19_rndDesicion", "C19_rndTiempoLectura","C19_TiempoLectura","C19_TiempoSalida",
                "C20_estado", "C20_TiempoEntrada", "C20_rndDesicion", "C20_rndTiempoLectura","C20_TiempoLectura","C20_TiempoSalida"


        };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        table1.setModel(model);
        scrollPanel.setViewportView(table1);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVisible(true);
    }

    private void applyNumberFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            private final String regex = "\\d*\\.?\\d*";

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.isEmpty() || string.matches(regex)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.isEmpty() || text.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    private void addDocumentListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onTextChange();
            }
        });
    }

    private void onTextChange() {
        try {
            double probSocioValue = Double.parseDouble(probSocio.getText());
            double probDevolverValue = Double.parseDouble(probDevolver.getText());
            double probPedirValue = Double.parseDouble(probPedir.getText());
            double tiempoMinValue = Double.parseDouble(tiempoMin.getText());
            double tiempoMaxValue = Double.parseDouble(tiempoMax.getText());
            double probLecturaValue = Double.parseDouble(probLectura.getText());
            double promLecturaValue = Double.parseDouble(promLectura.getText());
            double frecLlegadaValue = Double.parseDouble(frecLlegada.getText());
            int filasValue = Integer.parseInt(filas.getText());
            int tiempoValue = Integer.parseInt(tiempo.getText());

            if (probSocioValue + probDevolverValue + probPedirValue != 1) {
                simularButton.setEnabled(false);
                return;
            }

            if (tiempoMinValue < 0 || tiempoMaxValue < 0 || tiempoMinValue >= tiempoMaxValue) {
                simularButton.setEnabled(false);
                return;
            }

            if (frecLlegadaValue <= 0 || probLecturaValue <= 0 || probLecturaValue >= 1 || promLecturaValue <= 0) {
                simularButton.setEnabled(false);
                return;
            }

            if (filasValue <= 0 || tiempoValue <= 0) {
                simularButton.setEnabled(false);
                return;
            }

            simularButton.setEnabled(true);
        } catch (NumberFormatException e) {
            simularButton.setEnabled(false);
        }
    }

    private void onSimularButtonClick() {
        // Aquí va la lógica que quieres ejecutar cuando se hace clic en el botón "Simular"
        System.out.println("Botón 'Simular' clickeado");
        // Puedes agregar la lógica que necesites aquí, por ejemplo:
        // - Leer y procesar los datos de los JTextFields
        // - Realizar cálculos o simulaciones
        // - Mostrar resultados en la tabla o en otro componente
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        new Simulador();
    }
}
