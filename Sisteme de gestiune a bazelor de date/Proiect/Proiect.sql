-- Spectacolele organizate de o firma organizatoare de spectacole

SET SERVEROUTPUT ON;

CREATE SEQUENCE seq_id
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE OR REPLACE TYPE contact IS OBJECT (telefon VARCHAR(10), email VARCHAR(30));
/

CREATE OR REPLACE TYPE contacte IS TABLE OF contact;
/

-------------------------------------------------------------------
-----------------------      CREATE
-------------------------------------------------------------------

CREATE TABLE Locatie(
    id_locatie INT PRIMARY KEY,
    nume_locatie VARCHAR(20) NOT NULL,
    adresa VARCHAR(50) NOT NULL,
    cod_postal VARCHAR(10) CHECK (length(cod_postal) = 6),
    oras VARCHAR(15) NOT NULL,
    judet VARCHAR(15) NOT NULL,
    contact contacte)
    NESTED TABLE contact STORE AS date_contact;
/

ALTER TABLE Locatie 
ADD UNIQUE(adresa, oras, judet);

/*CREATE TABLE Firma(
    id_firma INT PRIMARY KEY,
    nume_firma VARCHAR(15) NOT NULL,
    adresa_firma VARCHAR(50) NOT NULL,
    contact_firma contacte)
    NESTED TABLE contact_firma STORE AS date_contact_f;
/*/

CREATE TABLE Spectacol(
    id_spect INT PRIMARY KEY,
    nume_spect VARCHAR(20) NOT NULL,
    data_inceput DATE NOT NULL,
    data_sfarsit DATE NOT NULL,
);

CREATE TABLE Sponsor(
    id_sponsor INT PRIMARY KEY, 
    nume_sponsor VARCHAR(15) NOT NULL,
    contact_sponsor contacte)
    NESTED TABLE contact_sponsor STORE AS date_contact_s;
/

CREATE TABLE Contract(
    id_contract INT PRIMARY KEY,
    id_sponsor INT,
    id_spect INT,
    data_contract DATE NOT NULL,
    numar_contract VARCHAR(10) NOT NULL UNIQUE,
    suma NUMBER(6),
    CONSTRAINT FK_Contract_Sponsor FOREIGN KEY (id_sponsor) REFERENCES Sponsor(id_sponsor),
    CONSTRAINT FK_Contract_Spectacol FOREIGN KEY (id_spect) REFERENCES Spectacol(id_spect) 
);

ALTER TABLE Contract
ADD UNIQUE(numar_contract);

CREATE TABLE Program(
    id_prog INT,
    id_spect INT,
    descriere VARCHAR(50) NOT NULL,
    timp_inc TIMESTAMP NOT NULL, 
    timp_sf TIMESTAMP NOT NULL,
    CONSTRAINT PK_Program PRIMARY KEY(id_prog, id_spect),
    CONSTRAINT FK_Program_Spectacol FOREIGN KEY (id_spect) REFERENCES Spectacol(id_spect)
);

CREATE TABLE Angajat(
    id_ang INT PRIMARY KEY,
    nume_ang VARCHAR(15) NOT NULL,
    prenume_ang VARCHAR(25) NOT NULL,
    id_manager INT,
    arie_activ VARCHAR(15),
    contact_ang contacte,
    CONSTRAINT FK_Angajat FOREIGN KEY (id_manager) REFERENCES Angajat(id_ang))
    NESTED TABLE contact_ang STORE AS date_contact_a;
/

CREATE TABLE Sarcina(
    id_sarcina INT,
    id_spect INT,
    id_ang INT,
    nume_sarcina VARCHAR(20) NOT NULL,
    salariu NUMBER(5),
    desc_sarcina VARCHAR(50),
    CONSTRAINT PK_Sarcina PRIMARY KEY (id_sarcina, id_spect, id_ang),
    CONSTRAINT FK_sarcina_Spectacol FOREIGN KEY (id_spect) REFERENCES Spectacol(id_spect),
    CONSTRAINT FK_Sarcina_Angajat FOREIGN KEY (id_ang) REFERENCES Angajat(id_ang)
);

CREATE TABLE Artist(
    id_art INT PRIMARY KEY,
    nume_art VARCHAR(15) NOT NULL,
    prenume_art VARCHAR(25) NOT NULL,
    contact_art contacte,
    nume_scena VARCHAR(15))
    NESTED TABLE contact_art STORE AS date_contact_ar;
/

CREATE TABLE Performeaza(
    id_art INT,
    id_spect INT,
    cost_perf NUMBER(4),
    CONSTRAINT PK_Performeaza PRIMARY KEY (id_art, id_spect),
    CONSTRAINT FK_Performeaza_Artist FOREIGN KEY (id_art) REFERENCES Artist(id_art),
    CONSTRAINT FK_Performeaza_Spectacol FOREIGN KEY (id_spect) REFERENCES Spectacol(id_spect)
);

CREATE TABLE Participant(
    id_part INT PRIMARY KEY,
    nume_part VARCHAR(20) NOT NULL,
    prenume_part VARCHAR(25) NOT NULL
);

CREATE TABLE Bilet(
    id_part INT,
    id_spect INT,
    pret NUMBER(6, 2),
    numar_bilet NUMBER(10),
    CONSTRAINT PK_Bilet PRIMARY KEY (id_part, id_spect),
    CONSTRAINT FK_Bilet_Participant FOREIGN KEY (id_part) REFERENCES Participant(id_part),
    CONSTRAINT FK_Bilet_Spectacol FOREIGN KEY (id_spect) REFERENCES Spectacol(id_spect)
);

CREATE TABLE Device(
    id_device INT PRIMARY KEY,
    nume_device VARCHAR(15) NOT NULL
);
-- device-urile pot fi achizitionate sau inchiriate
CREATE TABLE Achizitie(
    id_device INT,
    id_spect INT,
    data_ach DATE NOT NULL,
    data_retur DATE,
    cost NUMBER(8, 2),
    CONSTRAINT PK_Achizitie PRIMARY KEY (id_device, id_spect),
    CONSTRAINT FK_Achizitie_Device FOREIGN KEY (id_device) REFERENCES Device(id_device),
    CONSTRAINT FK_Achizitie_Spectacol FOREIGN KEY (id_spect) REFERENCES Spectacol(id_spect) 
);

CREATE TABLE Desfasurare(
    id_spect INT,
    id_locatie INT,
    CONSTRAINT PK_Desfasurare PRIMARY KEY(id_spect, id_locatie),
    CONSTRAINT FK_Desfasurare_Spectacol FOREIGN KEY (id_spect) REFERENCES Spectacol (id_spect),
    CONSTRAINT FK_Desfasurare_Locatie FOREIGN KEY (id_locatie) REFERENCES Locatie (id_locatie)
);
------------------------------------------------------------------
-------------------- PACHET
-----------------------------------------------------------------
CREATE OR REPLACE PACKAGE p_cerinte AS
    -- exercitiul 6
    PROCEDURE f_afis_sponsor(v_nume sponsor.nume_sponsor%TYPE);
    -- exercitiul 7
    PROCEDURE f_afis_spectacole(v_nume spectacol.nume_spect%TYPE);
    --exercitiul 8
    FUNCTION f_costuri_spectacol (v_id spectacol.id_spect%TYPE)
    RETURN NUMBER;
    --exercitiul 9
    PROCEDURE f_more_info_spectacol(v_id spectacol.id_spect%TYPE);
END;
/

CREATE OR REPLACE PACKAGE BODY p_cerinte AS
    -- exercitiul 6
    PROCEDURE f_afis_sponsor(v_nume sponsor.nume_sponsor%TYPE) IS
        TYPE tablou_indexat IS TABLE OF Sponsor.id_sponsor%TYPE INDEX BY BINARY_INTEGER; 
        t tablou_indexat;
        TYPE contact_record IS RECORD (tel VARCHAR(10), mail VARCHAR(30));
        TYPE tablou_date IS TABLE OF contact_record INDEX BY BINARY_INTEGER;
        t_date tablou_date;
    BEGIN
        SELECT id_sponsor
        BULK COLLECT INTO t
        FROM Sponsor
        WHERE nume_sponsor = v_nume;
       
       IF t.COUNT = 0 THEN 
        DBMS_OUTPUT.PUT_LINE('Nu a fost gasit niciun sponsor cu acest nume.'); 
       ELSE 
        DBMS_OUTPUT.PUT_LINE('Exista ' || t.count || ' sponsori cu numele ' || v_nume); 
        FOR i IN t.FIRST..t.LAST LOOP
                SELECT tt.*
                BULK COLLECT INTO t_date
                FROM sponsor s, TABLE(s.contact_sponsor) tt
                WHERE s.id_sponsor = t(i);
                DBMS_OUTPUT.PUT_LINE('id ' || t(i) || ' - contact: ');
                FOR j IN t_date.FIRST..t_date.LAST LOOP
                    DBMS_OUTPUT.PUT_LINE('  telefon: ' || t_date(j).tel || ', email: ' || t_date(j).mail);
                END LOOP;
                t_date.delete;
        END LOOP;
       END IF;
    END f_afis_sponsor;
    
    -- exercitiul 7
    PROCEDURE f_afis_spectacole(v_nume spectacol.nume_spect%TYPE) IS
        TYPE refcursor IS REF CURSOR;
        CURSOR c IS 
            SELECT id_spect, data_inceput, data_sfarsit, 
                CURSOR(SELECT nume_art, prenume_art, nume_scena
                        FROM performeaza p join artist a using (id_art)
                        WHERE p.id_spect = s.id_spect)
            FROM spectacol s
            WHERE nume_spect = v_nume;
         v_cursor refcursor;
         v_id spectacol.id_spect%TYPE;
         v_inc spectacol.data_inceput%TYPE;
         v_sf spectacol.data_sfarsit%TYPE;
         v_nume_a artist.nume_art%TYPE;
         v_prenume_a artist.prenume_art%TYPE;
         v_numes artist.nume_scena%TYPE;
         exista boolean := false;    
    BEGIN
       OPEN c;
       LOOP
        FETCH c INTO v_id, v_inc, v_sf, v_cursor;
        EXIT WHEN c%NOTFOUND;
        exista := true;
        IF v_inc = v_sf THEN
                DBMS_OUTPUT.PUT_LINE('La spectacolul cu id-ul ' || v_id || ' din data de ' || to_char(v_inc, 'MM/DD/YYYY') || ' participa artistii: ' ); 
            ELSE
                DBMS_OUTPUT.PUT_LINE('La spectacolul cu id-ul ' || v_id || ' din perioada ' || v_inc || ' - ' || v_sf || ' participa artistii: '); 
            END IF;
        LOOP 
            FETCH v_cursor 
            INTO v_nume_a, v_prenume_a, v_numes; 
            EXIT WHEN v_cursor%NOTFOUND;
            
            IF v_numes is NULL THEN
                DBMS_OUTPUT.PUT_LINE('   ' || v_prenume_a || ' ' || v_nume_a); 
            ELSE
                DBMS_OUTPUT.PUT_LINE('   ' || v_numes);
            END IF;
        END LOOP;    
       END LOOP;
       CLOSE c;
       IF exista = false THEN 
        DBMS_OUTPUT.PUT_LINE('Nu a fost gasit niciun spectacol cu acest nume.'); 
       END IF;
    END f_afis_spectacole;
    
    -- exercitiul 8 
    -- date din 4 tabele: contract, sarcina, bilet, achizitie
    FUNCTION f_costuri_spectacol(v_id spectacol.id_spect%TYPE) 
    RETURN NUMBER IS 
         v_into NUMBER(10, 2);
         v_res NUMBER(10, 2):= 0;
    BEGIN 
        SELECT NVL(SUM(suma), 0)
        INTO v_into
        FROM contract
        WHERE id_spect = v_id;
        
        DBMS_OUTPUT.PUT_LINE('Spectacolul a fost sponsorizat cu suma de ' || v_into || ' RON.'); 
        
        v_res := v_res + v_into;
        
        SELECT NVL(SUM(salariu), 0)
        INTO v_into
        FROM sarcina
        WHERE id_spect = v_id;
        
        DBMS_OUTPUT.PUT_LINE('Salariile platite au fost in valoare de ' || v_into || ' RON.'); 
        
        v_res := v_res - v_into;
        
        SELECT NVL(SUM(pret), 0)
        INTO v_into
        FROM bilet
        WHERE id_spect = v_id;
        
        DBMS_OUTPUT.PUT_LINE('Au fost cumparate bilete in valoare de ' || v_into || ' RON.'); 
        
        v_res := v_res + v_into;
        
        SELECT NVL(SUM(cost), 0)
        INTO v_into
        FROM achizitie
        WHERE id_spect = v_id;
        
        DBMS_OUTPUT.PUT_LINE('Au fost achizitionate device-uri in valoare de ' || v_into || ' RON.'); 
        
        v_res := v_res - v_into;       
        RETURN v_res;
        
    EXCEPTION 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20023,'Eroare la obtinerea costurilor spectacolului.'); 
    END f_costuri_spectacol; 
    
    -- exercitiul 9
    PROCEDURE f_more_info_spectacol(v_id spectacol.id_spect%TYPE) IS
        TYPE artist_record IS RECORD (
            nume artist.nume_art%TYPE, 
            prenume artist.prenume_art%TYPE,
            scena artist.nume_scena%TYPE);
        TYPE tablou_indexat_a IS TABLE OF artist_record INDEX BY BINARY_INTEGER; 
        t_art tablou_indexat_a;
        
        v_nume_s spectacol.nume_spect%TYPE;
        v_data_i spectacol.data_inceput%TYPE;
        v_data_sf spectacol.data_sfarsit%TYPE;
        
        TYPE tablou_indexat_s IS TABLE OF sponsor.nume_sponsor%TYPE INDEX BY BINARY_INTEGER; 
        t_spon tablou_indexat_s;
        
        TYPE program_record IS RECORD (
            descriere program.descriere%TYPE, 
            t_i program.timp_inc%TYPE,
            t_s program.timp_sf%TYPE);
        TYPE tablou_indexat_p IS TABLE OF program_record INDEX BY BINARY_INTEGER; 
        t_prog tablou_indexat_p;
        
        TYPE tablou_indexat_l IS TABLE OF locatie.nume_locatie%TYPE INDEX BY BINARY_INTEGER; 
        t_loc tablou_indexat_l;
    BEGIN
        SELECT nume_spect, data_inceput, data_sfarsit
        INTO v_nume_s, v_data_i, v_data_sf
        FROM spectacol
        WHERE id_spect = v_id;
        
        IF v_data_i = v_data_sf THEN
            DBMS_OUTPUT.PUT_LINE('Spectacolul ' || v_nume_s || ' are loc pe ' || to_char(v_data_i, 'MM/DD/YYYY')); 
        ELSE
            DBMS_OUTPUT.PUT_LINE('Spectacolul ' || v_nume_s || ' are loc pe ' || v_data_i || ' - ' || v_data_sf); 
        END IF;
        
        SELECT nume_locatie
        BULK COLLECT INTO t_loc
        FROM locatie join desfasurare using (id_locatie)
        WHERE id_spect = v_id;
        
        IF t_loc.count = 1 THEN 
            DBMS_OUTPUT.PUT_LINE('la ' || t_loc(1)); 
        ELSE 
            DBMS_OUTPUT.PUT_LINE('in locatiile: ');
            
            FOR i IN t_loc.FIRST..t_loc.LAST LOOP
                DBMS_OUTPUT.PUT(t_loc(i) || ' ');
            END LOOP;
            DBMS_OUTPUT.PUT_LINE('');
        END IF;
        
        SELECT nume_art, prenume_art, nume_scena
        BULK COLLECT INTO t_art
        FROM artist join performeaza using (id_art)
        WHERE id_spect = v_id;
        
        DBMS_OUTPUT.PUT('La spectacol participa artistii ');
        FOR i IN t_art.FIRST..t_art.LAST LOOP
                IF t_art(i).scena is NULL THEN
                    DBMS_OUTPUT.PUT(t_art(i).prenume|| ' ' || t_art(i).nume || ' ');
                ELSE
                    DBMS_OUTPUT.PUT(t_art(i).scena || ' ');
                END IF;
        END LOOP;
        DBMS_OUTPUT.PUT_LINE('');
        
        SELECT descriere, timp_inc, timp_sf
        BULK COLLECT INTO t_prog
        FROM program
        WHERE id_spect = v_id;
        
        DBMS_OUTPUT.PUT('cu programul ');
        FOR i IN t_prog.FIRST..t_prog.LAST LOOP
                DBMS_OUTPUT.PUT_LINE(t_prog(i).descriere || ' ' || to_char(t_prog(i).t_i, 'HH24:MI') || ' - ' || to_char(t_prog(i).t_s, 'HH24:MI') || ' ');
        END LOOP;
        
        SELECT nume_sponsor
        BULK COLLECT INTO t_spon
        FROM sponsor join contract using(id_sponsor)
        WHERE id_spect = v_id
        ORDER BY suma DESC;
        
        DBMS_OUTPUT.PUT('Spectacolul este sponsorizat de ');
        FOR i IN t_spon.FIRST..t_spon.LAST LOOP
                DBMS_OUTPUT.PUT_LINE(t_spon(i) || ' ');
        END LOOP;
        
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20024, 'Nu exista spectacol cu acest id.'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20025, 'Eroare la obtinerea informatiilor.');
    END f_more_info_spectacol;
    
END p_cerinte;
/

-- Exercitiul 14
CREATE OR REPLACE PACKAGE p_get_data AS
    --FUNCTION f_id_firma (v_nume firma.nume_firma%TYPE) 
        --RETURN firma.id_firma%type; 
    FUNCTION f_id_spectacol(v_nume spectacol.nume_spect%TYPE) 
        RETURN spectacol.id_spect%type;
        
    FUNCTION f_id_locatie(v_nume locatie.nume_locatie%TYPE) 
        RETURN locatie.id_locatie%type; 
        
    -- overload pe functia f_id_artist
    FUNCTION f_id_artist(v_nume artist.nume_art%TYPE, v_prenume artist.prenume_art%TYPE) 
        RETURN artist.id_art%type; 
        
    FUNCTION f_id_artist (v_nume artist.nume_scena%TYPE) 
        RETURN artist.id_art%type; 
        
    FUNCTION f_id_sponsor(v_nume sponsor.nume_sponsor%TYPE) 
        RETURN sponsor.id_sponsor%type; 
        
    
    FUNCTION f_id_device(v_nume device.nume_device%TYPE) 
        RETURN device.id_device%type;
        
    FUNCTION f_id_angajat(v_nume angajat.nume_ang%TYPE, v_prenume angajat.prenume_ang%TYPE) 
        RETURN angajat.id_ang%type; 
        
    FUNCTION f_id_participant(v_nume participant.nume_part%TYPE, v_prenume participant.prenume_part%TYPE) 
        RETURN participant.id_part%type; 
        
    PROCEDURE f_afis_locatii(v_nume locatie.nume_locatie%TYPE); 
    
    FUNCTION f_numar_bilet(v_id spectacol.id_spect%TYPE) 
        RETURN bilet.numar_bilet%type;
END p_get_data;
/

CREATE OR REPLACE PACKAGE BODY p_get_data AS
    /*FUNCTION f_id_firma (v_nume firma.nume_firma%TYPE) 
    RETURN firma.id_firma%type IS nume_f firma.id_firma%type; 
    BEGIN 
        SELECT id_firma 
        INTO nume_f
        FROM firma 
        WHERE nume_firma = v_nume; 
        RETURN nume_f; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20000, 'Nu exista firma cu numele dat'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20001, 'Exista mai multe firme cu numele dat'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20002,'Eroare la obtinerea id-ului fimei.'); 
    END f_id_firma; */
    
    FUNCTION f_id_spectacol(v_nume spectacol.nume_spect%TYPE) 
    RETURN spectacol.id_spect%type IS nume_s spectacol.id_spect%type; 
    BEGIN 
        SELECT id_spect
        INTO nume_s
        FROM spectacol 
        WHERE nume_spect = v_nume; 
        RETURN nume_s; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20003, 'Nu exista spectacol cu numele dat. Verifica daca numele introdus este corect.'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20004, 'Exista mai multe spectacole cu numele dat. Apeleaza f_afis_spectacole pentru a afla care este cel cautat.'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20005,'Eroare la obtinerea id-ului spectacolului.'); 
    END f_id_spectacol; 
    
        
    FUNCTION f_id_locatie(v_nume locatie.nume_locatie%TYPE) 
    RETURN locatie.id_locatie%type IS nume_l locatie.id_locatie%type; 
    BEGIN 
        SELECT id_locatie
        INTO nume_l
        FROM locatie
        WHERE nume_locatie = v_nume; 
        RETURN nume_l; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20006, 'Nu exista locatie cu numele dat.'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20007, 'Exista mai multe locatii cu numele dat'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20008,'Eroare la obtinerea id-ului locatiei.'); 
    END f_id_locatie; 
    
    
    FUNCTION f_id_artist(v_nume artist.nume_art%TYPE, v_prenume artist.prenume_art%TYPE) 
    RETURN artist.id_art%type IS nume_a artist.id_art%type; 
    BEGIN 
        SELECT id_art
        INTO nume_a
        FROM artist
        WHERE nume_art = v_nume AND prenume_art = v_prenume; 
        RETURN nume_a; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20009, 'Nu exista artist cu acest numele'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20010, 'Exista mai multi artisti cu acest nume'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20011,'Eroare la obtinerea id-ului artistului.'); 
    END f_id_artist; 
    
    FUNCTION f_id_artist (v_nume artist.nume_scena%TYPE) 
    RETURN artist.id_art%type IS nume_a artist.id_art%type; 
    BEGIN 
        SELECT id_art
        INTO nume_a
        FROM artist
        WHERE nume_scena = v_nume; 
        RETURN nume_a; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20009, 'Nu exista artist cu acest numele de scena.'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20010, 'Exista mai multi artisti cu acest nume de scena'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20011,'Eroare la obtinerea id-ului artistului.'); 
    END f_id_artist; 
    
    FUNCTION f_id_sponsor(v_nume sponsor.nume_sponsor%TYPE) 
    RETURN sponsor.id_sponsor%type IS nume_s sponsor.id_sponsor%type; 
    BEGIN 
        SELECT id_sponsor
        INTO nume_s
        FROM sponsor 
        WHERE nume_sponsor = v_nume; 
        RETURN nume_s; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20012, 'Nu exista sponsor cu numele dat.'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20013, 'Exista mai multi sponsori cu numele dat'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20014,'Eroare la obtinerea id-ului sponsorului.'); 
    END f_id_sponsor; 
    
    FUNCTION f_id_device(v_nume device.nume_device%TYPE) 
        RETURN device.id_device%TYPE IS id_d device.id_device%TYPE;
        BEGIN 
            SELECT id_device
            INTO id_d
            FROM device
            WHERE nume_device = v_nume;
            RETURN id_d; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20016, 'Nu exista device cu numele dat.'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20017, 'Exista mai multe device-uri cu numele dat.'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20018,'Eroare la obtinerea id-ului device-ului.'); 
    END f_id_device; 
    
    FUNCTION f_id_angajat(v_nume angajat.nume_ang%TYPE, v_prenume angajat.prenume_ang%TYPE) 
    RETURN angajat.id_ang%type IS nume_a angajat.id_ang%type; 
    BEGIN 
        SELECT id_ang
        INTO nume_a
        FROM angajat
        WHERE nume_ang = v_nume AND prenume_ang = v_prenume; 
        RETURN nume_a; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20019, 'Nu exista angajat cu acest nume.'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20020, 'Exista mai multi angajati cu acest nume.'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20021,'Eroare la obtinerea id-ului angajatului.'); 
    END f_id_angajat; 
    
    FUNCTION f_id_participant(v_nume participant.nume_part%TYPE, v_prenume participant.prenume_part%TYPE) 
    RETURN participant.id_part%type IS nume_p participant.id_part%type; 
    BEGIN 
        SELECT id_part
        INTO nume_p
        FROM participant
        WHERE nume_part = v_nume AND prenume_part = v_prenume; 
        RETURN nume_p; 
    EXCEPTION 
        WHEN NO_DATA_FOUND THEN RAISE_APPLICATION_ERROR(-20021, 'Nu exista participant cu acest nume.'); 
        WHEN TOO_MANY_ROWS THEN RAISE_APPLICATION_ERROR(-20022, 'Exista mai multi participanti cu acest nume.'); 
        WHEN OTHERS THEN RAISE_APPLICATION_ERROR(-20023,'Eroare la obtinerea id-ului participantului.'); 
    END f_id_participant; 
    
    PROCEDURE f_afis_locatii(v_nume locatie.nume_locatie%TYPE) IS
        TYPE tablou_indexat IS TABLE OF Locatie%ROWTYPE INDEX BY BINARY_INTEGER; 
        t tablou_indexat;
    BEGIN
        SELECT *
        BULK COLLECT INTO t
        FROM Locatie
        WHERE nume_locatie = v_nume;
       
       IF t.COUNT = 0 THEN 
        DBMS_OUTPUT.PUT_LINE('Nu a fost gasita nicio locatie cu acest nume.'); 
       ELSE 
        DBMS_OUTPUT.PUT_LINE('Exista ' || t.count || ' locatii cu numele: ' || v_nume); 
        FOR i IN t.FIRST..t.LAST LOOP 
                DBMS_OUTPUT.PUT_LINE('id ' || t(i).id_locatie || ' ' || t(i).oras || ' ' ||t(i).judet || ' ' || t(i).adresa); 
        END LOOP;
       END IF;
    END f_afis_locatii;
    
    FUNCTION f_numar_bilet(v_id spectacol.id_spect%TYPE) 
        RETURN bilet.numar_bilet%type IS v_nr bilet.numar_bilet%type;
    BEGIN
        SELECT max(numar_bilet) + 1 
        INTO v_nr
        FROM bilet 
        where id_spect = v_id; 
        IF v_nr is NULL THEN 
            RETURN 1;
        ELSE
            RETURN v_nr;
        END IF;
    END f_numar_bilet;
    
END p_get_data;
/

-------------------------------------------------------------------
-----------------------      TRIGGERI
-------------------------------------------------------------------
-- exercitiul 10
CREATE OR REPLACE TRIGGER trig_vacanta
    BEFORE INSERT ON achizitie
DECLARE
    zi NUMBER := EXTRACT(DAY FROM sysdate);
    luna NUMBER := EXTRACT(MONTH FROM sysdate);
BEGIN
    IF (zi = 25 and luna = 12) or (zi  = 1 and luna = 1) THEN 
        RAISE_APPLICATION_ERROR(-20026, 'Astazi nu se pot face achizitii.');
    END IF;
END;
/

-- exercitiul 11
CREATE OR REPLACE TRIGGER trig_insert_program 
    BEFORE INSERT ON Program
    FOR EACH ROW 
DECLARE
    v_inc spectacol.data_inceput%TYPE;
    v_sf spectacol.data_sfarsit%TYPE;
    exceptie EXCEPTION;
BEGIN 
    SELECT s.data_inceput, s.data_sfarsit
    INTO v_inc, v_sf
    FROM spectacol s
    WHERE s.id_spect = :NEW.id_spect;
    
    IF (NOT((:NEW.timp_inc > TRUNC(v_inc)) and (:NEW.timp_inc < :NEW.timp_sf) and (:NEW.timp_sf <  (TRUNC(v_sf) + INTERVAL '1' DAY - INTERVAL '1' SECOND))))
        THEN RAISE exceptie;
    END IF;
EXCEPTION
    WHEN EXCEPTIE THEN
        RAISE_APPLICATION_ERROR(-20025,'Programul nu este corect.'); 
END; 
/
-- exercitiul 12
CREATE OR REPLACE TRIGGER trig_LDD_owner
    BEFORE CREATE OR DROP OR ALTER ON SCHEMA
    BEGIN 
        IF USER != UPPER('LAURATENDER') THEN 
        RAISE_APPLICATION_ERROR(-20025,'Nu ai drepturi pentru comenzi LDD.'); 
    END IF; 
END; 
/ 

CREATE TABLE Test(
    id INT PRIMARY KEY,
    nume VARCHAR(15) NOT NULL
);

DROP TRIGGER trig_LDD_owner;
-------------------------------------------------------------------
-----------------------      INSERT
-------------------------------------------------------------------
--INSERT INTO Firma
--VALUES (seq_id.nextval, 'Best events', 'Bd. Uverturii, nr.161 Bucuresti', contacte(contact('0736381039', 'office@bestevent.ro'))); 

INSERT INTO Spectacol
VALUES (seq_id.nextval, 'Comedy Show', '16-DEC-20', '16-DEC-20');
INSERT INTO Spectacol
VALUES (seq_id.nextval, 'Poveste Craciun', '29-DEC-20', '29-DEC-20');
INSERT INTO Spectacol
VALUES (seq_id.nextval, 'Comedy Show', '23-JAN-21', '23-JAN-21');
INSERT INTO Spectacol
VALUES (seq_id.nextval, 'Lacul lebedelor', '10-MAR-21', '10-MAR-21');
INSERT INTO Spectacol
VALUES (seq_id.nextval, 'Festival vara', '13-AUG-21', '15-AUG-21');
INSERT INTO Spectacol
VALUES (seq_id.nextval, 'Tosca', '13-MAY-21', '13-MAY-21');

INSERT INTO Locatie
VALUES (seq_id.nextval, 'Club 99', 'Bd. Dacia, nr.22', NULL, 'Bucuresti', 'Sector 2', contacte(contact('0726688658', 'manager@99club.ro'), contact('0733500301', 'office@club99.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Teatrul Coquette', 'Calea Calarasi, nr.94', NULL, 'Bucuresti', 'Sector 3', contacte(contact('0754990017', 'acasa@teatrulcoquette.ro'), contact('0721191675', 'rezervari@teatrulcoquette.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Comics Club', 'str. Nerva Traian, nr. 4', '031045', 'Bucuresti', 'Sector 3', contacte(contact('021139437', 'secretariat.tnb@gmail.com')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Teatrul National', 'bd. Nicolae Balcescu Nr.2', '010051', 'Bucuresti', 'Sector 1', contacte(contact('0741426426', 'events@comicsclub.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Stirbei sala mare', 'Strada Stirbei Voda 36', '070000', 'Buftea', 'Ilfov', contacte(contact('0732332906', 'office@domeniu.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Stirbei sala mica', 'Strada Stirbei Voda 37', '070000', 'Buftea', 'Ilfov', contacte(contact('0732332906', 'office@domeniu.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Stirbei pavilion', 'Strada Stirbei Voda 39', '070000', 'Buftea', 'Ilfov', contacte(contact('0732332906', 'office@domeniu.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Stirbei gradina', 'Strada Stirbei Voda 38', '070000', 'Buftea', 'Ilfov', contacte(contact('0732332906', 'office@domeniu.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Stirbei domeniu', 'Strada Stirbei Voda 40', '070000', 'Buftea', 'Ilfov', contacte(contact('0732332906', 'office@domeniu.ro')));
INSERT INTO Locatie
VALUES (seq_id.nextval, 'Opera nationala', 'Bd. Mihail Kogalniceanu 70-72', NULL, 'Bucuresti', 'Sector 5', contacte(contact('0757030555', 'bilete@operanb.ro')));

INSERT INTO Desfasurare
VALUES (2, p_get_data.f_id_locatie('Club 99'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Poveste Craciun'), p_get_data.f_id_locatie('Teatrul Coquette'));
INSERT INTO Desfasurare
VALUES (31, p_get_data.f_id_locatie('Comics Club'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Tosca'), p_get_data.f_id_locatie('Opera nationala'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Lacul lebedelor'), p_get_data.f_id_locatie('Teatrul National'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_locatie('Stirbei sala mare'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_locatie('Stirbei sala mica'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_locatie('Stirbei pavilion'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_locatie('Stirbei gradina'));
INSERT INTO Desfasurare
VALUES (p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_locatie('Stirbei domeniu'));

INSERT INTO Artist
VALUES (seq_id.nextval, 'Teohari', 'Claudiu', contacte(contact('0727799769', 'teo_stand-up@gmail.com')), 'Teo');
INSERT INTO Artist
VALUES (seq_id.nextval, 'Dragu', 'Viorel', contacte(contact('0737700789', 'vio_stand-up@gmail.com')), 'Vio');
INSERT INTO Artist
VALUES (seq_id.nextval, 'Bojog', 'Costel', contacte(contact('0717765769', 'costel_bojog@gmail.com')), 'Costel');
INSERT INTO Artist
VALUES (seq_id.nextval, 'Nita', 'Gabriela', NULL, NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Nitu', 'Celina', NULL, NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Covache', 'Marius', contacte(contact('0759220221', 'marius_covache@gmail.com')), NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Serban', 'Alex', contacte(contact('0751094647', 'alex_serban@gmail.com')), NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Ignat', 'Stefan', NULL, NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Pascariu', 'Daniel', NULL, NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Nita', 'Greta', NULL, NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Enache', 'Robert', NULL, NULL);
INSERT INTO Artist
VALUES (seq_id.nextval, 'Bogaerde', 'Jasmine', NULL, 'Birdy');
INSERT INTO Artist
VALUES (seq_id.nextval, 'Trupa', 'Trupa', NULL, 'Ntg but tvs');
INSERT INTO Artist
VALUES (seq_id.nextval, 'Trupa', 'Trupa', NULL, 'Roosevelt');
INSERT INTO Artist
VALUES (seq_id.nextval, 'Trupa', 'Trupa', NULL, 'The 1975');
INSERT INTO Artist
VALUES (seq_id.nextval, 'August', 'David', NULL, NULL);

INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Teo'), p_get_data.f_id_spectacol('Comedy Show'), 500);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Vio'), p_get_data.f_id_spectacol('Comedy Show'), 500);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Costel'), p_get_data.f_id_spectacol('Comedy Show'), 500);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Nita', 'Gabriela'), p_get_data.f_id_spectacol('Poveste Craciun'), 300);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Nitu', 'Celina'), p_get_data.f_id_spectacol('Poveste Craciun'), 300);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Covache', 'Marius'), 31, 250);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Serban', 'Alex'), 31, 250);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Ignat', 'Stefan'), p_get_data.f_id_spectacol('Tosca'), 250);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Pascariu', 'Daniel'), p_get_data.f_id_spectacol('Tosca'), 250);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Nita', 'Greta'), p_get_data.f_id_spectacol('Lacul lebedelor'), 250);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Enache', 'Robert'), p_get_data.f_id_spectacol('Lacul lebedelor'), 250);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('August', 'David'), p_get_data.f_id_spectacol('Festival vara'), 1000);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('August', 'David'), p_get_data.f_id_spectacol('Festival vara'), 1000);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Birdy'), p_get_data.f_id_spectacol('Festival vara'), 2000);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Ntg but tvs'), p_get_data.f_id_spectacol('Festival vara'), 2000);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('The 1975'), p_get_data.f_id_spectacol('Festival vara'), 2000);
INSERT INTO Performeaza
VALUES (p_get_data.f_id_artist('Roosevelt'), p_get_data.f_id_spectacol('Festival vara'), 1500);

INSERT INTO Sponsor
VALUES (seq_id.nextval, 'Softbinator', contacte(contact('0757404781', 'contact@softbinator.com')));
INSERT INTO Sponsor
VALUES (seq_id.nextval, 'Aqua Carpatica', contacte(contact('8477305616', 'contact@aquacarpatica.com')));
INSERT INTO Sponsor
VALUES (seq_id.nextval, 'Pepsi', contacte(contact('021467704', 'contact@pepsi.com')));
INSERT INTO Sponsor
VALUES (seq_id.nextval, 'Kiss FM', contacte(contact('0213188000', 'contact@kiss.com')));
INSERT INTO Sponsor
VALUES (seq_id.nextval, 'Elle', contacte(contact('0212030800', 'contact@elle.com')));

INSERT INTO Contract
VALUES (seq_id.nextval, p_get_data.f_id_sponsor('Softbinator'), p_get_data.f_id_spectacol('Comedy Show'), '14-DEC-20', '000534', 3000);
INSERT INTO Contract
VALUES (seq_id.nextval, p_get_data.f_id_sponsor('Kiss FM'), 31, '15-JAN-21', '000538', 1000);
INSERT INTO Contract
VALUES (seq_id.nextval, p_get_data.f_id_sponsor('Elle'), p_get_data.f_id_spectacol('Lacul lebedelor'), '28-FEB-21', '000596', 2000);
INSERT INTO Contract
VALUES (seq_id.nextval, p_get_data.f_id_sponsor('Aqua Carpatica'), p_get_data.f_id_spectacol('Festival vara'), '01-AUG-21', '000535', 5000);
INSERT INTO Contract
VALUES (seq_id.nextval, p_get_data.f_id_sponsor('Pepsi'), p_get_data.f_id_spectacol('Festival vara'), '01-AUG-21', '000536', 5000);
INSERT INTO Contract
VALUES (seq_id.nextval, p_get_data.f_id_sponsor('Kiss FM'), p_get_data.f_id_spectacol('Festival vara'), '01-AUG-21', '000537', 5000);


INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Comedy Show'), 'Show-ul lui Teo', timestamp '2020-12-16 20:00:00', timestamp '2020-12-16 20:20:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Comedy Show'), 'Show-ul lui Vio', timestamp '2020-12-16 20:20:00', timestamp '2020-12-16 20:40:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Comedy Show'), 'Show-ul lui Costel', timestamp '2020-12-16 20:40:00', timestamp '2020-12-16 21:00:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Poveste Craciun'), 'Teatru', timestamp '2020-12-29 19:00:00', timestamp '2020-12-29 21:00:00');
INSERT INTO Program
VALUES (seq_id.nextval, 31, 'Stand-up comedy', timestamp '2021-01-23 20:00:00', timestamp '2021-01-23 21:00:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Lacul lebedelor'), 'Balet', timestamp '2021-03-10 19:00:00', timestamp '2021-03-10 21:00:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Tosca'), 'Opera', timestamp '2021-05-13 20:00:00', timestamp '2021-05-13 22:00:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), 'Roosevelt', timestamp '2021-08-13 20:00:00', timestamp '2021-08-13 21:30:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), 'Nothing but Thieves', timestamp '2021-08-13 21:30:00', timestamp '2021-08-13 23:00:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), 'Birdy', timestamp '2021-08-14 20:00:00', timestamp '2021-08-14 22:00:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), 'The 1975', timestamp '2021-08-15 20:00:00', timestamp '2021-08-15 21:30:00');
INSERT INTO Program
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), 'August David', timestamp '2021-08-15 21:30:00', timestamp '2021-08-15 23:00:00');

INSERT INTO Device
VALUES (seq_id.nextval, 'Camera video');
INSERT INTO Device
VALUES (seq_id.nextval, 'Lumini');
INSERT INTO Device
VALUES (seq_id.nextval, 'Boxa');
INSERT INTO Device
VALUES (seq_id.nextval, 'Microfon');
INSERT INTO Device
VALUES (seq_id.nextval, 'Computer');

INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Camera video'), 31, '23-JAN-21', '24-JAN-21', 50);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Lumini'), 31, '23-JAN-21', '24-JAN-21', 30);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Boxa'), 31, '23-JAN-21', '24-JAN-21', 50);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Microfon'), 31, '23-JAN-21', '24-JAN-21', 20);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Computer'), 31, '23-JAN-21', '24-JAN-21', 150);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Camera video'), p_get_data.f_id_spectacol('Tosca'), '13-MAY-21', '14-MAY-21', 50);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Lumini'), p_get_data.f_id_spectacol('Tosca'), '13-MAY-21', '14-MAY-21', 30);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Boxa'), p_get_data.f_id_spectacol('Tosca'), '13-MAY-21', '14-MAY-21', 50);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Microfon'), p_get_data.f_id_spectacol('Tosca'), '13-MAY-21', '14-MAY-21', 20);
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Computer'), p_get_data.f_id_spectacol('Tosca'), '13-MAY-21', '14-MAY-21', 150);

INSERT INTO Angajat
VALUES (seq_id.nextval, 'Badea', 'Nicolae', NULL, 'Resurse Umane', contacte(contact('0721111111', 'nicolae_badea@gmail.com')));
INSERT INTO Angajat
VALUES (seq_id.nextval, 'Costin', 'Ana', p_get_data.f_id_angajat('Badea', 'Nicolae'), 'Logistica', contacte(contact('0722222222', 'ana_costin@gmail.com')));
INSERT INTO Angajat
VALUES (seq_id.nextval, 'Enache', 'Sorin', p_get_data.f_id_angajat('Badea', 'Nicolae') , 'IT', contacte(contact('074444444', 'sorin_enache@gmail.com')));
INSERT INTO Angajat
VALUES (seq_id.nextval, 'David', 'Radu', p_get_data.f_id_angajat('Enache', 'Sorin'), 'Promovare', contacte(contact('0733333333', 'radu_david@gmail.com')));
INSERT INTO Angajat
VALUES (seq_id.nextval, 'Jurca', 'Traian', p_get_data.f_id_angajat('Badea', 'Nicolae'), 'HoReCa', contacte(contact('0755555555', 'traian_jurca@gmail.com')));
INSERT INTO Angajat
VALUES (seq_id.nextval, 'Lunga', 'Eduard', p_get_data.f_id_angajat('Jurca', 'Traian'), 'Fundraising', contacte(contact('076666666', 'eduard_lunga@gmail.com')));

INSERT INTO Sarcina
VALUES (seq_id.nextval, 2, p_get_data.f_id_angajat('Costin', 'Ana'), 'Organizare', 200, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, 31, p_get_data.f_id_angajat('Costin', 'Ana'), 'Organizare', 200, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Poveste Craciun'), p_get_data.f_id_angajat('Badea', 'Nicolae'), 'Organizare', 250, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Lacul lebedelor'), p_get_data.f_id_angajat('Enache', 'Sorin'), 'Imagine', 200, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Lacul lebedelor'), p_get_data.f_id_angajat('David', 'Radu'), 'Coordonare', 200, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Tosca'), p_get_data.f_id_angajat('Enache', 'Sorin'), 'Imagine', 200, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Tosca'), p_get_data.f_id_angajat('David', 'Radu'), 'Coordonare', 200, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_angajat('Costin', 'Ana'), 'Coordonare', 400, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_angajat('Enache', 'Sorin'), 'IT', 400, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_angajat('David', 'Radu'), 'Promovare', 400, NULL);
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_angajat('Lunga', 'Eduard'), 'FR', 400, 'Comunicare sponsori');
INSERT INTO Sarcina
VALUES (seq_id.nextval, p_get_data.f_id_spectacol('Festival vara'), p_get_data.f_id_angajat('Jurca', 'Traian'), 'HoReCa', 400, 'Responsabil produse comercializate');

INSERT INTO Participant
VALUES (seq_id.nextval, 'Macovei', 'Gheorghe');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Lascu', 'Dan');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Leonida', 'Tudor');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Olteanu', 'Dan');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Popa', 'Valentin');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Popescu', 'Mihaela');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Sandu', 'Florin');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Stanescu', 'Constantin');
INSERT INTO Participant
VALUES (seq_id.nextval, 'Ursea', 'Lucian');

INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Macovei', 'Gheorghe'), p_get_data.f_id_spectacol('Festival vara'), 250, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Festival vara')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Ursea', 'Lucian'), p_get_data.f_id_spectacol('Festival vara'), 250, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Festival vara')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Stanescu', 'Constantin'), p_get_data.f_id_spectacol('Festival vara'), 250, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Festival vara')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Sandu', 'Florin'), p_get_data.f_id_spectacol('Festival vara'), 250, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Festival vara')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Popescu', 'Mihaela'), p_get_data.f_id_spectacol('Festival vara'), 250, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Festival vara')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Popa', 'Valentin'), p_get_data.f_id_spectacol('Festival vara'), 250, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Festival vara')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Popa', 'Valentin'), 2, 30, p_get_data.f_numar_bilet(2));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Lascu', 'Dan'), 31, 30, p_get_data.f_numar_bilet(31));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Leonida', 'Tudor'), p_get_data.f_id_spectacol('Poveste Craciun'), 40, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Poveste Craciun')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Olteanu', 'Dan'), p_get_data.f_id_spectacol('Poveste Craciun'), 40, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Poveste Craciun')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Popescu', 'Mihaela'), p_get_data.f_id_spectacol('Lacul lebedelor'), 45, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Lacul lebedelor')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Sandu', 'Florin'), p_get_data.f_id_spectacol('Lacul lebedelor'), 45, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Lacul lebedelor')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Ursea', 'Lucian'), p_get_data.f_id_spectacol('Tosca'), 45, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Tosca')));
INSERT INTO Bilet
VALUES(p_get_data.f_id_participant('Macovei', 'Gheorghe'), p_get_data.f_id_spectacol('Tosca'), 45, p_get_data.f_numar_bilet(p_get_data.f_id_spectacol('Tosca')));

COMMIT;
 
-------------------------------------------------------------------
-----------------------     APELARE CERINTE
-------------------------------------------------------------------
-- exercitiul 6
BEGIN
    p_cerinte.f_afis_sponsor('Softbinator');
END;
/

-- exercitiul 7
BEGIN
    p_cerinte.f_afis_spectacole('Comedy Show');
END;
/

-- exercitiul 8
BEGIN
    DBMS_OUTPUT.PUT_LINE('Total ' || p_cerinte.f_costuri_spectacol(p_get_data.f_id_spectacol('Festival vara')));
END;
/

-- exercitiul 9
BEGIN
     p_cerinte.f_more_info_spectacol(p_get_data.f_id_spectacol('Festival vara'));
END;
/
-- exercitiul 10
INSERT INTO Achizitie
VALUES (p_get_data.f_id_device('Camera video'), 31, '23-JAN-21', '24-JAN-21', 50);

-- exercitiul 11
INSERT INTO Program
VALUES (seq_id.nextval, 31, 'Stand-up Comedy', timestamp '2020-12-29 19:00:00', timestamp '2020-12-29 21:00:00');

