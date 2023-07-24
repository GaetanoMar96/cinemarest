insert into cinema.t_cinema_movie (id_movie, movie_name, director, actors, duration, aging_rate, summary)
values (1, 'Inception', 'nolan', '{"Di Caprio","Tom Hardy"}', 133, 13, '');

insert into cinema.t_cinema_hall(hall_name, id_movie, base_cost, total_seats, available_seats)
values ('SALA', 1, 10, 50, '{1,2,3,4,5}');

insert into cinema.t_cinema_user (user_id, name, surname, email, password, phone, wallet)
values ('41e822f6-d648-4a1c-acc3-44c8336b4665', 'mario', 'rossi', 'email', 'pwd', '333', 0);