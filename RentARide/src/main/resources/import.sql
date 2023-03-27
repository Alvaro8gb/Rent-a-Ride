-- insert admin (username a, password aa)
INSERT INTO CONFIGURATION(CONFIG_KEY, CONFIG_VALUE) VALUES
('APP_NAME', 'Rent A Ride'),
('APP_LOGO', 'logo.png');

INSERT INTO IWUser (id, enabled, roles, username, password, first_Name, last_Name, email, DNI, image_Path)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'admin', 'admin', 'admin@gmail.com', '78655489F', 'gatito.jpg');
INSERT INTO IWUser (id, enabled, roles, username, password, first_Name, last_Name, email, DNI, image_Path)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'Pepe', 'Martinez', 'pepeM@gmail.com', '23400978T', 'profile_sample.png');

INSERT INTO location (id, name, contact_number)
VALUES
(1, 'Madrid', 675883445),
(2, 'Barcelona', 644223004),
(3, 'Sevilla', 668234075),
(4, 'Valencia', 664098237),
(5, 'Málaga', 644991205);

INSERT INTO vehicle (ID, CV, LICENSE, AUTONOMY, CONSUMPTION, DOORS, FUEL, MODEL_NAME, BRAND, OLD_YEAR, SEATS, TRANSMISSION, IMAGE_PATH, LOCATION, PRICE_BY_DAY) VALUES
(1, 100, '8673GVZ', 800, 8.6, 4, 'Gasolina', 'A3', 'Audi', '2020-12-25', 5, 'Automatico', 'audiA3.png', 1, 100),
(2, 200, '8673GVZ', 750, 9.1, 4, 'Gasolina', 'A4', 'Audi',  '2020-12-25', 5, 'Automatico', 'audiA4.png', 2, 110),
(3, 300, '8673GVZ', 320, 19.0, 5, 'Electrico', 'iX3', 'BMW', '2020-12-25',  5, 'Automatico', 'bmwIX40.png', 3, 120),
(4, 100, '8673GVZ', 280, 12.7, 2, 'Gasolina', 'Z4', 'BMW', '2020-12-25', 2, 'Automatico', 'bmwZ4.png', 4, 130),
(6, 100, '8673GVZ', 780, 3.6, 5, 'Diesel', 'C4', 'Citroën', '2020-12-25', 5, 'Manual', 'citroenC4.png', 5, 140),
(7, 100, '8673GVZ', 640, 6.2, 5, 'Gasolina', 'Corsa', 'Opel', '2020-12-25', 5, 'Manual', 'corsa.png', 1, 150),
(8, 100, '8673GVZ', 305, 12.5, 2, 'Gasolina', 'RX-7', 'Mazda', '2020-12-25', 2, 'Manual', 'mazdaRX7.png', 2, 160),
(9, 100, '8673GVZ', 345, 7.6, 5, 'Gasolina', 'A-Class', 'Mercedes', '2020-12-25', 5, 'Automatico', 'mercedesClaseA.png', 3, 170),
(10,100, '8673GVZ',  800, 8.4, 3, 'Diesel', 'Sprinter', 'Mercedes', '2020-12-25',  7, 'Automatico', 'mercedesVan.png', 4, 180),
(12,100, '8673GVZ',  326, 22, 5, 'Electrico', 'Model Y', 'Tesla', '2020-12-25', 5, 'Automatico', 'teslaY.png', 5, 190);

INSERT INTO booking (USER_ID, VEHICLE_ID, IN_DATE, OUT_DATE, PRICE)
VALUES
(1, 1, '2023-03-21', '2023-03-22', 50),
(2, 2, '2023-03-21', '2023-03-23', 80),
(1, 3, '2023-03-21', '2023-03-24', 100);
-- (2, 4, '2023-03-21', '2023-03-25', 120),
-- (1, 5, '2023-03-21', '2023-03-26', 150),
-- (2, 6, '2023-03-21', '2023-03-27', 200),
-- (1, 7, '2023-03-21', '2023-03-28', 250),
-- (2, 8, '2023-03-21', '2023-03-29', 300),
-- (1, 9, '2023-03-21', '2023-03-30', 350),
-- (2, 10, '2023-03-21', '2023-03-31', 400);

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;