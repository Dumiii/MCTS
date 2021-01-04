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
		private int winScore;
		private List<State> childArray;

		public State() {
			this.layout = new TicTacToe();
			childArray = new ArrayList<>();
		}

		public State(Ilayout layout) {
			this.layout = layout;
			childArray = new ArrayList<>();
		}

		public State(Ilayout layout, State father, List<State> childArray) {
			this.layout = layout;
			this.father = father;
			this.childArray = childArray;
		}

		public State(State state) {
			this.childArray = new ArrayList<>();
			this.layout = state.getLayout();
			if(state.getFather() != null)
				this.father = state.getFather();
			List<State> childArray = state.getChildArray();
			for(State child : childArray) {
				this.childArray.add(new State(child));
			}
		}

		public String toString(){
			return layout.toString();
		}

		public Ilayout getLayout() {
			return layout;
		}

		public void setLayout(Ilayout layout) {
			this.layout = layout;
		}

		public State getFather() {
			return father;
		}

		public void setFather(State father) {
			this.father = father;
		}

		public List<State> getChildArray() {
			return childArray;
		}

		public char getPlayer() {
			return player;
		}

		public void setPlayer(char player) {
			this.player = player;
		}

		public char getOpponent() {
			return player == 'X' ? 'O' : 'X';
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
			if(this.winScore != Integer.MIN_VALUE)
				this.winScore += score;
		}

		public List<State> sucessores() {
			List<State> sucs = new ArrayList<>();
			List<Ilayout> children = this.layout.children();
			for(Ilayout e : children) {
				State nn = new State(e);
				sucs.add(nn);
			}
			return sucs;
		}

		public State getRandomChildState() {
			int possibleMoves =  this.childArray.size();
			int randomSelect = (int) (Math.random() * possibleMoves);
			return this.childArray.get(randomSelect);
		}

		public State getChildWithMaxScore() {
			return Collections.max(this.childArray, Comparator.comparing(c -> { return c.getVisits(); }));
		}
	}

	private final int WIN_SCORE = 10;
	private final double EXPLORATION_FACTOR = 4 * Math.sqrt(2);
	private int simulations;
	private char player;
	private char opponent;
	
	public MCTS(int simulations) {
		this.simulations = simulations;
	}

	public int getSimulations() {
		return simulations;
	}
	

	private double uctValue(int totalVisits, double stateWinScore, int stateVisits) {
		if(stateVisits == 0) {
			return Integer.MAX_VALUE;
		}
		return (stateWinScore / (double) stateVisits) + EXPLORATION_FACTOR * Math.sqrt(Math.log(totalVisits) / (double) stateVisits);
	}

	private State bestStateUCT(State state) {
		int parentVisits = state.getVisits();
		return Collections.max(state.getChildArray(), Comparator.comparing(c -> uctValue(parentVisits, c.getWinScore(), c.getVisits())));
	}

	private State selectPromisingState(State rootState) {
		State state = rootState;
		while(state.getChildArray().size() != 0)
			state = bestStateUCT(state);
		return state;
	}

	private void expandState(State state) {
		List<State> possibleStates = state.sucessores();
		for(State s : possibleStates) {
			State newState = new State(s);
			newState.setFather(state);
			newState.setPlayer(state.getOpponent());
			state.getChildArray().add(newState);
		}
	}

	private void backPropagation(State stateToExplore, char playoutResult) {
		State tempState = stateToExplore;
		while(tempState != null) {
			tempState.incrementVisits();
			if(tempState.getPlayer() == playoutResult)
				tempState.addScore(WIN_SCORE);
			tempState = tempState.getFather();
		}
	}

	private char simulateRandomPlayout(State state) {
		TicTacToe t = new TicTacToe(state.toString());

		if(t.winCheck(this.opponent)){
			state.getFather().setWinScore(Integer.MIN_VALUE);
			return this.opponent;
		}
		
		while(!t.gameOver()) {
			List<Integer> availablePositions = t.getEmptyPositions();
			int move = (int) (Math.random() * availablePositions.size());
			t.play(availablePositions.get(move));
		}

		return t.drawCheck() ? '-' : (t.winCheck(player) ? player : opponent);
	}

	public Ilayout findNextMove(Ilayout layout, char player) {
		this.player = player;
		this.opponent = player == 'O' ? 'X' : 'O';
		
		Tree tree = new Tree();
		State initialState = tree.getRoot();
		initialState.setLayout(layout);
		initialState.setPlayer(opponent);

		for(int i = 0; i < this.getSimulations(); i++){

			// Phase 1 - Selection
			State promisingState = selectPromisingState(initialState);
			
			// Phase 2 - Expansion
			if(!((TicTacToe) promisingState.getLayout()).gameOver())
				expandState(promisingState);

			// Phase 3 - Simulation
			State stateToExplore = promisingState;
			if(promisingState.getChildArray().size() > 0) {
				stateToExplore = promisingState.getRandomChildState();
			}
			char playoutResult = simulateRandomPlayout(stateToExplore);

			// Phase 4 - Update
			backPropagation(stateToExplore, playoutResult);
		}

		State winnerState = initialState.getChildWithMaxScore();
		tree.setRoot(winnerState);
		return winnerState.getLayout();
	}
}