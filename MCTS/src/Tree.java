
public class Tree {
    MCTS.State root;

    public Tree() {
        root = new MCTS.State();
    }

    public Tree(MCTS.State root) {
        this.root = root;
    }

    public MCTS.State getRoot() {
        return root;
    }

    public void setRoot(MCTS.State root) {
        this.root = root;
    }

    public void addChild(MCTS.State parent, MCTS.State child) {
        parent.getChildArray().add(child);
    }
}
