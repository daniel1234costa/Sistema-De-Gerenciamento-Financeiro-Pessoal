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
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar Extras");
            System.out.println("3. Listar Fixas");
            System.out.println("4. Editar");
            System.out.println("5. Excluir");
            System.out.println("6. Visualizar");
            System.out.println("7. Total Mensal");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

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
                default: System.out.println("Inválido!");
            }
        }
    }

    private void cadastrar() {
        System.out.println("\n--- NOVA RENDA ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Valor: ");
        double valor = scanner.nextDouble();
        
        // Pedindo separado para evitar erro de digitação
        System.out.print("Dia: ");
        int dia = scanner.nextInt();
        System.out.print("Mês: ");
        int mes = scanner.nextInt();
        System.out.print("Ano: ");
        int ano = scanner.nextInt();
        
        // Monta a String "dd/MM/yyyy" para o UtilData converter
       // Monta no formato Ano-Mês-Dia para o UtilData aceitar
        String dataTexto = String.format("%d-%02d-%02d", ano, mes, dia);
        Date data = UtilData.parseData(dataTexto);

        System.out.print("É fixa? (true/false): ");
        boolean tipo = scanner.nextBoolean();
        scanner.nextLine(); // Limpar buffer

        controller.cadastrarRenda(nome, valor, data, tipo);
    }

    private void listarExtras() {
        System.out.println("\n--- EXTRAS ---");
        List<Renda> lista = controller.listarExtras();
        for (Renda r : lista) System.out.println(r.getIdRenda() + " | " + r.getNomeRenda() + " | R$ " + r.getValor());
    }

    private void listarFixas() {
        System.out.println("\n--- FIXAS ---");
        List<Renda> lista = controller.listarFixas();
        for (Renda r : lista) System.out.println(r.getIdRenda() + " | " + r.getNomeRenda() + " | R$ " + r.getValor());
    }

    private void editar() {
        System.out.print("ID: ");
        String id = scanner.next();
        scanner.nextLine(); 
        System.out.print("Novo Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo Valor: ");
        double valor = scanner.nextDouble();
        controller.editarRenda(id, nome, valor);
    }

    private void excluir() {
        System.out.print("Informe o id da renda que deseja excluir: ");
        String id = scanner.next();
        controller.excluirRenda(id);
    }

    private void visualizar() {
        System.out.print("ID: ");
        controller.visualizarRenda(scanner.next());
    }

    private void totalMensal() {
        System.out.print("Mês: ");
        int mes = scanner.nextInt();
        System.out.print("Ano: ");
        int ano = scanner.nextInt();
        System.out.println("Total: R$ " + controller.totalMensal(mes, ano));
    }
}