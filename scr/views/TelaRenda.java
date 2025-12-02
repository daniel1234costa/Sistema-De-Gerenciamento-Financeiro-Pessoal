package views;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.Renda;
import model.UtilData;

public class TelaRenda {

    private Scanner leitor = new Scanner(System.in);

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

            String entrada = leitor.nextLine();
            int opcao;
              try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números.");
                continue;
            }

            if (opcao == 0) break;

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listarExtras();
                case 3 -> listarFixas();
                case 4 -> editar();
                case 5 -> excluir();
                case 6 -> visualizar();
                case 7 -> totalMensal();
                default -> System.out.println("Inválido!");
            }
        }
    }

  private void cadastrar() {
        System.out.println("\n--- NOVA RENDA ---");
        System.out.print("Nome: ");
        String nome = leitor.nextLine();

        // 1. VALIDAÇÃO DE VALOR (Loop até ser positivo)
        double valor = -1;
        while (valor <= 0) {
            System.out.print("Valor (maior que 0): ");
            try {
                valor = Double.parseDouble(leitor.nextLine().replace(",", "."));
                if (valor <= 0) System.out.println("Erro: O valor deve ser positivo!");
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas números.");
            }
        }

        Date data = null;
        while (data == null) {
            try {
                System.out.println("--- Informe a Data ---");
                System.out.print("Dia: ");
                int dia = Integer.parseInt(leitor.nextLine());
                System.out.print("Mês: ");
                int mes = Integer.parseInt(leitor.nextLine());
                System.out.print("Ano: ");
                int ano = Integer.parseInt(leitor.nextLine());

                // Regra do Ano
                int anoAtual = java.time.Year.now().getValue(); 
                if (ano > anoAtual) {
                    System.out.println(" Erro: O ano não pode ser maior que " + anoAtual);
                    continue; // Volta pro começo do while
                }
                
                // Monta e Valida formatação
                String dataTexto = String.format("%02d/%02d/%d", dia, mes, ano);
                data = UtilData.parseDataUsuario(dataTexto); 

                if (data == null) System.out.println(" Data inválida! Verifique dia e mês.");

            } catch (NumberFormatException e) {
                System.out.println(" Digite apenas números inteiros!");
            }
        }

        System.out.print("É fixa? (1-Sim / 0-Não): ");
        String tipoStr = leitor.nextLine();
        boolean tipo = tipoStr.equals("1");

        Renda novaRenda = Renda.cadastrarRenda(nome, valor, data, tipo);

        
        if (novaRenda != null) {
            System.out.println("\nSucesso! Renda cadastrada.");
            System.out.println("ID gerado: " + novaRenda.getIdRenda());
        } else {
            System.out.println("\n Falha ao cadastrar. Tente novamente.");
        }
    }
    
    private void listarExtras() {
        System.out.println("\n--- EXTRAS ---");
        List<Renda> lista = Renda.listarRendasExtras();
        lista.forEach(r -> System.out.println(r.getIdRenda() + " | " + r.getNomeRenda() + " | R$ " + r.getValor()));
    }

    private void listarFixas() {
        System.out.println("\n--- FIXAS ---");
        List<Renda> lista = Renda.listarRendasFixas();
        lista.forEach(r -> System.out.println(r.getIdRenda() + " | " + r.getNomeRenda() + " | R$ " + r.getValor()));
    }

    private void editar() {
        System.out.print("ID da renda: ");
        String id = leitor.nextLine();

        Renda renda = Renda.buscarPorId(id);

        if (renda == null) {
            System.out.println("Renda não encontrada.");
            return;
        }

        System.out.print("Novo nome (" + renda.getNomeRenda() + "): ");
        String nome = leitor.nextLine();
        if (!nome.isEmpty()) renda.setNomeRenda(nome);

        System.out.print("Novo valor (" + renda.getValor() + "): ");
        String valorStr = leitor.nextLine();
        if (!valorStr.isEmpty()) renda.setValor(Double.parseDouble(valorStr));

        renda.editarRenda(renda.getNomeRenda(), renda.getValor());
        System.out.println(" Renda atualizada!");
    }

    private void excluir() {
        System.out.print("ID da renda: ");
        String id = leitor.nextLine();

        Renda renda = Renda.buscarPorId(id);

        if (renda == null) {
            System.out.println("Renda não encontrada.");
            return;
        }

        if (Renda.excluirRenda(renda)) System.out.println(" Renda excluída!");
        else System.out.println(" Erro ao excluir.");
    }

    private void visualizar() {
        System.out.println("\n--- ESCOLHA UMA RENDA ---");

        List<Renda> todas = Renda.listarRendasFixas();
        todas.addAll(Renda.listarRendasExtras());

        if (todas.isEmpty()) {
            System.out.println("Nenhuma renda cadastrada.");
            return;
        }

        for (int i = 0; i < todas.size(); i++) {
            Renda r = todas.get(i);
            System.out.println((i + 1) + ". " + r.getNomeRenda() + " (R$ " + r.getValor() + ")");
        }

        System.out.print("Número: ");
        int opcao = Integer.parseInt(leitor.nextLine());

        if (opcao < 1 || opcao > todas.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Renda selecionada = todas.get(opcao - 1);
        selecionada.visualizarRenda();
    }

    private void totalMensal() {
        System.out.print("Mês: ");
        int mes = Integer.parseInt(leitor.nextLine());

        System.out.print("Ano: ");
        int ano = Integer.parseInt(leitor.nextLine());

        System.out.println("Total: R$ " + Renda.calcularRendaTotalMensal(mes, ano));
    }
}