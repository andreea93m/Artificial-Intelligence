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

// A* Search
public class ASTAR<T extends Node<T>> implements Search<T> {

	private Function<T> cost;
	private Function<T> heuristic;

	public ASTAR(Function<T> costs, Function<T> heuristic) {
		this.cost = costs;
		this.heuristic = heuristic;
	}

	@Override
	public List<T> search(T start, Predicate<T> endPredicate) {
		// remember all possible paths
		StablePriorityQueue<Double, Pair<Double, List<T>>> frontier = new StablePriorityQueue<>();
		Pair<Double, Pair<Double, List<T>>> path = new Pair<Double, Pair<Double, List<T>>>(new Double(cost.value(start) + heuristic.value(start)), new Pair<Double, List<T>>(new Double(cost.value(start)), new ArrayList<T>()));
		path.s.s.add(start);
		frontier.add(path);
		// remember all explored nodes
		Set<T> explored = new HashSet<>();
		while (! frontier.isEmpty()) {
			path = frontier.remove();
			T current = path.s.s.get(path.s.s.size() - 1);
			if (endPredicate.isTrueFor(current)) {
				// solution found
				return path.s.s;
			}
			explored.add(current);
			for (T t : current.adjacent()) {
				if (! explored.contains(t)) {
					// search in this child path
					List<T> childPath = new ArrayList<>(path.s.s);
					childPath.add(t);
					frontier.add(new Pair<Double, Pair<Double, List<T>>>(new Double(path.s.f + cost.value(t) + heuristic.value(t)), new Pair<Double, List<T>>(new Double(path.s.f + cost.value(t)), childPath)));
				}
			}
		}
		// failure
		return null;
	}

}
