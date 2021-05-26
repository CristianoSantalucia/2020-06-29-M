package it.polito.tdp.imdb.model;

public class Lavori
{
	private Director d;
	private Actor a;
	private Movie m;

	public Lavori(Director d, Actor a, Movie m)
	{
		this.d = d;
		this.a = a;
		this.m = m;
	}

	public Director getD()
	{
		return d;
	}
	public void setD(Director d)
	{
		this.d = d;
	}
	public Actor getA()
	{
		return a;
	}
	public void setA(Actor a)
	{
		this.a = a;
	}
	public Movie getM()
	{
		return m;
	}
	public void setM(Movie m)
	{
		this.m = m;
	}
}
