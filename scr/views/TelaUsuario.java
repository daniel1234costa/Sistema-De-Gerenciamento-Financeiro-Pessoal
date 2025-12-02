package views;

import dao.UsuarioDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import model.Sessao;
import model.Usuario;
import model.UtilData;

public class TelaUsuario {

    private final Scanner scanner;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public TelaUsuario(Scanner scanner) {
        this.scanner = scanner;
    }


    public String exibirMenuLogin() {
        Usuario usuario = realizarLogin();
        return (usuario != null) ? usuario.getIdUsuario() : null; 
    }

    public void exibirMenuCadastro() {
        cadastrarUsuario();
    }

    public void exibirMenuPerfil() {
        int opcao = -1;
        
        while (opcao != 0) {
            if (!Sessao.isLogado()) return;

            System.out.println("\n===== MEU PERFIL =====");
            System.out.println("1. Visualizar Meus Dados");
            System.out.println("2. Editar Meus Dados");
            System.out.println("3. Excluir Minha Conta");
            System.out.println("4. Visualizar Relatório Financeiro");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.println("======================");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); 
            } else {
                scanner.nextLine();
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1:
                    exibirDadosUsuarioLogado();
                    break;
                case 2:
                    editarPerfil();
                    break;
                case 3:
                    if (excluirConta()) {
                        return; 
                    }
                case 4:
                    visualizarRelatorioFinanceiro();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }


   private void exibirDadosUsuarioLogado() {
    Usuario usuario = buscarUsuarioLogado();
    if (usuario != null) {
        usuario.visualizarUsuario(usuario); 
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}

    private void editarPerfil() {
        Usuario usuario = buscarUsuarioLogado();
        if (usuario == null) return;

        System.out.println("\n--- EDITAR PERFIL ---");
        System.out.println("(Dica: Deixe vazio e aperte Enter para manter o valor atual)");

        System.out.print("Novo Nome [" + usuario.getNome() + "]: ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            usuario.setNome(novoNome);
        }

        System.out.print("Novo Email [" + usuario.getEmail() + "]: ");
        String novoEmail = scanner.nextLine();
        if (!novoEmail.trim().isEmpty()) {
            usuario.setEmail(novoEmail);
        }

        String dataAtualStr = (usuario.getDataNascimento() != null) ? UtilData.formatarData(usuario.getDataNascimento()) : "N/D";
        System.out.print("Nova Data (dd/MM/yyyy) [" + dataAtualStr + "]: ");
        String novaDataStr = scanner.nextLine();
        
        if (!novaDataStr.trim().isEmpty()) {
            try {
                Date novaData = dateFormat.parse(novaDataStr);
                usuario.setDataNascimento(novaData);
            } catch (ParseException e) {
                System.out.println("Data inválida. A data antiga foi mantida.");
            }
        }

        if (Usuario.editarUsuario(usuario)) {
            System.out.println("------Novos dados foram fornecidos-----");
        } else {
            System.out.println("Erro ao atualizar perfil (Email pode já estar em uso).");
        }
    }

    private boolean excluirConta() {
        System.out.println("\n ATENÇÃO: EXCLUSÃO DE CONTA ");
        System.out.println("Tem certeza que deseja excluir sua conta?");
        System.out.println("Isso apagará TODAS as suas Despesas e Rendas permanentemente.");
        System.out.print("Digite 'SIM' para confirmar a exclusão: ");
        
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("SIM")) {
            Usuario usuario = buscarUsuarioLogado();
            if (usuario != null) {
                UsuarioDAO dao = new UsuarioDAO();
                
                if (dao.excluir(usuario.getIdUsuario())) { 
                    System.out.println("Conta excluída com sucesso.");
                    System.out.println("Você será deslogado agora.");
                    Sessao.deslogar();
                    return true; 
                } else {
                    System.out.println("Erro ao excluir conta.");
                }
            }
        } else {
            System.out.println("Operação cancelada.");
        }
        return false;
    }
    
    private Usuario buscarUsuarioLogado() {
        String idLogado = Sessao.getIdUsuarioLogado();
        if (idLogado == null) return null;
        
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.buscarPorId(idLogado);
        
        if (u == null) {
            System.out.println("Erro crítico: Usuário logado não encontrado no banco.");
            Sessao.deslogar();
        }
        return u;
    }

    private void cadastrarUsuario() {
    System.out.println("\n--- CADASTRO DE USUÁRIO ---");
    System.out.print("Nome: ");
    String nome = scanner.nextLine();
    System.out.print("Email: ");
    String email = scanner.nextLine();
    System.out.print("Senha: ");
    String senha = scanner.nextLine();

   
    java.util.Date dataNascimento = null;
    final int ANO_LIMITE = java.time.Year.now().getValue(); 
    
    while (dataNascimento == null) {
        try {
            System.out.println("--- Informe a Data de Nascimento (Limite: " + ANO_LIMITE + ") ---");
            System.out.print("Dia: ");
            int dia = Integer.parseInt(scanner.nextLine());
            System.out.print("Mês: ");
            int mes = Integer.parseInt(scanner.nextLine());
            System.out.print("Ano: ");
            int ano = Integer.parseInt(scanner.nextLine());

            // 1.1. Regra do Ano
            if (ano > ANO_LIMITE) {
                System.out.println("Erro: O ano de nascimento não pode ser maior que " + ANO_LIMITE + ".");
                continue;
            }
            
           
            String dataTexto = String.format("%02d/%02d/%d", dia, mes, ano);
           
            dataNascimento = UtilData.parseDataUsuario(dataTexto); 

            if (dataNascimento == null) {
                System.out.println("Data inválida! Verifique se o dia e o mês são válidos.");
                continue;
            }

            if (dataNascimento.after(new java.util.Date())) {
                System.out.println("Erro: A data de nascimento não pode ser no futuro!");
                dataNascimento = null;
            }

        } catch (NumberFormatException e) {
            System.out.println("Digite apenas números inteiros para dia, mês e ano!");
        }
    }

    boolean sucesso = Usuario.registrarUsuario(nome, email, senha, dataNascimento);
    
    if (sucesso) {
        System.out.println("Usuário registrado com sucesso! Faça login para continuar.");
    } else {
        System.err.println("Falha ao registrar. - O email já existe.");
    }
}

    private Usuario realizarLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        Usuario usuarioLogado = Usuario.login(email, senha);
        
        if (usuarioLogado == null) {
            System.err.println(" Login falhou. Email ou senha incorretos.");
        }
        return usuarioLogado;
    }

    private void visualizarRelatorioFinanceiro() {

        Usuario usuario = buscarUsuarioLogado();
        if (usuario == null) return;

        System.out.println("\n--- RELATÓRIO FINANCEIRO ---");

        System.out.print("Data inicial (dd/MM/yyyy): ");
        String inicio = scanner.nextLine();

        System.out.print("Data final (dd/MM/yyyy): ");
        String fim = scanner.nextLine();

        String relatorio = usuario.listarRendasDespesasPorPeriodo(inicio, fim);

        System.out.println(relatorio);
    }
}