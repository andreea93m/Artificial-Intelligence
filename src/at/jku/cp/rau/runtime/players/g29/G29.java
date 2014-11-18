package at.jku.cp.rau.runtime.players.g29;

import at.jku.cp.rau.adversarialsearch.AdversarialSearch;
import at.jku.cp.rau.game.IBoard;
import at.jku.cp.rau.game.objects.Move;
import at.jku.cp.rau.runtime.players.Player;
import at.jku.cp.rau.runtime.players.PlayerInfo;
import at.jku.cp.rau.search.nodes.BNode;

public class G29 implements Player {

	private final AdversarialSearch<BNode> search = new AlphaBetaSearch<>(new CutoffTest<BNode>());
	private final EvalFunction<BNode> eval = new EvalFunction<>();

	@Override
	public Move getNextMove(PlayerInfo mappings, IBoard board) {
		return search.search(new BNode(board), eval).f.getMove();
	}

}
