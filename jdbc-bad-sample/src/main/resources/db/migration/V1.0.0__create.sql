CREATE TABLE TABLE_A (
    id INT(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(30),
    address VARCHAR (256)
);

CREATE TABLE TABLE_B (
    name VARCHAR(30),
    address VARCHAR (256)
);

INSERT INTO TABLE_A (id, name, address) VALUES (1, 'name1', null);
INSERT INTO TABLE_B (name, address) VALUES ('name1', 'address1');
