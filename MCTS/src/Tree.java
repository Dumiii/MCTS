/**
 * Game tree that holds all States
 */
public class Tree {
	MCTS.State root;

	/**
	 * Default constructor that creates a empty root that is set after tree creation
	 */
	public Tree() {
		root = new MCTS.State();
	}

	/**
	 * Constructor that accepts a state to be root
	 * @param root State to be set as the top of the tree
	 */
	public Tree(MCTS.State root) {
		this.root = root;
	}

	/**
	 * Get root method
	 * @return root of the tree instance
	 */
	public MCTS.State getRoot() {
		return root;
	}

	/**
	 * Set root method
	 * @param root State to be set as the root of the tree
	 */
	public void setRoot(MCTS.State root) {
		this.root = root;
	}

	/**
	 * Adds the child state to the parent state's child array
	 * @param parent
	 * @param child
	 */
	public void addChild(MCTS.State parent, MCTS.State child) {
		parent.getChildArray().add(child);
	}
}
