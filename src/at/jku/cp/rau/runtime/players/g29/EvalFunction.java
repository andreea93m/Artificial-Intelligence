package at.jku.cp.rau.runtime.players.g29;

import at.jku.cp.rau.game.endconditions.PointCollecting;
import at.jku.cp.rau.game.functions.Function;
import at.jku.cp.rau.search.nodes.ContainsBoard;

public class EvalFunction<T extends ContainsBoard> implements Function<T> {

	@Override
	public double value(T t) {
		PointCollecting pce = ((PointCollecting)t.getBoard().getEndCondition());
		int winner = pce.getWinner();
		int points = (pce.getScore(0) - pce.getScore(1));
		if(winner != -1) {
			if(winner == 0) {
				points += (10000 - t.getBoard().getTick());
			} else if (winner == 1) {
				points += -(10000 - t.getBoard().getTick());
			}
		}
		return points;
	}

}
