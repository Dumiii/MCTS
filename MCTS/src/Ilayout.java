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
	 * @param player char representing the player whose ratio will be returned
	 * @return ratio of wins and visits
	 */
	double getRatio(char player);
}