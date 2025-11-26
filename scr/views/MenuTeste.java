package views; 

import controller.UsuarioController;
import model.Usuario;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat; 
import java.util.List; 
import java.text.ParseException; 

public class MenuTeste {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
      
        UsuarioController controller = new UsuarioController(); 
        
        Usuario usuarioLogado = null;
        
      
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- SISTEMA FINANCEIRO ---");
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
                System.err.println(" Opção inválida. Digite um número.");
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
                            } catch (ParseException e) { 
                                System.err.println("Erro: Formato de data inválido. Use dd/MM/yyyy.");
                                break;
                            }
                            
                          
                            boolean sucesso = UsuarioController.registrarUsuario(nome, email, senha, dataNascimento); 
                            if (sucesso) {
                                System.out.println("Usuário registrado com sucesso!");
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
                           
                            usuarioLogado = UsuarioController.login(email, senha);
                        }
                        break;
                    case 3:
                      
                        if (usuarioLogado != null) {
                            System.out.println("\n--- Lista de Usuários ---");
                          
                            System.out.println("Função de listagem ainda não implementada no Controller.");
                        }
                        break;
                   // Dentro do MenuTeste.main(), case 4:
// Dentro do MenuTeste.main(), case 4:
case 4:
    // EDIÇÃO DE PERFIL (MÉTODO NÃO ESTÁTICO)
    if (usuarioLogado != null) {
        System.out.println("\n--- Editar Perfil ---");
        System.out.println("Deixe em branco para manter o valor atual.");
        
        // Novo Nome
        System.out.print("Novo Nome (" + usuarioLogado.getNome() + "): ");
        String novoNome = scanner.nextLine();
        
        // Novo Email
        System.out.print("Novo Email (" + usuarioLogado.getEmail() + "): ");
        String novoEmail = scanner.nextLine();
        
        // Nova Data
        // Usamos um formato simples para exibir a data atual, prevenindo NullPointerException
        String dataAtualStr = (usuarioLogado.getDataNascimento() != null) ? dateFormat.format(usuarioLogado.getDataNascimento()) : "N/D";
        System.out.print("Nova Data Nasc. (" + dataAtualStr + "): ");
        String novaDataStr = scanner.nextLine();
        
        
        // Cria um objeto Usuario com os dados para atualização
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setIdUsuario(usuarioLogado.getIdUsuario());
        
        // 1. Define Nome e Email
        usuarioAtualizado.setNome(novoNome.isEmpty() ? usuarioLogado.getNome() : novoNome); 
        usuarioAtualizado.setEmail(novoEmail.isEmpty() ? usuarioLogado.getEmail() : novoEmail); 
        
        // 2. Define a Data (GARANTINDO QUE NÃO SEJA NULA se for NOT NULL no DB)
        if (novaDataStr.isEmpty()) {
            // Se vazio, usa a data ORIGINAL (CORREÇÃO DE FLUXO)
            usuarioAtualizado.setDataNascimento(usuarioLogado.getDataNascimento());
        } else {
            try {
                // Tenta parsear a nova data
                usuarioAtualizado.setDataNascimento(dateFormat.parse(novaDataStr));
            } catch (ParseException e) {
                System.err.println("Erro: Formato de data inválido. Edição cancelada.");
                break;
            }
        }
        
        // 3. Executa a edição
        if (controller.editarUsuario(usuarioAtualizado)) {
            // Atualiza o objeto de sessão (mantendo a senha null)
            usuarioLogado.setNome(usuarioAtualizado.getNome());
            usuarioLogado.setEmail(usuarioAtualizado.getEmail());
            usuarioLogado.setDataNascimento(usuarioAtualizado.getDataNascimento());
            System.out.println("✅ Perfil atualizado com sucesso!");
        }
    }
    break;
                    case 5:
                       
                        if (usuarioLogado != null) {
                            System.out.print("Tem certeza que deseja EXCLUIR sua conta (" + usuarioLogado.getEmail() + ")? Digite SIM: ");
                            String confirmacao = scanner.nextLine();
                            
                            if (confirmacao.equalsIgnoreCase("SIM")) {
                              
                                UsuarioController.excluirUsuario();
                                
                                usuarioLogado = null; 
                                System.out.println("Sessão encerrada.");
                            } else {
                                System.out.println("Exclusão cancelada.");
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
                System.err.println(" Ocorreu um erro interno durante a operação: " + e.getMessage());
              
            }
        }
        scanner.close();
    }
}