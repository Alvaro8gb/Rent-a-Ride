INSERT INTO CONFIGURATION(CONFIG_KEY, CONFIG_VALUE) VALUES
('APP_NAME', 'Rent A Ride'),
('APP_LOGO', 'logo.png');

-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password, first_Name, last_Name, email, DNI)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'admin', 'admin', 'admin@gmail.com', '78655489F');
INSERT INTO IWUser (id, enabled, roles, username, password, first_Name, last_Name, email, DNI)
VALUES (2, TRUE, 'GESTOR,USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Gestor', 'Gestor', 'juli@gmail.com', '23432478T');
INSERT INTO IWUser (id, enabled, roles, username, password, first_Name, last_Name, email, DNI)
VALUES (3, TRUE, 'USER', 'c',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Cliente', 'Cliente', 'pepeM@gmail.com', '23400978T');

/*
INSERT INTO Message (id, date_sent, date_read, unattended, text, recipient_id, sender_id)
VALUES
  (1, NOW(), NULL, FALSE, 'Hola usuario 3, ¿cómo estás?', 3, 2),
  (2, NOW(), NULL, FALSE, 'Mi coche se ha partido en dos', 2, 3),
  (3, NOW(), NULL, FALSE, 'No olvides la reunión mañana', 2, 1),
  (4, NOW(), NULL, FALSE, '¡Feliz cumpleaños!', 1, 2),
  (5, NOW(), NULL, FALSE, 'Gracias', 2, 1),
  (6, NOW(), NULL, FALSE, 'Recuerda enviar el informe hoy', 2, 1),
  (7, NOW(), NULL, FALSE, 'Le he echado gasolina en vez de diesel, ¿pasa algo?', 2, 3);
*/

INSERT INTO location (id, name, contact_number)
VALUES
(1, 'Madrid', 675883445),
(2, 'Barcelona', 644223004),
(3, 'Sevilla', 668234075),
(4, 'Valencia', 664098237),
(5, 'Málaga', 644991205);

INSERT INTO vehicle (CV, LICENSE, AUTONOMY, CONSUMPTION, DOORS, FUEL, MODEL_NAME, BRAND, OLD_YEAR, SEATS, TRANSMISSION, LOCATION, PRICE_BY_DAY, OFERTA) VALUES
(100, '8673GVZ', 800, 8.6, 5, 'Gasolina', 'A3', 'Audi', 2020, 5, 'Automatico', 1, 100, 80),
(200, '8673GVZ', 750, 9.1, 5, 'Gasolina', 'A4', 'Audi',  2020, 5, 'Automatico', 2, 110, NULL),
(300, '8673GVZ', 320, 19.0, 5, 'Electrico', 'iX3', 'BMW', 2022,  5, 'Automatico', 3, 120, NULL),
(100, '8673GVZ', 280, 12.7, 3, 'Gasolina', 'Z4', 'BMW', 2010, 2, 'Automatico', 4, 130, NULL),
(100, '8673GVZ', 780, 3.6, 5, 'Diesel', 'C4', 'Citroën', 2020, 5, 'Manual', 5, 140, NULL),
(100, '8673GVZ', 640, 6.2, 5, 'Gasolina', 'Corsa', 'Opel', 2020, 5, 'Manual', 1, 150, 120),
(100, '8673GVZ', 305, 12.5, 3, 'Gasolina', 'RX-7', 'Mazda', 2020, 2, 'Manual', 2, 160, NULL),
(100, '8673GVZ', 345, 7.6, 5, 'Gasolina', 'A-Class', 'Mercedes', 2020, 5, 'Automatico', 3, 170, 159),
(100, '8673GVZ',  800, 8.4, 3, 'Diesel', 'Sprinter', 'Mercedes', 2020,  7, 'Automatico', 4, 180, NULL),
(100, '8673GVZ',  326, 22, 5, 'Electrico', 'Model Y', 'Tesla', 2021, 5, 'Automatico', 5, 190, 150);

INSERT INTO booking (USER_ID, VEHICLE_ID, IN_DATE, OUT_DATE, PRICE)
VALUES
(1, 1, '2023-05-3', '2023-05-22', 50),
(2, 2, '2023-05-1', '2023-05-3', 80),
(1, 3, '2023-05-4', '2023-05-24', 100),
(2, 4, '2023-05-1', '2023-05-4', 120),
(1, 5, '2023-05-10', '2023-05-26', 150),
(2, 6, '2023-05-1', '2023-05-10', 200),
(1, 7, '2023-05-21', '2023-05-28', 250),
(2, 8, '2023-05-21', '2023-05-29', 300),
(1, 9, '2023-05-21', '2023-05-30', 350),
(2, 10, '2023-05-21', '2023-05-30', 400),
(3, 1, '2022-05-3', '2022-05-22', 50);



-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;