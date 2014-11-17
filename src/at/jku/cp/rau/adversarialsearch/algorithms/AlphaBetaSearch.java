package at.jku.cp.rau.adversarialsearch.algorithms;

import at.jku.cp.rau.adversarialsearch.AdversarialSearch;
import at.jku.cp.rau.adversarialsearch.predicates.SearchLimitingPredicate;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.Node;
import at.jku.cp.rau.utils.Pair;

public class AlphaBetaSearch<T extends Node<T>> implements AdversarialSearch <T> {

	private final SearchLimitingPredicate<T> slp;

	/**
	 * To limit the extent of the search, this implementation should honor a limiting predicate
	 * @param slp
	 */
	public AlphaBetaSearch(SearchLimitingPredicate<T> slp) {
		this.slp = slp;
	}

	public Pair<T, Double> search(T start, Function<T> evalFunction) {
		T max = null;
		double v = Double.NEGATIVE_INFINITY;
		for (T t : start.adjacent()) {
			double v2 = minValue(t, evalFunction, 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (v2 > v) {
				max = t;
				v = v2;
			}
		}
		return new Pair<>(max, v);
	}

	public double maxValue(T state, Function<T> evalFunction, int depth, double alpha, double beta) {
		if (state.isLeaf() || ! slp.expandFurther(depth, state)) {
			return evalFunction.value(state);
		}
		double v = Double.NEGATIVE_INFINITY;
		for (T t : state.adjacent()) {
			v = Math.max(v, minValue(t, evalFunction, depth + 1, alpha, beta));
			if (v >= beta) {
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	public double minValue(T state, Function<T> evalFunction, int depth, double alpha, double beta) {
		if (state.isLeaf() || ! slp.expandFurther(depth, state)) {
			return evalFunction.value(state);
		}
		double v = Double.POSITIVE_INFINITY;
		for (T t : state.adjacent()) {
			v = Math.min(v, maxValue(t, evalFunction, depth + 1, alpha, beta));
			if (v <= alpha) {
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}

}
