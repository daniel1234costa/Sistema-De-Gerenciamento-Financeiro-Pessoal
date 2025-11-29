package controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import model.Categoria;
import model.Despesa;
import model.DespesaDAO;

public class DespesaController {

    private DespesaDAO dao;

    public DespesaController() {
        this.dao = new DespesaDAO();
    }

    public void cadastrarDespesa(String nome, double valor, Date data, Categoria categoria) {
        String id = UUID.randomUUID().toString();
        Despesa despesa = new Despesa(id, nome, valor, data, categoria);
        dao.cadastrarDespesa(despesa);
    }

    public void editarDespesa(String id, String nome, double valor, Date data, Categoria categoria) {
        dao.editarDespesa(id, nome, valor, data, categoria);
    }

    public boolean excluirDespesa(String id) {
        return dao.excluirDespesa(id);
    }

    public List<Despesa> listarDespesas() {
        return dao.listarDespesas();
    }

    public Despesa buscarDespesa(String termo) {
        return dao.buscarDespesa(termo);
    }

    public List<Despesa> listarDespesasPorPeriodo(Date inicio, Date fim) {
        return dao.listarDespesasPorPeriodo(inicio, fim);
    }

    public List<Despesa> listarDespesasPorCategoria(Categoria categoria) {
        return dao.listarDespesasPorCategoria(categoria);
    }

    public double calcularDespesaTotalMensal(int mes, int ano) {
        return dao.calcularDespesaTotalMensal(mes, ano);
    }

    public void visualizarDespesa(String id) {
        Despesa despesa = dao.buscarDespesaPorId(id);
        if (despesa != null) {
            despesa.visualizarDespesa();
        } else {
            System.out.println("Despesa não encontrada.");
        }
    }
}