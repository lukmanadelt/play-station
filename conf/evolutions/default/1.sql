-- users schema

-- !Ups

CREATE TABLE users (
  id SERIAL NOT NULL PRIMARY KEY,
  user_full_name character varying(255) NOT NULL,
  user_email character varying(100) NOT NULL,
  user_password character varying(255) NOT NULL,
  user_address character varying(255) NOT NULL,
  user_phone character varying(20) NOT NULL
);

-- !Downs

DROP TABLE users;