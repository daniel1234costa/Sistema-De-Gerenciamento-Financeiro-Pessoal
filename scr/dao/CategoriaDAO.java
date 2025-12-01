package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;
import model.DatabaseConnector;

public class CategoriaDAO {

    public boolean inserir(Categoria categoria) {
        String sql = "INSERT INTO Categoria (idCategoria, nomeCategoria, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getIdCategoria());
            stmt.setString(2, categoria.getNomeCategoria());
            stmt.setBoolean(3, categoria.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir categoria: " + e.getMessage());
            return false;
        }
    }

    public List<Categoria> listarTodos() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }

        return categorias;
    }

    public Categoria buscarCategoria(String nome) {
        String sql = "SELECT * FROM Categoria WHERE nomeCategoria LIKE ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria: " + e.getMessage());
        }

        return null;
    }

    public boolean atualizar(Categoria categoria) {
        String sql = "UPDATE Categoria SET nomeCategoria = ?, status = ? WHERE idCategoria = ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNomeCategoria());
            stmt.setBoolean(2, categoria.getStatus());
            stmt.setString(3, categoria.getIdCategoria());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar categoria: " + e.getMessage());
            return false;
        }
    }
}