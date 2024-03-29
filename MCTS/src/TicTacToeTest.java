import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class TicTacToeTest {
	@Test
	public void testConstructor1(){	// also tests toString
		TicTacToe ttt = new TicTacToe();
		String expected =	"---\n"+
							"---\n"+
							"---\n";
		assertEquals(expected, ttt.toString());
	}

	@Test
	public void testConstructor2(){	// also tests toString
		String expected =	"-O-\n"+
							"O--\n"+
							"--X\n";

		TicTacToe ttt = new TicTacToe(expected);
		assertEquals(expected, ttt.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorExceptions1(){
		String wrong =	"-O--\n"+	//grid is too big in this line
						"O--\n"+
						"--X\n";
		new TicTacToe(wrong);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorExceptions2(){
		String wrong =	"-O-\n"+
						"Oa-\n"+	// has invalid char "a" in this line
						"--X\n";
		new TicTacToe(wrong);
	}
	
	@Test
	public void testInBounds(){
		TicTacToe ttt = new TicTacToe();
		assertTrue(ttt.inBounds(0));
		assertTrue(ttt.inBounds(1));
		assertTrue(ttt.inBounds(2));
		assertFalse(ttt.inBounds(-1));
		assertFalse(ttt.inBounds(-98376));
		assertFalse(ttt.inBounds(98376));
		assertFalse(ttt.inBounds(3));
	}

	@Test
	public void testIsValidPlayer(){
		TicTacToe ttt = new TicTacToe();
		assertTrue(ttt.isValidPlayer('O'));
		assertTrue(ttt.isValidPlayer('X'));
		assertFalse(ttt.isValidPlayer('-'));
		assertFalse(ttt.isValidPlayer('ḉ'));
		assertFalse(ttt.isValidPlayer('p'));
		assertFalse(ttt.isValidPlayer('P'));
		assertFalse(ttt.isValidPlayer('>'));
	}
	
	@Test
	public void testIsValidCharacter(){
		TicTacToe ttt = new TicTacToe();
		assertTrue(ttt.isValidCharacter('O'));
		assertTrue(ttt.isValidCharacter('X'));
		assertTrue(ttt.isValidCharacter('-'));
		assertFalse(ttt.isValidCharacter('ḉ'));
		assertFalse(ttt.isValidCharacter('p'));
		assertFalse(ttt.isValidCharacter('P'));
		assertFalse(ttt.isValidCharacter('>'));
	}

	@Test
	public void testGet(){
		TicTacToe ttt = new TicTacToe();
		char expected = '-';
		assertEquals(expected, ttt.get(0, 0));
        assertEquals(expected, ttt.get(2, 2));
		assertEquals(expected, ttt.get(1, 0));
		
		String input =	"-O-\n"+
						"O--\n"+
						"--X\n";
		ttt = new TicTacToe(input);
		char expected1 = 'O';
		char expected2 = 'X';
		assertEquals(expected1, ttt.get(1, 0));
		assertEquals(expected1, ttt.get(0, 1));
		assertEquals(expected2, ttt.get(2, 2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetException1(){
		(new TicTacToe()).get(9, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetException2(){
		(new TicTacToe()).get(2, -12);
	}

	@Test
	public void testPlay1(){
		TicTacToe ttt = new TicTacToe();
		char expected = '-';
		assertEquals(expected, ttt.get(0, 1));
		char change = 'O';
		ttt.play(0, 1, change);
		assertEquals(change, ttt.get(0, 1));
		assertEquals(expected, ttt.get(2, 1));
		change = 'X';
		ttt.play(2, 1, change);
		assertEquals(change, ttt.get(2, 1));
		assertEquals(expected, ttt.get(1, 1));
	}

	@Test
	public void testPlay2(){
		TicTacToe ttt = new TicTacToe();
		char expectedCell = '-';
		char expectedTurn = 'X';
		assertEquals(expectedCell, ttt.get(0, 1));
		assertEquals(expectedTurn, ttt.getTurn());
		char change = 'X';
		expectedTurn = 'O';
		ttt.play(0, 1);
		assertEquals(change, ttt.get(0, 1));
		assertEquals(expectedCell, ttt.get(2, 1));
		assertEquals(expectedTurn, ttt.getTurn());
		change = 'O';
		expectedTurn = 'X';
		ttt.play(2, 1);
		assertEquals(change, ttt.get(2, 1));
		assertEquals(expectedCell, ttt.get(1, 1));
		assertEquals(expectedTurn, ttt.getTurn());
	}

	@Test
	public void testPlay3(){
		TicTacToe ttt = new TicTacToe();
		char expectedCell = '-';
		char expectedTurn = 'X';
		assertEquals(expectedCell, ttt.get(3));
		assertEquals(expectedTurn, ttt.getTurn());
		char change = 'X';
		expectedTurn = 'O';
		ttt.play(3);
		assertEquals(change, ttt.get(3));
		assertEquals(expectedCell, ttt.get(5));
		assertEquals(expectedTurn, ttt.getTurn());
		change = 'O';
		expectedTurn = 'X';
		ttt.play(5);
		assertEquals(change, ttt.get(5));
		assertEquals(expectedCell, ttt.get(4));
		assertEquals(expectedTurn, ttt.getTurn());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlayExceptions1(){
		(new TicTacToe()).play(0, 1, '-');
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlayExceptions2(){
		String input =	"---\n"+
						"O--\n"+	// 0,1 is already occupied by 'O'
						"--X\n";
		(new TicTacToe(input)).play(0, 1, 'X');
	}

	@Test
	public void testGetTurn(){
		TicTacToe ttt = new TicTacToe();
		char expectedTurn = 'X';
		assertEquals(expectedTurn, ttt.getTurn());
		expectedTurn = 'O';
		ttt.play(0, 1);
		assertEquals(expectedTurn, ttt.getTurn());
		expectedTurn = 'X';
		ttt.play(2, 1);
		assertEquals(expectedTurn, ttt.getTurn());
	}

	@Test
	public void testGetLastPlayed(){
		TicTacToe ttt = new TicTacToe();
		ttt.play(0);
		assertEquals(0, ttt.getLastPlayed());
		ttt.play(1);
		assertEquals(1, ttt.getLastPlayed());
		ttt.play(2);
		assertEquals(2, ttt.getLastPlayed());
	}

	@Test
	public void testWinCheck(){
		String input =	"-OO\n"+
						"OO-\n"+
						"XXX\n";
		TicTacToe ttt = new TicTacToe(input);
		assertTrue(ttt.winCheck('X'));
		assertTrue(!ttt.winCheck('O'));

		input =	"O--\n"+
				"-O-\n"+
				"XXO\n";
		ttt = new TicTacToe(input);
		assertTrue(ttt.winCheck('O'));
		assertTrue(!ttt.winCheck('X'));
		
		input =	"OOX\n"+
				"OXO\n"+
				"XXO\n";
		ttt = new TicTacToe(input);
		assertTrue(!ttt.winCheck('O'));
		assertTrue(ttt.winCheck('X'));
		
		input =	"---\n"+
				"OXO\n"+
				"---\n";
		ttt = new TicTacToe(input);
		assertTrue(!ttt.winCheck('O'));
		assertTrue(!ttt.winCheck('X'));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWinCheckException(){
		(new TicTacToe()).winCheck('a');
	}

	@Test
	public void testDrawCheck(){
		String input =	"OOX\n"+
						"OXO\n"+
						"XXO\n";
		TicTacToe ttt = new TicTacToe(input);
		assertTrue(!ttt.drawCheck());
		input =	"XOX\n"+
				"OXO\n"+
				"OXO\n";
		ttt = new TicTacToe(input);
		assertTrue(ttt.drawCheck());
	}

	@Test
	public void testDrawCheckWithMap(){
		String input =	"OOX\n"+
						"OXO\n"+
						"XXO\n";
		TicTacToe ttt = new TicTacToe(input);
		assertTrue(!ttt.drawCheckWithMap());
		input =	"XOX\n"+
				"OXO\n"+
				"OXO\n";
		ttt = new TicTacToe(input);
		assertTrue(ttt.drawCheckWithMap());
	}

	@Test
	public void testGameOver(){
		String input =	"OOX\n"+
						"OXO\n"+
						"XXO\n";
		TicTacToe ttt = new TicTacToe(input);
		assertTrue(ttt.gameOver());

		input =	"XOX\n"+
				"OXO\n"+
				"OXO\n";
		ttt = new TicTacToe(input);
		assertTrue(ttt.gameOver());

		input =	"XOX\n"+
				"OX-\n"+
				"OXO\n";
		ttt = new TicTacToe(input);
		assertFalse(ttt.gameOver());

		assertFalse((new TicTacToe()).gameOver());
	}

	@Test
	public void testGetVisits(){
		assertEquals(1, (new TicTacToe()).getVisits());
	}

	@Test
	public void testGetWins(){
		assertEquals(1, (new TicTacToe()).getVisits());
	}

	@Test
	public void testEquals(){
		String input =	"---\n"+
						"---\n"+
						"--O\n";
		TicTacToe expected = new TicTacToe(input);
		TicTacToe actual = new TicTacToe();
		actual.play(2, 2, 'O');
		System.out.println(expected.getTurn() + " " + actual.getTurn());
		assertEquals(expected, actual);
		assertNotEquals(new TicTacToe(), actual);

		input =	"---\n"+
				"--X\n"+
				"--O\n";
		expected = new TicTacToe(input);
		actual.play(2, 1, 'X');
		assertTrue(expected.equals(actual));

		input =	"---\n"+
				"O-X\n"+
				"--O\n";
		expected = new TicTacToe(input);
		actual.play(0, 1, 'O');
		assertTrue(expected.equals(actual));
	}

	@Test
	public void testClone(){
		TicTacToe ttt = new TicTacToe();
		ttt.clone().equals(ttt);
		String input =	"-OO\n"+
						"OO-\n"+
						"XXX\n";
		
		ttt = new TicTacToe(input);
		ttt.clone().equals(ttt);
	}

	@Test
	public void testStringClone(){
		TicTacToe ttt = new TicTacToe();
		assertEquals(ttt, (TicTacToe) ttt.clone());
		String input =	"-OO\n"+
						"OO-\n"+
						"XXX\n";
		
		ttt = new TicTacToe(input);
		TicTacToe clone = (TicTacToe) ttt.clone();
		assertEquals(ttt, clone);
		clone.play(0);
		assertNotEquals(ttt, clone);
		ttt.play(0);
		assertEquals(ttt.getTurn(), clone.getTurn());
	}

	@Test
	public void testChildren(){
		String input =	"X-X\n"+
						"OXO\n"+
						"OX-\n";
		TicTacToe ttt = new TicTacToe(input);
		String expected0 =	"XOX\n"+
							"OXO\n"+
							"OX-\n";
		String expected1 =	"X-X\n"+
							"OXO\n"+
							"OXO\n";
		List<Ilayout> result = ttt.children();
		assertEquals(new TicTacToe(expected0), result.get(0));
		assertEquals(new TicTacToe(expected1), result.get(1));
	}
}
