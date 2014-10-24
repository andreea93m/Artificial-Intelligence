package at.jku.cp.rau.search.algorithms;

import java.util.ArrayList;
import java.util.List;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Depth-Limited Depth-First Search
public class DLDFS<T extends Node<T>> implements Search<T> {

	private int limit;
    private List<T> path = new ArrayList<T>();
    private Predicate<T> endPredicate;
	
	public DLDFS(int limit) {
		this.limit = limit;
	}
	
	@Override
	public List<T> search(T start, Predicate<T> endPredicate) {
        this.endPredicate = endPredicate;

        path.add(start);

        return (dfs() ? path : null);
	}

    private boolean dfs() {
        // Get the current node
        T node = path.get(path.size() - 1);

        if(endPredicate.isTrueFor(node)) {
            return true;
        }

        if(path.size() - 1 == limit) {
            return false;
        }

        for(T neighbour : node.adjacent()) {
            if(!path.contains(neighbour)) {
                path.add(neighbour);

                // If a path was found stop the search
                if(dfs()) {
                    return true;
                } else {
                    path.remove(neighbour);
                }
            }
        }

        return false;
    }
}
