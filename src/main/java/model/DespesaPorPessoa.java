package model;

import java.util.List;
import java.util.Map;

public class DespesaPorPessoa extends Despesa {
    private static final long serialVersionUID = 1L;

    public DespesaPorPessoa(String descricao, double valorUnitario, List<Participante> participantes) {
        super(descricao, valorUnitario, participantes);
    }

    @Override
    public void calcularRateio(Map<Participante, Double> extratoIndividual) {
        for (Participante p : participantesEnvolvidos) {
            extratoIndividual.put(p, extratoIndividual.getOrDefault(p, 0.0) + valor);
        }
    }
}
