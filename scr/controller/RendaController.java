package controller;

import java.util.Date;
import java.util.List;
import model.Renda;
import model.RendaDAO;

public class RendaController {

    private RendaDAO dao = new RendaDAO();

    public void cadastrarRenda(String nome, double valor, Date data, boolean tipo) {
        if (valor <= 0) {
            System.out.println("Erro: O valor deve ser maior que zero.");
            return;
        }
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("Erro: O nome é obrigatório.");
            return;
        }

        Renda novaRenda = dao.cadastrarRenda(nome, valor, data, tipo);
        
        if (novaRenda != null) {
            System.out.println("Sucesso: Renda " + novaRenda.getNomeRenda() + " cadastrada.");
        }
    }

    public void editarRenda(String id, String nome, double valor) {
        if (valor <= 0) {
            System.out.println("Erro: O valor deve ser maior que zero.");
            return;
        }
        dao.editarRenda(id, nome, valor);
    }

    public void excluirRenda(String id) {
        Renda rendaParaExcluir = new Renda();
        rendaParaExcluir.setIdRenda(id);
        
        dao.excluirRenda(rendaParaExcluir);
    }

    public void visualizarRenda(String id) {
        dao.visualizarRenda(id);
    }


    public List<Renda> listarRendasExtras() {
        return dao.listarRendasExtras();
    }

    public List<Renda> listarRendasFixas() {
        return dao.listarRendasFixas();
    }

    public double calcularRendaTotalMensal(int mes, int ano) {
        if (mes < 1 || mes > 12) {
            System.out.println("Erro: Mês inválido.");
            return 0.0;
        }
        return dao.calcularRendaTotalMensal(mes, ano);
    }
}