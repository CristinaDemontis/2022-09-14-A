package it.polito.tdp.itunes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private Graph<Album, DefaultEdge> grafo; 
	private List<Album> vertici; 
	private ItunesDAO dao;
	private Map<Integer, Album> mappaVertici; 
	
	
	public Model() {
		super();
	} 
	
	public void creaGrafo(double durata) {
		this.mappaVertici = new HashMap<>(); 
		this.dao = new ItunesDAO(); 
		this.grafo = new SimpleGraph<>(DefaultEdge.class); 
		
		this.vertici = this.dao.getAllAlbumsBYDurata(durata); 
		for(Album a: vertici) {
			mappaVertici.put(a.getAlbumId(), a); 
		}
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		List<Arco> archi = this.dao.getArchi(this.mappaVertici);
		
		for(Arco a: archi ) {
			if(vertici.contains(a.getA1()) && vertici.contains(a.getA2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2());
			}
		}
	
	}
	
	public Integer getNumComponeneteConnessa (Album a) {
		if(this.grafo != null) {
			ConnectivityInspector<Album, DefaultEdge > ci = new ConnectivityInspector<>(this.grafo);
			return ci.connectedSetOf(a).size();  
		}else {
			return null; 
		}
	}
	
	public Double getPesoTotComponenteConnessa(Album a) {
		Double pesoTot = 0.0; 
		if(this.grafo != null) {
			ConnectivityInspector<Album, DefaultEdge > ci = new ConnectivityInspector<>(this.grafo);
			Set<Album> componente = ci.connectedSetOf(a);
			for(Album aa: componente) {
				pesoTot += aa.getDurata(); 
			}
			
			return pesoTot; 
		}else {
			return null; 
		}
	}
	
	
	public int getnVertici() {
		return this.grafo.vertexSet().size() ;
	}
	
	public int getnArchi() {
		return this.grafo.edgeSet().size() ;
	}

	public List<Album> getVertici() {
		return vertici;
	}

	public ItunesDAO getDao() {
		return dao;
	}

	public Map<Integer, Album> getMappaVertici() {
		return mappaVertici;
	}
	
	public ConnectivityInspector<Album, DefaultEdge> getComponenteConnessa(){
		if(this.grafo != null) {
			ConnectivityInspector<Album, DefaultEdge > ci = new ConnectivityInspector<>(this.grafo);
			 
			return ci;  
		}else {
			return null; 
		}
	}
	
}
