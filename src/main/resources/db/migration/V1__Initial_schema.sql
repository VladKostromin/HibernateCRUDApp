CREATE TABLE writers (
                         id BIGSERIAL PRIMARY KEY,
                         firstName VARCHAR(255),
                         lastName VARCHAR(255)
);

CREATE TABLE posts (
                       id BIGSERIAL PRIMARY KEY,
                       content TEXT,
                       created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       writer_id BIGINT REFERENCES writers(id),
                       status VARCHAR(255)
);

CREATE TABLE labels (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) UNIQUE
);

CREATE TABLE post_labels
(
    post_id  BIGINT NOT NULL,
    label_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (label_id) REFERENCES labels (id),
    UNIQUE (post_id, label_id)
);
