package model;

import java.io.Serializable;

public class Participante implements Serializable {
    private static final long serialVersionUID = 1L;
    private int codigo;
    private String nome;
    private double orcamento;

    public Participante(int codigo, String nome, double orcamento) {
        if (orcamento < 0) throw new IllegalArgumentException("O orçamento não pode ser negativo.");
        this.codigo = codigo;
        this.nome = nome;
        this.orcamento = orcamento;
    }

    public int getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public double getOrcamento() { return orcamento; }
    public void setNome(String nome) { this.nome = nome; }
    public void setOrcamento(double orcamento) { this.orcamento = orcamento; }

    @Override
    public String toString() {
        return codigo + " - " + nome + " (R$ " + String.format("%.2f", orcamento) + ")";
    }
}
