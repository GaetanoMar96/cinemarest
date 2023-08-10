insert into cinema.t_cinema_movie (id_movie, movie_name)
values (1, 'Inception');

insert into cinema.t_cinema_hall(hall_name, id_movie, base_cost, total_seats, available_seats)
values ('SALA', 1, 10, 50, '{1,2,3,4,5}');

insert into cinema.t_cinema_user (user_id, firstname, lastname, email, password, wallet, role)
values ('41e822f6-d648-4a1c-acc3-44c8336b4665', 'mario', 'rossi', 'email', 'pwd', 0, 'USER');

insert into cinema.t_cinema_movie_show(id, id_movie, start_date, start_time)
values (1, 1, '2023-08-10', '14:00:00'),
       (2, 1, '2023-08-10', '16:00:00'),
       (3, 1, '2023-08-10', '20:00:00');
