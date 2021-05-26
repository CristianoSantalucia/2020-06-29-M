package it.polito.tdp.imdb.model;

import java.util.Comparator;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class MyCompartor implements Comparator<Director>
{
	Graph<Director, DefaultWeightedEdge> grafo;
	Director vertice;

	public MyCompartor(Graph<Director, DefaultWeightedEdge> grafo, Director vertice)
	{
		this.grafo = grafo;
		this.vertice = vertice;
	}

	@Override public int compare(Director d1, Director d2)
	{ 
		return - (int) (this.grafo.getEdgeWeight(this.grafo.getEdge(vertice, d1)) - this.grafo.getEdgeWeight(this.grafo.getEdge(vertice, d2))); 
	}
} 