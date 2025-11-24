import java.util.Date;

class Usuario{
    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Date dataNascimento;


public Usuario(String idUsuario, String nome, String email, String senha, Date dataNascimento){
    this.idUsuario = idUsuario;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.dataNascimento = dataNascimento;
}

public Usuario(){}

public String getIdUsuario(){
    return idUsuario;
}
public String getNome(){
    return nome;
}
public String getEmail(){
    return email;
}
public String getSenha(){
    return senha;
}
public Date getDataNascimento(){
    return dataNascimento;
}
  
public boolean editarUsuario(String nome, String email, String senha, Date dataNascimento){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        return true;
    }
public void visualizarUsuario(){
    System.out.println("ID: " + idUsuario);
    System.out.println("Nome: "  + nome);
    System.out.println("Email: " + email);
    System.out.println("Data de Nascimento: " + dataNascimento);
}

public static boolean excluirUsuarioI(Usuario usuario){
    if (usuario == null){
        System.out.println("Usuário não encontrado.");
        return false;
    } else {
        System.out.println("Usuário " + usuario.getNome() + " excluído com sucesso.");
        return true;
    }
}


}