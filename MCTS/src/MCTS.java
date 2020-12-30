import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class MCTS {
	static class State{
		private Ilayout layout;
		private State father;
		private char player;
		private int visits;
		private int wins;

		public State(Ilayout l, State n, char player) {
			this.layout = l;
			this.father = n;
			this.player = player;
			if(((TicTacToe) layout).winCheck(player)){
				this.wins = 1;
			}
			if(father != null){
				this.visits = father.visits + l.getVisits();
			}
		}

		public String toString(){
			return layout.toString();
		}

		public char getPlayer() {
			return player;
		}

		public int getVisits() {
			return visits;
		}

		public int getWins() {
			return wins;
		}
	}

	private int level;
	private char opponent;
	
	public MCTS() {
		this.level = 3;
	}

	private int getMillisForCurrentLevel() {
		return 2 * (this.level - 1) + 1;
	}

	final private List<State> sucessores(State n){
		List<State> sucs = new ArrayList<>();
		List<Ilayout> children = n.layout.children();
		for(Ilayout e : children){
			if(n.father == null || !e.equals(n.father.layout)){
				State nn = new State(e, n, this.opponent);
				sucs.add(nn);
			}
		}
		return sucs;
	}

	private double uctValue(int totalVisits, double stateWinScore, int stateVisits) {
		if(stateVisits == 0) {
			return Integer.MAX_VALUE;
		}
		return (stateWinScore / (double) stateVisits) + 1.41 * Math.sqrt(Math.log(totalVisits) / (double) stateVisits);
	}

	private State selectPromisingState(State s, List<State> sucs) {
		State state = s;
		int parentVisits = state.getVisits();
		//System.out.println(sucs.toString());
		return sucs.size() > 0 ? s : Collections.max(sucs, Comparator.comparing(c -> uctValue(parentVisits, state.getWins(), state.getVisits())));
	}

	private char simulateRandomPlayout(State state) {
		TicTacToe t = new TicTacToe(state.toString());

		if(t.winCheck(opponent)) {
			return opponent;
		}

		while(!t.gameOver()) {
			List<Integer> availablePositions = t.getEmptyPositions();
			int move = (int) (Math.random() * availablePositions.size());
			t.play(availablePositions.get(move));
		}

		return t.winCheck('X') ? 'X' : 'O';
	}

	private void backPropagation(State stateToExplore, char player) {
		State tempState = stateToExplore;

		while(tempState != null) {
			tempState.visits++;
			if(tempState.getPlayer() == player)
				tempState.wins++;
			tempState = tempState.father;
		}
	}

	public Ilayout findNextMove(Ilayout layout, char player) {
		long start = System.currentTimeMillis();
		long end = start + 60 * getMillisForCurrentLevel();

		opponent = player == 'O' ? 'X' : 'O';
		State initialState = new State(layout, null, opponent);
		List<State> currentSucs = sucessores(initialState);

		while(System.currentTimeMillis() < end) {

			//System.out.println(counter);
			// Phase 1 - Selection
			State promisingState = selectPromisingState(initialState, currentSucs);

			// Phase 2 - Expansion
			if(!((TicTacToe) layout).gameOver())
				currentSucs = sucessores(promisingState);

			// Phase 3 - Simulation
			State stateToExplore = promisingState;
			if(currentSucs.size() > 0) {
				stateToExplore = currentSucs.get((int) Math.random() * currentSucs.size());
			}
			char playoutResult = simulateRandomPlayout(stateToExplore);

			// Phase 4 - Update
			backPropagation(stateToExplore, playoutResult);
		}

		State winnerState = Collections.max(currentSucs, Comparator.comparing(c -> { return c.getVisits(); }));
		initialState = winnerState;
		return winnerState.layout;
	}
}