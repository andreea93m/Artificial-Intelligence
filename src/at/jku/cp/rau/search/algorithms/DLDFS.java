package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.datastructures.StackWithFastContains;
import at.jku.cp.rau.search.predicates.Predicate;

// Depth-Limited Depth-First Search
public class DLDFS<T extends Node<T>> implements Search<T> {

	private int limit;
	
	public DLDFS(int limit) {
		this.limit = limit;
	}
	
	@Override
	public List<T> search(T start, Predicate<T> endPredicate) {
		// remember all possible paths
		StackWithFastContains<List<T>> frontier = new StackWithFastContains<>();
		List<T> path = new ArrayList<>();
		path.add(start);
		frontier.add(path);
		if (endPredicate.isTrueFor(start)) {
			// start is solution
			return path;
		}
		while (! frontier.isEmpty()) {
			path = frontier.pop();
			T current = path.get(path.size() - 1);
			// search iteratively in child paths
			List<T> adjacent = current.adjacent();
			// the following line is needed to accord with the JUnit tests
			Collections.reverse(adjacent);
			for (T t : adjacent) {
				if (! path.contains(t)) {
					// search in this child path
					List<T> childPath = new ArrayList<>(path);
					childPath.add(t);
					if (endPredicate.isTrueFor(t)) {
						// solution found
						return childPath;
					}
					if (limit != path.size() - 1) {
						frontier.add(childPath);
					}
				}
			}
		}
		// failure
		return null;
	}

}
