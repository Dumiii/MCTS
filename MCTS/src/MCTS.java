import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class responsible for implementing the Monte Carlo Tree Search algorithm
 */
class MCTS {

	/**
	 * State class which the tree is made out of
	 * holds the config of the game and information for the MCTS algo
	 */
	static class State{

		/**
		 * Game config
		 */
		private Ilayout layout;
		
		/**
		 * State parent
		 */
		private State father;

		/**
		 * Indicates which player this State belongs to
		 */
		private char player;

		/**
		 * amount of visits this instance has had
		 */
		private int visits;

		/**
		 * current win score of this state
		 */
		private int winScore;

		/**
		 * children of this state instance
		 */
		private List<State> childArray;

		/**
		 * default constructor
		 * sets the layout to an empty board
		 * sets child array to empty arraylist
		 */
		public State() {
			this.setLayout(new TicTacToe());
			this.setChildArray(new ArrayList<>());
		}

		/**
		 * Constructor that recives a game config
		 * Sets child array to empty arraylist
		 * @param layout Ilayout that reprensents the config of the game
		 */
		public State(Ilayout layout) {
			this.setLayout(layout);
			this.setChildArray(new ArrayList<>());
		}

		/**
		 * Constructor that recieves a game config, a father, and a child array
		 * @param layout Ilayout that reprensents the config of the game
		 * @param father State that will be the father of this instance
		 * @param childArray List of states that will be this state's children
		 */
		public State(Ilayout layout, State father, List<State> childArray) {
			this.setLayout(layout);
			this.setFather(father);
			this.setChildArray(childArray);
		}

		/**
		 * Consturctor that takes a state and copies it
		 * effectively clones the argument
		 * @param state the State that will have its properties copied
		 */
		public State(State state) {
			this.setChildArray(new ArrayList<>());
			this.layout = state.getLayout();
			if(state.getFather() != null)
				this.father = state.getFather();
			List<State> childArray = state.getChildArray();
			for(State child : childArray) {
				this.childArray.add(new State(child));
			}
		}

		/**
		 * uses the layout's toString method
		 * @return the layout as a string
		 */
		public String toString(){
			return layout.toString();
		}

		/**
		 * getter for the layout
		 * @return this instance's layout
		 */
		public Ilayout getLayout() {
			return layout;
		}

		/**
		 * setter for the layout
		 * @param layout takes a Ilayout game config
		 */
		public void setLayout(Ilayout layout) {
			this.layout = layout;
		}

		/**
		 * getter for this state's father
		 * @return State father
		 */
		public State getFather() {
			return father;
		}

		/**
		 * setter for this state's father
		 * @param father State to be set as father
		 */
		public void setFather(State father) {
			this.father = father;
		}

		/**
		 * getter for the list of children
		 * @return this state's list of children
		 */
		public List<State> getChildArray() {
			return childArray;
		}

		/**
		 * setter for the list of children
		 * @param childArray list of children
		 */
		public void setChildArray(List<State> childArray) {
			this.childArray = childArray;
		}

		/**
		 * getter for player
		 * @return player as character
		 */
		public char getPlayer() {
			return player;
		}

		/**
		 * setter for player
		 * @param player a char representing the player (either 'O' or 'X')
		 */
		public void setPlayer(char player) {
			if(layout.isValidPlayer(player))
				this.player = player;
		}

		/**
		 * @return char representing the opponent of the player of the current player
		 */
		public char getOpponent() {
			return player == 'X' ? 'O' : 'X';
		}

		/**
		 * getter for the visits
		 * @return an int representing the amount of visits this State has had
		 */
		public int getVisits() {
			return visits;
		}

		/**
		 * increments the visits for when this State is visitied in the algorithm
		 */
		public void incrementVisits() {
			this.visits++;
		}
		
		/**
		 * getter for the win score
		 * @return an int representing the win score of this node
		 */
		public int getWinScore() {
			return winScore;
		}

		/**
		 * setter for win score
		 * @param winScore and int representing the new win score of this state
		 */
		public void setWinScore(int winScore) {
			this.winScore = winScore;
		}

		/**
		 * part of the backpropagation phase
		 * updates this State's score if it isn't a losing state
		 * @param score an int representing the score
		 */
		public void addScore(int score) {
			if(this.winScore != Integer.MIN_VALUE)
				this.winScore += score;
		}

		/**
		 * generates the successors of this state
		 * @return a list with the successors
		 */
		public List<State> sucessores() {
			List<State> sucs = new ArrayList<>();
			List<Ilayout> children = this.layout.children();
			for(Ilayout e : children) {
				State nn = new State(e);
				sucs.add(nn);
			}
			return sucs;
		}

		/**
		 * applies a random factor to chose a child
		 * @return a random child state
		 */
		public State getRandomChildState() {
			int possibleMoves =  this.childArray.size();
			int randomSelect = (int) (Math.random() * possibleMoves);
			return this.childArray.get(randomSelect);
		}

		/**
		 * chooses the child with the max score
		 * @return a State which is the child with the highest score
		 */
		public State getChildWithMaxScore() {
			return Collections.max(this.childArray, Comparator.comparing(c -> { return c.getVisits(); }));
		}
	}

	/**
	 * score value to be added to a winning score
	 */
	private final int WIN_SCORE = 10;

	/**
	 * factor to be used in the uct value function
	 */
	private double EXPLORATION_FACTOR = 4 * Math.sqrt(2);

	/**
	 * number of simulations to be made for each turn
	 */
	private int simulations;

	/**
	 * a char that represents the player that the mcts algorithm is benefiting
	 */
	private char player;

	/**
	 * a char that represents the player that the mcts algorithm is fighting against
	 */
	private char opponent;
	
	/**
	 * constructor that sets the simulations attribute
	 * @param simulations int to be set as the simulations attribute
	 */
	public MCTS(int simulations) {
		this.simulations = simulations;
	}

	/**
	 * getter for simulations
	 */
	public int getSimulations() {
		return simulations;
	}

	/**
	 * method that determins the upper confidence bound for the game tree
	 * @param totalVisits parent state's visits
	 * @param stateWinScore win score of current state
	 * @param stateVisits visits of current state
	 * @return double representing the uctValue
	 */
	private double uctValue(int totalVisits, double stateWinScore, int stateVisits) {
		if(stateVisits == 0) {
			return Integer.MAX_VALUE;
		}
		return (stateWinScore / (double) stateVisits) + EXPLORATION_FACTOR * Math.sqrt(Math.log(totalVisits) / (double) stateVisits);
	}

	/**
	 * Selects the child with the highest uctValue
	 * @param state State whose children will be compared to find the highest uctValue
	 * @return the child with the highest uct value
	 */
	private State bestStateUCT(State state) {
		int parentVisits = state.getVisits();
		return Collections.max(state.getChildArray(), Comparator.comparing(c -> uctValue(parentVisits, c.getWinScore(), c.getVisits())));
	}

	/**
	 * selects the most promising state, starting from the root state
	 * @param rootState the state from which to begin selection
	 * @return a State in which the game is over
	 */
	private State selectPromisingState(State rootState) {
		State state = rootState;
		while(state.getChildArray().size() != 0)
			state = bestStateUCT(state);
		return state;
	}

	/**
	 * expands the given state by appending a list of possible states
	 * @param state the State to expand
	 */
	private void expandState(State state) {
		List<State> possibleStates = state.sucessores();
		for(State s : possibleStates) {
			State newState = new State(s);
			newState.setFather(state);
			newState.setPlayer(state.getOpponent());
			state.getChildArray().add(newState);
		}
	}

	/**
	 * propagates the rewards back up the tree to update all state statistics
	 * @param stateToExplore a state that has the rewards that will be given to the father
	 * @param playoutResult simulation result ['O', 'X', '-'], '-' represents draw
	 */
	private void backPropagation(State stateToExplore, char playoutResult) {
		State tempState = stateToExplore;
		while(tempState != null) {
			tempState.incrementVisits();
			if(tempState.getPlayer() == playoutResult)
				tempState.addScore(WIN_SCORE);
			tempState = tempState.getFather();
		}
	}

	/**
	 * simulates a random playout from a given state
	 * @param state the state from which to start the simulation
	 * @return char representing the player ('O' or 'X') that won the simulation or '-' if ended in a draw
	 */
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

	/**
	 * finds the best move from a given layout and player
	 * @param layout the game config in Ilayout
	 * @param player char representing the player that is about to play
	 * @return a Ilayout representing the move
	 */
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
			if(!(promisingState.getLayout().gameOver()))
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