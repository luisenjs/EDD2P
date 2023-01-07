package casa.proyectoedd2p;

import clases.Archivo;
import clases.Tree;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class PrimaryController {

    @FXML
    private VBox explorador;

    @FXML
    private TextField ruta;
    
    @FXML
    private Button btnPath;

    private static Tree<Archivo> directory;

    private static HashMap<String, Color> colors = new HashMap();

    @FXML
    private void switchToSecondary() {

        explorador.getChildren().clear();
        StackPane treemap = createTreemap(directory, 1000, 600);
        explorador.getChildren().add(treemap);
        System.out.println("Padre: " + (double) directory.getRoot().getContent().getSize() / (1024 * 1024 * 1024) + "MB");
//        directory.imprimir();
        colors.put("", Color.rgb(250, 250, 114));
    }

    @FXML
    private void selectPath() {
        DirectoryChooser fc = new DirectoryChooser();
        File selecedFile = fc.showDialog(null);
        if (selecedFile != null) {
            ruta.setText(selecedFile.getPath());
            directory = crearDirectorio(selecedFile);
        }
    }

    private Tree<Archivo> crearDirectorio(File file) {
        Tree<Archivo> directory = new Tree<Archivo>(new Archivo(file.getName(), file.getPath(), file.length()));
        LinkedList<Tree<Archivo>> hijos = new LinkedList<>();
        if (file.isDirectory()) {
            directory.getRoot().getContent().setDirectory(true);
            long totalSize = 0;
            for (File f : file.listFiles()) {
                Tree<Archivo> temp = crearDirectorio(f);
                hijos.add(temp);
                totalSize += temp.getRoot().getContent().getSize();
            }
            directory.getRoot().getContent().setSize(totalSize);
        }
        directory.getRoot().setChildren(hijos);
        return directory;
    }

    private static Color generateRandomColor() {
        int[] rgb = new int[3];
        Random rd = new Random();
        int pos = rd.nextInt(3);
        for (int i = 0; i < 3; i++) {
            if (i == pos) {
                rgb[i] = 250;
            } else {
                rgb[i] = rd.nextInt(250);
            }
        }

        Color c = Color.rgb(rgb[0], rgb[1], rgb[2]);
//        String[] letters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
//        String color = "#";
//        for (int i = 0; i < 6; i++) {
//            color += letters[(int) Math.round(Math.random() * 15)];
//        }
        return c;
    }

    public static StackPane createTreemap(Tree<Archivo> directorio, double width, double height) {
        return createTreemap(directorio, 0, 0, width, height, false);
    }

    private static StackPane createTreemap(Tree<Archivo> directorio, double x, double y, double width, double height, boolean rotate) {

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.TOP_LEFT);

        Rectangle rect = new Rectangle(width, height);
        String extension = getExtension(directorio.getRoot().getContent().getPath());
        Color c = colors.getOrDefault(extension, generateRandomColor());
        colors.put(extension, c);
        rect.setFill(c);
        stackPane.getChildren().add(rect);
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(2);
        rect.setTranslateX(x);
        rect.setTranslateY(y);
        double initialX = x;
        double initialY = y;

        LinkedList<Tree<Archivo>> children = directorio.getRoot().getChildren();
        if (children != null || !children.isEmpty()) {
            int numChildren = children.size();
            if (numChildren > 0) {
                if (!rotate) {
                    for (int i = 0; i < numChildren; i++) {
                        Tree<Archivo> child = children.get(i);
                        double childWidth = (width * child.getRoot().getContent().getSize()) / directorio.getRoot().getContent().getSize();
                        stackPane.getChildren().add(createTreemap(child, x, y, childWidth, height, true));
                        x += childWidth;
                    }
                } else {
                    for (int i = 0; i < numChildren; i++) {
                        Tree<Archivo> child = children.get(i);
                        double childHeight = (height * child.getRoot().getContent().getSize()) / directorio.getRoot().getContent().getSize();
                        stackPane.getChildren().add(createTreemap(child, x, y, width, childHeight, false));
                        y += childHeight;
                    }
                }
                String name = directorio.getRoot().getContent().getName(); //1073741824
                double size = directorio.getRoot().getContent().getSize() / (1024 * 1024);
                String text = String.format("%s - %.2f MB", name, size);
                Label lb = new Label(text);
                lb.setStyle("-fx-font-size: 15;");
                stackPane.getChildren().add(stackPane.getChildren().size(), lb);
                lb.setWrapText(true);
                lb.setTranslateX(initialX + 5);
                lb.setTranslateY(initialY);
            }
        }

        return stackPane;
    }

    private static String getExtension(String path) {
        String extension = "";

        int i = path.lastIndexOf('.');
        int p = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

        if (i > p) {
            extension = path.substring(i + 1);
        }
        return extension;
    }

}
