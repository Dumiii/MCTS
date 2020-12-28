import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

class MCTS {
	static class State{
		private Ilayout layout;
        private State father;
        private int wins;
        private int visits;
        private double ratio;
		private char player;
		
		public State(Ilayout l, State n, char player) {//?????????????????
			layout = l;
			father = n;
			this.player = player;
			if(((TicTacToe) layout).winCheck(player)){
				this.wins = 1;
			}
			if(father != null){
				this.visits=father.visits + l.getVisits();
				this.backPropagation();
			}
			ratio = this.wins/this.visits;
		}

		public void backPropagation(){//?????????????????
			if(father!=null){
				if(layout.gameOver()){
					father.wins++;
				}
				father.visits++;
				father.ratio = father.wins/father.ratio;
				father.backPropagation();
			}
		}

		public String toString(){
			return layout.toString();
		}

		// public double getG(){
		// 	return g;
		// }

		// public int compareToGoal(Ilayout obj){
		// 	int result = 0;
		// 	for(Stack<Character> curr : ((Table) this.layout).getList()) {
		// 		for(Stack<Character> goal :((Table) obj).getList()){
		// 			if(!curr.isEmpty() && !goal.isEmpty()){
		// 				result += countWhile(curr, goal);
		// 			}
		// 		}
		// 	}
		// 	return ((Table) this.layout).getDim() - result + ((int)this.getG());
		// }

		// public int findC(Character c, Stack<Character> s2){
		// 	int result = 0;
		// 	while(result<s2.size() && !c.equals(s2.get(result))){
		// 		result++;
		// 	}
		// 	return result;
		// }

		// public int countWhileReverse(int start, Stack<Character> s1, Stack<Character> s2){
		// 	int result = 0;
		// 	int index = start;
		// 	while(result<s1.size() && index>=0 && index<s2.size() && s1.get(result).equals(s2.get(index))){
		// 		result++;
		// 		index--;
		// 	}
		// 	return result;
		// }

		// public int countWhile(Stack<Character> s1, Stack<Character> s2){
		// 	int result = 0;
		// 	while(result<s1.size() && result<s2.size() && s1.get(result).equals(s2.get(result))){
		// 		result++;
		// 	}
		// 	return result;
		// }

		// public int compareToOther(State other, Ilayout goal){
		// 	int result = this.compareToGoal(goal) - other.compareToGoal(goal);
		// 	return result != 0 ? result : (int) (this.getG()-other.getG());
		// }
		// @Override
		// public boolean equals(Object other){
		// 	if (other == this) return true;
		// 	if (other == null) return false;
		// 	State that = (State) other;
		// 	if(getClass() != that.getClass()) return false;
		// 	return ((Table) this.layout).equals((Table) that.layout) && this.getG() == that.getG();
		// }
	}
	// protected Queue<State> abertos;
	// private List<State> fechados;
	// private State actual;
	// private Ilayout objective;

	// final private List<State> sucessores(State n){
	// 	List<State> sucs = new ArrayList<>();
	// 	List<Ilayout> children = n.layout.children();
	// 	for(Ilayout e: children){
	// 		if(n.father == null || !e.equals(n.father.layout)){
	// 			State nn = new State(e, n);
	// 			sucs.add(nn);
	// 		}
	// 	}
	// 	return sucs;
	// }

	// final public Iterator<State> solve(Ilayout s, Ilayout goal){
	// 	objective = goal;
	// 	abertos = new PriorityQueue<>(10, (s1, s2) -> (int) Math.signum(s1.compareToGoal(objective) - s2.compareToGoal(objective)));
	// 	fechados = new ArrayList<>();
	// 	abertos.add(new State(s, null));
	// 	List<State> sucs;
	// 	if(s.isGoal(objective)) return abertos.iterator();
	// 	while(!s.equals(objective)){
	// 		if(abertos.isEmpty())
	// 			return null;
	// 		actual = abertos.poll();
	// 		System.out.println(actual.getG() + " " + actual.compareToGoal(objective) + "\n-----------------");
	// 		System.out.println(actual);
	// 		if(actual.layout.isGoal(objective)){
	// 			ArrayList<State> result = new ArrayList<>();
	// 			State current = actual;
	// 			while(current != null){
	// 				result.add(current);
	// 				current = current.father;
	// 			}
	// 			Collections.reverse(result);
	// 			return result.iterator();
	// 		}
	// 		else{
	// 			sucs = sucessores(actual);
	// 			fechados.add(actual);
	// 			for(State suc: sucs)
	// 				if(!fechados.contains(suc))
	// 					abertos.add(suc);
	// 		}
	// 	}
	// 	return null;
	// }
}