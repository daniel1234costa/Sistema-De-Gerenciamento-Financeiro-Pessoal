package views;

import java.util.List;
import java.util.Scanner;
import model.Categoria;

public class TelaCategoria {

    private Scanner leitor = new Scanner(System.in);

    public void exibirMenu() {

        while (true) {

            System.out.println("\n====== MENU DE CATEGORIAS ======");
            System.out.println("1 - Cadastrar Categoria");
            System.out.println("2 - Listar Categorias");
            System.out.println("3 - Buscar Categoria");
            System.out.println("4 - Editar Categoria");
            System.out.println("5 - Desativar Categoria");
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

                case 1:
                    cadastrar();
                    break;

                case 2:
                    listar();
                    break;

                case 3:
                    buscar();
                    break;

                case 4:
                    editar();
                    break;

                case 5:
                    desativar();
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrar() {
        System.out.print("Digite o nome da categoria: ");
        String nome = leitor.nextLine().trim();

        Categoria.cadastrarCategoria(nome);
    }

    private void listar() {
        List<Categoria> lista = Categoria.listarCategorias();

        if (lista.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
            return;
        }

        for (Categoria c : lista) {
            c.visualizarCategoria();
        }
    }

    private void buscar() {
        System.out.print("Digite o nome da categoria para buscar: ");
        String nome = leitor.nextLine().trim();

        Categoria c = Categoria.buscarCategoria(nome);

        if (c == null) {
            System.out.println("Categoria não encontrada.");
            return;
        }

        c.visualizarCategoria();
    }

    private void editar() {
        System.out.print("Digite o nome da categoria que deseja editar: ");
        String nomeAtual = leitor.nextLine().trim();

        Categoria cat = Categoria.buscarCategoria(nomeAtual);

        if (cat == null) {
            System.out.println("[ERRO] Categoria não encontrada.");
            return;
        }

        System.out.print("Digite o NOVO nome da categoria: ");
        String novoNome = leitor.nextLine().trim();

        cat.editarCategoria(novoNome);
    }

    private void desativar() {
        System.out.print("Digite o nome da categoria que deseja desativar: ");
        String nome = leitor.nextLine().trim();

        Categoria cat = Categoria.buscarCategoria(nome);

        if (cat == null) {
            System.out.println("[ERRO] Categoria não encontrada.");
            return;
        }

        cat.desativarCategoria();
    }
}