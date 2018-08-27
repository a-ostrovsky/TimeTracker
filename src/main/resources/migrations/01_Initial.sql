CREATE TABLE Projects (
    id bigint auto_increment,
    name varchar(255)
);

CREATE TABLE WorkingTimes(
    id bigint auto_increment,
    project_id bigint,
    start timestamp,
    end timestamp,
    FOREIGN KEY (project_id) references Projects(id)
);

INSERT INTO _DataModel(version) VALUES (1);