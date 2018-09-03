CREATE TABLE Projects (
    id bigint auto_increment primary key,
    name varchar(255) not null,
);

CREATE TABLE WorkingTimes(
    id bigint auto_increment primary key,
    project_id bigint not null,
    start timestamp not null,
    end timestamp not null,
    FOREIGN KEY (project_id) references Projects(id)
);

CREATE TABLE CurrentIteration(
    id bigint auto_increment primary key,
    project_id bigint not null,
    start timestamp not null,
    FOREIGN KEY (project_id) references Projects(id)
);

INSERT INTO _DataModel(version) VALUES (1);