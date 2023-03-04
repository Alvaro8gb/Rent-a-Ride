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

INSERT INTO vehicle (ID, AUTONOMY, CITY_CONSUMPTION, DESCRIPTION, DOORS, FUEL, VEHICLE, OLD_YEAR, ROAD_CONSUMPTION, SEATS, TRANSMISSION, LOCATION)
VALUES
(1, 700, 7.2, 'Sedan de lujo', 4, 'Gasolina', 'Camry', 2019, 5.5, 5, 'Automatico', 1),
(2, 800, 6.8, 'SUV para la familia', 5, 'Gasolina', 'CR-V', 2020, 6.0, 7, 'Automatico', 2),
(3, 650, 8.3, 'Coche deportivo', 2, 'Gasolina', 'Mustang', 2018, 8.0, 2, 'Manual', 3),
(4, 1000, 5.0, 'Autobús escolar', 2, 'Diesel', 'Blue Bird', 2015, 10.0, 50, 'Automatico', 4),
(5, 900, 6.5, 'Furgoneta de carga', 3, 'Diesel', 'Transit', 2017, 7.5, 2, 'Manual', 5);

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
