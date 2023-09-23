insert into cinema.t_cinema_movie (id_movie, movie_name)
values (872585, 'Inception');

insert into cinema.t_cinema_hall(hall_name, id_movie, base_cost, total_seats, available_seats)
values ('SALA1', 872585, 10, 50, '{A1,A2,A3,A4,A5}');

insert into cinema.t_cinema_user (user_id, firstname, lastname, email, password, role)
values ('41e822f6-d648-4a1c-acc3-44c8336b4665', 'mario', 'rossi', 'email', 'pwd', 'USER');

insert into cinema.t_cinema_movie_show(id_show, id_movie, start_date, start_time)
values (1, 872585, '2023-08-10', '14:00:00'),
       (2, 872585, '2023-08-10', '16:00:00'),
       (3, 872585, '2023-08-10', '20:00:00');

insert into cinema.t_cinema_ticket (ticket_id, id_movie, cost)
values (99, 872585, 10);

insert into cinema.t_cinema_transaction (transaction_id, ticket_id, id_movie, user_id)
values ('fe25b558-980c-470d-9a86-c2e47c1cea69', 99, 872585, '41e822f6-d648-4a1c-acc3-44c8336b4665');