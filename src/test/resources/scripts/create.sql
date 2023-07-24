CREATE SCHEMA cinema;

CREATE SEQUENCE IF NOT EXISTS cinema.seq_cinema_movie
    increment 1
    minvalue 1
    maxvalue 999999999
    START WITH 1
    NO CYCLE;

CREATE SEQUENCE IF NOT EXISTS cinema.seq_cinema_ticket
    increment 1
    minvalue 1
    maxvalue 999999999
    START WITH 1
    NO CYCLE;

CREATE TABLE cinema.t_cinema_movie
(
    id_movie   bigint PRIMARY KEY NOT NULL,
    movie_name character varying(50),
    director   character varying(50),
    actors     character varying(255)[],
    duration   integer,
    aging_rate integer,
    summary    text
);

CREATE TABLE cinema.t_cinema_hall
(
    hall_name       character varying(50) PRIMARY KEY NOT NULL,
    id_movie        bigint                            NOT NULL,
    base_cost       double precision                  NOT NULL,
    total_seats     integer,
    available_seats integer[]
);

CREATE TABLE cinema.t_cinema_user
(
    user_id  uuid PRIMARY KEY       NOT NULL,
    name     character varying(50)  NOT NULL,
    surname  character varying(50)  NOT NULL,
    email    character varying(100) NOT NULL,
    password character varying(50)  NOT NULL,
    phone    character varying(20),
    wallet   double precision
);

CREATE TABLE cinema.t_cinema_ticket
(
    ticket_id bigint PRIMARY KEY NOT NULL,
    id_movie  bigint             NOT NULL,
    cost      double precision   NOT NULL
);

CREATE TABLE cinema.t_cinema_transaction
(
    transaction_id uuid PRIMARY KEY NOT NULL,
    ticket_id      bigint           NOT NULL,
    id_movie       bigint           NOT NULL,
    user_id        uuid             NOT NULL
);

ALTER TABLE cinema.t_cinema_hall
    ADD FOREIGN KEY (id_movie) REFERENCES cinema.t_cinema_movie (id_movie);

ALTER TABLE cinema.t_cinema_ticket
    ADD FOREIGN KEY (id_movie) REFERENCES cinema.t_cinema_movie (id_movie);

ALTER TABLE cinema.t_cinema_transaction
    ADD FOREIGN KEY (id_movie) REFERENCES cinema.t_cinema_movie (id_movie);

ALTER TABLE cinema.t_cinema_transaction
    ADD FOREIGN KEY (ticket_id) REFERENCES cinema.t_cinema_ticket (ticket_id);

ALTER TABLE cinema.t_cinema_transaction
    ADD FOREIGN KEY (user_id) REFERENCES cinema.t_cinema_user (user_id);

