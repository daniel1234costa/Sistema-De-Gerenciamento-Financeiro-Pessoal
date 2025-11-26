package app;

import java.util.Scanner;
import views.TelaRenda;
// import views.TelaUsuario; // (Descomente quando tiver a tela de usuário pronta)
// import views.TelaDespesa; // (Descomente quando tiver a tela de despesa pronta)

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Loop infinito para o menu principal só fechar quando escolher Sair
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("      SISTEMA DE FINANÇAS PESSOAIS");
            System.out.println("==========================================");
            System.out.println("1. Módulo de Rendas 💰");
            System.out.println("2. Módulo de Despesas 💸 (Em breve)");
            System.out.println("3. Módulo de Usuários 👤 (Em breve)");
            System.out.println("0. Sair do Sistema");
            System.out.println("==========================================");
            System.out.print("Escolha uma opção: ");

            int opcao = 0;
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar o 'enter' do buffer
            } catch (Exception e) {
                System.out.println("❌ Por favor, digite apenas números!");
                scanner.nextLine(); // Limpa a sujeira do scanner
                continue; // Volta pro começo do loop
            }

            if (opcao == 0) {
                System.out.println("Saindo... Até logo! 👋");
                break;
            }

            switch (opcao) {
                case 1:
                    // AQUI É O PULO DO GATO:
                    // O Main chama a Tela da Renda e passa o controle para ela.
                    TelaRenda telaRenda = new TelaRenda();
                    telaRenda.exibirMenu(); 
                    // Quando o usuário sair do menu da renda, o código volta pra cá
                    break;
                
                case 2:
                    System.out.println("⚠️  O módulo de Despesas ainda está em construção!");
                    break;

                case 3:
                    System.out.println("⚠️  O módulo de Usuários ainda está em construção!");
                    // TelaUsuario telaUser = new TelaUsuario();
                    // telaUser.exibirMenu();
                    break;

                default:
                    System.out.println("❌ Opção inválida!");
            }
        }
        
        scanner.close();
    }
}