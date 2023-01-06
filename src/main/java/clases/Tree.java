package clases;


public class Tree<E> {
    
    private NodeTree<E> root;

    public Tree() {
        this.root = null;
    }

    public Tree(E contenido) {
        this.root = new NodeTree(contenido);
    }
    
    public boolean isEmpty(){
        return this.root == null;
    }
    
    public boolean isLeaf(){
        return !isEmpty() && this.root.getChildren().isEmpty();
    }

    public NodeTree<E> getRoot() {
        return root;
    }

    public void setRoot(NodeTree<E> root) {
        this.root = root;
    }
    
}
