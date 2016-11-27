DROP TABLE gdeltmaster IF EXISTS;

CREATE TABLE gdeltmaster  (
    gdeltmaster_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    mmURL VARCHAR(255),
    mmType VARCHAR(50)
);