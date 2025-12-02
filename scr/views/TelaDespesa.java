package views;

import dao.CategoriaDAO;
import dao.DespesaDAO;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import model.Categoria;
import model.Despesa;
import model.Sessao;
import model.UtilData;

public class TelaDespesa {

    private Scanner leitor = new Scanner(System.in);

    public void exibirMenu() {

        while (true) {

            System.out.println("\n====== MENU DE DESPESAS ======");
            System.out.println("1 - Cadastrar Despesa");
            System.out.println("2 - Listar Despesas");
            System.out.println("3 - Editar Despesa");
            System.out.println("4 - Excluir Despesa");
            System.out.println("5 - Visualizar Despesa");
            System.out.println("6 - Listar por Categoria");
            System.out.println("7 - Listar por Período");
            System.out.println("8 - Buscar por Termo");
            System.out.println("9 - Total Mensal");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            String entrada = leitor.nextLine().trim();

            int opcao;

            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números.");
                continue;
            }

            if (opcao == 0) break;

            switch (opcao) {
                case 1 -> cadastrarDespesa();
                case 2 -> listarDespesas();
                case 3 -> editarDespesa();
                case 4 -> excluirDespesa();
                case 5 -> visualizarDespesa();
                case 6 -> listarPorCategoria();
                case 7 -> listarPorPeriodo();
                case 8 -> buscarDespesa();
                case 9 -> totalMensal();
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void cadastrarDespesa() {

        System.out.print("Nome da despesa: ");
        String nome = leitor.nextLine();

        System.out.print("Valor: ");
        double valor;
        try {
            valor = Double.parseDouble(leitor.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Tente novamente.");
            return; 
        }

        System.out.print("Data (dd/MM/yyyy): ");
        Date data = UtilData.parseDataUsuario(leitor.nextLine());
        
        if (data == null) {
            System.out.println("Data inválida. Tente novamente.");
            return;
        }

        List<Categoria> categorias = Categoria.listarCategorias();

        if (categorias.isEmpty()) {
            System.out.println("Você não possui categorias cadastradas.");
            return;
        }

        System.out.println("\n--- Categorias disponíveis ---");
        for (int i = 0; i < categorias.size(); i++) {
            Categoria c = categorias.get(i);
            System.out.println("[" + i + "] " + c.getNomeCategoria() + " (ID: " + c.getIdCategoria() + ")");
        }
        System.out.println("------------------------------");

        System.out.print("Digite a POSIÇÃO da categoria (ex: 0, 1): ");
        String entradaPosicao = leitor.nextLine();
        
        int posicao;
        try {
            posicao = Integer.parseInt(entradaPosicao);
        } catch (NumberFormatException e) {
            System.out.println("Posição inválida. Por favor, digite um número inteiro.");
            return;
        }

        if (posicao < 0 || posicao >= categorias.size()) {
            System.out.println("Posição fora do intervalo. Digite um número de 0 a " + (categorias.size() - 1));
            return;
        }
        
        Categoria categoriaSelecionada = categorias.get(posicao);
        String idCategoria = categoriaSelecionada.getIdCategoria(); 

        if (!categoriaSelecionada.getStatus()) {
            System.out.println("Categoria desativada. Não é possível cadastrar despesas nela.");
            return;
        }
        
        Despesa despesa = new Despesa(
            nome,
            valor,
            data,
            categoriaSelecionada,
            Sessao.getIdUsuarioLogado()
        );

        if (Despesa.cadastrarDespesa(despesa)) {
            System.out.println("Despesa cadastrada com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar despesa.");
        }
    }

    private void listarDespesas() {

        List<Despesa> lista = Despesa.listarDespesas();

        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        for (Despesa d : lista) {
            d.visualizarDespesa();
        }
    }

    private void editarDespesa() {

        System.out.print("Digite o ID da despesa: ");
        String id = leitor.nextLine();

        DespesaDAO dao = new DespesaDAO();
        Despesa d = dao.buscarPorId(id, Sessao.getIdUsuarioLogado());

        if (d == null) {
            System.out.println("Despesa não encontrada.");
            return;
        }

        System.out.print("Novo nome: ");
        d.setNomeDespesa(leitor.nextLine());

        System.out.print("Novo valor: ");
        d.setValor(Double.parseDouble(leitor.nextLine()));

        System.out.print("Nova data (dd/MM/yyyy): ");
        d.setData(UtilData.parseDataUsuario(leitor.nextLine()));

        System.out.print("Novo ID de categoria: ");
        String idCategoria = leitor.nextLine();

        Categoria categoria = new CategoriaDAO().buscarCategoriaPorId(idCategoria);

        if (categoria == null) {
            System.out.println("Categoria inválida.");
            return;
        }

        if (!categoria.getStatus()) {
            System.out.println("Categoria desativada. Não é possível mover a despesa para ela.");
            return;
        }

        d.setCategoria(categoria);

        d.editarDespesa();
        System.out.println("Despesa atualizada!");
    }

    private void excluirDespesa() {

        System.out.print("Digite o ID da despesa: ");
        String id = leitor.nextLine();

        if (Despesa.excluirDespesa(id)) {
            System.out.println("Despesa excluída com sucesso.");
        } else {
            System.out.println("Erro ao excluir.");
        }
    }

    private void visualizarDespesa() {

        System.out.print("Digite um termo para buscar a despesa: ");
        String termo = leitor.nextLine();

        Despesa d = Despesa.buscarDespesa(termo);

        if (d == null) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        d.visualizarDespesa();
    }

    private void listarPorCategoria() {

        System.out.print("Digite o ID da categoria: ");
        String idCategoria = leitor.nextLine();

        List<Despesa> lista = Despesa.listarDespesasPorCategoria(idCategoria);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        for (Despesa d : lista) {
            d.visualizarDespesa();
        }
    }

    private void listarPorPeriodo() {

        System.out.print("Data inicial (dd/MM/yyyy): ");
        Date inicio = UtilData.parseDataUsuario(leitor.nextLine());

        System.out.print("Data final (dd/MM/yyyy): ");
        Date fim = UtilData.parseDataUsuario(leitor.nextLine());

        List<Despesa> lista = Despesa.listarDespesasPorPeriodo(inicio, fim);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        for (Despesa d : lista) {
            d.visualizarDespesa();
        }
    }

    private void buscarDespesa() {

        System.out.print("Digite um termo para buscar: ");
        String termo = leitor.nextLine();

        Despesa d = Despesa.buscarDespesa(termo);

        if (d == null) {
            System.out.println("Nenhuma despesa encontrada.");
            return;
        }

        d.visualizarDespesa();
    }

    private void totalMensal() {

        System.out.print("Digite o mês (1-12): ");
        int mes = Integer.parseInt(leitor.nextLine());

        System.out.print("Digite o ano: ");
        int ano = Integer.parseInt(leitor.nextLine());

        double total = Despesa.calcularDespesaTotalMensal(mes, ano);

        System.out.println("Total de despesas no período: R$ " + total);
    }
}