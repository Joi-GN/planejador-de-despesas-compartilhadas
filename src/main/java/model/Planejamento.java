package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Planejamento implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Participante> participantes = new ArrayList<>();
    private List<Despesa> despesas = new ArrayList<>();

    public void adicionarParticipante(Participante p) {
        for (Participante existente : participantes) {
            if (existente.getCodigo() == p.getCodigo()) {
                throw new IllegalArgumentException("Já existe um participante com este código.");
            }
        }
        participantes.add(p);
    }

    public void adicionarDespesa(Despesa d) { despesas.add(d); }

    public Map<Participante, Double> calcularTotaisIndividuais() {
        Map<Participante, Double> totais = new HashMap<>();
        for (Participante p : participantes) { totais.put(p, 0.0); }
        for (Despesa d : despesas) { d.calcularRateio(totais); }
        return totais;
    }

    public double calcularCustoTotal() {
        double total = 0;
        for (double val : calcularTotaisIndividuais().values()) { total += val; }
        return Math.round(total * 100.0) / 100.0;
    }

    public List<Participante> getParticipantes() { return participantes; }
    public List<Despesa> getDespesas() { return despesas; }
}
