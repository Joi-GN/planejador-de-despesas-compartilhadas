package model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class Despesa implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String descricao;
    protected double valor;
    protected List<Participante> participantesEnvolvidos;

    public Despesa(String descricao, double valor, List<Participante> participantesEnvolvidos) {
        if (valor <= 0) throw new IllegalArgumentException("O valor deve ser maior que zero.");
        if (participantesEnvolvidos == null || participantesEnvolvidos.isEmpty()) 
            throw new IllegalArgumentException("A despesa deve ter pelo menos um participante.");
        this.descricao = descricao;
        this.valor = valor;
        this.participantesEnvolvidos = participantesEnvolvidos;
    }

    public abstract void calcularRateio(Map<Participante, Double> extratoIndividual);

    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public List<Participante> getParticipantesEnvolvidos() { return participantesEnvolvidos; }
}