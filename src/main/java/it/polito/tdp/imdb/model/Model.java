package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model
{
	private Map<Integer, Director> idRegisti;
	private Map<Integer, Actor> idAttori;
	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge> grafo;

	public Model()
	{
		this.dao = new ImdbDAO();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}

	public void creaGrafo(Integer anno)
	{
		this.idRegisti = new HashMap<>();
		this.idAttori = new HashMap<>(); // TODO

		// vertici
		List<Director> registi = new ArrayList<>(this.dao.directorsOfYear(this.idRegisti, anno).values());
		List<Actor> attori = new ArrayList<>(this.dao.listAllActors(this.idAttori).values());
		Graphs.addAllVertices(this.grafo, registi);
		System.out.println(this.grafo.vertexSet().size());

		// archi
		ArrayList<Lavori> lavori = new ArrayList<>(this.dao.getLavori(idRegisti, idAttori, anno));
		System.out.println(lavori.size());

		for (int i = 0; i < lavori.size(); i++)
		{
			for (int j = i + 1; j < lavori.size(); j++)
			{
				if (!lavori.get(i).getD().getId().equals(lavori.get(j).getD().getId())
						&& lavori.get(i).getA().getId().equals(lavori.get(j).getA().getId()))
				{
					DefaultWeightedEdge e = this.grafo.getEdge(idRegisti.get(lavori.get(i).getD().getId()),
							idRegisti.get(lavori.get(j).getD().getId()));

					if (this.grafo.containsEdge(e))
					{
						double t = this.grafo.getEdgeWeight(e);
						this.grafo.setEdgeWeight(e, t + 1);
					}
					else
					{
						Graphs.addEdgeWithVertices(this.grafo, idRegisti.get(lavori.get(i).getD().getId()),
								idRegisti.get(lavori.get(j).getD().getId()), 1);

					}

				}
			}
		}

//		for (Lavori l1 : lavori)
//		{
//			for (Lavori l2 : lavori)
//			{
//				if (l1.getA().equals(l2.getA()) && !l1.getD().equals(l2.getD()))
//				{
//					DefaultWeightedEdge e = this.grafo.getEdge(l1.getD(), l2.getD());
//
//					if (!this.grafo.containsEdge(e))
//						Graphs.addEdgeWithVertices(this.grafo, l1.getD(), l2.getD(), 1);
//					else this.grafo.setEdgeWeight(e, this.grafo.getEdgeWeight(e) + 1);
//				}
//			}
//		}
		System.out.println(this.grafo.edgeSet().size());
	}

	ArrayList<Director> registi;

	public Collection<Director> getDirectorsOfYear(Integer anno)
	{
		registi = new ArrayList<>(this.dao.directorsOfYear(idRegisti, anno).values());

		registi.sort((d1, d2) -> (int) (d1.getId() - d2.getId()));

		return registi;
	}

	public String getAdiacenze(Director d)
	{
		String s = "";

		ArrayList<Director> adj = new ArrayList<>(Graphs.neighborListOf(this.grafo, d));

		Collections.sort(adj, new MyCompartor((Graph<Director, DefaultWeightedEdge>) this.grafo, d));

		for (Director dir : adj)
		{
			s += dir + " PESO: " + this.grafo.getEdgeWeight(this.grafo.getEdge(dir, d)) + "\n";
		}

		return s;
	}

	List<Director> best;
	int CMAX;

	public List<Director> ricerca(Director d, int CMAX)
	{
		List<Director> parziale = new ArrayList<>();
		best = new ArrayList<>();
		this.CMAX = CMAX;
		
		parziale.add(d);

		this.camminoMassimo(parziale);
		return best;
	}

	public void camminoMassimo(List<Director> parziale)
	{
		if (parziale.size() > best.size())
		{
			best = new ArrayList<>(parziale);
			return;
		}

		for (Director d : this.registi)
		{
			if (calcolaC(parziale) < CMAX && !parziale.contains(d))
			{
				parziale.add(d);
				this.camminoMassimo(parziale);
				parziale.remove(d);
			}
		}
	}

	public int calcolaC(List<Director> parziale)
	{
		int c = 0; 
		for (int i = 1; i < this.registi.size(); i++)
		{
			DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(i), parziale.get(i-1));
			System.out.println(e);
			if (e != null)
			{
				c += this.grafo.getEdgeWeight(e);
				System.out.println(this.grafo.getEdgeWeight(e));
			}
		}
		return c;
	}
}