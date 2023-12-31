CREATE SCHEMA IF NOT EXISTS cinema;

CREATE SEQUENCE IF NOT EXISTS cinema.seq_cinema_ticket
    increment 1
    minvalue 1
    maxvalue 999999999
    START WITH 1
    NO CYCLE;

CREATE TABLE cinema.t_cinema_movie
(
    id_movie   bigint PRIMARY KEY NOT NULL,
    movie_name character varying(50)
);

CREATE TABLE cinema.t_cinema_hall
(
    hall_name       character varying(50) PRIMARY KEY NOT NULL,
    id_movie        bigint                            NOT NULL,
    base_cost       double precision                  NOT NULL,
    total_seats     integer,
    available_seats varchar[]
);

CREATE TABLE cinema.t_cinema_user
(
    user_id  uuid PRIMARY KEY       NOT NULL,
    firstname     character varying(50)  NOT NULL,
    lastname  character varying(50)  NOT NULL,
    email    character varying(100) NOT NULL,
    password character varying(100)  NOT NULL,
    role character varying(255)  NOT NULL
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

CREATE TABLE cinema.t_cinema_movie_show
(
    id_show      bigint PRIMARY KEY  NOT NULL,
    id_movie       bigint       NOT NULL,
    start_date     date         NULL,
    start_time     time         NULL
);


