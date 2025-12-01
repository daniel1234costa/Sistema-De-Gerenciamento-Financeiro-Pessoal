package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import model.Despesa;
import model.DatabaseConnector;
import model.UtilData;

public class DespesaDAO {

    // --- SQL Queries ---
    // Mapeamento: nomeDespesa -> descricao | data -> data_despesa
    private static final String SQL_INSERT = 
        "INSERT INTO Despesa (id_despesa, id_usuario, descricao, valor, data_despesa, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
    
    // Ordena por data decrescente (mais recente primeiro)
    // O 'substr' é usado porque a data está salva como texto dd/MM/yyyy no SQLite
    private static final String SQL_SELECT_ALL_BY_USER = 
        "SELECT * FROM Despesa WHERE id_usuario = ? ORDER BY substr(data_despesa, 7, 4) || substr(data_despesa, 4, 2) || substr(data_despesa, 1, 2) DESC";

    private static final String SQL_SELECT_BY_ID = 
        "SELECT * FROM Despesa WHERE id_despesa = ?";

    private static final String SQL_UPDATE = 
        "UPDATE Despesa SET descricao = ?, valor = ?, data_despesa = ?, id_categoria = ? WHERE id_despesa = ?";

    private static final String SQL_DELETE = 
        "DELETE FROM Despesa WHERE id_despesa = ?";
    
    private static final String SQL_SUM_MONTH = 
        "SELECT SUM(valor) as total FROM Despesa WHERE id_usuario = ? AND substr(data_despesa, 4, 2) = ? AND substr(data_despesa, 7, 4) = ?";

    // Query para filtrar por intervalo de datas (convertendo texto para formato comparável yyyyMMdd)
    private static final String SQL_SELECT_BY_PERIOD = 
        "SELECT * FROM Despesa WHERE id_usuario = ? " +
        "AND (substr(data_despesa, 7, 4) || substr(data_despesa, 4, 2) || substr(data_despesa, 1, 2)) BETWEEN ? AND ? " +
        "ORDER BY substr(data_despesa, 7, 4) || substr(data_despesa, 4, 2) || substr(data_despesa, 1, 2) DESC";


    // --- CADASTRAR ---
    public boolean cadastrarDespesa(Despesa despesa) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {
            
            // Gera UUID se não existir
            if (despesa.getIdDespesa() == null || despesa.getIdDespesa().isEmpty()) {
                despesa.setIdDespesa(UUID.randomUUID().toString());
            }

            stmt.setString(1, despesa.getIdDespesa());
            stmt.setString(2, despesa.getIdUsuario());
            stmt.setString(3, despesa.getNomeDespesa()); // Java: nomeDespesa -> Banco: descricao
            stmt.setDouble(4, despesa.getValor());
            stmt.setString(5, UtilData.formatarData(despesa.getData())); // Java: Date -> Banco: String
            stmt.setString(6, despesa.getIdCategoria());

            stmt.execute();
            System.out.println("✅ Despesa cadastrada com sucesso!");
            return true;
            
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar despesa: " + e.getMessage());
            return false;
        }
    }

    // --- LISTAR TODAS DO USUÁRIO ---
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

    // --- LISTAR POR PERÍODO ---
    public List<Despesa> listarDespesasPorPeriodo(String idUsuario, Date inicio, Date fim) {
        List<Despesa> lista = new ArrayList<>();
        
        // Formata as datas Java para yyyyMMdd para bater com o SQL
        SimpleDateFormat sdfSql = new SimpleDateFormat("yyyyMMdd");

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_PERIOD)) {
            
            stmt.setString(1, idUsuario);
            stmt.setString(2, sdfSql.format(inicio));
            stmt.setString(3, sdfSql.format(fim));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapResultSetToDespesa(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar por período: " + e.getMessage());
        }
        return lista;
    }

    // --- BUSCAR POR ID ---
    public Despesa buscarPorId(String idDespesa) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            
            stmt.setString(1, idDespesa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToDespesa(rs);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar despesa: " + e.getMessage());
        }
        return null;
    }

    // --- EDITAR ---
    public boolean editarDespesa(Despesa despesa) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, despesa.getNomeDespesa());
            stmt.setDouble(2, despesa.getValor());
            stmt.setString(3, UtilData.formatarData(despesa.getData()));
            stmt.setString(4, despesa.getIdCategoria());
            stmt.setString(5, despesa.getIdDespesa()); // WHERE id_despesa = ?

            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("✅ Despesa atualizada!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao editar despesa: " + e.getMessage());
        }
        return false;
    }

    // --- EXCLUIR ---
    public boolean excluirDespesa(String idDespesa) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setString(1, idDespesa);
            int linhas = stmt.executeUpdate();
            
            if (linhas > 0) {
                System.out.println("✅ Despesa excluída.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir despesa: " + e.getMessage());
        }
        return false;
    }

    // --- VISUALIZAR ---
    public void visualizarDespesa(String idDespesa) {
        Despesa d = buscarPorId(idDespesa);
        if (d != null) {
            System.out.println("--- DETALHES DA DESPESA ---");
            System.out.println("ID: " + d.getIdDespesa());
            System.out.println("Descrição: " + d.getNomeDespesa());
            System.out.println("Valor: R$ " + d.getValor());
            System.out.println("Data: " + UtilData.formatarData(d.getData()));
            System.out.println("Categoria: " + d.getIdCategoria());
        } else {
            System.out.println("⚠️ Despesa não encontrada.");
        }
    }

    // --- CALCULAR TOTAL MENSAL ---
    public double calcularDespesaTotalMensal(int mes, int ano, String idUsuario) {
        double total = 0;
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SUM_MONTH)) {
            
            stmt.setString(1, idUsuario);
            stmt.setString(2, String.format("%02d", mes)); // Garante "05" em vez de "5"
            stmt.setString(3, String.valueOf(ano));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular total: " + e.getMessage());
        }
        return total;
    }

    // --- MÉTODO AUXILIAR: Converte ResultSet em Objeto Java ---
    private Despesa mapResultSetToDespesa(ResultSet rs) throws SQLException {
        Despesa d = new Despesa();
        d.setIdDespesa(rs.getString("id_despesa"));
        d.setIdUsuario(rs.getString("id_usuario"));
        d.setNomeDespesa(rs.getString("descricao")); // Pega do banco 'descricao' e põe em 'nomeDespesa'
        d.setValor(rs.getDouble("valor"));
        d.setData(UtilData.parseData(rs.getString("data_despesa")));
        d.setIdCategoria(rs.getString("id_categoria"));
        return d;
    }
}
