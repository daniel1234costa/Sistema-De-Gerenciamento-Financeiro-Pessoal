package model;

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

    public Renda(String idUsuario, String nomeRenda, double valor, Date data, boolean tipoRenda) {
        this.idUsuario = idUsuario;
        this.nomeRenda = nomeRenda;
        this.valor = valor;
        this.data = data;
        this.tipoRenda = tipoRenda;
    }

    public static Renda cadastrarRenda(String nome, double valor, Date data, boolean tipo) {

        if (!Sessao.isLogado()) {
            System.out.println("Erro: Nenhum usuário logado.");
            return null;
        }

        Renda nova = new Renda(
            Sessao.getIdUsuarioLogado(), nome, valor, data, tipo);

        return new RendaDAO().cadastrarRenda(nova);
    }

    public static boolean excluirRenda(Renda renda) {
        return new RendaDAO().excluirRenda(renda);
    }

    public static List<Renda> listarRendasExtras() {
        return new RendaDAO().listarRendasExtras(Sessao.getIdUsuarioLogado());
    }

    public static List<Renda> listarRendasFixas() {
        return new RendaDAO().listarRendasFixas(Sessao.getIdUsuarioLogado());
    }

    public static double calcularRendaTotalMensal(int mes, int ano) {
        return new RendaDAO().calcularRendaTotalMensal(mes, ano, Sessao.getIdUsuarioLogado());
    }

    public static Renda buscarPorId(String id) {
        if (!Sessao.isLogado()) return null;
        return new RendaDAO().buscarPorId(id, Sessao.getIdUsuarioLogado());
    }

    public void editarRenda(String nome, double valor) {
        new RendaDAO().editarRenda(this.idRenda, nome, valor);
    }

    public void visualizarRenda() {
        new RendaDAO().visualizarRenda(this.idRenda);
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