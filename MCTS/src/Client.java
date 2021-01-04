import java.util.Scanner;

public class Client {

	public static void main(String[] args) {

		System.out.println("Welcome to Tic Tac Toe!");
		
		Scanner sc = new Scanner(System.in);

		TicTacToe t = new TicTacToe();
		int counter = 0;
		while(t.drawCheck() || !t.gameOver()){
			t = new TicTacToe();
			
			MCTS m = new MCTS(1000);

			while(!t.gameOver()) {
				//System.out.println("The MCTS algorithm suggests the following move for you to play: ");
				Ilayout suggested = m.findNextMove(t, t.getTurn());
				//System.out.println();
				//System.out.println("Enter a number from 0-8: ");
				t.play(((TicTacToe) suggested).getLastPlayed());
				// System.out.println("Current board status");
				// System.out.println(t.toString());
				// System.out.println("Switching player");
			}
			sc.close();
			if(t.drawCheck()){
				// System.out.println("Game ended in a draw.");
				counter++;
				System.out.println(counter);
			}
			else{
				System.out.println("Game ended, player" + " " + (t.winCheck('X') ? 'X' : 'O') + " " + "won. Goodbye!");
			}
		}
	}
}