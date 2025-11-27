package views;

import controller.UsuarioController;
import model.Usuario;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TelaUsuario {

    // Controller instanciada para uso geral
    private final UsuarioController controller = new UsuarioController();
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void exibirMenu() {
        Usuario usuarioLogado = null;
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- SISTEMA FINANCEIRO ---");
            if (usuarioLogado == null) {
                System.out.println("1. Registrar Novo Usuário");
                System.out.println("2. Fazer Login");
                System.out.println("0. Sair");
            } else {
                System.out.println("Bem-vindo(a), " + usuarioLogado.getNome());
                System.out.println("3. Listar Todos os Usuários (Admin - Em Breve)");
                System.out.println("4. Editar Perfil");
                System.out.println("5. Excluir Minha Conta");
                System.out.println("6. Logout");
                System.out.println("0. Sair");
            }

            System.out.print("Digite a opção desejada: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
            } else {
                System.err.println("Opção inválida. Digite um número.");
                scanner.nextLine();
                continue;
            }

            try {
                switch (opcao) {
                    case 1: // Registrar
                        if (usuarioLogado == null) {
                            cadastrarUsuario();
                        }
                        break;
                    case 2: // Login
                        if (usuarioLogado == null) {
                            usuarioLogado = realizarLogin();
                        }
                        break;
                    case 3: // Listar
                        if (usuarioLogado != null) {
                            System.out.println("Funcionalidade em desenvolvimento.");
                        }
                        break;
                    case 4: // Editar
                        if (usuarioLogado != null) {
                            editarPerfil(usuarioLogado);
                        }
                        break;
                    case 5: // Excluir
                        if (usuarioLogado != null) {
                            if (excluirConta(usuarioLogado)) {
                                usuarioLogado = null; // Logout forçado após excluir
                            }
                        }
                        break;
                    case 6: // Logout
                        if (usuarioLogado != null) {
                            usuarioLogado = null;
                            System.out.println("Logout realizado com sucesso.");
                        }
                        break;
                    case 0:
                        System.out.println("Encerrando sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro no menu: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- Métodos Auxiliares para limpar o código ---

    private void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Data de Nascimento (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();

        try {
            Date dataNascimento = dateFormat.parse(dataStr);
            boolean sucesso = controller.registrarUsuario(nome, email, senha, dataNascimento);
            
            if (sucesso) System.out.println(" Usuário registrado com sucesso!");
            else System.err.println(" Falha ao registrar. Email pode já existir.");
            
        } catch (ParseException e) {
            System.err.println(" Data inválida.");
        }
    }

    private Usuario realizarLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        return controller.login(email, senha);
    }

    private void editarPerfil(Usuario usuarioLogado) {
        System.out.println("\n--- Editar Perfil (Deixe vazio para manter) ---");
        
        System.out.print("Novo Nome (" + usuarioLogado.getNome() + "): ");
        String novoNome = scanner.nextLine();

        System.out.print("Novo Email (" + usuarioLogado.getEmail() + "): ");
        String novoEmail = scanner.nextLine();

        String dataAtual = (usuarioLogado.getDataNascimento() != null) ? dateFormat.format(usuarioLogado.getDataNascimento()) : "N/D";
        System.out.print("Nova Data (" + dataAtual + "): ");
        String novaDataStr = scanner.nextLine();

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setIdUsuario(usuarioLogado.getIdUsuario()); // ID é fundamental
        usuarioAtualizado.setNome(novoNome.isEmpty() ? usuarioLogado.getNome() : novoNome);
        usuarioAtualizado.setEmail(novoEmail.isEmpty() ? usuarioLogado.getEmail() : novoEmail);

        try {
            if (novaDataStr.isEmpty()) {
                usuarioAtualizado.setDataNascimento(usuarioLogado.getDataNascimento());
            } else {
                usuarioAtualizado.setDataNascimento(dateFormat.parse(novaDataStr));
            }

            if (controller.editarUsuario(usuarioAtualizado)) {
                // Atualiza a sessão local
                usuarioLogado.setNome(usuarioAtualizado.getNome());
                usuarioLogado.setEmail(usuarioAtualizado.getEmail());
                usuarioLogado.setDataNascimento(usuarioAtualizado.getDataNascimento());
            }
        } catch (ParseException e) {
            System.err.println("Data inválida. Edição cancelada.");
        }
    }

        private boolean excluirConta(Usuario usuarioLogado) {
        System.out.print("Deseja realmente EXCLUIR sua conta? Digite 'SIM': ");
        if (scanner.nextLine().equalsIgnoreCase("SIM")) {

            controller.excluirUsuario(usuarioLogado.getEmail()); 
            return true;
        }
        return false;
    }
}