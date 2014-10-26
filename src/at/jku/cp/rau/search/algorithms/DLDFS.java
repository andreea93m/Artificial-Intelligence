package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.List;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Depth-Limited Depth-First Search
public class DLDFS<T extends Node<T>> implements Search<T> {

	private int limit;

	public DLDFS(int limit) {
		this.limit = limit;
	}

	@Override
	public List<T> search(T start, Predicate<T> endPredicate) {
		List<T> path = new ArrayList<>();
		path.add(start);
		return recursiveSearch(path, endPredicate);
	}

	private List<T> recursiveSearch(List<T> path, Predicate<T> endPredicate) {
		// get the current node
		T current = path.get(path.size() - 1);
		if(endPredicate.isTrueFor(current)) {
			// solution found
			return path;
		}
		if(path.size() - 1 == limit) {
			return null;
		}
		// search recursively in child paths
		for(T t : current.adjacent()) {
			if(! path.contains(t)) {
				// t still unexplored along this path
				path.add(t);
				List<T> resultPath = recursiveSearch(path, endPredicate);
				// if a solution is found stop the search
				if(resultPath != null) {
					return resultPath;
				} else {
					path.remove(t);
				}
			}
		}
		// no solution found in child paths, failure
		return null;
	}

}
