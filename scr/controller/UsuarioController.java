package controller;

import model.Usuario;
import model.UsuarioDAO;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Date;
import java.util.UUID;
import java.util.Scanner;
import java.sql.SQLException;
import java.sql.Types; // Import necessário para java.sql.Types

public class UsuarioController {

    public boolean registrarUsuario(String nome, String email, String senhaPura, Date dataNascimento) {

        return Usuario.registrarUsuario(nome, email, senhaPura, dataNascimento);

    }

    public Usuario login(String email, String senhaPura) {
        return Usuario.login(email, senhaPura);
    }

    public boolean atualizarUsuario(Usuario usuario) {
        return Usuario.atualizarUsuario(usuario);
    }

    public boolean editarUsuario(Usuario usuario) {

        return Usuario.editarUsuario(usuario);
    }

    public static void excluirUsuario(String email) {

        Usuario.excluirUsuario(email);
    }

}
