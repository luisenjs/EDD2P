package casa.proyectoedd2p;

import clases.Tree;
import java.util.LinkedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PrimaryController {

    @FXML
    private VBox explorador;

    @FXML
    private void switchToSecondary() {
        Tree<Double> directorio = new Tree<>(8.);
        directorio.getRoot().addChild(4.0);
        directorio.getRoot().addChild(2.0);
        directorio.getRoot().addChild(2.0);
        directorio.getRoot().getChildren().get(0).getRoot().addChild(3.0);
        directorio.getRoot().getChildren().get(0).getRoot().getChildren().get(0).getRoot().addChild(2.0);
        directorio.getRoot().getChildren().get(0).getRoot().getChildren().get(0).getRoot().addChild(1.0);
        directorio.getRoot().getChildren().get(0).getRoot().addChild(1.0);
        directorio.getRoot().getChildren().get(0).getRoot().getChildren().get(1).getRoot().addChild(0.75);
        directorio.getRoot().getChildren().get(0).getRoot().getChildren().get(1).getRoot().addChild(0.25);
        
        explorador.getChildren().clear();
        StackPane treemap = createTreemap(directorio, 600, 600);
        explorador.getChildren().add(treemap);
    }

    private static String generateRandomColor() {
        String[] letters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String color = "#";
        for (int i = 0; i < 6; i++) {
            color += letters[(int) Math.round(Math.random() * 15)];
        }
        return color;
    }

    public static StackPane createTreemap(Tree<Double> directorio, double width, double height) {
        return createTreemap(directorio, 0, 0, width, height, false);
    }

    private static StackPane createTreemap(Tree<Double> directorio, double x, double y, double width, double height, boolean rotate) {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);
        Rectangle rect = new Rectangle(width, height);
        Color c = Color.web(generateRandomColor());
        rect.setFill(c);
        stackPane.getChildren().add(rect);
        rect.setTranslateX(x);
        rect.setTranslateY(y);
        LinkedList<Tree<Double>> children = directorio.getRoot().getChildren();
        if (children != null) {
            int numChildren = children.size();
            if (numChildren > 0) {
                if (!rotate) {
                    for (int i = 0; i < numChildren; i++) {
                        Tree<Double> child = children.get(i);
                        double childWidth = (width*child.getRoot().getContent()) / directorio.getRoot().getContent();
                        stackPane.getChildren().add(createTreemap(child, x, y, childWidth, height,true));
                        x+=childWidth;
                    }
                } else {
                    for (int i = 0; i < numChildren; i++) {
                        Tree<Double> child = children.get(i);
                        double childHeight = (height*child.getRoot().getContent()) / directorio.getRoot().getContent();
                        stackPane.getChildren().add(createTreemap(child, x, y, width, childHeight, false));
                        y+=childHeight;
                    }
                }
            }
        }

        return stackPane;
    }
}
