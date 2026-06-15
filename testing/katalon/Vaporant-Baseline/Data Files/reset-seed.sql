-- ============================================================================
-- reset-seed.sql — Ripristino dei DATI seed al baseline as-delivered.
-- Fixture di TEST ENVIRONMENT (precondizione), eseguita dal Test Listener
-- SeedReset prima di OGNI test case: garantisce indipendenza e ripetibilita'.
--
-- NON e' logica di test e NON e' un oracolo: nessun test legge il DB per
-- decidere pass/fail (gli oracoli restano osservati dalla UI = black-box).
--
-- Reset DATA-ONLY (niente DROP/CREATE): mantiene lo schema e le connessioni
-- del pool di Tomcat valide. Le righe INSERT sono copiate VERBATIM da
-- db/storage.sql (il seed ufficiale). Se storage.sql cambia, riallineare qui.
--
-- Si usa DELETE (non TRUNCATE): TRUNCATE richiede un metadata lock ESCLUSIVO e
-- si blocca se l'app (Tomcat) tiene una transazione aperta sulle tabelle (leak
-- del legacy, che CR_02 sistemera'). DELETE usa un lock SHARED_WRITE, compatibile
-- -> nessun hang. Gli ID di seed sono espliciti -> stato identico al baseline
-- (l'AUTO_INCREMENT non azzerato e' irrilevante per gli oracoli). I timeout brevi
-- evitano attese lunghe in caso di lock residuo.
-- ============================================================================

SET SESSION lock_wait_timeout = 10;
SET SESSION innodb_lock_wait_timeout = 10;
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM Contenuto;
DELETE FROM Ordine;
DELETE FROM Indirizzo;
DELETE FROM Prodotto;
DELETE FROM Utente;
SET FOREIGN_KEY_CHECKS = 1;

-- --- seed Utente (verbatim da storage.sql) ---
INSERT INTO Utente VALUES(1,'Gianfranco','Barba', '2002-02-15', 'BRBGFR02B15A508B', '3290026234', 'g.barba14@studenti.unisa.it', '$2a$12$g.c7oNc7197Wt2BR9eVAiOoxvK/tOejfZpPD3CW10Ov8P9lVFmU2i','admin', NULL);
INSERT INTO Utente VALUES(2,'Luigi','Guida', '2002-11-09', 'GDDLGG10G11A908B', '3336543123', 'l.guida6@studenti.unisa.it', '$2a$12$9lnSPIScaJjUVDT94rFTI.vjzFialrQHToZ4B0A65ksVGJ3p5zVJu','admin', NULL);
INSERT INTO Utente VALUES(3,'Francesco','Corcione', '2002-07-07', 'CRC07FRC07A567C', '3389076543', 'f.corcione5@studenti.unisa.it', '$2a$12$sChRszCXRN.9ytny/9bDReVqno6es.Z82djlHjbOQLE9ibvsBOpHa', 'admin', NULL);
INSERT INTO Utente VALUES(4,'Tullio','Mansi', '2002-02-20', 'MNS02TLL20A678D', '3409876321', 't.mansi@studenti.unisa.it', '$2a$12$xbN.le//qKCsYfWwnyuzd.rJFNkeh0qjEwIzMcGByVev36n5rYKkS', 'user', NULL);

-- --- seed Indirizzo (verbatim da storage.sql) ---
INSERT INTO Indirizzo VALUES(1, 1, 'Italia', 'Avella', 'Via F. Vittoria', '12',  '83021', 'AV');
INSERT INTO Indirizzo VALUES(2, 2, 'Italia', 'Boscotrecase', 'Via Trecase', '1',  '80042', 'NA');
INSERT INTO Indirizzo VALUES(3, 3, 'Italia', 'Ottaviano', 'Via Terre Sperdute', '3',  '84010', 'NA');
INSERT INTO Indirizzo VALUES(4, 4, 'Italia', 'Ravello', 'Via Sulla Montagna', '4',  '84011', 'SA');
INSERT INTO Indirizzo VALUES(5, 4, 'Italia', 'Ravello', 'Via Sul Mare', '4B',  '84011', 'SA');

-- --- seed Prodotto (verbatim da storage.sql) ---
insert into prodotto values (1,"noisy creek 2", "box semimeccanica", 12, 29.99, 'Svapo', 'Argento');
insert into prodotto values (2,"kiwi", "pod entry level", 25, 79.99, 'Svapo', 'Rosa');
insert into prodotto values (3,"voopoo drag 2", "box completa", 5, 49.99, 'Svapo', 'Nero');
insert into prodotto values (4,"smok nord", "pod", 22, 69.99, 'Svapo', 'Nero');

-- --- seed Ordine (verbatim da storage.sql) ---
INSERT INTO Ordine VALUES (1,4,4,36.58,'2023-05-08', 'PayPal');
INSERT INTO Ordine VALUES (2,4,5,7.58,'2023-05-08', 'PayPal');
INSERT INTO Ordine VALUES (3,4,4,73.17,'2023-05-08', 'PayPal');

-- --- seed Contenuto (verbatim da storage.sql) ---
INSERT INTO Contenuto VALUES(1,1,1,29.99,22);
INSERT INTO Contenuto VALUES(2,2,1,79.99,22);
INSERT INTO Contenuto VALUES(3,1,2,29.99,22);
