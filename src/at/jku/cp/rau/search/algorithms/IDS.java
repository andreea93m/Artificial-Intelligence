package at.jku.cp.rau.search.algorithms;

import java.util.List;

import at.jku.cp.rau.search.Search;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.search.predicates.Predicate;

// Iterative Deepening Search
public class IDS<T extends Node<T>> implements Search<T>
{
	private int limit;

	public IDS(int limit)
	{
		this.limit = limit;
	}

	@Override
	public List<T> search(T start, Predicate<T> endPredicate)
	{
		int l = 0;
		while (l < limit) {
			// search using depth-limited depth-first search
			DLDFS<T> searcher = new DLDFS<>(l);
			List<T> path = searcher.search(start, endPredicate);
			if (path != null) {
				// solution found
				return path;
			}
			l++;
		}
		// failure
		return null;
	}

}
