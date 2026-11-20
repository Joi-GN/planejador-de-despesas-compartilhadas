import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import view.TelaPrincipal;

public class PlanejadorDeDespesasCompartilhadas {

    public static void main(String[] args) {
        //Seta o tema
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        // Inicializa a Interface Gráfica Swing
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}
