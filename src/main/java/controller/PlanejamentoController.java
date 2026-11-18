package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import model.Despesa;
import model.DespesaFixa;
import model.DespesaPorPessoa;
import model.Participante;
import model.Planejamento;

/**
 *
 * @author Joice
 */
public class PlanejamentoController {

    private final List<Planejamento> planejamentos = new ArrayList<>();

    public Planejamento criarPlanejamento(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do planejamento não pode estar vazio.");
        }
        Planejamento p = new Planejamento(nome.trim());
        planejamentos.add(p);
        p.setId(planejamentos.size());
        return p;
    }

    public void excluirPlanejamento(Planejamento p) {
        planejamentos.remove(p);
    }

    public List<Planejamento> listarPlanejamentos() {
        return Collections.unmodifiableList(planejamentos);
    }

    public void adicionarParticipante(Planejamento p, int codigo, String nome, double orcamento) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode estar vazio.");
        }
        p.adicionarParticipante(new Participante(codigo, nome.trim(), orcamento));
    }

    public boolean existeCodigo(Planejamento p, int codigo) {
        return p.existeParticipanteComCodigo(codigo);
    }

    public void adicionarDespesa(Planejamento p, String descricao, double valor,
            boolean fixa, List<Participante> envolvidos) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descrição.");
        }
        if (envolvidos == null || envolvidos.isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos um participante.");
        }
        Despesa d = fixa
                ? new DespesaFixa(descricao.trim(), valor, envolvidos)
                : new DespesaPorPessoa(descricao.trim(), valor, envolvidos);
        p.adicionarDespesa(d);
    }

    public List<String> montarColunasResumo(Planejamento p) {
        List<String> colunas = new ArrayList<>(List.of("Participantes", "Orçamento"));
        for (Despesa d : p.getDespesas()) {
            colunas.add(d.getDescricao());
        }
        colunas.add("Total Individual");
        colunas.add("Situação");
        return colunas;
    }

    public List<Object[]> montarLinhasResumo(Planejamento p) {
        List<Map<Participante, Double>> rateios = new ArrayList<>();
        for (Despesa d : p.getDespesas()) {
            Map<Participante, Double> mapa = new HashMap<>();
            d.calcularRateio(mapa);
            rateios.add(mapa);
        }

        List<Object[]> linhas = new ArrayList<>();
        for (Participante part : p.getParticipantes()) {
            List<Object> linha = new ArrayList<>();
            linha.add(part.getNome());
            linha.add(String.format(new Locale("pt", "BR"), "R$ %.2f", part.getOrcamento()));

            double total = 0.0;
            for (Map<Participante, Double> mapa : rateios) {
                double gasto = mapa.getOrDefault(part, 0.0);
                total += gasto;
                linha.add(String.format(new Locale("pt", "BR"), "R$ %.2f", gasto));
            }

            linha.add(String.format(new Locale("pt", "BR"), "R$ %.2f", total));
            linha.add(situacao(part, total));
            linhas.add(linha.toArray());
        }
        return linhas;
    }

    public String calcularCustoTotal(Planejamento p) {
        if (p == null) {
            return "Custo Total: R$ 0,00";
        } else {
            return "Custo Total: " + String.format(new Locale("pt", "BR"), "R$ %.2f", p.calcularCustoTotal());
        }
    }

    public String situacao(Participante part, double total) {
        if (total <= part.getOrcamento()) {
            return "Dentro do orçamento";
        }
        return "Excedeu em " + String.format(new Locale("pt", "BR"), "R$ %.2f", total - part.getOrcamento());
    }
}
