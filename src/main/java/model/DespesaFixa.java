package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DespesaFixa extends Despesa {
    private static final long serialVersionUID = 1L;

    public DespesaFixa(String descricao, double valorTotal, List<Participante> participantes) {
        super(descricao, valorTotal, participantes);
    }

    @Override
    public void calcularRateio(Map<Participante, Double> extratoIndividual) {
        int qtd = participantesEnvolvidos.size();
        double valorBase = Math.floor((valor / qtd) * 100.0) / 100.0;
        double resto = Math.round((valor - (valorBase * qtd)) * 100.0) / 100.0;

        List<Participante> ordenados = new ArrayList<>(participantesEnvolvidos);
        ordenados.sort(Comparator.comparingInt(Participante::getCodigo));

        int centavosRestantes = (int) Math.round(resto * 100);

        for (int i = 0; i < ordenados.size(); i++) {
            Participante p = ordenados.get(i);
            double acrescimo = (i < centavosRestantes) ? 0.01 : 0.0;
            double cota = valorBase + acrescimo;
            extratoIndividual.put(p, extratoIndividual.getOrDefault(p, 0.0) + cota);
        }
    }
}