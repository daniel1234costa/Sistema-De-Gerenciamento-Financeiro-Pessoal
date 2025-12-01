package app;

import java.util.Scanner;

import views.TelaCategoria;
import views.TelaRenda;
import views.TelaUsuario;
import views.TelaDespesa;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
      
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("      SISTEMA DE FINANÇAS PESSOAIS");
            System.out.println("==========================================");
            System.out.println("1. Módulo de Rendas ");
            System.out.println("2. Módulo de Despesas ");
            System.out.println("3. Módulo de Usuários ");
            System.out.println("4. Módulo de Categorias ");
            System.out.println("0. Sair do Sistema");
            System.out.println("==========================================");
            System.out.print("Escolha uma opção: ");

            int opcao = 0;
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar o 'enter' do buffer
            } catch (Exception e) {
                System.out.println("Por favor, digite apenas números!");
                scanner.nextLine(); // Limpa a sujeira do scanner
                continue; // Volta pro começo do loop
            }

            if (opcao == 0) {
                System.out.println("Saindo... Até logo!");
                break;
            }

            switch (opcao) {
                case 1:
                   
                    TelaRenda telaRenda = new TelaRenda();
                    telaRenda.exibirMenu(); 
                   
                    break;
                
                case 2:
                    TelaDespesa telaDespesa = new TelaDespesa();
                    telaDespesa.exibirMenu();
                    break;

                case 3:
                    TelaUsuario telaUser = new TelaUsuario();
                    telaUser.exibirMenu();
                    break;
                
                case 4:
                    TelaCategoria telaCategoria = new TelaCategoria();
                    telaCategoria.exibirMenu();
                    break;

                default:
                    System.out.println(" Opção inválida!");
            }
        }
        
        scanner.close();
    }
}