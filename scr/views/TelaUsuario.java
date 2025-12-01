package views;

import model.Usuario;
import model.Sessao;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale; 

public class TelaUsuario {

    private final Scanner scanner;
    // O dateFormat é mantido, mas é recomendado usar UtilData.parseData/formatarData
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    // --- CONSTRUTOR CORRIGIDO ---
    public TelaUsuario(Scanner scanner) {
        this.scanner = scanner;
    }

    // ==========================================================
    // === MÉTODOS REQUERIDOS PELO MAIN (INTERFACE) =============
    // ==========================================================

    /**
     * Implementa o login e retorna o ID do usuário para a Sessão.
     */
    public String exibirMenuLogin() {
        Usuario usuario = realizarLogin();
        return (usuario != null) ? usuario.getIdUsuario() : null; 
    }

    /**
     * Permite o cadastro de um novo usuário.
     */
    public void exibirMenuCadastro() {
        cadastrarUsuario();
    }

    // ==========================================================
    // === MÉTODOS AUXILIARES PRIVADOS (Lógica original do Menu) ==
    // ==========================================================

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
            
            // Garanta que esta chamada (registrarUsuario) existe na sua classe Usuario/DAO
            boolean sucesso = Usuario.registrarUsuario(nome, email, senha, dataNascimento);
            
            if (sucesso) System.out.println("✅ Usuário registrado com sucesso!");
            else System.err.println("⚠️ Falha ao registrar. Email pode já existir.");
            
        } catch (ParseException e) {
            System.err.println("⚠️ Data inválida. Use o formato dd/MM/yyyy.");
        }
    }

    private Usuario realizarLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        Usuario usuarioLogado = Usuario.login(email, senha);
        
        if (usuarioLogado == null) {
            System.err.println("❌ Login falhou. Email ou senha incorretos.");
        }
        return usuarioLogado;
    }

    // Mantenha os métodos: editarPerfil e excluirConta.
    // ... (restante do código: editarPerfil e excluirConta) ...
}