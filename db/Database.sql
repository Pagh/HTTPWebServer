DROP DATABASE IF EXISTS Database_TPSIT;
CREATE DATABASE Database_TPSIT;
USE Database_TPSIT;

CREATE TABLE IF NOT EXISTS  Alunni (
    Nome VARCHAR(45),
    Cognome VARCHAR(45)
);

INSERT INTO Alunni (Nome, Cognome)
VALUES 
	("Francesco", "Lazzarelli"),
    ("Edoardo", "Croci"),
    ("Andrea", "Lotti"),
    ("Francesco", "Filippini"),
    ("Niccolo", "Fappani"),
    ("Samuele", "Pagliarello"),
    ("Samuele", "Marrani"),
    ("Leonardo", "Romagnoli"),
    ("Lorenzo", "Camigliano"),
    ("Alessandro", "Feri");