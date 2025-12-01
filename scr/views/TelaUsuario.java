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
            
           
            boolean sucesso = Usuario.registrarUsuario(nome, email, senha, dataNascimento);
            
            if (sucesso) System.out.println("Usuário registrado com sucesso!");
            else System.err.println("Falha ao registrar. Email pode já existir.");
            
        } catch (ParseException e) {
            System.err.println("Data inválida. Use o formato dd/MM/yyyy.");
        }
    }

    private Usuario realizarLogin() {
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

}