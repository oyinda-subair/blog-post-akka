create table user_by_id (
    user_id SERIAL PRIMARY KEY,
    name varchar(100) not null,
    email varchar(150) not null,
    password varchar(150) not null
);
CREATE INDEX  index_name  ON user_by_id (name);
CREATE INDEX  index_email  ON user_by_id (email);
