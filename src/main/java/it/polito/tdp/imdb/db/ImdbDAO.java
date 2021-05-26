package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Lavori;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO
{

	public Map<Integer, Actor> listAllActors(Map<Integer, Actor> map)
	{
		String sql = "SELECT * FROM actors";

		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));

				if (!map.containsKey(actor.getId()))
				{
					map.put(actor.getId(), actor);
				}
			}
			conn.close();
			return map;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Movie> listAllMovies()
	{
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), res.getInt("year"),
						res.getDouble("rank"));

				result.add(movie);
			}
			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Director> listAllDirectors()
	{
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{

				Director director = new Director(res.getInt("id"), res.getString("first_name"),
						res.getString("last_name"));

				result.add(director);
			}
			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	// *********************

	public Map<Integer, Director> directorsOfYear(Map<Integer, Director> map, Integer anno)
	{
		String sql = "SELECT DISTINCT(directors.id), directors.first_name, directors.last_name "
				+ "FROM movies_directors, directors, movies " 
				+ "WHERE movies_directors.director_id = directors.id "
				+ "		AND movies_directors.movie_id = movies.id " 
				+ "		AND movies.year = ?";

		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				Director director = new Director(res.getInt("id"), res.getString("first_name"),
						res.getString("last_name"));

//				if(!map.containsKey(director.getId()))
//				{
				map.put(director.getId(), director);
//				}

			}
			conn.close();
			return map;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Map<Integer, List<Actor>> getDirectorsActors(Map<Integer, List<Actor>> map, Integer anno)
	{
		String sql = "SELECT director_id as d, r.actor_id as a"
				+ "FROM movies_directors AS md, roles AS r, movies AS m " 
				+ "WHERE md.movie_id = r.movie_id "
				+ "		AND m.id = md.movie_id " 
				+ "		AND m.year = ? " 
				+ "ORDER BY director_id ";

		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				Director director = new Director(res.getInt("d"), null, null);
				Actor a = new Actor(res.getInt("a"), null, null, null);

				if (map.containsKey(director.getId()))
				{
					map.get(director.getId()).add(a);
				}
				else
				{
					map.put(director.getId(), new ArrayList<Actor>());
				}
			}
			conn.close();
			return map;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Lavori> getLavori(Map<Integer,Director> mapD, Map<Integer,Actor> mapA, Integer anno)
	{
		String sql = "SELECT md.director_id as d, r.actor_id as a, r.movie_id as m "
				+ "FROM movies_directors md, roles r, movies m " 
				+ "WHERE md.movie_id = r.movie_id " + "AND m.year = ? "
				+ "AND m.id = md.movie_id " + "ORDER BY r.movie_id ";

		List<Lavori> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				Director d = mapD.get(res.getInt("d"));
				Actor a = mapA.get(res.getInt("a"));
				Movie m = new Movie(res.getInt("m"), null, null, null);
				Lavori l = new Lavori(d, a, m);
				result.add(l);
			}
			conn.close();
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
