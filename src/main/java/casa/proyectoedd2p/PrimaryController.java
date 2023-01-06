package casa.proyectoedd2p;

import clases.Tree;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PrimaryController {

    @FXML
    private HBox explorador;

    @FXML
    private void switchToSecondary() {
        Tree<Double> directorio = new Tree<>(600.0);
        directorio.getRoot().addChild(200.0);
        directorio.getRoot().addChild(100.0);
        directorio.getRoot().addChild(300.0);
//        directorio.getRoot().getChildren().get(0).getRoot().addChild(100.0);
//        directorio.getRoot().getChildren().get(0).getRoot().addChild(100.0);
//        directorio.getRoot().getChildren().get(0).getRoot().getChildren().get(1).getRoot().addChild(75.0);
//        directorio.getRoot().getChildren().get(0).getRoot().getChildren().get(1).getRoot().addChild(25.0);
        explorador.getChildren().clear();
        MapTreeVertical(directorio.getRoot().getContent(), directorio, explorador);
    }

    private String generateRandomColor() {
        String[] letters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String color = "#";
        for (int i = 0; i < 6; i++) {
            color += letters[(int) Math.round(Math.random() * 15)];
        }
        return color;
    }

    public void MapTreeVertical(double pesopadre, Tree<Double> d, Pane cont) {
        FlowPane v = new FlowPane();
        v.setStyle("-fx-background-color: " + generateRandomColor() + "; -fx-border-color: #000000; -fx-border-width: 0.5");
        v.setPrefHeight(calcularsize(cont.getHeight(), pesopadre, d.getRoot().getContent()));
        v.setPrefWidth(cont.getWidth());
//        v.setPrefSize(d.getRoot().getContent(), d.getRoot().getContent());
//        v.setPrefSize(cont.getWidth(), calcularsize(cont.getHeight(), pesopadre, d.getRoot().getContent()));
        System.out.println("ALTO: " + v.getHeight());
        cont.getChildren().add(v);
        if (!d.isLeaf()) {
            for (Tree<Double> t : d.getRoot().getChildren()) {
                MapTreeHorizontal(d.getRoot().getContent(), t, v);
            }
        }
    }

    public void MapTreeHorizontal(double pesopadre, Tree<Double> d, Pane cont) {
        FlowPane h = new FlowPane();
        h.setStyle("-fx-background-color: " + generateRandomColor() + "; -fx-border-color: #000000; -fx-border-width: 0.5");
        h.setPrefWidth(calcularsize(cont.getHeight(), pesopadre, d.getRoot().getContent()));
        h.setPrefHeight(cont.getHeight());
//        h.setPrefSize(d.getRoot().getContent(), d.getRoot().getContent());
        System.out.println("ANCHO: " + h.getHeight());
//        h.setPrefSize(calcularsize(cont.getWidth(), pesopadre, d.getRoot().getContent()), cont.getHeight());
        cont.getChildren().add(h);
        if (!d.isLeaf()) {
            for (Tree<Double> t : d.getRoot().getChildren()) {
                MapTreeVertical(d.getRoot().getContent(), t, h);
            }
        }
    }

    private double calcularsize(double lado, double pesopadre, double pesohijo) {
        double diferencia = pesopadre - pesohijo;
        double porcentajemenor = diferencia / pesopadre;
        double porcentajemayor = 1 - porcentajemenor;
        return lado * porcentajemayor;
    }

}
