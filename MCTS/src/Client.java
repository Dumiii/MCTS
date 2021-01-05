import java.util.Scanner;

public class Client {

	public static void main(String[] args) {

		System.out.println("Welcome to Tic Tac Toe!");
		
		Scanner sc = new Scanner(System.in);

		TicTacToe t = new TicTacToe();
			
		MCTS m = new MCTS(1750);

		while(!t.gameOver()) {
			System.out.println("Current player: " + t.getTurn());
			Ilayout suggested = m.findNextMove(t, t.getTurn());
			System.out.println("The MCTS algorithm suggests the following move for you to play: " + ((TicTacToe) suggested).getLastPlayed());
			System.out.println("Enter a number from 0-8: ");
			int move = sc.nextInt();
			t.play(move);
			System.out.println("Current board status");
			System.out.println(t.toString());
			System.out.println("Switching player");
		}
		sc.close();
		if(t.drawCheck())
			System.out.println("Game ended in a draw.");
		else
			System.out.println("Game ended, player" + " " + (t.winCheck('X') ? 'X' : 'O') + " " + "won. Goodbye!");
	}
}