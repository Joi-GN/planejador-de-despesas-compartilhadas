
import javax.swing.SwingUtilities;
import view.TelaPrincipal;

public class PlanejadorDeDespesasCompartilhadas {

    public static void main(String[] args) {
        // Inicializa a Interface Gráfica Swing
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}
