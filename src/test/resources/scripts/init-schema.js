db = db.getSiblingDB('cinema');

db.createUser({
  user: 'test_container',
  pwd: 'test_container',
  roles: [
    { role: 'readWrite', db: 'cinema' }
  ]
});

db.movies.insertOne({
  title: "Avatar",
  year: "2009",
  rated: "PG-13",
  released: "18 Dec 2009",
  runtime: "162 min",
  genre: "Action, Adventure, Fantasy",
  director: "James Cameron",
  actors: "Sam Worthington, Zoe Saldana, Sigourney Weaver, Stephen Lang",
  plot: "A paraplegic marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
  poster: "http://ia.media-imdb.com/images/M/MV5BMTYwOTEwNjAzMl5BMl5BanBnXkFtZTcwODc5MTUwMw@@._V1_SX300.jpg",
  imdbRating: "7.9"
});

db.movies.insertOne({
  title: "I Am Legend",
  year: "2007",
  rated: "PG-13",
  released: "14 Dec 2007",
  runtime: "101 min",
  genre: "Drama, Horror, Sci-Fi",
  director: "Francis Lawrence",
  actors: "Will Smith, Alice Braga, Charlie Tahan, Salli Richardson-Whitfield",
  plot: "Years after a plague kills most of humanity and transforms the rest into monsters, the sole survivor in New York City struggles valiantly to find a cure.",
  poster: "http://ia.media-imdb.com/images/M/MV5BMTU4NzMyNDk1OV5BMl5BanBnXkFtZTcwOTEwMzU1MQ@@._V1_SX300.jpg",
  imdbRating: "7.2"
});

db.movies.insertOne({
  title: "300",
  year: "2006",
  rated: "R",
  released: "09 Mar 2007",
  runtime: "117 min",
  genre: "Action, Drama, Fantasy",
  director: "Zack Snyder",
  writer: "Zack Snyder (screenplay), Kurt Johnstad (screenplay), Michael Gordon (screenplay), Frank Miller (graphic novel), Lynn Varley (graphic novel)",
  actors: "Gerard Butler, Lena Headey, Dominic West, David Wenham",
  plot: "King Leonidas of Sparta and a force of 300 men fight the Persians at Thermopylae in 480 B.C.",
  poster: "http://ia.media-imdb.com/images/M/MV5BMjAzNTkzNjcxNl5BMl5BanBnXkFtZTYwNDA4NjE3._V1_SX300.jpg",
  imdbRating: "7.7"
});

db.movies.insertOne({
  title: "The Avengers",
  year: "2012",
  rated: "PG-13",
  released: "04 May 2012",
  runtime: "143 min",
  genre: "Action, Sci-Fi, Thriller",
  director: "Joss Whedon",
  actors: "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth",
  plot: "Earth's mightiest heroes must come together and learn to fight as a team if they are to stop the mischievous Loki and his alien army from enslaving humanity.",
  poster: "http://ia.media-imdb.com/images/M/MV5BMTk2NTI1MTU4N15BMl5BanBnXkFtZTcwODg0OTY0Nw@@._V1_SX300.jpg",
  imdbRating: "8.1"
});

db.movies.insertOne({
  title: "The Wolf of Wall Street",
  year: "2013",
  rated: "R",
  released: "25 Dec 2013",
  runtime: "180 min",
  genre: "Biography, Comedy, Crime",
  director: "Martin Scorsese",
  actors: "Leonardo DiCaprio, Jonah Hill, Margot Robbie, Matthew McConaughey",
  plot: "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
  poster: "http://ia.media-imdb.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_SX300.jpg",
  imdbRating: "8.2"
});

db.movies.insertOne({
  title: "Interstellar",
  year: "2014",
  rated: "PG-13",
  released: "07 Nov 2014",
  runtime: "169 min",
  genre: "Adventure, Drama, Sci-Fi",
  director: "Christopher Nolan",
  actors: "Ellen Burstyn, Matthew McConaughey, Mackenzie Foy, John Lithgow",
  plot: "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
  poster: "http://ia.media-imdb.com/images/M/MV5BMjIxNTU4MzY4MF5BMl5BanBnXkFtZTgwMzM4ODI3MjE@._V1_SX300.jpg",
  imdbRating: "8.6"
});
