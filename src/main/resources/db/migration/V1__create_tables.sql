create table user_by_id (
                            user_id SERIAL PRIMARY KEY,
                            name varchar(100) not null,
                            email varchar(150) not null,
                            password varchar(150) not null
);
CREATE INDEX  index_name  ON user_by_id (name);
CREATE INDEX  index_email  ON user_by_id (email);

create table post_by_id (
                            post_id SERIAL PRIMARY KEY,
                            title varchar(250) not null,
                            content text not null,
                            user_id int REFERENCES user_by_id(user_id) ON DELETE CASCADE
);
CREATE INDEX  index_title  ON post_by_id (title);

create table comment_by_id (
                               comment_id SERIAL PRIMARY KEY,
                               comment varchar(250) not null,
                               user_id int REFERENCES user_by_id(user_id) ON DELETE CASCADE,
                               post_id int REFERENCES post_by_id(post_id) ON DELETE CASCADE
);
