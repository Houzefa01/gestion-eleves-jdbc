-- ============================================================================
-- Setup complet GestionEleves (MariaDB / MySQL)
-- Cree la base, les tables, les utilisateurs et 20 eleves.
-- ============================================================================
-- Usage :
--   mysql -u root -p < script.sql
-- ============================================================================

CREATE DATABASE IF NOT EXISTS gestion_eleves CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE gestion_eleves;

-- ============================================================================
-- Table : utilisateurs
-- ============================================================================
CREATE TABLE IF NOT EXISTS utilisateurs (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nom      VARCHAR(100) NOT NULL,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'USER'
) ENGINE=InnoDB;

-- ============================================================================
-- Table : eleves
-- ============================================================================
CREATE TABLE IF NOT EXISTS eleves (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nom            VARCHAR(100) NOT NULL,
    prenom         VARCHAR(100) NOT NULL,
    date_naissance DATE         NOT NULL,
    sexe           VARCHAR(10)  NOT NULL,
    adresse        VARCHAR(255),
    telephone      VARCHAR(20),
    email          VARCHAR(100),
    classe         VARCHAR(50)  NOT NULL
) ENGINE=InnoDB;

-- ============================================================================
-- Utilisateurs (mots de passe hashes BCrypt)
-- admin / admin   |   user / user
-- ============================================================================
INSERT IGNORE INTO utilisateurs (nom, username, password, role) VALUES
('Administrateur',     'admin', '$2a$12$j0Jix6SQEijXJkwTHYksWuE1IFilj6OOoru6jfAdV76xpcpYCHdRS', 'ADMIN'),
('Utilisateur Standard','user', '$2a$12$W7ljhEw6Yft9EybZVdufzu1VWZu.P4R7bbj1rpfLPZ2KbF9WQgG/y', 'USER');

-- ============================================================================
-- Eleves (20)
-- ============================================================================
INSERT IGNORE INTO eleves (nom, prenom, date_naissance, sexe, adresse, telephone, email, classe) VALUES
('Dupont',    'Jean',    '2005-03-15', 'M', '12 rue de Paris, 75001 Paris',       '0612345678', 'jean.dupont@email.com',    'Terminale S'),
('Martin',    'Sophie',  '2006-07-22', 'F', '45 avenue des Fleurs, 69002 Lyon',    '0623456789', 'sophie.martin@email.com',  'Terminale S'),
('Bernard',   'Lucas',   '2005-11-08', 'M', '8 boulevard Haussmann, 75008 Paris',  '0634567890', 'lucas.bernard@email.com',  'Première ES'),
('Petit',     'Emma',    '2007-01-30', 'F', '23 rue du Commerce, 33000 Bordeaux',  '0645678901', 'emma.petit@email.com',     'Seconde'),
('Durand',    'Thomas',  '2006-05-17', 'M', '67 chemin des Vignes, 31000 Toulouse','0656789012', 'thomas.durand@email.com',  'Première S'),
('Leroy',     'Camille', '2005-09-03', 'F', '34 rue de la Paix, 44000 Nantes',     '0667890123', 'camille.leroy@email.com',  'Terminale ES'),
('Moreau',    'Hugo',    '2007-04-11', 'M', '56 avenue Victor Hugo, 13001 Marseille','0678901234','hugo.moreau@email.com',    'Seconde'),
('Simon',     'Léa',     '2006-08-25', 'F', '78 rue du Général Leclerc, 59000 Lille','0689012345','lea.simon@email.com',      'Première L'),
('Laurent',   'Nathan',  '2005-02-14', 'M', '90 boulevard Saint-Michel, 75005 Paris','0690123456','nathan.laurent@email.com', 'Terminale S'),
('Michel',    'Chloé',   '2007-06-19', 'F', '12 place Bellecour, 69001 Lyon',      '0601234567', 'chloe.michel@email.com',   'Seconde'),
('Lefebvre',  'Antoine', '2006-10-07', 'M', '34 rue Nationale, 37000 Tours',       '0612345670', 'antoine.lefebvre@email.com','Première S'),
('Roux',      'Manon',   '2005-12-28', 'F', '56 allée des Tilleuls, 67000 Strasbourg','0623456780','manon.roux@email.com',     'Terminale L'),
('David',     'Alexis',  '2006-03-09', 'M', '78 rue de la Liberté, 21000 Dijon',   '0634567801', 'alexis.david@email.com',   'Première ES'),
('Bertrand',  'Sarah',   '2007-07-14', 'F', '90 place de la Comédie, 34000 Montpellier','0645678902','sarah.bertrand@email.com','Seconde'),
('Robert',    'Maxime',  '2005-05-21', 'M', '23 avenue Jean Jaurès, 69007 Lyon',   '0656789023', 'maxime.robert@email.com',  'Terminale S'),
('Richard',   'Julie',   '2006-11-01', 'F', '45 rue du Faubourg Saint-Honoré, 75008 Paris','0667890134','julie.richard@email.com','Première L'),
('Girard',    'Quentin', '2007-02-16', 'M', '67 boulevard de la Croisette, 06400 Cannes','0678901245','quentin.girard@email.com','Seconde'),
('Thomas',    'Amélie',  '2005-08-30', 'F', '89 chemin de la Côte, 13007 Marseille','0689012356','amelie.thomas@email.com', 'Terminale ES'),
('Dumont',    'Paul',    '2006-04-05', 'M', '12 rue du Moulin, 59800 Lille',       '0690123467', 'paul.dumont@email.com',    'Première S'),
('Fontaine',  'Alice',   '2007-09-22', 'F', '34 avenue des Champs-Élysées, 75008 Paris','0601234578','alice.fontaine@email.com','Seconde');
