package views;

import controller.CategoriaController;
import java.util.List;
import java.util.Scanner;
import model.Categoria;

public class TelaCategoria {
    private CategoriaController controller = new CategoriaController();
    private Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== CATEGORIAS ===");
            System.out.println("1. Cadastrar Categoria");
            System.out.println("2. Listar Categorias");
            System.out.println("3. Editar Categoria");
            System.out.println("4. Desativar Categoria");
            System.out.println("5. Visualizar Categoria");
            System.out.println("6. Buscar Categoria");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 0) break;

            switch (opcao) {
                case 1: cadastrar(); break;
                case 2: listar(); break;
                case 3: editar(); break;
                case 4: desativar(); break;
                case 5: visualizar(); break;
                case 6: buscar(); break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrar() {
        System.out.println("\n--- NOVA CATEGORIA ---");
        System.out.print("Nome da categoria: ");
        String nome = scanner.nextLine();

        controller.cadastrarCategoria(nome);
    }

    private void listar() {
        System.out.println("\n--- LISTA DE CATEGORIAS ---");
        List<Categoria> lista = controller.listarCategorias();

        if (lista.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada.");
        } else {
            for (Categoria c : lista) {
                System.out.println("ID: " + c.getIdCategoria() + " | Nome: " + c.getNomeCategoria() + " | Status: " + c.getStatus());
            }
        }
    }

    private void editar() {
        System.out.println("\n--- EDITAR CATEGORIA ---");
        System.out.print("ID da categoria: ");
        String id = scanner.nextLine();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        controller.editarCategoria(id, nome);
    }

    private void desativar() {
        System.out.println("\n--- DESATIVAR CATEGORIA ---");
        System.out.print("ID da categoria: ");
        String id = scanner.nextLine();

        controller.desativarCategoria(id);
    }

    private void visualizar() {
        System.out.println("\n--- DETALHES DA CATEGORIA ---");
        System.out.print("ID da categoria: ");
        String id = scanner.nextLine();

        controller.visualizarCategoria(id);
    }

    private void buscar() {
        System.out.println("\n--- BUSCAR CATEGORIA ---");
        System.out.print("Nome ou ID: ");
        String termo = scanner.nextLine();

        Categoria categoria = controller.buscarCategoria(termo);

        if (categoria == null) {
            System.out.println("Categoria não encontrada.");
        } else {
            System.out.println("ID: " + categoria.getIdCategoria());
            System.out.println("Nome: " + categoria.getNomeCategoria());
            System.out.println("Status: " + categoria.getStatus());
        }
    }
}