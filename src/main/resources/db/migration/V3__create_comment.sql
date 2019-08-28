create table comment_by_id (
    comment_id SERIAL PRIMARY KEY,
    comment varchar(250) not null,
    user_id int REFERENCES user_by_id(user_id) ON DELETE CASCADE,
    post_id int REFERENCES post_by_id(post_id) ON DELETE CASCADE
);
