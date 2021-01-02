import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

class MCTS {
	static class State{
		private Ilayout layout;
		private State father;
		private char player;
		private int visits;
		private int winScore;

		public State(Ilayout l, State n, char player) {
			this.layout = l;
			this.father = n;
			this.player = player;
			if(father != null){
				this.visits = father.visits + l.getVisits();
				this.winScore = father.winScore + l.getWins(player);
			}
			else {
				this.visits = 0;
				this.winScore = 0;
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

		public void incrementVisits() {
			this.visits++;
		}

		public int getWinScore() {
			return winScore;
		}

		public void setWinScore(int winScore) {
			this.winScore = winScore;
		}

		public void addScore(int score) {
			this.winScore += score;
		}
	}

	private int simulations;
	private char player;
	private char opponent;
	
	public MCTS(int simulations) {
		this.simulations = simulations;
	}

	public int getSimulations() {
		return this.simulations;
	}

	final private List<State> sucessores(State n){
		List<State> sucs = new ArrayList<>();
		List<Ilayout> children = n.layout.children();
		for(Ilayout e : children){
			if(n.father == null || !e.equals(n.father.layout)){
				State nn = new State(e, n, n.getPlayer() == 'X' ? 'O' : 'X');
				sucs.add(nn);
			}
		}
		return sucs;
	}

	private double getUctValue(State state) {
		double uctValue;

		if(state.getVisits() == 0) {
			uctValue = 1;
		}
		else {
			uctValue
                = (1.0 * state.getWinScore()) / (state.getVisits() * 1.0)
                + (Math.sqrt(2 * (Math.log(state.father.getVisits() * 1.0) / state.getVisits())));
		}

		Random r = new Random();
		uctValue += (r.nextDouble() / 10000000);
		return uctValue;
	}

	// private State selectPromisingState(State s) {
	// 	State state = s;
	// 	int parentVisits = state.getVisits();
	// 	return Collections.max(sucessores(state), Comparator.comparing(c -> uctValue(parentVisits, state.getWinScore(), state.getVisits())));
	// }

	private State selectPromisingState(State s) {
		State selectedState = s;
		double max = Integer.MIN_VALUE;
		List<State> children = sucessores(selectedState);

		for(State state : children) {
			double uctValue = getUctValue(state);

			if(uctValue > max) {
				max = uctValue;
				selectedState = state;
			}
		}

		return selectedState;
	}

	private char simulateRandomPlayout(State state) {
		TicTacToe t = new TicTacToe(state.toString());

		if(t.winCheck(this.opponent)) {
			state.father.setWinScore(Integer.MIN_VALUE);
			return this.opponent;
		}

		while(!t.gameOver()) {
			List<Integer> availablePositions = t.getEmptyPositions();
			int move = (int) (Math.random() * availablePositions.size());
			t.play(availablePositions.get(move));
		}

		// System.out.println(t.drawCheck() ? "GAME ENDED IN A DRAW" : t.winCheck(player) ? "PLAYER WON": "OPPONENT WON");
		// System.out.println();

		return t.drawCheck() ? '-' : (t.winCheck(player) ? player : opponent);
	}

	private void backPropagation(State stateToExplore, char playoutResult) {
		stateToExplore.incrementVisits();
		if(stateToExplore.getPlayer() == playoutResult)
			stateToExplore.addScore(10);
		if(stateToExplore.father != null)
			backPropagation(stateToExplore.father, playoutResult);
	}

	public Ilayout findNextMove(Ilayout layout, char player) {
		this.player = player;
		this.opponent = player == 'O' ? 'X' : 'O';
		State initialState = new State(layout, null, player);
		// List<State> currentSucs = sucessores(initialState);

		List<State> stateToExploreChildren = new ArrayList<>();

		for(int i = 0; i < this.getSimulations(); i++){

			// Phase 1 - Selection
			State promisingState = selectPromisingState(initialState);
			
			// Phase 2 - Expansion
			if(!((TicTacToe) promisingState.layout).gameOver()) {
				stateToExploreChildren = sucessores(promisingState);
			}
			else{
				return promisingState.layout;
			}

			// Phase 3 - Simulation
			State stateToExplore = promisingState;
			if(stateToExploreChildren.size() > 0) {
				stateToExplore = stateToExploreChildren.get((int) Math.random() * stateToExploreChildren.size());
			}
			char playoutResult = simulateRandomPlayout(stateToExplore);

			// Phase 4 - Update
			backPropagation(stateToExplore, playoutResult);
		}

		State winnerState = Collections.max(stateToExploreChildren, Comparator.comparing(c -> { return c.getVisits(); }));
		initialState = winnerState;
		return winnerState.father.layout;
	}
}