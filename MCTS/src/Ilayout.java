import java.util.List;

interface Ilayout{
	/**
	 * @return children of reciever
	*/
	List<Ilayout> children();

	/**
	 * @return bool if game is over
	*/
	boolean gameOver();

	/**
	 * @param player char representing the player whose wins will be returned
	 * @return amount of wins
	*/
	int getWins(char player);

	/**
	 * @return amount of visits
	 */
	int getVisits();

	/**
	 * @param player char that represents the player
	 * @return whether or not the arg is a valid player
	 */
	boolean isValidPlayer(char player);
}