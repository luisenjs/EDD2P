package casa.proyectoedd2p;

import clases.Archivo;
import clases.Tree;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {

    @FXML
    private VBox explorador;

    @FXML
    private TextField ruta;

    @FXML
    private Button btnPath;

//    @FXML
//    private ChoiceBox cbextensiones;
    @FXML
    private CheckBox checkFiltro;
    
    private static Tree<Archivo> directory;

    private static Rectangle temp_r = null;

    private static HashMap<String, Color> colors = new HashMap();

    private static HashMap<Integer, String> node_color = new HashMap();
    
    private final Stack<String> rutasanteriores = new Stack<>();

    @FXML
    private Button btnExplorador;

    private static boolean popup;

    private void renderizarTreemap() {
        btnExplorador.setDisable(false);
        explorador.getChildren().clear();
        if (directory != null) {
            ArrayList<String> arr = new ArrayList<>();
//            arr.add("pdf");
            StackPane treemap = new StackPane();
//            cbextensiones.getItems().addAll(colors.keySet());
            createTreemap(directory, 1000, 600, arr, treemap);
            explorador.getChildren().add(treemap);
            treemap.setOnMouseExited(e -> {
                treemap.getChildren().clear();
                createTreemap(directory, 1000, 600, arr, treemap);
            });
            checkFiltro.selectedProperty().addListener((observable, oldValue, newValue) -> {
                popup = newValue;
                if (newValue) {
                    treemap.setOnMouseMoved(e -> {
                        changeColor(temp_r, treemap);
                    });
                } else {
                    treemap.setOnMouseMoved(null);
                    treemap.setOnMouseExited(null);

                }
            });
        }
    }

    @FXML
    private void switchToSecondary() {
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void selectPath() {
        checkFiltro.setSelected(false);
        btnExplorador.setDisable(true);
        DirectoryChooser fc = new DirectoryChooser();
        File selecedFile = fc.showDialog(null);
        if (selecedFile != null) {
            colors.put("", Color.rgb(250, 250, 114));
            ruta.setText(selecedFile.getPath());
            directory = crearDirectorio(selecedFile);
            node_color.clear();
            renderizarTreemap();
        }
    }

    private Tree<Archivo> crearDirectorio(File file) {
        System.out.println(file.getAbsolutePath());
        Tree<Archivo> tree_directory = new Tree(new Archivo(file.getName(), file.getPath(), file.length()));
        LinkedList<Tree<Archivo>> hijos = new LinkedList<>();
        if (file.isDirectory()) {
            tree_directory.getRoot().getContent().setDirectory(true);
            long totalSize = 0;
            for (File f : file.listFiles()) {
                try {

                    Tree<Archivo> temp = crearDirectorio(f);
                    hijos.add(temp);
                    totalSize += temp.getRoot().getContent().getSize();
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            tree_directory.getRoot().getContent().setSize(totalSize);
        }
        tree_directory.getRoot().setChildren(hijos);
        return tree_directory;
    }

    private static void changeColor(Rectangle r, StackPane explorador) {
        if (r == null) {
            return;
        }
        for (Node childs : explorador.getChildren()) {
            if (!childs.getStyle().equals(r.getStyle())) {
                childs.setStyle("-fx-fill: #e0e0e0; -fx-stroke: white; -fx-font-size: 15");
            }
        }

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
        return c;
    }

    public static void createTreemap(Tree<Archivo> directorio, double width, double height, StackPane treemap) {
        createTreemap(directorio, 0, 0, width, height, false, null, treemap);
    }

    public static void createTreemap(Tree<Archivo> directorio, double width, double height, ArrayList<String> permitidas, StackPane treemap) {
        createTreemap(directorio, 0, 0, width, height, false, permitidas, treemap);
    }

    private static void createTreemap(Tree<Archivo> directorio, double x, double y, double width, double height, boolean rotate, ArrayList<String> permitidas, StackPane stackPane) {

        stackPane.setAlignment(Pos.TOP_LEFT);
        Rectangle rect = new Rectangle(width, height);
        String extension = directorio.getRoot().getContent().getExtension();
        Color c;
        if (permitidas != null && !permitidas.isEmpty() && !permitidas.contains(extension)) {
            c = Color.web("#e0e0e0");
        } else {
            c = colors.getOrDefault(extension, generateRandomColor());
        }
        String hex = String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255));
        colors.put(extension, c);
        rect.setStyle("-fx-fill: " + hex + "; -fx-stroke: white;");
        node_color.put(stackPane.getChildren().size(), "-fx-fill: " + hex + "; -fx-stroke: white; -fx-font-size: 15");

        rect.setTranslateX(x);
        rect.setTranslateY(y);

        double initialX = x;
        double initialY = y;

        LinkedList<Tree<Archivo>> children = directorio.getRoot().getChildren();
        int numChildren = children.size();

        if (children.isEmpty()) {
            rect.setOnMouseEntered(eh -> {
                temp_r = rect;
                double mouseX = eh.getScreenX();
                double mouseY = eh.getScreenY();
                if (!popup) {
                    Stage stage = abrirPopup(directorio, mouseX, mouseY);
                    rect.setOnMouseExited(me -> {
                        cerrarPopup(stage);
                    });

                }
            });
            rect.setOnMouseExited(eh -> {
                for (int i : node_color.keySet()) {
                    stackPane.getChildren().get(i).setStyle(node_color.get(i));
                }
            });
        }

        stackPane.getChildren().add(rect);

        if (!rotate) {
            for (int i = 0; i < numChildren; i++) {
                Tree<Archivo> child = children.get(i);
                double childWidth = (width * child.getRoot().getContent().getSize()) / directorio.getRoot().getContent().getSize();
                createTreemap(child, x, y, childWidth, height, true, permitidas, stackPane);
                x += childWidth;
            }
        } else {
            for (int i = 0; i < numChildren; i++) {
                Tree<Archivo> child = children.get(i);
                double childHeight = (height * child.getRoot().getContent().getSize()) / directorio.getRoot().getContent().getSize();
                createTreemap(child, x, y, width, childHeight, false, permitidas, stackPane);
                y += childHeight;
            }
        }
//        if (numChildren > 0) {
//            String name = directorio.getRoot().getContent().getName(); //1073741824
//            double size = directorio.getRoot().getContent().getSize() / (1024 * 1024);
//            String text = String.format("%s - %.2f MB", name, size);
//            Label lb = new Label(text);
//            lb.setStyle("-fx-font-size: 15");
//            stackPane.getChildren().add(stackPane.getChildren().size(), lb);
//            lb.setWrapText(true);
//            lb.setTranslateX(initialX + 5);
//            lb.setTranslateY(initialY);
//        }
    }
    
    private void buscar(String path, Button back, VBox explorador) {
        if (!rutasanteriores.isEmpty()) {
            back.setDisable(false);
        } else {
            back.setDisable(true);
        }
        explorador.getChildren().clear();
        File f = new File(path);
        String[] archivos = f.list();
        try {
            for (String archivo : archivos) {
                Label nombrearchivo = new Label(archivo);
                nombrearchivo.setFont(new Font("System", 14));
                String[] datos = archivo.split("\\.");
                String extension = "carpeta";
                try {
                    extension = datos[1];
                } catch (RuntimeException e) {
                }
                ImageView imgview = new ImageView();
                try ( FileInputStream input = new FileInputStream("src/main/resources/imagenes/" + extension + ".png")) {
                    Image img = new Image(input);
                    imgview.setImage(img);
                    imgview.setFitHeight(20);
                    imgview.setFitWidth(20);
                } catch (FileNotFoundException e) {
                    try ( FileInputStream input = new FileInputStream("src/main/resources/imagenes/unknownfile.png")) {
                        Image img = new Image(input);
                        imgview.setImage(img);
                        imgview.setFitHeight(20);
                        imgview.setFitWidth(20);
                    } catch (IOException ex) {
                    }
                } catch (IOException ex) {
                }
                HBox archivoBox = new HBox(imgview, nombrearchivo);
                archivoBox.setOnMouseClicked(mc -> {
                    rutasanteriores.push(path);
                    buscar(path + "/" + nombrearchivo.getText(), back, explorador);
                });
                archivoBox.setSpacing(5);
                archivoBox.setPadding(new Insets(3, 3, 3, 3));
                explorador.getChildren().add(archivoBox);
            }
        } catch (RuntimeException e) {

        }
    }

    @FXML
    void explorar() {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        ScrollPane contedido = new ScrollPane();
        VBox exploBox = new VBox();
        exploBox.setStyle("-fx-background-color: WHITE");
        exploBox.setPadding(new Insets(5, 5, 5, 5));
        Button back = new Button("Volver");
        back.setOnMouseClicked(mc -> {
            buscar(rutasanteriores.pop(), back, exploBox);
        });
        buscar(ruta.getText(), back, exploBox);
        contedido.setContent(exploBox);
        root.getChildren().addAll(back, contedido);
        root.setSpacing(5);
        root.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(root, 500, 400);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static Stage abrirPopup(Tree<Archivo> directorio, double mouseX, double mouseY) {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.CENTER);
        Label lbarchivo = new Label("Nombre: " + directorio.getRoot().getContent().getName());
        lbarchivo.setFont(Font.font("System", 14));
        lbarchivo.setWrapText(true);
        double peso = (double)Math.round(directorio.getRoot().getContent().getSize()/2048 * 100d) / 100d;
        Label lbpeso = new Label("Peso: " + String.valueOf(peso) + "MB");
        lbpeso.setFont(Font.font("System", 14));
        lbpeso.setWrapText(true);
        ImageView imgview = new ImageView();
                try ( FileInputStream input = new FileInputStream("src/main/resources/imagenes/" + directorio.getRoot().getContent().getExtension() + ".png")) {
                    Image img = new Image(input);
                    imgview.setImage(img);
                    imgview.setFitHeight(50);
                    imgview.setFitWidth(50);
                } catch (FileNotFoundException e) {
                    try ( FileInputStream input = new FileInputStream("src/main/resources/imagenes/unknownfile.png")) {
                        Image img = new Image(input);
                        imgview.setImage(img);
                        imgview.setFitHeight(50);
                        imgview.setFitWidth(50);
                    } catch (IOException ex) {
                    }
                } catch (IOException ex) {
                }
        root.getChildren().addAll(lbarchivo, lbpeso, imgview);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setX(mouseX + 20);
        stage.setY(mouseY - 100);
        return stage;
    }

    private static void cerrarPopup(Stage s) {
        s.close();
    }

}
