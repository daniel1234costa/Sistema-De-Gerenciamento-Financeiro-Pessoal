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

    private static final String SQL_INSERT = 
        "INSERT INTO Despesa (idDespesa, idUsuario, nomeDespesa, valor, data, idCategoria) VALUES (?, ?, ?, ?, ?, ?)";
   
    private static final String SQL_SELECT_ALL_BY_USER = 
        "SELECT * FROM Despesa WHERE idUsuario = ? ORDER BY substr(data, 7, 4) || substr(data, 4, 2) || substr(data, 1, 2) DESC";

    private static final String SQL_SELECT_BY_ID = 
        "SELECT * FROM Despesa WHERE idDespesa = ?";

    private static final String SQL_UPDATE = 
        "UPDATE Despesa SET nomeDespesa = ?, valor = ?, data = ?, idCategoria = ? WHERE idDespesa = ?";

    private static final String SQL_DELETE = 
        "DELETE FROM Despesa WHERE idDespesa = ?";

    
    private static final String SQL_SELECT_BY_PERIOD = 
        "SELECT * FROM Despesa WHERE idUsuario = ? AND data BETWEEN ? AND ? ORDER BY data DESC";
    
    private static final String SQL_CALCULATE_MONTHLY = 
        "SELECT SUM(valor) AS total FROM Despesa WHERE idUsuario = ? AND strftime('%m', data) = ? AND strftime('%Y', data) = ?";


   
    private Despesa mapResultSetToDespesa(ResultSet rs) throws SQLException {
        Despesa d = new Despesa();
       
        d.setIdDespesa(rs.getString("idDespesa"));
        d.setIdUsuario(rs.getString("idUsuario"));
        d.setNomeDespesa(rs.getString("nomeDespesa")); 
        d.setValor(rs.getDouble("valor"));
       
        d.setData(UtilData.parseData(rs.getString("data"))); 
        d.setIdCategoria(rs.getString("idCategoria"));
        return d;
    }


  
    public boolean cadastrarDespesa(Despesa despesa) {
      
        if (despesa.getIdDespesa() == null || despesa.getIdDespesa().isEmpty()) {
             despesa.setIdDespesa(UUID.randomUUID().toString());
         }

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {
            
            stmt.setString(1, despesa.getIdDespesa());
            stmt.setString(2, despesa.getIdUsuario());
            stmt.setString(3, despesa.getNomeDespesa()); 
            stmt.setDouble(4, despesa.getValor());
            stmt.setString(5, UtilData.formatarData(despesa.getData())); 
            stmt.setString(6, despesa.getIdCategoria());

            stmt.execute();
          
            // System.out.println(" Despesa cadastrada com sucesso!"); 
            return true;
            
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

 
    public List<Despesa> listarDespesasPorCategoria(String idUsuario, String idCategoria) {
        List<Despesa> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM Despesa WHERE idUsuario = ? AND idCategoria = ? ORDER BY substr(data, 7, 4) || substr(data, 4, 2) || substr(data, 1, 2) DESC";
        
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idUsuario);
            stmt.setString(2, idCategoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapResultSetToDespesa(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar despesas por categoria: " + e.getMessage());
        }
        return lista;
    }

   
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

  
    public List<Despesa> listarDespesasPorPeriodo(String idUsuario, Date inicio, Date fim) {
        List<Despesa> lista = new ArrayList<>();
    
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_PERIOD)) {
            
            stmt.setString(1, idUsuario);
            
            stmt.setString(2, sdf.format(inicio)); 
            stmt.setString(3, sdf.format(fim));
            
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
        double total = 0;
        
        String mesStr = String.format("%02d", mes); // Garante 01, 02, etc.
        String anoStr = String.valueOf(ano);
        
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_CALCULATE_MONTHLY)) {
            
            stmt.setString(1, idUsuario);
            stmt.setString(2, mesStr);
            stmt.setString(3, anoStr);
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
             
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular despesa mensal: " + e.getMessage());
        }
        return total;
    }


    public boolean editarDespesa(Despesa despesa) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, despesa.getNomeDespesa());
            stmt.setDouble(2, despesa.getValor());
            stmt.setString(3, UtilData.formatarData(despesa.getData()));
            stmt.setString(4, despesa.getIdCategoria());
            stmt.setString(5, despesa.getIdDespesa()); 

            int linhas = stmt.executeUpdate();
            
            return linhas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao editar despesa: " + e.getMessage());
            return false;
        }
    }

  
    public boolean excluirDespesa(String idDespesa) {
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setString(1, idDespesa);
            int linhas = stmt.executeUpdate();
            
         
            return linhas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir despesa: " + e.getMessage());
            return false;
        }
    }
}