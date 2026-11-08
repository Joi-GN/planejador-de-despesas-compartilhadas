import java.util.Arrays;
import java.util.List;
import javax.swing.SwingUtilities;
import model.DespesaFixa;
import model.DespesaPorPessoa;
import model.Participante;
import model.Planejamento;
import view.TelaPrincipal;

public class PlanejadorDeDespesasCompartilhadas {

     public static void main(String[] args) {
        Planejamento planejamento = new Planejamento();

        // Carrega automaticamente o caso de referência inicial da Seção 4 para testes rápidos
        try {
            Participante ana = new Participante(1, "Ana", 70.00);
            Participante bruno = new Participante(2, "Bruno", 60.00);
            Participante carla = new Participante(3, "Carla", 70.00);
            planejamento.adicionarParticipante(ana);
            planejamento.adicionarParticipante(bruno);
            planejamento.adicionarParticipante(carla);

            List<Participante> todos = Arrays.asList(ana, bruno, carla);
            List<Participante> anaCarla = Arrays.asList(ana, carla);

            planejamento.adicionarDespesa(new DespesaFixa("Transporte", 90.00, todos));
            planejamento.adicionarDespesa(new DespesaPorPessoa("Almoço", 20.00, todos));
            planejamento.adicionarDespesa(new DespesaFixa("Atividade recreativa", 30.00, anaCarla));
            planejamento.adicionarDespesa(new DespesaPorPessoa("Sobremesa", 10.00, todos));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicializa a Interface Gráfica Swing
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal(planejamento).setVisible(true);
        });
    }
}
