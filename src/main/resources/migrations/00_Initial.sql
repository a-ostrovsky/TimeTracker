CREATE TABLE Projects (
    id bigint auto_increment,
    name varchar(255)
);

CREATE TABLE _DataModel (
    version int
);

INSERT INTO _DataModel(version) VALUES (1);