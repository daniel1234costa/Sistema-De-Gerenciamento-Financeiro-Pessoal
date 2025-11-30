PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Usuario (
    id_usuario VARCHAR(255) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_nascimento DATE
);


CREATE TABLE IF NOT EXISTS Categoria (
    idCategoria VARCHAR(255) PRIMARY KEY,
    nomeCategoria VARCHAR(255) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE
);


CREATE TABLE IF NOT EXISTS Renda (
    idRenda INTEGER PRIMARY KEY AUTOINCREMENT,
    idUsuario INTEGER NOT NULL,
    nomeRenda VARCHAR(255) NOT NULL,
    valor NUMERIC(10, 2) NOT NULL,
    data TEXT NOT NULL,                       
    tipo BOOLEAN NOT NULL,                   
    FOREIGN KEY (idUsuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Despesa (
    idDespesa VARCHAR(255) PRIMARY KEY,
    idUsuario VARCHAR(255) NOT NULL,
    idCategoria VARCHAR(255) NOT NULL,
    nomeDespesa VARCHAR(255) NOT NULL,
    valor NUMERIC(10, 2) NOT NULL,
    data DATE NOT NULL,
    
   
    FOREIGN KEY (idUsuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE,
    
    FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria) ON DELETE RESTRICT 
);