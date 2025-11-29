package views;

import controller.DespesaController;
import model.Categoria;
import model.Despesa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TelaDespesa {

    private DespesaController despesaController;
    private Scanner scanner;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public TelaDespesa() {
        this.despesaController = new DespesaController();
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenuDespesas() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Menu de Despesas ---");
            System.out.println("1. Cadastrar Despesa");
            System.out.println("2. Editar Despesa");
            System.out.println("3. Excluir Despesa");
            System.out.println("4. Listar Todas as Despesas");
            System.out.println("5. Buscar Despesa por Nome");
            System.out.println("6. Listar Despesas por Período");
            System.out.println("7. Listar Despesas por Categoria");
            System.out.println("8. Calcular Total de Despesas Mensal");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        cadastrarDespesa();
                        break;
                    case 2:
                        editarDespesa();
                        break;
                    case 3:
                        excluirDespesa();
                        break;
                    case 4:
                        listarDespesas();
                        break;
                    case 5:
                        buscarDespesa();
                        break;
                    case 6:
                        listarPorPeriodo();
                        break;
                    case 7:
                        listarPorCategoria();
                        break;
                    case 8:
                        totalMensal();
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, digite um número.");
            }
        }
    }

    private void cadastrarDespesa() {
        try {
            System.out.println("\n--- Cadastro de Nova Despesa ---");
            System.out.print("Nome da Despesa: ");
            String nome = scanner.nextLine();

            System.out.print("Valor: ");
            double valor = Double.parseDouble(scanner.nextLine());

            System.out.print("Data (dd/MM/yyyy): ");
            Date data = dateFormat.parse(scanner.nextLine());
            
            // Para simplificar, vamos usar uma categoria mockada.
            // Em um sistema real, você teria uma tela para selecionar ou cadastrar categorias.
            Categoria categoria = new Categoria("cat-1", "Alimentação", true);

            despesaController.cadastrarDespesa(nome, valor, data, categoria);
            // Mensagem de sucesso já é impressa pelo DAO

        } catch (ParseException e) {
            System.err.println("Formato de data inválido. Use dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            System.err.println("Formato de valor inválido. Use números (ex: 50.75).");
        }
    }

    private void editarDespesa() {
        try {
            System.out.println("\n--- Edição de Despesa ---");
            System.out.print("ID da Despesa a ser editada: ");
            String id = scanner.nextLine();

            // Seria bom buscar a despesa primeiro para mostrar os dados atuais, mas vamos simplificar
            System.out.print("Novo Nome da Despesa: ");
            String nome = scanner.nextLine();

            System.out.print("Novo Valor: ");
            double valor = Double.parseDouble(scanner.nextLine());

            System.out.print("Nova Data (dd/MM/yyyy): ");
            Date data = dateFormat.parse(scanner.nextLine());
            
            Categoria categoria = new Categoria("cat-1", "Alimentação", true);

            despesaController.editarDespesa(id, nome, valor, data, categoria);
            // Mensagem de sucesso já é impressa pelo DAO

        } catch (ParseException e) {
            System.err.println("Formato de data inválido. Use dd/MM/yyyy.");
        } catch (NumberFormatException e) {
            System.err.println("Formato de valor inválido.");
        }
    }

    private void excluirDespesa() {
        System.out.println("\n--- Exclusão de Despesa ---");
        System.out.print("ID da Despesa a ser excluída: ");
        String id = scanner.nextLine();

        despesaController.excluirDespesa(id);
        // Mensagem de sucesso/falha já é impressa pelo DAO
    }

    private void listarDespesas() {
        System.out.println("\n--- Lista de Todas as Despesas ---");
        List<Despesa> despesas = despesaController.listarDespesas();
        if (despesas.isEmpty()) {
            System.out.println("Nenhuma despesa cadastrada.");
        } else {
            for (Despesa d : despesas) {
                System.out.println("--------------------");
                d.visualizarDespesa();
            }
        }
    }

    private void buscarDespesa() {
        System.out.println("\n--- Buscar Despesa por Nome ---");
        System.out.print("Termo de busca: ");
        String termo = scanner.nextLine();
        Despesa despesa = despesaController.buscarDespesa(termo);

        if (despesa != null) {
            despesa.visualizarDespesa();
        } else {
            System.out.println("Nenhuma despesa encontrada com esse nome.");
        }
    }
    
    private void listarPorPeriodo() {
        try {
            System.out.println("\n--- Listar Despesas por Período ---");
            System.out.print("Data de Início (dd/MM/yyyy): ");
            Date inicio = dateFormat.parse(scanner.nextLine());
            System.out.print("Data de Fim (dd/MM/yyyy): ");
            Date fim = dateFormat.parse(scanner.nextLine());

            List<Despesa> despesas = despesaController.listarDespesasPorPeriodo(inicio, fim);
            if (despesas.isEmpty()) {
                System.out.println("Nenhuma despesa encontrada nesse período.");
            } else {
                for (Despesa d : despesas) {
                    d.visualizarDespesa();
                    System.out.println("---");
                }
            }
        } catch (ParseException e) {
            System.err.println("Formato de data inválido.");
        }
    }

    private void listarPorCategoria() {
        System.out.println("\n--- Listar Despesas por Categoria ---");
        System.out.print("ID da Categoria (ex: cat-1): ");
        String idCategoria = scanner.nextLine();
        Categoria categoria = new Categoria(idCategoria, "Categoria Buscada", true); 

        List<Despesa> despesas = despesaController.listarDespesasPorCategoria(categoria);
        if (despesas.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada para esta categoria.");
        } else {
            for (Despesa d : despesas) {
                d.visualizarDespesa();
                System.out.println("---");
            }
        }
    }

    private void totalMensal() {
        try {
            System.out.println("\n--- Total de Despesas Mensal ---");
            System.out.print("Mês (1-12): ");
            int mes = Integer.parseInt(scanner.nextLine());
            System.out.print("Ano (ex: 2023): ");
            int ano = Integer.parseInt(scanner.nextLine());

            double total = despesaController.calcularDespesaTotalMensal(mes, ano);
            System.out.printf("Total de despesas para %02d/%d: R$ %.2f\n", mes, ano, total);

        } catch (NumberFormatException e) {
            System.err.println("Mês e ano devem ser números.");
        }
    }
}
