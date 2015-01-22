package at.jku.cp.rau.learning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.utils.Pair;

public class QLearner
{
	/**
	 *  The qmatrix is (very) sparse, that is why this is a hashmap.
	 */
	private Map<Pair<IBoard, Move>, Double> qmatrix;
	private int numEpisodes;

	/**
	 * If you use a randomness source, please use this one. it's passed
	 * in in the unit-tests...
	 */
	private Random random;
	private double discountFactor;


	public QLearner()
	{
		this(20001, 0.9);
	}

	public QLearner(int numEpisodes, double discountFactor)
	{
		this(new Random(), numEpisodes, discountFactor);
	}

	/**
	 * This QLearner has no learning rate, because in a deterministic environment,
	 * such as the Rainbows and Unicorns game, a learning rate of 1.0 is optimal.
	 * @param random the random number generator to be used
	 * @param numEpisodes the number of episodes for learning the model
	 * @param discountFactor this determines the importance of future rewards; in our
	 * test cases this plays a very minor role.
	 */
	public QLearner(Random random, int numEpisodes, double discountFactor)
	{
		if(discountFactor < 0d)
		{
			throw new RuntimeException("discountFactor must be greater than 0.0!");
		}

		this.random = random;
		this.numEpisodes = numEpisodes;
		this.discountFactor = discountFactor;
		this.qmatrix = new HashMap<>();
	}

	/**
	 * Starting with a given board, learn a model for behaviour
	 * @param board the board given
	 */
	public void learnQFunction(IBoard board)
	{
		List<Move> moves;
		Move move;
		IBoard before, after;
		Double currentReward;
		double immediateReward, bestReward;
		for (int e = 0; e < numEpisodes; e++) {
			before = board.copy();
			while (before.isRunning()) {
				moves = before.getPossibleMoves();
				move = moves.get(random.nextInt(moves.size()));
				after = before.copy();
				after.executeMove(move);
				if (after.isRunning()) {
					// still running
					immediateReward = 0.0;
				} else {
					if (after.getEndCondition().getWinner() == 0) {
						// has evaporated cloud
						immediateReward = 1.0;
					} else {
						// has gone sailing
						immediateReward = 0.0;
					}
				}
				synchronized (qmatrix) {
					if (after.isRunning()) {
						bestReward = 0.0;
						for (Move m : after.getPossibleMoves()) {
							currentReward = qmatrix.get(new Pair<>(after, m));
							if (currentReward != null && currentReward.doubleValue() > bestReward) {
								bestReward = currentReward.doubleValue();
							}
						}
					} else {
						bestReward = 0.0;
					}
					qmatrix.put(new Pair<>(before, move), immediateReward + discountFactor * bestReward);
				}
				before = after;
			}
		}
	}

	/**
	 * Based on the learned model, this function shall return the
	 * best move for a given board.
	 * @param board the board given
	 * @return a move
	 */
	public Move getMove(IBoard board)
	{
		Move bestMove = Move.STAY;
		double bestReward = Double.NEGATIVE_INFINITY;
		Double currentReward;
		for (Move m : board.getPossibleMoves()) {
			synchronized (qmatrix) {
				currentReward = qmatrix.get(new Pair<>(board, m));
			}
			if (currentReward != null && currentReward.doubleValue() > bestReward) {
				bestMove = m;
				bestReward = currentReward.doubleValue();
			}
		}
		return bestMove;
	}
}