import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Simulador extends JFrame {
    private JPanel Main;
    private JButton button1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField primero;
    private JTextField textField4;
    private JTextField textField5;
    private JTable table1;
    private JTextField textField6;

    public Simulador() {
        add(Main);
        setTitle("Simulador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        addDocumentListener(primero);

    }
    private void addDocumentListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTextChange(textField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTextChange(textField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onTextChange(textField);
            }
        });
    }

    private void onTextChange(JTextField textField) {
        // Aquí llamas a tu función X cuando haya un cambio en el contenido del JTextField
        System.out.println("Cambio en el " );
    }

    public static void main(String[] args) {
            new Simulador();
    }
}