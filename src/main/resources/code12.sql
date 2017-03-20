DROP TABLE IF EXISTS COMMENTARY;
DROP TABLE IF EXISTS MESSAGE;
DROP TABLE IF EXISTS DISCUSSION;
DROP TABLE IF EXISTS FILE;
DROP TABLE IF EXISTS MEMBER;
DROP TABLE IF EXISTS NEWS;
DROP TABLE IF EXISTS TICKET;
DROP TABLE IF EXISTS PROJECT;
DROP TABLE IF EXISTS LANGUAGE;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS ROLE;
DROP TABLE IF EXISTS persistent_logins;

CREATE TABLE USER (
  ID_USER   INT NOT NULL AUTO_INCREMENT,
  PSEUDO    VARCHAR(25) UNIQUE,
  FIRSTNAME VARCHAR(50),
  LASTNAME  VARCHAR(25),
  PASSWORD  VARCHAR(250),
  EMAIL     VARCHAR(100),
  ACCESS    VARCHAR(50) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (ID_USER)
)

  ENGINE = INNODB;
CREATE TABLE LANGUAGE (
  ID_LANGUAGE INT   NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(25)  NOT NULL UNIQUE,
  PRIMARY KEY (ID_LANGUAGE)
)

  ENGINE = INNODB;
CREATE TABLE PROJECT (
  ID_PROJECT  INT         NOT NULL AUTO_INCREMENT,
  NAME        VARCHAR(25) NOT NULL,
  PATH        VARCHAR(100) NOT NULL,
  GIT          BOOLEAN NOT NULL ,
  ID_LANGUAGE INT,
  PRIMARY KEY (ID_PROJECT),
  FOREIGN KEY (ID_LANGUAGE) REFERENCES LANGUAGE (ID_LANGUAGE)
)

  ENGINE = INNODB;
CREATE TABLE ROLE (
  ID_ROLE INT         NOT NULL AUTO_INCREMENT,
  NAME    VARCHAR(25) NOT NULL,
  PRIMARY KEY (ID_ROLE)
)

  ENGINE = INNODB;
CREATE TABLE MEMBER (
  ID_PROJECT INT,
  ID_USER    INT,
  ID_ROLE    INT,
  PRIMARY KEY (ID_PROJECT, ID_USER),
  FOREIGN KEY (ID_PROJECT) REFERENCES PROJECT (ID_PROJECT),
  FOREIGN KEY (ID_USER) REFERENCES USER (ID_USER),
  FOREIGN KEY (ID_ROLE) REFERENCES ROLE (ID_ROLE)
)

  ENGINE = INNODB;
CREATE TABLE TICKET (
  ID_TICKET     INT         NOT NULL AUTO_INCREMENT,
  ID_PROJECT    INT         NOT NULL,
  TITLE         VARCHAR(50) NOT NULL,
  CONTENT       VARCHAR(200),
  START_DATE    DATETIME    NOT NULL,
  END_DATE      DATETIME,
  ETAT          INT         NOT NULL,
  PRIORITY      INT         NOT NULL,
  TYPE          INT         NOT NULL,
  ID_AUTHOR     INT         NOT NULL,
  ID_SUPERVISOR INT,
  PRIMARY KEY (ID_TICKET),
  FOREIGN KEY (ID_PROJECT) REFERENCES PROJECT (ID_PROJECT),
  FOREIGN KEY (ID_AUTHOR) REFERENCES USER (ID_USER),
  FOREIGN KEY (ID_SUPERVISOR) REFERENCES USER (ID_USER)
)

  ENGINE = INNODB;
CREATE TABLE DISCUSSION (
  ID_DISCUSSION INT NOT NULL AUTO_INCREMENT,
  ID_USER       INT NOT NULL,
  ID_PROJECT    INT NOT NULL,
  PRESENT       BOOLEAN NOT NULL,
  PRIMARY KEY (ID_DISCUSSION, ID_USER),
  FOREIGN KEY (ID_PROJECT) REFERENCES PROJECT (ID_PROJECT),
  FOREIGN KEY (ID_USER) REFERENCES USER(ID_USER)
)

  ENGINE = INNODB;
CREATE TABLE MESSAGE (
  ID_MESSAGE    INT      NOT NULL AUTO_INCREMENT,
  TEXT          VARCHAR(500),
  DATE          DATETIME NOT NULL,
  ID_DISCUSSION INT      NOT NULL,
  ID_AUTHOR     INT      NOT NULL,
  PRIMARY KEY (ID_MESSAGE),
  FOREIGN KEY(ID_DISCUSSION,ID_AUTHOR) REFERENCES DISCUSSION(ID_DISCUSSION,ID_USER)
)

  ENGINE = INNODB;
CREATE TABLE FILE (
  ID_FILE    INT     NOT NULL AUTO_INCREMENT,
  ISLOCKING  INT,
  ID_PROJECT INT     NOT NULL,
  NAME VARCHAR(50) NOT NULL,
  PATH VARCHAR(255) NOT NULL,
  PRIMARY KEY (ID_FILE),
  FOREIGN KEY(ISLOCKING) REFERENCES USER (ID_USER),
  FOREIGN KEY (ID_PROJECT) REFERENCES PROJECT (ID_PROJECT)
)

  ENGINE = INNODB;
CREATE TABLE COMMENTARY (
  ID_COMMENTARY INT NOT NULL AUTO_INCREMENT,
  ID_AUTHOR     INT NOT NULL,
  ID_TICKET    INT NOT NULL,
  DATE    DATETIME    NOT NULL,
  CONTENT VARCHAR(500) NOT NULL,
  PRIMARY KEY (ID_COMMENTARY),
  FOREIGN KEY (ID_AUTHOR) REFERENCES USER (ID_USER),
  FOREIGN KEY (ID_TICKET) REFERENCES TICKET (ID_TICKET)
)

  ENGINE = INNODB;
CREATE TABLE NEWS (
  ID_NEWS    INT      NOT NULL AUTO_INCREMENT,
  DATE       DATETIME NOT NULL,
  ID_PROJECT INT      NOT NULL,
  ID_FILE    INT,
  ID_USER    INT,
  ID_TICKET INT,
  CONTENT    VARCHAR(500),
  PRIMARY KEY (ID_NEWS),
  FOREIGN KEY (ID_PROJECT) REFERENCES PROJECT (ID_PROJECT),
  FOREIGN KEY (ID_USER) REFERENCES USER (ID_USER),
  FOREIGN KEY (ID_TICKET) REFERENCES TICKET(ID_TICKET)
)

  ENGINE = INNODB;
CREATE TABLE persistent_logins (
  username varchar(64) not null,
  series varchar(64) not null,
  token varchar(64) not null,
  last_used timestamp not null,
  PRIMARY KEY (series)
)
INSERT INTO ROLE VALUES (NULL, 'CHEF');
INSERT INTO ROLE VALUES (NULL, 'DEVELOPPEUR');
INSERT INTO ROLE VALUES (NULL, 'REPORTER');
INSERT INTO ROLE VALUES (NULL, 'OLDMEMBER');
INSERT INTO LANGUAGE VALUES (NULL, 'Java');
INSERT INTO LANGUAGE VALUES (NULL, 'C++');
INSERT INTO LANGUAGE VALUES (NULL, 'C');
/** Trigger for NEWS TABLE **/

/** Ajout NEWS à la création d'un membre **/

CREATE TRIGGER after_insert_member AFTER INSERT
  ON MEMBER FOR EACH ROW
  BEGIN
    INSERT INTO NEWS VALUES(NULL, NOW(), NEW.ID_PROJECT, NULL, NEW.ID_USER,NULL, '1');
  END;
/** Ajout NEWS à la création d'un fichier **/

CREATE TRIGGER after_insert_file AFTER INSERT
  ON FILE FOR EACH ROW
  BEGIN
    INSERT INTO NEWS VALUES(NULL, NOW(), NEW.ID_PROJECT, NEW.ID_FILE, NULL,NULL, '1');
  END;
/** Ajout NEWS à la création d'un ticket **/

CREATE TRIGGER after_create_ticket AFTER INSERT
  ON TICKET FOR EACH ROW
  BEGIN
    INSERT INTO NEWS VALUES(NULL, NOW(), NEW.ID_PROJECT, NULL, NEW.ID_AUTHOR,NEW.ID_TICKET, '1');
  END;
/**Trigger sur le changement de role d'un membre**/

CREATE TRIGGER before_change_role_member BEFORE UPDATE
  ON MEMBER FOR EACH ROW
  BEGIN
    IF OLD.ID_ROLE != NEW.ID_ROLE THEN
      IF OLD.ID_ROLE = 4 THEN
        INSERT INTO NEWS VALUES(NULL, NOW(), NEW.ID_PROJECT, NULL, NEW.ID_USER,NULL, '1');
      ELSE
        INSERT INTO NEWS VALUES(NULL, NOW(), NEW.ID_PROJECT, NULL, NEW.ID_USER,NULL, '3');
      END IF;
    END IF;
  END;
/**Trigger sur la suppression dun membre d'un projet : Role = 4 **/

CREATE TRIGGER after_delete_member AFTER UPDATE
  ON MEMBER FOR EACH ROW
  BEGIN
    IF NEW.ID_ROLE = 4 THEN
      INSERT INTO NEWS VALUES(NULL, NOW(), NEW.ID_PROJECT, NULL, NEW.ID_USER,NULL, '2');
    END IF;
  END;

CREATE TRIGGER before_delete_file BEFORE delete
  ON FILE FOR EACH ROW
  BEGIN
    DELETE FROM NEWS WHERE ID_FILE=OLD.ID_FILE;
  END;