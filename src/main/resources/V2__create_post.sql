create table post_by_id (
    post_id SERIAL PRIMARY KEY,
    title varchar(250) not null,
    content text not null,
    user_id int REFERENCES user_by_id(user_id) ON DELETE CASCADE
);
CREATE INDEX  index_title  ON post_by_id (title);