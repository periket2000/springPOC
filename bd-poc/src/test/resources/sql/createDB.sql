CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(255),
    creation_date TIMESTAMP,
    update_date TIMESTAMP
);
INSERT INTO users (user_id, first_name, last_name, phone, email, creation_date, update_date) VALUES (41, 'LYDIA', 'MAYNE', '+34666544334', 'lydia@microsoft.com', '2017-12-30 00:02:57', '2017-12-30 00:02:57');
