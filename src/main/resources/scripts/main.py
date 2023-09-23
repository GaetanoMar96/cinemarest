import requests
import psycopg2
import os

# Method to update records in db according to external service response
def main():
    # API key
    api_key = os.getenv('API_KEY')

    # Base URL for TMDb API
    base_url = "https://api.themoviedb.org/3/"

    # Endpoint for getting now playing movies
    now_playing_endpoint = "movie/now_playing"

    # Parameters for the API request
    params = {
        "api_key": api_key,
        "page": 1,
        "region": "IT",
    }

    token = "Bearer " + os.getenv('TOKEN')
    headers = {
        "accept": "application/json",
        "Authorization": token
    }

    # Make the API request to get now playing movies
    response = requests.get(base_url + now_playing_endpoint, params=params, headers=headers)

    if response.status_code == 200:
        now_playing_movies = response.json().get("results", [])

        try:
            # Establish a connection to your database
            conn = psycopg2.connect(
                dbname="postgres",
                user=os.getenv('USER'),  # Db USER
                password=os.getenv('PWD'),  # Db PWD
                host="localhost",
            )

            # Create a cursor
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM cinema.t_cinema_movie")
            existing_movie_ids = {row[0] for row in cursor.fetchall()}
            not_existing_movie = set()
            for movie in now_playing_movies[:4]:
                # Check if the movie ID exists in your database
                movie_id = movie["id"]
                movie_title = movie["original_title"]
                if movie_id not in existing_movie_ids:
                    not_existing_movie.add((movie_id, movie_title))  # Storing tuple of id and title
            count = 0
            while not_existing_movie:
                if existing_movie_ids:
                    id_movie = existing_movie_ids.pop()
                    new_id_movie, new_movie_title = not_existing_movie.pop()

                    # Update the movie id and title in the 't_cinema_movie' table
                    movies_query = "UPDATE cinema.t_cinema_movie SET id_movie = %s, movie_name = %s WHERE id_movie = %s"
                    cursor.execute(movies_query, (new_id_movie, new_movie_title, id_movie))

                    # Update the movie id in the 't_cinema_hall' table
                    hall_query = "UPDATE cinema.t_cinema_hall SET id_movie = %s WHERE id_movie = %s"
                    cursor.execute(hall_query, (new_id_movie, id_movie))

                    # Update the movie id in the 't_cinema_movie_show' table
                    show_query = "UPDATE cinema.t_cinema_movie_show SET id_movie = %s WHERE id_movie = %s"
                    cursor.execute(show_query, (new_id_movie, id_movie))

                    conn.commit()
                    count += 1
            print(f"Records updated -> {count}")
            # Close the cursor and connection
            cursor.close()
            conn.close()
        except Exception as e:
            print(f"Failed to update data -> {e}")

    else:
        print(f"Failed to fetch now playing movies. Status code: {response.status_code}")


if __name__ == '__main__':
    main()
