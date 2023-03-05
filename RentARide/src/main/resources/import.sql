-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO location (id, name)
VALUES
(1, 'Madrid'),
(2, 'Barcelona'),
(3, 'Sevilla'),
(4, 'Valencia'),
(5, 'Málaga');

INSERT INTO vehicle (ID, AUTONOMY, CITY_CONSUMPTION, DESCRIPTION, DOORS, FUEL, VEHICLE, OLD_YEAR, ROAD_CONSUMPTION, SEATS, TRANSMISSION, IMAGE_PATH, LOCATION) VALUES
(1, 800, 8.6, 'compact executive car produced by Audi since 1996.', 4, 'Gasolina', 'Audi A3', 2015, 6.2, 5, 'Automatico', 'audiA3.png', 1),
(2, 750, 9.1, 'compact executive cars produced by Audi.', 4, 'Gasolina', 'Audi A4', 2014, 6.5, 5, 'Automatico', 'audiA4.png', 2),
(3, 320, 19.0, 'fully electric compact luxury SUV produced by BMW.', 5, 'Electrico', 'BMW iX3', 2022, 18.6, 5, 'Automatico', 'bmwIX40.png', 3),
(4, 280, 12.7, 'range of two-seat roadster and coupe models.', 2, 'Gasolina', 'BMW Z4', 2020, 8.0, 2, 'Automatico', 'bmwZ4.png', 4),
(6, 780, 3.6, 'compact car produced by Citroën.', 5, 'Diesel', 'Citroën C4', 2018, 2.8, 5, 'Manual', 'citroenC4.png', 5),
(7, 640, 6.2, 'supermini car.', 5, 'Gasolina', 'Opel Corsa', 2020, 4.6, 5, 'Manual', 'corsa.png', 1),
(8, 305, 12.5, 'Japanese sports car.', 2, 'Gasolina', 'Mazda RX-7', 2000, 7.8, 2, 'Manual', 'mazdaRX7.png', 2),
(9, 345, 7.6, 'subcompact executive car.', 5, 'Gasolina', 'Mercedes A-Class', 2015, 5.1, 5, 'Automatico', 'mercedesClaseA.png', 3),
(10, 800, 8.4, 'light commercial vehicle.', 3, 'Diesel', 'Mercedes Sprinter', 2021, 10.2, 7, 'Automatico', 'mercedesVan.png', 4),
(12, 326, 22, 'electric compact crossover', 5, 'Electrico', 'Tesla Model Y', 2020, 20, 5, 'Automatico', 'teslaY.png', 5);

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
