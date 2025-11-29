package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;

public class DespesaDAO {

    public void cadastrarDespesa(Despesa despesa) {
        String sql = "INSERT INTO Despesa (idDespesa, nomeDespesa, valor, data, idCategoria) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, despesa.getIdDespesa());
            stmt.setString(2, despesa.getNomeDespesa());
            stmt.setDouble(3, despesa.getValor());
            stmt.setDate(4, new java.sql.Date(despesa.getData().getTime()));
            stmt.setString(5, despesa.getCategoria().getIdCategoria());
            stmt.execute();

            System.out.println("Despesa cadastrada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar despesa: " + e.getMessage());
        }
    }

    public boolean excluirDespesa(String id) {
        String sql = "DELETE FROM Despesa WHERE idDespesa = ?";
        boolean sucesso = false;

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Despesa excluída com sucesso!");
                sucesso = true;
            } else {
                System.out.println("Nenhuma despesa encontrada com esse ID.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir despesa: " + e.getMessage());
        }
        return sucesso;
    }

    public void editarDespesa(String id, String novoNome, double novoValor, Date novaData, Categoria novaCategoria) {
        String sql = "UPDATE Despesa SET nomeDespesa = ?, valor = ?, data = ?, idCategoria = ? WHERE idDespesa = ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setDouble(2, novoValor);
            stmt.setDate(3, new java.sql.Date(novaData.getTime()));
            stmt.setString(4, novaCategoria.getIdCategoria());
            stmt.setString(5, id);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Despesa atualizada com sucesso!");
            } else {
                System.out.println("Nenhuma despesa encontrada com esse ID.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao editar despesa: " + e.getMessage());
        }
    }

    public List<Despesa> listarDespesas() {
        List<Despesa> despesas = new ArrayList<>();
        String sql = "SELECT d.*, c.nomeCategoria, c.status FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
                Despesa d = new Despesa(
                    rs.getString("idDespesa"),
                    rs.getString("nomeDespesa"),
                    rs.getDouble("valor"),
                    rs.getDate("data"),
                    categoria
                );
                despesas.add(d);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas: " + e.getMessage());
        }
        return despesas;
    }

    public Despesa buscarDespesa(String termo) {
        String sql = "SELECT d.*, c.nomeCategoria, c.status FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria WHERE d.nomeDespesa LIKE ?";
        Despesa despesa = null;

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + termo + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
                despesa = new Despesa(
                    rs.getString("idDespesa"),
                    rs.getString("nomeDespesa"),
                    rs.getDouble("valor"),
                    rs.getDate("data"),
                    categoria
                );
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar despesa: " + e.getMessage());
        }
        return despesa;
    }

    public double calcularDespesaTotalMensal(int mes, int ano) {
        String sql = "SELECT SUM(valor) FROM Despesa WHERE strftime('%m', data) = ? AND strftime('%Y', data) = ?"; // SQLite specific date functions
        double total = 0.0;

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.format("%02d", mes)); // Format month to 2 digits
            stmt.setString(2, String.valueOf(ano));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao calcular despesa total mensal: " + e.getMessage());
        }
        return total;
    }

    public List<Despesa> listarDespesasPorPeriodo(Date inicio, Date fim) {
        List<Despesa> despesas = new ArrayList<>();
        String sql = "SELECT d.*, c.nomeCategoria, c.status FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria WHERE d.data BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(inicio.getTime()));
            stmt.setDate(2, new java.sql.Date(fim.getTime()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
                Despesa d = new Despesa(
                    rs.getString("idDespesa"),
                    rs.getString("nomeDespesa"),
                    rs.getDouble("valor"),
                    rs.getDate("data"),
                    categoria
                );
                despesas.add(d);
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas por período: " + e.getMessage());
        }
        return despesas;
    }

    public List<Despesa> listarDespesasPorCategoria(Categoria categoria) {
        List<Despesa> despesas = new ArrayList<>();
        String sql = "SELECT d.*, c.nomeCategoria, c.status FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria WHERE d.idCategoria = ?";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getIdCategoria());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Categoria cat = new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
                Despesa d = new Despesa(
                    rs.getString("idDespesa"),
                    rs.getString("nomeDespesa"),
                    rs.getDouble("valor"),
                    rs.getDate("data"),
                    cat
                );
                despesas.add(d);
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas por categoria: " + e.getMessage());
        }
        return despesas;
    }

    public Despesa buscarDespesaPorId(String id) {
        String sql = "SELECT d.*, c.nomeCategoria, c.status FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria WHERE d.idDespesa = ?";
        Despesa despesa = null;

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Categoria categoria = new Categoria(
                    rs.getString("idCategoria"),
                    rs.getString("nomeCategoria"),
                    rs.getBoolean("status")
                );
                despesa = new Despesa(
                    rs.getString("idDespesa"),
                    rs.getString("nomeDespesa"),
                    rs.getDouble("valor"),
                    rs.getDate("data"),
                    categoria
                );
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar despesa por ID: " + e.getMessage());
        }
        return despesa;
    }
}
