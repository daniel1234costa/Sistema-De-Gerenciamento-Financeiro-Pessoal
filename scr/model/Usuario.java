package model;

import java.util.Date;
import java.util.UUID;
import model.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Date dataNascimento;

    public Usuario() {

    }

    public Usuario(String idUsuario, String nome, String email, String senha, Date dataNascimento) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public static boolean registrarUsuario(String nome, String email, String senhaPura, Date dataNascimento) {
        final UsuarioDAO usuarioDAO = new UsuarioDAO();
        String senhaHash = BCrypt.hashpw(senhaPura, BCrypt.gensalt());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setIdUsuario(UUID.randomUUID().toString());
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senhaHash);
        novoUsuario.setDataNascimento(dataNascimento);

        return usuarioDAO.registrarUsuario(novoUsuario);
    }

    public static Usuario login(String email, String senhaPura) {
        final UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioDB = usuarioDAO.buscarPorEmail(email);

        if (usuarioDB == null) {
            System.out.println("Erro de Login: Usuário não encontrado.");
            return null;
        }

        String hashSalvo = usuarioDB.getSenha();

        if (BCrypt.checkpw(senhaPura, hashSalvo)) {

            usuarioDB.setSenha(null);
            System.out.println("Login bem-sucedido! Bem-vindo(a), " + usuarioDB.getNome());
            return usuarioDB;
        } else {
            System.out.println("Erro de Login: Senha incorreta.");
            return null;
        }
    }

    public static boolean atualizarUsuario(Usuario usuario) {
        final UsuarioDAO usuarioDAO = new UsuarioDAO();
        String sql = "UPDATE Usuario SET nome = ?, email = ?, data_nascimento = ? WHERE id_usuario = ?";

        try (java.sql.Connection conn = model.DatabaseConnector.conectar(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());

            if (usuario.getDataNascimento() != null) {

                java.sql.Date sqlDate = new java.sql.Date(usuario.getDataNascimento().getTime());

                stmt.setDate(3, sqlDate);
            } else {

                stmt.setNull(3, java.sql.Types.DATE);
            }

            stmt.setString(4, usuario.getIdUsuario());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (java.sql.SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    public static boolean editarUsuario(Usuario usuario) {
        final UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
            System.err.println("Erro: ID do usuário é necessário para a edição.");
            return false;
        }

        System.out.println("Tentando atualizar perfil do usuário: " + usuario.getNome());

        boolean sucesso = Usuario.atualizarUsuario(usuario);

        if (sucesso) {
            System.out.println("Perfil atualizado com sucesso!");
        } else {
            System.err.println("Falha ao atualizar o perfil. Verifique o ID e o email.");
        }
        return sucesso;
    }

    public static void excluirUsuario(String email) {
        final UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null) {
            usuarioDAO.excluir(usuario.getIdUsuario());
            System.out.println("Usuário excluído com sucesso.");
        } else {
            System.out.println("Usuário não encontrado com o email: " + email);
        }
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
