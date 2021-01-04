import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Tic-Tac-Toe game
 * dim 3*3
 * ASSUME p1 ('O') starts first
 */

public class TicTacToe implements Ilayout, Cloneable {

	/**
	 * Dimension of the board (for TTT is always 3)
	 */
	private final int dim = 3;

	/**
	 * The board
	 */
	private Character[][] board = new Character[dim][dim];

	/**
	 * char emptyCell represents the char in the free cells
	 * char p1 is the player with the 'O' cell here we always assume p1 starts first
	 * char p2 is the player with the 'X' cell
	 */
	private final char emptyCell = '-';
	private final char p1 = 'X';
	private final char p2 = 'O';

	/**
	 * char turn is the current player turn in this instance
	 * score counts the amount of 'X's and 'O's in the board as to know who's turn it is (always assumes player 'O' went first) and can be used to know if there was a draw (if the sum is dim*dim and no one won, it's a draw)
	 */
	private char turn = p1;
	private Map<Character, Integer> score;

	/**
	 * holds the cell number in which the last move was done
	 * exists in [0, dim*dim)
	 */
	public int lastPlayed;
	/**
	 * default constructor which starts a game from the begining aka with an empty board
	 */
	TicTacToe(){
		init();
		this.lastPlayed = 0;
	}

	/**
	 * Constructor that accepts a string as a board state (mostly for testing)
	 * @param str - board config in string (eg.: "X-O\nOXO\nXO-")
	 * @throws IllegalArgumentException - if the parameter doesn't fit the example above
	 */
	TicTacToe(String str) throws IllegalArgumentException {
		String[] lines = str.split("\n");
		if (lines.length != dim)
			throw new IllegalArgumentException("wrong size of argument (rows): "+lines.length);
		init();
		for(int y = 0; y < lines.length; y++){
			if (lines[y].length() != dim)
				throw new IllegalArgumentException("size of argument (columns): "+lines[y].length());
			for(int x = 0; x < lines[y].length(); x++){
				if(!isValidCharacter(lines[y].charAt(x)))
					throw new IllegalArgumentException("Invalid character in argument: "+lines[y].charAt(x));
				if(isValidPlayer(lines[y].charAt(x))){
					play(x, y, lines[y].charAt(x));
					this.score.put(lines[y].charAt(x), this.score.get(lines[y].charAt(x)));
				}
			}
		}
		this.lastPlayed = 0;
		determineTurn();
	}

	/**
	 * populates the score map by counting the number of 'X's and 'O's in the board
	 * @return a map with the keys 'O' and 'X' and the respective amount of each in the board
	 */
	public Map<Character, Integer> countScore(){
		Map<Character, Integer> result = new HashMap<>(2);
		result.put(p1, 0);
		result.put(p2, 0);
		for(int i = 0; i < dim*dim; i++){
			char cell = this.get(i%dim, i/dim);
			if(cell != emptyCell)
				result.put(cell, result.get(cell)+1);
		}
		return result;
	}

	/**
	 * initializes the board by placing the emptyCell property into all cells and syncs the local variabls
	 */
	private void init(){
		for(int i = 0; i < this.dim*this.dim; i++)
			this.set(i%this.dim, i/this.dim, this.emptyCell);
		determineTurn();
	}

	/**
	 * checks if the coordinate x is within the board (literally says wether x is > 0 and < dim)
	 * @param x coordinate to eval
	 * @return true if x exists in [0, dim), false otherwise
	 */
	private boolean inBounds(int x){
		return x >= 0 && x < dim;
	}

	/**
	 * checks if the char is a player (mostly used to see if the cell is already ocupied by a player)
	 * @param c - char to eval
	 * @return true if c is 'O' or 'X'
	 */
	private boolean isValidPlayer(char c){
		return c == p1 || c == p2;
	}

	/**
	 * checks if char is a valid character in the context of this game
	 * @param c - char to eval
	 * @return true if c is 'O', 'X', or '-'
	 */
	private boolean isValidCharacter(char c){
		return isValidPlayer(c) || c == emptyCell;
	}

	/**
	 * gets the char in the board at x and y
	 * @param x int x coordinate
	 * @param y int y coordinate
	 * @return char in the x,y on the board
	 * @throws IllegalArgumentException if x or y aren't in bounds aka [0, dim]
	 */
	public char get(int x, int y) throws IllegalArgumentException {
		if(!inBounds(x) || !inBounds(y))
			throw new IllegalArgumentException(x+" or "+y+" are not in bounds");
		return this.board[y][x];
	}
	
	/**
	 * gets the char in the nth space in the board
	 * @param i space on the board i.e.: i=3 is the top-right space in a 3x3 board
	 * @return the output of the other get function with the arguments: x = i%dim and y = i/dim
	 * @throws IllegalArgumentException if doesn't belong in [0, dim*dim)
	 */
	public char get(int i) throws IllegalArgumentException{
		return get(i%dim, i/dim);
	}

	/**
	 * sets the char in the x,y on the board to c
	 * @pre x and y cant be out of bounds, c cant be different than X or O, cell cant be occupied (aka what says in the @throws, what even the point of this?)
	 * @param x int x coordinate
	 * @param y int y coordinate
	 * @param c char to set on the board
	 * @throws IllegalArgumentException if x or y aren't in [0, dim), c isn't an 'X', 'O', or '-'(emptyCell)
	 */
	private void set(int x, int y, char c) throws IllegalArgumentException {
		if(!inBounds(x) || !inBounds(y) || !isValidCharacter(c))
			throw new IllegalArgumentException(x+" or "+y+" are not in bounds or char '"+c+"' isn't a valid character, valid characters: "+p1+", "+p2+" and "+emptyCell);
		this.board[y][x] = c;
	}

	/**
	 * uses function "set" to set the char "player" onto the board if it is a valid player and the cell inst occupied by a palyer
	 * updates the score after playing
	 * @param x int x coordinate
	 * @param y int y coordinate
	 * @param player char representing the player to play this turn
	 * @throws IllegalArgumentException if char player isn't a valid player or if the cell is occupied by player and if arguments aren't accepted by set(x, y)
	 */
	public void play(int x, int y, char player) throws IllegalArgumentException{
		if(!isValidPlayer(player))
			throw new IllegalArgumentException("char '"+player+"' isn't a valid player, valid players: "+p1+" and "+p2);
		if(isValidPlayer(this.get(x, y)))
			throw new IllegalArgumentException("this cell is already occupied by "+this.get(x, y));
		this.set(x, y, player);
		this.lastPlayed = y*dim + x;
		score.put(player, score.get(player)+1);
		determineTurn();
	}

	/**
	 * uses function play(x, y) and then updates the turn
	 * @param x int x coordinate
	 * @param y int y coordinate
	 * @throws IllegalArgumentException if the arguments aren't accepted by play(x, y, player)
	 */
	public void play(int x, int y) throws IllegalArgumentException{
		this.play(x, y, turn);
		determineTurn();
	}

	/**
	 * gets the turn
	 * @return the local turn variable
	 */
	public char getTurn(){
		return this.turn;
	}

	public int getLastPlayed() {
		return lastPlayed;
	}

	/**
	 * uses function playTurn(x, y) but instead of x and y it's a single argument that represents a space in the board
	 * @param i int i is split into: i%dim which represents x and i/dim which represents y in the function playTurn(x, y)
	 */
	public void play(int i){
		this.play(i%dim, i/dim);
	}

	/**
	 * spits out a list with all derivable configs from the current config
	 * @return list of all possible layouts from this config
	 */
	public List<Ilayout> children(){
		List<Ilayout> result = new LinkedList<>();
		for(int i=0; i<dim*dim; i++){		//TODO check if works properly
			if(this.get(i%dim, i/dim) == emptyCell){
				TicTacToe child = (TicTacToe) this.stringClone();
				child.play(i);
				result.add(child);
			}
		}
		return result;
	}

	/**
	 * checks if game is over and if argument "player" won the game
	 * @param player char that represents the player
	 * @return true if player won aka any line with size "dim" is filled with the player's symbol ('O' or 'X'), false otherwise
	 * @throws IllegalArgumentException if char "player" isn't a valid player aka isn't a 'O' or 'X'
	 */
	public boolean winCheck(char player) throws IllegalArgumentException{ //TODO make better plese
		if(!isValidPlayer(player))
			throw new IllegalArgumentException("char "+player+" isn't a valid player, valid players: "+p1+" and "+p2);
		
		int[][] counters = {{0, 0, 0}, {0, 0, 0}};
		int[] diags = {0, 0};
			
		for(int y = 0; y < dim; y++){ //SCUFFED 
			for(int x = 0; x < dim; x++){
				diags[0] += this.get(x, y) == player && x == y ? 1 : 0;	//diagonal from top-left to bot-right
				diags[1] += this.get(x, y) == player && x + y == dim-1 ? 1 : 0;	//diagonal from top-right to bot-left
				counters[0][x] += this.get(x, y) == player ? 1 : 0;
				counters[1][y] += this.get(x, y) == player ? 1 : 0;
			}
		}
		// return Arrays.asList(counters).contains(dim); //THIS DOESNT WORK WHY FUUUUUUUU
		for(int i=0; i< counters.length; i++){
			for(int j=0; j<counters[i].length; j++){
				if(counters[i][j] >= dim)
					return true;
			}
		}
		for(int i=0; i< diags.length; i++){
			if(diags[i] >= dim)
				return true;
		}
		return false;
	}

	/**
	 * checks if the game is over by draw
	 * @return true if it's a draw aka gameboard has no cells with the "emptyCell" char, false otherwise
	 */
	public boolean drawCheck(){
		for(int i = 0; i < dim*dim; i++){
			if(this.get(i%dim, i/dim) == emptyCell)
				return false;
		}
		return !winCheck('O') && ! winCheck('X');
	}

	/**
	 * same as drawCheck but uses the local score map which might be more elegant in the fact that holds more information
	 * @return true if the amount of times p1 and p2 played is equals to the amount of spaces in the board and if there are no winners, all of this means that it is a draw, else returns false
	 */
	public boolean drawCheckWithMap(){
		return score.get(p1) + score.get(p2) == dim*dim && !winCheck(p1) && !winCheck(p2);
	}

	/**
	 * updates the local variable "turn"
	 * p1 = amount of times player 'O' played,
	 * p2 = amount of times player 'X' played,
	 * if p1 - p2 == 0, it's player 'O's turn, else (can only be 0 or 1) it's player 'X's turn
	 */
	private void determineTurn(){	//assuming p1 aka 'O' goes first
		this.score = this.countScore();
		this.turn = score.get(p1)-score.get(p2) == 0 ? p1 : p2;
	}

	//TODO TESTS
	/**
	 * @return true if game is over and false otherwise
	 */
	public boolean gameOver(){
		return this.winCheck(p1) || this.winCheck(p2) || this.drawCheck();
	}

	/**
	 * @return amount of visits
	 */
	public int getVisits(){
		return 1;
	}

	//TODO TESTS
	/**
	 * @param player char representing the player whose wins will be returned
	 * @return amount of "player"'s wins
	 */
	public int getWins(char player){
		return winCheck(player) ? 1 : 0;
	}

	//TODO TESTS
	/**
	 * @param player char representing the player whose ratio of wins/visits will be returned
	 * @return ratio of wins and vistis
	 */
	public double getRatio(char player){
		return getWins(player)/getVisits();
	}
	
	public List<Integer> getEmptyPositions() {
		List<Integer> emptyPositions = new ArrayList<>();
		for(int i = 0; i < this.dim*this.dim; i++)
			if(this.get(i%dim, i/dim) == emptyCell)
				emptyPositions.add(i);
				
		return emptyPositions;
	}

	/**
	 * @param other object (assumed to be of the same type) to eval
	 * @return true if boards are the same, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (other == null) return false;
		TicTacToe that = (TicTacToe) other;
		// TODO check this PLEASE
		for(int i = 0; i < dim*dim; i++)
			if(this.get(i%dim, i/dim) != that.get(i%dim, i/dim))
				return false;
		return this.getTurn() == that.getTurn();
	}

	/**
	 * toString method
	 * @return a string representing the config of the current game
	 */
	@Override
	public String toString(){
		String result = "";
		for(int y = 0; y<dim; y++){
			for(int x = 0; x<dim; x++)
				result += this.get(x, y);
			result+='\n';
		}
		return result;
	}

	/**
	 * has same functionality as clone method but, instead of using the Cloneable interface,
	 * uses the constructor with the string argument and the toString method's output (which match perfectly with eachother)
	 * to create a new instance with the same configuration
	 * @return a "clone" of this instance aka a new instance with the same variables but with different references
	 */
	public TicTacToe stringClone(){
		return new TicTacToe(this.toString());
	}

	/**
	 * clone method
	 * @return a clone of the instance used upon
	 */
	// @SuppressWarnings("unchecked")
	public Object clone(){
		TicTacToe result;
		try{
			result = (TicTacToe) super.clone();
			result.board = new Character[dim][dim];
			for(int i = 0; i < dim*dim; i++){
				result.set(i%dim, i/dim, this.get(i%dim, i/dim));
			}
			result.score = new HashMap<>(2);
			result.score.put(p1, this.score.get(p1));
			result.score.put(p2, this.score.get(p2));
			result.determineTurn();
		}catch(CloneNotSupportedException e){
			throw new AssertionError(e);			//this line should be unreachable since this object is clonable
		}
		
		return result;
	}
}