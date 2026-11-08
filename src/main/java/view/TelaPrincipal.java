package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Despesa;
import model.DespesaFixa;
import model.DespesaPorPessoa;
import model.Participante;
import model.Planejamento;

public class TelaPrincipal extends JFrame {
    private Planejamento planejamento;
    
    private JTextField txtCodParticipante, txtNomeParticipante, txtOrcamentoParticipante;
    private DefaultListModel<Participante> modelParticipantes;
    
    private JTextField txtDescricaoDespesa, txtValorDespesa;
    private JComboBox<String> cbTipoDespesa;
    private JList<Participante> listSelecaoParticipantes;

    private JTable tabelaResumo;
    private DefaultTableModel modelTabelaResumo;
    private JLabel lblCustoTotal;

    public TelaPrincipal(Planejamento planejamento) {
        this.planejamento = planejamento;
        setTitle("Cabe no Bolso - Planejador de Despesas Compartilhadas");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.add("Participantes", criarPainelParticipantes());
        abas.add("Despesas", criarPainelDespesas());
        abas.add("Resumo e Rateio Detalhado", criarPainelResumo());

        add(abas);
    }

    private JPanel criarPainelParticipantes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Código (inteiro):"));
        txtCodParticipante = new JTextField();
        form.add(txtCodParticipante);

        form.add(new JLabel("Nome:"));
        txtNomeParticipante = new JTextField();
        form.add(txtNomeParticipante);

        form.add(new JLabel("Orçamento Disponível (R$):"));
        txtOrcamentoParticipante = new JTextField();
        form.add(txtOrcamentoParticipante);

        JButton btnAdicionar = new JButton("Cadastrar Participante");
        form.add(new JLabel(""));
        form.add(btnAdicionar);

        painel.add(form, BorderLayout.NORTH);

        modelParticipantes = new DefaultListModel<>();
        atualizarListaParticipantes();
        painel.add(new JScrollPane(new JList<>(modelParticipantes)), BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> {
            try {
                int cod = Integer.parseInt(txtCodParticipante.getText().trim());
                String nome = txtNomeParticipante.getText().trim();
                double orc = Double.parseDouble(txtOrcamentoParticipante.getText().trim().replace(",", "."));

                if (nome.isEmpty()) throw new IllegalArgumentException("O nome não pode estar vazio.");

                Participante p = new Participante(cod, nome, orc);
                planejamento.adicionarParticipante(p);
                atualizarListaParticipantes();
                
                txtCodParticipante.setText("");
                txtNomeParticipante.setText("");
                txtOrcamentoParticipante.setText("");
                JOptionPane.showMessageDialog(this, "Participante cadastrado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: Código e Orçamento devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painel;
    }

    private JPanel criarPainelDespesas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Descrição da Despesa:"));
        txtDescricaoDespesa = new JTextField();
        form.add(txtDescricaoDespesa);

        form.add(new JLabel("Valor (Total ou Unitário):"));
        txtValorDespesa = new JTextField();
        form.add(txtValorDespesa);

        form.add(new JLabel("Tipo de Despesa:"));
        cbTipoDespesa = new JComboBox<>(new String[]{"Fixa (Dividida entre selecionados)", "Por Pessoa (Cobrada por integrante)"});
        form.add(cbTipoDespesa);

        form.add(new JLabel("Participantes Envolvidos (Segure CTRL para vários):"));
        listSelecaoParticipantes = new JList<>();
        atualizarSelecaoParticipantes();
        form.add(new JScrollPane(listSelecaoParticipantes));

        JButton btnAdicionarDespesa = new JButton("Incluir Despesa");
        form.add(new JLabel(""));
        form.add(btnAdicionarDespesa);

        painel.add(form, BorderLayout.CENTER);

        btnAdicionarDespesa.addActionListener(e -> {
            try {
                String desc = txtDescricaoDespesa.getText().trim();
                double val = Double.parseDouble(txtValorDespesa.getText().trim().replace(",", "."));
                List<Participante> selecionados = listSelecaoParticipantes.getSelectedValuesList();

                if (desc.isEmpty()) throw new IllegalArgumentException("Informe a descrição.");
                if (selecionados.isEmpty()) throw new IllegalArgumentException("Selecione ao menos um participante.");

                Despesa d;
                if (cbTipoDespesa.getSelectedIndex() == 0) {
                    d = new DespesaFixa(desc, val, selecionados);
                } else {
                    d = new DespesaPorPessoa(desc, val, selecionados);
                }

                planejamento.adicionarDespesa(d);
                txtDescricaoDespesa.setText("");
                txtValorDespesa.setText("");
                JOptionPane.showMessageDialog(this, "Despesa incluída com sucesso!");
                atualizarTabelaResumo();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erro: O valor deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painel;
    }

    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelTabelaResumo = new DefaultTableModel();
        tabelaResumo = new JTable(modelTabelaResumo);
        painel.add(new JScrollPane(tabelaResumo), BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        lblCustoTotal = new JLabel("Custo Total do Passeio: R$ 0,00");
        lblCustoTotal.setFont(new Font("Arial", Font.BOLD, 14));
        rodape.add(lblCustoTotal);

        JButton btnAtualizar = new JButton("Atualizar / Recalcular Resumo");
        btnAtualizar.addActionListener(e -> atualizarTabelaResumo());
        rodape.add(btnAtualizar);

        painel.add(rodape, BorderLayout.SOUTH);

        atualizarTabelaResumo();
        return painel;
    }

    private void atualizarListaParticipantes() {
        modelParticipantes.clear();
        for (Participante p : planejamento.getParticipantes()) {
            modelParticipantes.addElement(p);
        }
    }

    private void atualizarSelecaoParticipantes() {
        DefaultListModel<Participante> modeloLista = new DefaultListModel<>();
        for (Participante p : planejamento.getParticipantes()) {
            modeloLista.addElement(p);
        }
        listSelecaoParticipantes.setModel(modeloLista);
    }

    private void atualizarTabelaResumo() {
        atualizarSelecaoParticipantes();

        List<Despesa> despesas = planejamento.getDespesas();
        List<Participante> participantes = planejamento.getParticipantes();

        // 1. Montar dinamicamente as colunas: Participante | [Despesa 1] | [Despesa 2] ... | Total | Situação
        List<String> colunas = new ArrayList<>();
        colunas.add("Participante");
        colunas.add("Orçamento");
        for (Despesa d : despesas) {
            colunas.add(d.getDescricao());
        }
        colunas.add("Total Individual");
        colunas.add("Situação");

        modelTabelaResumo.setColumnIdentifiers(colunas.toArray());
        modelTabelaResumo.setRowCount(0);

        // 2. Calcular o detalhamento individual por despesa
        // Mapeia cada participante para um mapa de (Nome da Despesa -> Valor gasto)
        Map<Participante, Double> totaisGerais = planejamento.calcularTotaisIndividuais();

        for (Participante p : participantes) {
            List<Object> linha = new ArrayList<>();
            linha.add(p.getNome());
            linha.add(String.format("R$ %.2f", p.getOrcamento()));

            double totalInd = 0.0;

            for (Despesa d : despesas) {
                // Calcula quanto essa pessoa específica gastou nesta despesa isolada
                Map<Participante, Double> extratoItem = new java.util.HashMap<>();
                d.calcularRateio(extratoItem);
                double gastoItem = extratoItem.getOrDefault(p, 0.0);
                totalInd += gastoItem;

                linha.add(String.format("R$ %.2f", gastoItem));
            }

            linha.add(String.format("R$ %.2f", totalInd));

            String situacao;
            if (totalInd <= p.getOrcamento()) {
                situacao = "Dentro do orçamento";
            } else {
                situacao = "Excedeu em R$ " + String.format("%.2f", totalInd - p.getOrcamento());
            }
            linha.add(situacao);

            modelTabelaResumo.addRow(linha.toArray());
        }

        lblCustoTotal.setText("Custo Total do Passeio: R$ " + String.format("%.2f", planejamento.calcularCustoTotal()));
    }
}
