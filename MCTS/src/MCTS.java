import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class MCTS {

	static class Tree {
		State root;

		public Tree() {
			root = new State();
		}

		public Tree(State root) {
			this.root = root;
		}

		public State getRoot() {
			return root;
		}

		public void setRoot(State root) {
			this.root = root;
		}

		public void addChild(State parent, State child) {
			parent.getChildArray().add(child);
		}
	}

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
	private int simulations;
	private char player;
	private char opponent;
	
	public MCTS(int simulations) {
		this.simulations = simulations;
	}

	public int getSimulations() {
		return 5000;
	}
	

	public double uctValue(int totalVisits, double stateWinScore, int stateVisits) {
		if(stateVisits == 0) {
			return Integer.MAX_VALUE;
		}
		return (stateWinScore / (double) stateVisits) + 5.8 * Math.sqrt(Math.log(totalVisits) / (double) stateVisits);
	}

	private State bestStateUCT(State state) {
		int parentVisits = state.getVisits();
		return Collections.max(state.getChildArray(), Comparator.comparing(c -> uctValue(parentVisits, c.getWinScore(), c.getVisits())));
	}

	// private State selectPromisingState(State s) {
	// 	State selectedState = s;
	// 	double max = Integer.MIN_VALUE;

	// 	for(State state : sucessores(s)) {
	// 		double uctValue = getUctValue(state);

	// 		if(uctValue > max) {
	// 			max = uctValue;
	// 			selectedState = state;
	// 		}
	// 	}

	// 	return selectedState;
	// }

	private State selectPromisingState(State rootState) {
		State state = rootState;
		while(state.getChildArray().size() != 0)
			state = bestStateUCT(state);
		return state;
	}

	private void expandState(State state) {
		List<State> possibleStates = state.sucessores();
		possibleStates.forEach(s -> {
			State newState = new State(s);
			newState.setFather(state);
			newState.setPlayer(state.getOpponent());
			state.getChildArray().add(newState);
		});
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

		// System.out.println(t.drawCheck() ? "GAME ENDED IN A DRAW" : t.winCheck(player) ? "PLAYER WON": "OPPONENT WON");
		// System.out.println();

		return t.drawCheck() ? '-' : (t.winCheck(player) ? player : opponent);
	}

	// private void backPropagation(State stateToExplore, char playoutResult) {
	// 	stateToExplore.incrementVisits();
	// 	// Map<Character, Integer> cona =  ((TicTacToe) stateToExplore.layout).countScore();
	// 	// if(cona.get('O') + cona.get('X') == 2)
	// 		// System.out.println(stateToExplore.getVisits());
	// 	char opponent = playoutResult == '-' ? '-' : (playoutResult == 'X' ? 'O' : 'X');
	// 	if(stateToExplore.getPlayer() == playoutResult)
	// 		stateToExplore.addScore(1);
	// 	else if(stateToExplore.getPlayer() == opponent)
	// 		stateToExplore.addScore(-1);
	// 	else
	// 		stateToExplore.addScore(0);
	// 	if(stateToExplore.father != null)
	// 		backPropagation(stateToExplore.father, playoutResult);
	// }

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

			// System.out.println(promisingState.toString());
			
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

		// List<State> whatever = sucessores(initialState);

		// for(State state : stateToExploreChildren)
		// 	System.out.println(state.father.father.getVisits());


		// State winnerState = Collections.max(stateToExploreChildren, Comparator.comparing(c -> { return c.getVisits(); }));
		// initialState = winnerState;
		State winnerState = initialState.getChildWithMaxScore();
// 		for(State state : initialState.getChildArray()) {
// 			System.out.println(state.getVisits());
// 			// System.out.println(state.toString());
// 			// System.out.println();
// 			// System.out.println();
// 			// System.out.println();
// // 
// 		}
		tree.setRoot(winnerState);
		return winnerState.getLayout();
	}
}