DROP SEQUENCE IF EXISTS cinema.seq_cinema_ticket;

DROP TABLE IF EXISTS cinema.t_cinema_transaction CASCADE;
DROP TABLE IF EXISTS cinema.t_cinema_user CASCADE;
DROP TABLE IF EXISTS cinema.t_cinema_hall CASCADE;
DROP TABLE IF EXISTS cinema.t_cinema_tiket CASCADE;
DROP TABLE IF EXISTS cinema.t_cinema_movie CASCADE;

DROP SCHEMA IF EXISTS cinema CASCADE;