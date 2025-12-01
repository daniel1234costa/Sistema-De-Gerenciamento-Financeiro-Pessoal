package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.RendaDAO;

public class Renda {
    private String idRenda;
    private String nomeRenda;
    private double valor;
    private Date data;
    private boolean tipoRenda;
    private String idUsuario;

    public Renda() {}

    public Renda(String nomeRenda, double valor, Date data, boolean tipoRenda) {
        this.nomeRenda = nomeRenda;
        this.valor = valor;
        this.data = data;
        this.tipoRenda = tipoRenda;
    }

    // --- MÉTODOS ESTÁTICOS (Model chama DAO) ---

    public static Renda cadastrarRenda(String nome, double valor, Date data, boolean tipo) {
        if (valor <= 0) {
            System.out.println("Erro: O valor deve ser positivo.");
            return null;
        }
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Erro: O nome é obrigatório.");
            return null;
        }
        
        // VERIFICAÇÃO DE LOGIN COM SUA CLASSE SESSAO
        if (!Sessao.isLogado()) {
            System.out.println("Erro: Nenhum usuário logado.");
            return null;
        }

        Renda novaRenda = new Renda(nome, valor, data, tipo);
        // Pega o ID direto da Sessão (que é String)
        novaRenda.setIdUsuario(Sessao.getIdUsuarioLogado()); 

        RendaDAO dao = new RendaDAO();
        return dao.cadastrarRenda(novaRenda);
    }

    public static boolean excluirRenda(Renda renda) {
        RendaDAO dao = new RendaDAO();
        // Cria objeto temporário com ID para passar pro DAO
        Renda r = new Renda();
        r.setIdRenda(id);
        return dao.excluirRenda(r);
    }

    public static List<Renda> listarRendasExtras() {
        if (!Sessao.isLogado()) return new ArrayList<>(); 

        RendaDAO dao = new RendaDAO();
        // Passa o ID da Sessão
        return dao.listarRendasExtras(Sessao.getIdUsuarioLogado());
    }

    public static List<Renda> listarRendasFixas() {
        if (!Sessao.isLogado()) return new ArrayList<>();

        RendaDAO dao = new RendaDAO();
        return dao.listarRendasFixas(Sessao.getIdUsuarioLogado());
    }

    public static double calcularRendaTotalMensal(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            System.out.println("Erro: Mês inválido.");
            return 0.0;
        }
        if (!Sessao.isLogado()) return 0.0;

        RendaDAO dao = new RendaDAO();
        return dao.calcularRendaTotalMensal(mes, ano, Sessao.getIdUsuarioLogado());
    }

    // --- MÉTODOS DE INSTÂNCIA ---

    public void editarRenda(String nome, double valor) {
        if (valor <= 0) {
            System.out.println("Erro: O valor deve ser positivo.");
            return;
        }
        RendaDAO dao = new RendaDAO();
        dao.editarRenda(this.idRenda, nome, valor);
    }

    public void visualizarRenda() {
        RendaDAO dao = new RendaDAO();
        dao.visualizarRenda(this.idRenda);
    }

    // Getters e Setters
    public String getIdRenda() { return idRenda; }
    public void setIdRenda(String idRenda) { this.idRenda = idRenda; }
    public String getNomeRenda() { return nomeRenda; }
    public void setNomeRenda(String nomeRenda) { this.nomeRenda = nomeRenda; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
    public boolean isTipoRenda() { return tipoRenda; }
    public void setTipoRenda(boolean tipoRenda) { this.tipoRenda = tipoRenda; }
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
}