package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Breadth-First search
public class BFS<T extends Node<T>> implements Search<T> {

	@Override
	public List<T> search(T start, Predicate<T> endPredicate) {
		// remember all possible paths
		Queue<List<T>> frontier = new LinkedList<>();
		List<T> path = new ArrayList<>();
		path.add(start);
		frontier.add(path);
		if (endPredicate.isTrueFor(start)) {
			// start is solution
			return path;
		}
		// remember all explored nodes
		Set<T> explored = new HashSet<>();
		while (! frontier.isEmpty()) {
			path = frontier.remove();
			T current = path.get(path.size() - 1);
			explored.add(current);
			// search iteratively in child paths
			for (T t : current.adjacent()) {
				if (! explored.contains(t)) {
					// search in this child path
					List<T> childPath = new ArrayList<>(path);
					childPath.add(t);
					if (endPredicate.isTrueFor(t)) {
						// solution found
						return childPath;
					}
					frontier.add(childPath);
				}
			}
		}
		// failure
		return null;
	}

}
