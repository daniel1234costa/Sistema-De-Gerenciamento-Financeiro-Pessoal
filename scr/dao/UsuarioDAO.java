package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import model.DatabaseConnector; 
import model.Usuario; 
import model.UtilData; 

public class UsuarioDAO {

    private static final String SQL_INSERT = 
        "INSERT INTO Usuario (id_usuario, nome, email, senha, data_nascimento) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_EMAIL = 
        "SELECT id_usuario, nome, email, senha, data_nascimento FROM Usuario WHERE email = ?";
    private static final String SQL_DELETE = 
        "DELETE FROM Usuario WHERE id_usuario = ?";
    
    private static final String SQL_UPDATE = 
        "UPDATE Usuario SET nome = ?, email = ?, data_nascimento = ? WHERE id_usuario = ?";

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
            
            stmt.setString(5, UtilData.formatarData(usuario.getDataNascimento())); 

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            return false;
            
        } finally {
            DatabaseConnector.fecharConexao(conn);
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) {} }
        }
    }
    

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
                
                
                usuario.setDataNascimento(UtilData.parseDataBanco(rs.getString("data_nascimento"))); 
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        } finally {
            DatabaseConnector.fecharConexao(conn);
            
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) {} }
        }
        return usuario;
    }
    
  
    public boolean atualizarUsuario(Usuario usuario) {
        Connection conn = DatabaseConnector.conectar();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(SQL_UPDATE);
            
           
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            
           
            stmt.setString(3, UtilData.formatarData(usuario.getDataNascimento()));
            
           
            stmt.setString(4, usuario.getIdUsuario()); 

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
            
        } finally {
            DatabaseConnector.fecharConexao(conn);
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) {} }
        }
    }
    
    
    public boolean excluir(String idUsuario) { 
        boolean sucesso = false;

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setString(1, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Registro de usuário ID " + idUsuario + " excluído.");
                sucesso = true;
            } else {
                System.out.println("Nenhum registro encontrado para exclusão com o ID: " + idUsuario);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao executar exclusão no banco de dados: " + e.getMessage());
        }
        return sucesso;
    }
    
    public model.Usuario buscarPorId(String id) {
        String sql = "SELECT * FROM Usuario WHERE id_usuario = ?";
        try (java.sql.Connection conn = model.DatabaseConnector.conectar();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                model.Usuario u = new model.Usuario();
                u.setIdUsuario(rs.getString("id_usuario"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setDataNascimento(model.UtilData.parseDataBanco(rs.getString("data_nascimento")));
                return u;
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return null;
    }
}