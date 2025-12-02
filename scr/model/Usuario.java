package model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

import dao.DespesaDAO;
import dao.RendaDAO;
import dao.UsuarioDAO; 

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
            
            
            if (usuario.getNome().isEmpty()) {
                System.out.println("Erro: Nome não pode ser vazio.");
                return false;
            }

            UsuarioDAO dao = new UsuarioDAO();
            return dao.atualizarUsuario(usuario); 
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
     

    public void visualizarUsuario(Usuario usuario) { 
    if (usuario == null) {
        System.out.println("Erro: Usuário não informado para visualização.");
        return;
    }

    System.out.println("\n===== SEU PERFIL =====");
    System.out.println("ID: " + usuario.getIdUsuario());
    System.out.println("Nome: " + usuario.getNome());
    System.out.println("Email: " + usuario.getEmail());

    if (usuario.getDataNascimento() != null) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Data de Nascimento: " + sdf.format(usuario.getDataNascimento()));
    } else {
        System.out.println("Data de Nascimento: Não informada");
    }

    System.out.println("=======================\n");
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

    public String listarRendasDespesasPorPeriodo(String inicio, String fim) {
        Date dataInicio = UtilData.parseDataUsuario(inicio);
        Date dataFim = UtilData.parseDataUsuario(fim);

        if (dataInicio == null || dataFim == null) {
            return "Datas inválidas. Use o formato dd/MM/yyyy.";
        }

        String idUsuario = this.idUsuario;

        List<Despesa> despesas = new DespesaDAO().listarDespesasPorPeriodo(idUsuario, dataInicio, dataFim);

        double totalDespesas = 0;
        for (Despesa d : despesas) {
            totalDespesas += d.getValor();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInicio);

        int mes = cal.get(Calendar.MONTH) + 1;
        int ano = cal.get(Calendar.YEAR);

        double totalRendas = new RendaDAO().calcularRendaTotalMensal(mes, ano, idUsuario);

        double saldo = totalRendas - totalDespesas;

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("\n===== RELATÓRIO FINANCEIRO =====\n");
        relatorio.append("Período: ").append(inicio).append(" até ").append(fim).append("\n\n");

        relatorio.append("Total de Rendas (mês): R$ ").append(totalRendas).append("\n");
        relatorio.append("Total de Despesas (período): R$ ").append(totalDespesas).append("\n");
        relatorio.append("Saldo do Período: R$ ").append(saldo).append("\n");

        relatorio.append("\n===============================\n");

        return relatorio.toString();
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