package at.jku.cp.rau.runtime.players.g29;

import at.jku.cp.rau.adversarialsearch.predicates.SearchLimitingPredicate;
import at.jku.cp.rau.search.Node;

public class CutoffTest<T extends Node<T>> implements SearchLimitingPredicate<T> {
	
	private static final int MAX_DEPTH = 9;

	@Override
	public boolean expandFurther(int depth, T state) {
		return ! state.isLeaf() && depth < MAX_DEPTH;
	}

}
