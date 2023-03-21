-- insert admin (username a, password aa)
INSERT INTO CONFIGURATION(CONFIG_KEY, CONFIG_VALUE) VALUES
('APP_NAME', 'Rent A Ride'),
('APP_LOGO', 'logo.png');

INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
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

INSERT INTO vehicle (ID, AUTONOMY, CITY_CONSUMPTION, DESCRIPTION, DOORS, FUEL, VEHICLE, OLD_YEAR, ROAD_CONSUMPTION, SEATS, TRANSMISSION, IMAGE_PATH, LOCATION, PRICE_BY_DAY) VALUES
(1, 800, 8.6, 'compact executive car produced by Audi since 1996.', 4, 'Gasolina', 'Audi A3', 2015, 6.2, 5, 'Automatico', 'audiA3.png', 1, 100),
(2, 750, 9.1, 'compact executive cars produced by Audi.', 4, 'Gasolina', 'Audi A4', 2014, 6.5, 5, 'Automatico', 'audiA4.png', 2, 110),
(3, 320, 19.0, 'fully electric compact luxury SUV produced by BMW.', 5, 'Electrico', 'BMW iX3', 2022, 18.6, 5, 'Automatico', 'bmwIX40.png', 3, 120),
(4, 280, 12.7, 'range of two-seat roadster and coupe models.', 2, 'Gasolina', 'BMW Z4', 2020, 8.0, 2, 'Automatico', 'bmwZ4.png', 4, 130),
(6, 780, 3.6, 'compact car produced by Citroën.', 5, 'Diesel', 'Citroën C4', 2018, 2.8, 5, 'Manual', 'citroenC4.png', 5, 140),
(7, 640, 6.2, 'supermini car.', 5, 'Gasolina', 'Opel Corsa', 2020, 4.6, 5, 'Manual', 'corsa.png', 1, 150),
(8, 305, 12.5, 'Japanese sports car.', 2, 'Gasolina', 'Mazda RX-7', 2000, 7.8, 2, 'Manual', 'mazdaRX7.png', 2, 160),
(9, 345, 7.6, 'subcompact executive car.', 5, 'Gasolina', 'Mercedes A-Class', 2015, 5.1, 5, 'Automatico', 'mercedesClaseA.png', 3, 170),
(10, 800, 8.4, 'light commercial vehicle.', 3, 'Diesel', 'Mercedes Sprinter', 2021, 10.2, 7, 'Automatico', 'mercedesVan.png', 4, 180),
(12, 326, 22, 'electric compact crossover', 5, 'Electrico', 'Tesla Model Y', 2020, 20, 5, 'Automatico', 'teslaY.png', 5, 190);

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;