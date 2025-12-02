package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import model.Categoria;
import model.DatabaseConnector;
import model.Despesa;
import model.UtilData;

public class DespesaDAO {

    private static final String SQL_INSERT =
        "INSERT INTO Despesa (idDespesa, idUsuario, nomeDespesa, valor, data, idCategoria) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL_BY_USER =
        "SELECT d.*, c.nomeCategoria, c.status " +
        "FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria " +
        "WHERE d.idUsuario = ? ORDER BY date(d.data) DESC";

    private static final String SQL_SELECT_BY_ID =
        "SELECT d.*, c.nomeCategoria, c.status " +
        "FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria " +
        "WHERE d.idDespesa = ? AND d.idUsuario = ?";

    private static final String SQL_UPDATE =
        "UPDATE Despesa SET nomeDespesa = ?, valor = ?, data = ?, idCategoria = ? " +
        "WHERE idDespesa = ? AND idUsuario = ?";

    private static final String SQL_DELETE =
        "DELETE FROM Despesa WHERE idDespesa = ? AND idUsuario = ?";

    private static final String SQL_SELECT_PERIODO =
        "SELECT d.*, c.nomeCategoria, c.status " +
        "FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria " +
        "WHERE d.idUsuario = ? AND date(d.data) BETWEEN ? AND ?";

    private static final String SQL_TOTAL_MENSAL =
        "SELECT SUM(valor) AS total FROM Despesa " +
        "WHERE idUsuario = ? AND strftime('%m', data) = ? AND strftime('%Y', data) = ?";

    private static final String SQL_SELECT_BY_TERM =
        "SELECT d.*, c.nomeCategoria, c.status " +
        "FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria " +
        "WHERE d.nomeDespesa LIKE ? AND d.idUsuario = ?";

    private static final String SQL_SELECT_BY_CATEGORY =
        "SELECT d.*, c.nomeCategoria, c.status " +
        "FROM Despesa d JOIN Categoria c ON d.idCategoria = c.idCategoria " +
        "WHERE d.idCategoria = ? AND d.idUsuario = ?";

    public boolean cadastrarDespesa(Despesa despesa) {

      
        if (despesa.getCategoria() == null || !despesa.getCategoria().getStatus()) {
            System.out.println("Erro: a categoria está desativada. Não é possível cadastrar a despesa.");
            return false;
        }

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            if (despesa.getIdDespesa() == null || despesa.getIdDespesa().isEmpty()) {
                despesa.setIdDespesa(UUID.randomUUID().toString());
            }

            stmt.setString(1, despesa.getIdDespesa());
            stmt.setString(2, despesa.getIdUsuario());
            stmt.setString(3, despesa.getNomeDespesa());
            stmt.setDouble(4, despesa.getValor());
            stmt.setString(5, UtilData.formatarData(despesa.getData()));
            stmt.setString(6, despesa.getCategoria().getIdCategoria());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar despesa: " + e.getMessage());
            return false;
        }
    }

    public List<Despesa> listarDespesas(String idUsuario) {
        List<Despesa> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_BY_USER)) {

            stmt.setString(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapResultSetToDespesa(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas: " + e.getMessage());
        }

        return lista;
    }

    public List<Despesa> listarDespesasPorPeriodo(String idUsuario, Date inicio, Date fim) {
        List<Despesa> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_PERIODO)) {

            stmt.setString(1, idUsuario);
            stmt.setString(2, UtilData.formatarData(inicio));
            stmt.setString(3, UtilData.formatarData(fim));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapResultSetToDespesa(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas por período: " + e.getMessage());
        }

        return lista;
    }

    public double calcularDespesaTotalMensal(int mes, int ano, String idUsuario) {

        try (Connection conn = DatabaseConnector.conectar();
            PreparedStatement stmt = conn.prepareStatement(SQL_TOTAL_MENSAL)) {

            stmt.setString(1, idUsuario);
            stmt.setString(2, String.format("%02d", mes));
            stmt.setString(3, String.valueOf(ano));

            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getDouble("total") : 0.0;

        } catch (SQLException e) {
            System.out.println("Erro ao calcular total mensal: " + e.getMessage());
            return 0.0;
        }
    }

    public Despesa buscarPorId(String idDespesa, String idUsuario) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setString(1, idDespesa);
            stmt.setString(2, idUsuario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToDespesa(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar despesa: " + e.getMessage());
        }

        return null;
    }

    public Despesa buscarPorTermo(String termo, String idUsuario) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_TERM)) {

            stmt.setString(1, "%" + termo + "%");
            stmt.setString(2, idUsuario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToDespesa(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar despesa por termo: " + e.getMessage());
        }

        return null;
    }

    public boolean editarDespesa(Despesa despesa) {

        
        if (despesa.getCategoria() == null || !despesa.getCategoria().getStatus()) {
            System.out.println("Erro: a categoria está desativada. Não é possível editar a despesa.");
            return false;
        }

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, despesa.getNomeDespesa());
            stmt.setDouble(2, despesa.getValor());
            stmt.setString(3, UtilData.formatarData(despesa.getData()));
            stmt.setString(4, despesa.getCategoria().getIdCategoria());
            stmt.setString(5, despesa.getIdDespesa());
            stmt.setString(6, despesa.getIdUsuario());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao editar despesa: " + e.getMessage());
            return false;
        }
    }

    public boolean excluirDespesa(String idDespesa, String idUsuario) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setString(1, idDespesa);
            stmt.setString(2, idUsuario);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir despesa: " + e.getMessage());
            return false;
        }
    }

    public List<Despesa> listarPorCategoria(String idCategoria, String idUsuario) {
        List<Despesa> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_CATEGORY)) {

            stmt.setString(1, idCategoria);
            stmt.setString(2, idUsuario);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapResultSetToDespesa(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas por categoria: " + e.getMessage());
        }

        return lista;
    }

    private Despesa mapResultSetToDespesa(ResultSet rs) throws SQLException {

        Categoria categoria = new Categoria(
            rs.getString("idCategoria"),
            rs.getString("nomeCategoria"),
            rs.getBoolean("status"),
            rs.getString("idUsuario")
        );

        Despesa d = new Despesa();
        d.setIdDespesa(rs.getString("idDespesa"));
        d.setIdUsuario(rs.getString("idUsuario"));
        d.setNomeDespesa(rs.getString("nomeDespesa"));
        d.setValor(rs.getDouble("valor"));
        d.setData(UtilData.parseDataBanco(rs.getString("data")));
        d.setCategoria(categoria);

        return d;
    }
}