package views; // Mantenha o seu pacote

import controller.UsuarioController;
import model.Usuario;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat; 
import java.util.List; 

public class MenuTeste {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioController controller = new UsuarioController();
        Usuario usuarioLogado = null;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE USUÁRIO (TESTE) ---");
            if (usuarioLogado == null) {
                System.out.println("1. Registrar Novo Usuário");
                System.out.println("2. Fazer Login");
                System.out.println("0. Sair");
            } else {
                System.out.println("Você está logado como: " + usuarioLogado.getNome());
                System.out.println("3. Listar Todos os Usuários (Admin)");
                System.out.println("4. Editar Perfil");
                System.out.println("5. Excluir Minha Conta");
                System.out.println("6. Logout");
                System.out.println("0. Sair");
            }

            System.out.print("Digite a opção desejada: ");
            
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); 
            } else {
                System.err.println("Opção inválida. Digite um número.");
                scanner.nextLine();
                continue;
            }

            try {
                switch (opcao) {
                    case 1:
                        if (usuarioLogado == null) {
                            System.out.print("Nome: ");
                            String nome = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Senha: ");
                            String senha = scanner.nextLine();
                            System.out.print("Data de Nascimento (dd/MM/yyyy): ");
                            String dataStr = scanner.nextLine();
                            
                            Date dataNascimento = null;
                            try {
                              
                                dataNascimento = dateFormat.parse(dataStr); 
                            } catch (java.text.ParseException e) {
                                System.err.println("❌ Erro: Formato de data inválido. Use dd/MM/yyyy.");
                                break; // Volta ao menu se a data for inválida
                            }
                            
                            boolean sucesso = controller.registrarNovoUsuario(nome, email, senha, dataNascimento); 
                            if (sucesso) {
                                System.out.println(" Usuário registrado com sucesso!");
                            } else {
                                System.err.println("Falha no registro. O email pode já estar em uso.");
                            }
                        }
                        break;
                    case 2:
                        if (usuarioLogado == null) {
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Senha: ");
                            String senha = scanner.nextLine();
                            usuarioLogado = controller.login(email, senha);
                        }
                        break;
                    case 3:
                        if (usuarioLogado != null) {
                            System.out.println("\n--- Lista de Usuários ---");
                            controller.listarTodosUsuarios().forEach(u -> 
                                System.out.println("ID: " + u.getIdUsuario().substring(0, 8) + 
                                                   ", Nome: " + u.getNome() + 
                                                   ", Email: " + u.getEmail() +
                                                   ", Nasc: " + (u.getDataNascimento() != null ? dateFormat.format(u.getDataNascimento()) : "N/D")));
                            System.out.println("-------------------------");
                        }
                        break;
                    case 4:
                        if (usuarioLogado != null) {
                            System.out.print("Novo Nome (" + usuarioLogado.getNome() + "): ");
                            String novoNome = scanner.nextLine();
                            
                            // Cria um objeto Usuario com os dados atualizados
                            Usuario usuarioAtualizado = new Usuario();
                            usuarioAtualizado.setIdUsuario(usuarioLogado.getIdUsuario());
                            usuarioAtualizado.setNome(novoNome.isEmpty() ? usuarioLogado.getNome() : novoNome); 
                            usuarioAtualizado.setEmail(usuarioLogado.getEmail()); 
                            usuarioAtualizado.setDataNascimento(usuarioLogado.getDataNascimento());
                            
                            if (controller.editarUsuario(usuarioAtualizado)) {
                                usuarioLogado.setNome(usuarioAtualizado.getNome()); // Atualiza o objeto de sessão
                            }
                        }
                        break;
                    case 5:
                        if (usuarioLogado != null) {
                            System.out.print("Tem certeza que deseja EXCLUIR sua conta? Digite SIM: ");
                            String confirmacao = scanner.nextLine();
                            if (confirmacao.equalsIgnoreCase("SIM")) {
                                if (controller.excluirUsuario(usuarioLogado.getIdUsuario())) {
                                    System.out.println("Conta excluída com sucesso. Voltando ao menu principal.");
                                    usuarioLogado = null; // Encerra a sessão
                                }
                            }
                        }
                        break;
                    case 6:
                        if (usuarioLogado != null) {
                            usuarioLogado = null;
                            System.out.println("Logout realizado.");
                        }
                        break;
                    case 0:
                        System.out.println("Encerrando aplicação.");
                        break;
                    default:
                        System.err.println("Opção não reconhecida.");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro durante a operação: " + e.getMessage());
                // A exceção de parse da data (case 1) é tratada, mas outras exceções são capturadas aqui
            }
        }
        scanner.close();
    }
}