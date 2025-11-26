package model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID; // Para gerar o ID VARCHAR

public class UsuarioDAO {

    // --- SQL Queries ---
    private static final String SQL_INSERT = 
        "INSERT INTO Usuario (id_usuario, nome, email, senha, data_nascimento) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_EMAIL = 
        "SELECT id_usuario, nome, email, senha, data_nascimento FROM Usuario WHERE email = ?";
    private static final String SQL_DELETE = 
        "DELETE FROM Usuario WHERE id_usuario = ?";

    // --- (A) Registrar Usuário (CREATE) ---
    public boolean registrarUsuario(Usuario usuario) {
        Connection conn = DatabaseConnector.conectar();
        PreparedStatement stmt = null;
        
        try {
            if (usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
                usuario.setIdUsuario(UUID.randomUUID().toString());
            }

            stmt = conn.prepareStatement(SQL_INSERT);
            
            stmt.setString(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            
            // CONVERSÃO ESSENCIAL: java.util.Date para String (SQLite)
            stmt.setString(5, UtilData.formatarData(usuario.getDataNascimento())); 

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            return false;
            
        } finally {
            DatabaseConnector.fecharConexao(conn);
            // Fechamento seguro do stmt
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) {} }
        }
    }

    // --- (B) Buscar por Email (READ) ---
    public Usuario buscarPorEmail(String email) {
        Connection conn = DatabaseConnector.conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            stmt = conn.prepareStatement(SQL_SELECT_BY_EMAIL);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getString("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                
                // CONVERSÃO ESSENCIAL: String (SQLite) para java.util.Date
                usuario.setDataNascimento(UtilData.parseData(rs.getString("data_nascimento"))); 
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        } finally {
           DatabaseConnector.fecharConexao(conn);
            // Fechamento seguro dos recursos
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) {} }
        }
        return usuario;
    }
    
   public void excluir(String idUsuario) {
        // SQL para deletar um usuário pelo seu ID único
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o ID do usuário a ser excluído no primeiro parâmetro
            stmt.setString(1, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                // Mensagem de sucesso no nível DAO
                System.out.println("Registro de usuário ID " + idUsuario + " excluído.");
            } else {
                System.out.println("Nenhum registro encontrado para exclusão com o ID: " + idUsuario);
            }

        } catch (SQLException e) {
            // Loga o erro, mas não lança (o Controller lidará com o resultado booleano, se houver)
            System.err.println("Erro ao executar exclusão no banco de dados: " + e.getMessage());
        }
    }
}
