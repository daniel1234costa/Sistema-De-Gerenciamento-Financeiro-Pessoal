package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RendaDAO {

    public Renda cadastrarRenda(String nomeRenda, double valor, java.util.Date data, boolean tipoRenda) {
        Renda novaRenda = new Renda(nomeRenda, valor, data, tipoRenda);
        // ID Usuario 1 fixo por enquanto
        String sql = "INSERT INTO renda (nome, valor, data, tipo, id_usuario) VALUES (?, ?, ?, ?, 1)";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaRenda.getNomeRenda());
            stmt.setDouble(2, novaRenda.getValor());
            stmt.setString(3, UtilData.formatarData(novaRenda.getData()));
            stmt.setBoolean(4, novaRenda.isTipoRenda());

            stmt.execute();
            
            try (ResultSet gk = stmt.getGeneratedKeys()) {
                if (gk.next()) novaRenda.setIdRenda(String.valueOf(gk.getInt(1)));
            }
            System.out.println("✅ Renda cadastrada no SQLite!");

        } catch (SQLException e) {
            System.out.println(" Erro ao cadastrar: " + e.getMessage());
            return null;
        }
        return novaRenda;
    }

    public boolean excluirRenda(Renda renda) {
        String sql = "DELETE FROM renda WHERE id = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, Integer.parseInt(renda.getIdRenda()));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
            return false;
        }
    }

    public void editarRenda(String id, String nome, double valor) {
        String sql = "UPDATE renda SET nome = ?, valor = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setDouble(2, valor);
            stmt.setInt(3, Integer.parseInt(id));

            if (stmt.executeUpdate() > 0) System.out.println("✅ Renda atualizada!");
            else System.out.println("⚠️ ID não encontrado.");

        } catch (SQLException e) {
            System.out.println("Erro ao editar: " + e.getMessage());
        }
    }

    public void visualizarRenda(String id) {
        String sql = "SELECT * FROM renda WHERE id = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("--- DETALHES ---");
                System.out.println("Renda: " + rs.getString("nome") + " | Valor: R$ " + rs.getDouble("valor"));
                System.out.println("Data: " + rs.getString("data") + " | Tipo: " + (rs.getBoolean("tipo") ? "Fixa" : "Extra"));
            } else {
                System.out.println("Renda não encontrada.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao visualizar: " + e.getMessage());
        }
    }

    public List<Renda> listarRendasExtras() {
        return listarGenerico(0); // 0 = Extra
    }

    public List<Renda> listarRendasFixas() {
        return listarGenerico(1); // 1 = Fixa
    }

    private List<Renda> listarGenerico(int tipo) {
        List<Renda> lista = new ArrayList<>();
        String sql = "SELECT * FROM renda WHERE tipo = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, tipo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Renda r = new Renda();
                r.setIdRenda(String.valueOf(rs.getInt("id")));
                r.setNomeRenda(rs.getString("nome"));
                r.setValor(rs.getDouble("valor"));
                r.setData(UtilData.parseData(rs.getString("data")));
                r.setTipoRenda(rs.getBoolean("tipo"));
                lista.add(r);
            }
        } catch (SQLException e) { System.out.println("Erro listar: " + e.getMessage()); }
        return lista;
    }

    public double calcularRendaTotalMensal(int mes, int ano) {
        double total = 0;
        String sql = "SELECT SUM(valor) as total FROM renda WHERE substr(data, 4, 2) = ? AND substr(data, 7, 4) = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.format("%02d", mes));
            stmt.setString(2, String.valueOf(ano));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) total = rs.getDouble("total");
        } catch (SQLException e) { System.out.println("Erro calc: " + e.getMessage()); }
        return total;
    }
}