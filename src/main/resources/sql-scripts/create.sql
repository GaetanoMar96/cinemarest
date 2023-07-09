CREATE TABLE IF NOT EXISTS cinema.t_cinema_movie
(
    id_movie bigint NOT NULL DEFAULT nextval('seq_cinema_movie'),
    movie_name character varying(50) COLLATE pg_catalog."default",
    director character varying(50) COLLATE pg_catalog."default",
    actors character varying(255)[] COLLATE pg_catalog."default",
    duration time without time zone,
    aging_rate integer,
    summary text COLLATE pg_catalog."default",
    CONSTRAINT "T_CINEMA_MOVIE_pkey" PRIMARY KEY (id_movie)
)
