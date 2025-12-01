package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import dao.DespesaDAO;

public class Despesa {

    private String idDespesa;
    private String nomeDespesa;
    private double valor;
    private Date data;
    private Categoria categoria;
    private String idUsuario;

    public Despesa() {}

    public Despesa(String nome, double valor, Date data, Categoria categoria, String idUsuario) {
        this.nomeDespesa = nome;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.idUsuario = idUsuario;
    }

    public static boolean cadastrarDespesa(Despesa despesa) {

        // ✅ Impede cadastro se a categoria estiver desativada
        if (despesa.getCategoria() == null || !despesa.getCategoria().getStatus()) {
            System.out.println("Erro: a categoria está desativada. Não é possível cadastrar a despesa.");
            return false;
        }

        return new DespesaDAO().cadastrarDespesa(despesa);
    }

    public static boolean excluirDespesa(String idDespesa) {
        return new DespesaDAO().excluirDespesa(idDespesa, Sessao.getIdUsuarioLogado());
    }

    public static List<Despesa> listarDespesas() {
        return new DespesaDAO().listarDespesas(Sessao.getIdUsuarioLogado());
    }

    public static List<Despesa> listarDespesasPorPeriodo(Date inicio, Date fim) {
        return new DespesaDAO().listarDespesasPorPeriodo(
            Sessao.getIdUsuarioLogado(), inicio, fim
        );
    }

    public static double calcularDespesaTotalMensal(int mes, int ano) {
        return new DespesaDAO().calcularDespesaTotalMensal(mes, ano, Sessao.getIdUsuarioLogado());
    }

    public static Despesa buscarDespesa(String termoBusca) {
        return new DespesaDAO().buscarPorTermo(
            termoBusca, Sessao.getIdUsuarioLogado()
        );
    }

    public static List<Despesa> listarDespesasPorCategoria(String idCategoria) {
        return new DespesaDAO().listarPorCategoria(
            idCategoria, Sessao.getIdUsuarioLogado()
        );
    }

    public void editarDespesa() {

        // ✅ Impede edição se a categoria estiver desativada
        if (this.categoria == null || !this.categoria.getStatus()) {
            System.out.println("Erro: a categoria está desativada. Não é possível editar a despesa.");
            return;
        }

        new DespesaDAO().editarDespesa(this);
    }

    public void visualizarDespesa() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("\n--- Despesa ---");
        System.out.println("ID: " + idDespesa);
        System.out.println("Descrição: " + nomeDespesa);
        System.out.println("Valor: R$ " + valor);
        System.out.println("Data: " + sdf.format(data));
        System.out.println("Categoria: " + categoria.getNomeCategoria());
        System.out.println("Categoria (ID): " + categoria.getIdCategoria());
        System.out.println("Usuário (ID): " + idUsuario);
        System.out.println("---------------------------");
    }

    public String getIdDespesa() { return idDespesa; }
    public void setIdDespesa(String idDespesa) { this.idDespesa = idDespesa; }

    public String getNomeDespesa() { return nomeDespesa; }
    public void setNomeDespesa(String nomeDespesa) { this.nomeDespesa = nomeDespesa; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
}