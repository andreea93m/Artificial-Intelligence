package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.datastructures.StablePriorityQueue;
import at.jku.cp.rau.search.predicates.Predicate;
import at.jku.cp.rau.utils.Pair;

// Greedy Best-First Search
public class GBFS<T extends Node<T>> implements Search<T> {

	private Function<T> heuristic;
	
	public GBFS(Function<T> heuristic) {
		this.heuristic = heuristic;
	}
	
	@Override
	public List<T> search(T start, Predicate<T> endPredicate) {
		// remember all possible paths
		StablePriorityQueue<Double, List<T>> frontier = new StablePriorityQueue<>();
		Pair<Double, List<T>> path = new Pair<Double, List<T>>(new Double(heuristic.value(start)), new ArrayList<T>());
		path.s.add(start);
		frontier.add(path);
		// remember all explored nodes
		Set<T> explored = new HashSet<>();
		while (! frontier.isEmpty()) {
			path = frontier.remove();
			T current = path.s.get(path.s.size() - 1);
			if (endPredicate.isTrueFor(current)) {
				// solution found
				return path.s;
			}
			explored.add(current);
			for (T t : current.adjacent()) {
				if (! explored.contains(t)) {
					// search in this child path
					List<T> childPath = new ArrayList<>(path.s);
					childPath.add(t);
					frontier.add(new Pair<Double, List<T>>(new Double(this.heuristic.value(t)), childPath));
				}
			}
		}
		// failure
		return null;
	}
}
