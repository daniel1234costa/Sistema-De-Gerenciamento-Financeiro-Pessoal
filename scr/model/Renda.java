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

    public Renda(String nomeRenda, double valor, Date data, boolean tipoRenda) {
        this.nomeRenda = nomeRenda;
        this.valor = valor;
        this.data = data;
        this.tipoRenda = tipoRenda;
    }

    public static Renda cadastrarRenda(String nome, double valor, Date data, boolean tipo) {
        if (valor <= 0) {
            System.out.println("Erro: O valor deve ser positivo.");
            return null;
        }
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Erro: O nome é obrigatório.");
            return null;
        }
        
        RendaDAO dao = new RendaDAO();
        return dao.cadastrarRenda(nome, valor, data, tipo);
    }

    public static boolean excluirRenda(Renda renda) {
       
        RendaDAO dao = new RendaDAO();
        return dao.excluirRenda(renda);
    }

    public static List<Renda> listarRendasExtras() {
        RendaDAO dao = new RendaDAO();
        return dao.listarRendasExtras();
    }

    public static List<Renda> listarRendasFixas() {
        RendaDAO dao = new RendaDAO();
        return dao.listarRendasFixas();
    }

    public static double calcularRendaTotalMensal(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            System.out.println("Erro: Mês inválido.");
            return 0.0;
        }
        RendaDAO dao = new RendaDAO();
        return dao.calcularRendaTotalMensal(mes, ano);
    }

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