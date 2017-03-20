 
/*SCRIPT TEST SQL*/

/* Ajout de USER*/

INSERT INTO USER VALUES(NULL, 'Pseudo1','FirstName1', 'LastName1', 'password1', 'email1@email.com');
INSERT INTO USER VALUES(NULL, 'Pseudo2','FirstName2', 'LastName2', 'password2', 'email2@email.com');
INSERT INTO USER VALUES(NULL, 'Pseudo3','FirstName3', 'LastName3', 'password3', 'email3@email.com');
INSERT INTO USER VALUES(NULL, 'Pseudo4','FirstName4', 'LastName4', 'password4', 'email4@email.com');
INSERT INTO USER VALUES(NULL, 'Pseudo5','FirstName5', 'LastName5', 'password5', 'email5@email.com');
INSERT INTO USER VALUES(NULL, 'Pseudo6','FirstName6', 'LastName6', 'password6', 'email6@email.com');
INSERT INTO USER VALUES(NULL, 'Pseudo7','FirstName7', 'LastName7', 'password7', 'email7@email.com');
INSERT INTO USER VALUES(NULL, 'Pseudo8','FirstName8', 'LastName8', 'password8', 'email8@email.com');


/*Ajout de language */

INSERT INTO LANGUAGE VALUES (NULL, 'Java');
INSERT INTO LANGUAGE VALUES (NULL, 'C++');
INSERT INTO LANGUAGE VALUES (NULL, 'Python');

/*Ajout ROLE*/

/* Chef de projet : 1, Developpeur : 2, Reporter :3 */

INSERT INTO ROLE VALUES (NULL, 'Chef de Projet');
INSERT INTO ROLE VALUES (NULL, 'Developpeur');
INSERT INTO ROLE VALUES (NULL, 'Reporter');


/* Ajout de projet*/

INSERT INTO PROJECT VALUES (NULL, 'nameproject1', '/idproject', 2, 1);
INSERT INTO PROJECT VALUES (NULL, 'nameproject2', '/idproject2', 1, 2);

/* Ajout de membre au projet */

/* Projet 1 */
INSERT INTO MEMBER VALUES (1,1,1);
INSERT INTO MEMBER VALUES (1, 3, 2);
INSERT INTO MEMBER VALUES (1, 5, 2);
INSERT INTO MEMBER VALUES (1, 8, 2);
INSERT INTO MEMBER VALUES (1, 4, 3);
INSERT INTO MEMBER VALUES (1, 2, 3);


/*Projet2*/
INSERT INTO MEMBER VALUES (2, 2, 1);
INSERT INTO MEMBER VALUES (2, 6, 2);
INSERT INTO MEMBER VALUES (2, 7, 2);
INSERT INTO MEMBER VALUES (2, 4, 2);
INSERT INTO MEMBER VALUES (2, 8, 3);
INSERT INTO MEMBER VALUES (2, 5, 3);

/* Rajout de fichier dans les projets */

/* Projet 1 */
INSERT INTO FILE VALUES(NULL, false, 1, 'fichier1');
INSERT INTO FILE VALUES(NULL, false, 1, 'fichier2');
INSERT INTO FILE VALUES(NULL, false, 1, 'fichier3');
INSERT INTO FILE VALUES(NULL, false, 1, 'fichier4');

/* Projet 2 */
INSERT INTO FILE VALUES(NULL, false, 2, 'fichier1');
INSERT INTO FILE VALUES(NULL, false, 2, 'fichier2');
INSERT INTO FILE VALUES(NULL, false, 2, 'fichier3');
INSERT INTO FILE VALUES(NULL, false, 2, 'fichier4');


/* Création de tickets */

/*Projet 1*/

INSERT INTO TICKET VALUES (1, 1, 'ticket1', 'content1', '2016-10-19 12:00:00.000', NULL,'New', 'Normal', 1, NULL);


INSERT INTO TICKET VALUES (2, 1, 'ticket2', 'content2', '2016-10-19 12:00:00.000', '2016-10-19 15:00:00.000','Closed', 'Normal', 1, 3);
INSERT INTO TICKET VALUES (3, 1, 'ticket3', 'content3', '2016-10-19 12:00:00.000', NULL,'In Progress', 'Important',3 , 3);
INSERT INTO TICKET VALUES (4, 1, 'ticket4', 'content4', '2016-10-19 12:00:00.000', '2016-10-19 15:00:00.000','In Progress', 'Normal', 2, NULL);


/* Project 2 */
INSERT INTO TICKET VALUES (1, 2, 'ticket1', 'content1', '2016-10-19 12:00:00.000', NULL,'New', 'Normal', 2, 2);
INSERT INTO TICKET VALUES (2, 2, 'ticket2', 'content2', '2016-10-19 12:00:00.000', '2016-10-19 15:00:00.000','Closed', 'Normal', 6, 4);
INSERT INTO TICKET VALUES (3, 2, 'ticket3', 'content3', '2016-10-19 12:00:00.000', NULL,'In Progress', 'Important',7 , 6);
INSERT INTO TICKET VALUES (4, 2, 'ticket4', 'content4', '2016-10-19 12:00:00.000', '2016-10-19 15:00:00.000','In Progress', 'Normal', 8, 2);



/* Commentaire de tickets */

INSERT INTO COMMENTARY VALUES (NULL, 1, 1, 1, 'comment1');
INSERT INTO COMMENTARY VALUES (NULL, 1, 1, 2, 'comment2');
INSERT INTO COMMENTARY VALUES (NULL, 8, 1, 1, 'comment3');
INSERT INTO COMMENTARY VALUES (NULL, 2, 1, 3, 'comment4');

INSERT INTO COMMENTARY VALUES (NULL, 2, 2, 1, 'comment5');
INSERT INTO COMMENTARY VALUES (NULL, 5, 2, 2, 'comment6');
INSERT INTO COMMENTARY VALUES (NULL, 4, 2, 1, 'comment7');
INSERT INTO COMMENTARY VALUES (NULL, 6, 2, 3, 'comment8');



/* Ajout de discussion */

/* Projet 1 */

INSERT INTO DISCUSSION VALUES (NULL, 1,1);
INSERT INTO DISCUSSION VALUES (NULL, 1, 1);

/* Projet 2 */ 

INSERT INTO DISCUSSION VALUES (NULL, 2, 2);
INSERT INTO DISCUSSION VALUES (NULL, 4, 2);


/* Ajout de message dans les discussions */


/* Discussion 1 */
INSERT INTO MESSAGE VALUES (NULL, 'textmessage1', '2016-10-19 12:00:00.000', 1, 1);
INSERT INTO MESSAGE VALUES(NULL, 'textmessage2', '2016-10-19 12:01:00.000', 3,1);

/*Discussion 2 */
INSERT INTO MESSAGE VALUES (NULL, 'textmessage1', '2016-10-19 12:00:00.000', 8, 2);
INSERT INTO MESSAGE VALUES(NULL, 'textmessage2', '2016-10-19 12:01:00.000', 4, 2);
INSERT INTO MESSAGE VALUES(NULL, 'textmessage3', '2016-10-19 12:05:00.000', 8, 2);

/* Discussion 3 */
INSERT INTO MESSAGE VALUES (NULL, 'textmessage1', '2016-10-19 12:00:00.000', 2, 3);
INSERT INTO MESSAGE VALUES(NULL, 'textmessage2', '2016-10-19 12:01:00.000', 6, 3);
INSERT INTO MESSAGE VALUES(NULL, 'textmessage3', '2016-10-19 12:05:00.000', 2, 3);

/* Discussion 4 */
INSERT INTO MESSAGE VALUES (NULL, 'textmessage1', '2016-10-19 12:00:00.000', 7, 4);
INSERT INTO MESSAGE VALUES(NULL, 'textmessage2', '2016-10-19 12:01:00.000', 5,4);






