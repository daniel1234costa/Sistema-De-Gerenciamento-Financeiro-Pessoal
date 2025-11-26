package views;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import controller.RendaController;
import model.Renda;
import model.UtilData;

public class TelaRenda {

    private RendaController controller = new RendaController();
    private Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        while (true) {
            System.out.println("\n=== GESTÃO DE RENDAS ===");
            System.out.println("1. Cadastrar Renda");
            System.out.println("2. Listar Rendas Extras");
            System.out.println("3. Listar Rendas Fixas");
            System.out.println("4. Editar Renda");
            System.out.println("5. Excluir Renda");
            System.out.println("6. Visualizar Detalhes");
            System.out.println("7. Total Mensal");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            if (opcao == 0) break;

            switch (opcao) {
                case 1: cadastrar(); break;
                case 2: listarExtras(); break;
                case 3: listarFixas(); break;
                case 4: editar(); break;
                case 5: excluir(); break;
                case 6: visualizar(); break;
                case 7: totalMensal(); break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrar() {
        System.out.println("\n--- NOVA RENDA ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Data (yyyy-MM-dd): ");
        String dataTexto = scanner.nextLine();
        
        // Converte o texto digitado para Date para passar pro Controller
        Date data = UtilData.parseData(dataTexto); 

        System.out.print("É fixa? (true/false): ");
        boolean tipo = scanner.nextBoolean();

        controller.cadastrarRenda(nome, valor, data, tipo);
    }

    private void listarExtras() {
        System.out.println("\n--- RENDAS EXTRAS ---");
        List<Renda> lista = controller.listarRendasExtras();
        
        if (lista.isEmpty()) {
            System.out.println("Nenhuma renda extra encontrada.");
        } else {
            for (Renda r : lista) {
                System.out.println("ID: " + r.getIdRenda() + " | " + r.getNomeRenda() + " | R$ " + r.getValor());
            }
        }
    }

    private void listarFixas() {
        System.out.println("\n--- RENDAS FIXAS ---");
        List<Renda> lista = controller.listarRendasFixas();
        
        if (lista.isEmpty()) {
            System.out.println("Nenhuma renda fixa encontrada.");
        } else {
            for (Renda r : lista) {
                System.out.println("ID: " + r.getIdRenda() + " | " + r.getNomeRenda() + " | R$ " + r.getValor());
            }
        }
    }

    private void editar() {
        System.out.println("\n--- EDITAR RENDA ---");
        System.out.print("ID da renda: ");
        String id = scanner.next();
        scanner.nextLine(); 

        System.out.print("Novo Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Novo Valor: ");
        double valor = scanner.nextDouble();

        controller.editarRenda(id, nome, valor);
    }

    private void excluir() {
        System.out.println("\n--- EXCLUIR RENDA ---");
        System.out.print("ID da renda: ");
        String id = scanner.next();
        
        controller.excluirRenda(id);
    }

    private void visualizar() {
        System.out.println("\n--- DETALHES ---");
        System.out.print("ID da renda: ");
        String id = scanner.next();
        
        controller.visualizarRenda(id);
    }

    private void totalMensal() {
        System.out.println("\n--- TOTAL MENSAL ---");
        System.out.print("Mês (1-12): ");
        int mes = scanner.nextInt();
        System.out.print("Ano (yyyy): ");
        int ano = scanner.nextInt();

        double total = controller.calcularRendaTotalMensal(mes, ano);
        System.out.println("Total em " + mes + "/" + ano + ": R$ " + total);
    }
}