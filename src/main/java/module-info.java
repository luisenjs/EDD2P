module casa.proyectoedd2p {
    requires javafx.controls;
    requires javafx.fxml;

    opens casa.proyectoedd2p to javafx.fxml;
    exports casa.proyectoedd2p;
}
