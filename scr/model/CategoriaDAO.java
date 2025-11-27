package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // ✅ Cadastrar categoria
    public void cadastrarCategoria(String nomeCategoria) {
        String sql = "INSERT INTO Categoria (idCategoria, nomeCategoria, status) VALUES (?, ?, TRUE)";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String id = java.util.UUID.randomUUID().toString();

            stmt.setString(1, id);
            stmt.setString(2, nomeCategoria);
            stmt.execute();

            System.out.println("Categoria cadastrada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar categoria: " + e.getMessage());
        }
    }

    // ✅ Editar categoria
    public void editarCategoria(String id, String novoNome) {
        String sql = "UPDATE Categoria SET nomeCategoria = ? WHERE idCategoria = ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setString(2, id);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Categoria atualizada com sucesso!");
            } else {
                System.out.println("Nenhuma categoria encontrada com esse ID.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao editar categoria: " + e.getMessage());
        }
    }

    // ✅ Desativar categoria (status = FALSE)
    public void desativarCategoria(String id) {
        String sql = "UPDATE Categoria SET status = FALSE WHERE idCategoria = ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Categoria desativada com sucesso!");
            } else {
                System.out.println("Nenhuma categoria encontrada com esse ID.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao desativar categoria: " + e.getMessage());
        }
    }

    // ✅ Visualizar categoria
    public void visualizarCategoria(String id) {
        String sql = "SELECT * FROM Categoria WHERE idCategoria = ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n--- DETALHES DA CATEGORIA ---");
                System.out.println("ID: " + rs.getString("idCategoria"));
                System.out.println("Nome: " + rs.getString("nomeCategoria"));
                System.out.println("Status: " + (rs.getBoolean("status") ? "Ativa" : "Desativada"));
            } else {
                System.out.println("Nenhuma categoria encontrada com esse ID.");
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao visualizar categoria: " + e.getMessage());
        }
    }

    // ✅ Listar todas as categorias
    public List<Categoria> listarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categoria";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
                categorias.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }

        return categorias;
    }

    // ✅ Buscar categoria por ID
    public Categoria buscarCategoria(String id) {
        String sql = "SELECT * FROM Categoria WHERE idCategoria = ?";
        Categoria categoria = null;

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                categoria = new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria: " + e.getMessage());
        }

        return categoria;
    }
}