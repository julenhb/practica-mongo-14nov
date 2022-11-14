package com.mycompany.jhb_music_setlist;

import BD.ConnectionDB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;
import static utilidades.CRUD_Song.borrarCancion;
import static utilidades.CRUD_Song.fillGenres;
import static utilidades.CRUD_Song.fillListas;
import static utilidades.CRUD_Song.insertarCancion;
import static utilidades.CRUD_Song.modificarCancion;

public class PrimaryController implements Initializable {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    MongoClient con;
    //MongoDatabase database = con.getDatabase("jhbmusic");

    @FXML
    private TextField songName;

    @FXML
    private TextField songArtist;

    @FXML
    private TextField songAlbum;

    @FXML
    private TextField songYear;

    @FXML
    private TextField delName;

    @FXML
    private TextField delArtist;

    @FXML
    private TextField modName;

    @FXML
    private Label errorModPrev;

    @FXML
    private Label errorMod;

    @FXML
    private TextField modArtist;

    @FXML
    private TextField newName;

    @FXML
    private TextField newArtist;

    @FXML
    private TextField newAlbum;

    @FXML
    private TextField newYear;

    @FXML
    private ComboBox<String> genre;

    @FXML
    private ComboBox<String> newGenre;

    private ArrayList<String> genres = new ArrayList<>();

    @FXML
    private ComboBox<String> lists;

    private ArrayList<String> listas = new ArrayList<>();

    @FXML
    private Button addSong;

    @FXML
    private Button removeSong;

    @FXML
    private Button delOk;

    @FXML
    private Button modSong;

    @FXML
    private Button modPrev;

    @FXML
    private Button modOk;

    @FXML
    private Button confSetlist;

    @FXML
    private Button volverDel;

    @FXML
    private AnchorPane ventanaBorrar;

    @FXML
    private AnchorPane ventanaMod;

    @FXML
    private Button volverMod;

    @FXML
    private AnchorPane ventanaModPrev;

    @FXML
    private Button volverModPrev;

    @FXML
    private ImageView logo;

    @FXML
    private ImageView logoBright;

    @FXML
    private TableView<Song> tabla;

    @FXML
    private TableColumn<Song, String> colNombre;

    @FXML
    private TableColumn<Song, String> colArtista;

    @FXML
    private TableColumn<Song, String> colAlbum;

    @FXML
    private TableColumn<Song, String> colYear;

    @FXML
    private TableColumn<Song, String> colGenero;

    @FXML
    private Label errorLista;

    @FXML
    private Label errorInsert;
    
    @FXML
    private Label errorDel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillGenres(genres);
        for (int i = 0; i < genres.size(); i++) {
            genre.getItems().add(genres.get(i));
            newGenre.getItems().add(genres.get(i));
        }

        fillListas(con, listas);
        for (int i = 0; i < listas.size(); i++) {
            lists.getItems().add(listas.get(i));
        }

        logo.setVisible(true);

    }

    /**
     *
     */
    //LA TABLA
    @FXML
    private void fillTabla() {

        colNombre.setCellValueFactory(new PropertyValueFactory<Song, String>("name"));
        colArtista.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        colYear.setCellValueFactory(new PropertyValueFactory<Song, String>("year"));
        colGenero.setCellValueFactory(new PropertyValueFactory<Song, String>("genre"));

        tabla.setItems(fillArrayTabla(con, lists.getValue()));
        logoBright.setVisible(true);
        errorLista.setVisible(false);

    }

    public static ObservableList<Song> fillArrayTabla(MongoClient con, String setlist) {

        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        MongoCollection<Document> collection = database.getCollection(setlist);
        ObservableList<Song> listaTabla = FXCollections.observableArrayList();
        MongoCursor<Document> data = collection.find().iterator();

        while (data.hasNext()) {
            Document s1 = data.next();
            listaTabla.add(new Song(s1.getString("name"), s1.getString("artist"), s1.getString("album"), s1.getString("year"), s1.getString("genre")));

        }
        for (int i = 0; i < listaTabla.size(); i++) {
            System.out.println(listaTabla.get(i).getName() + listaTabla.get(i).getArtist() + listaTabla.get(i).getAlbum() + listaTabla.get(i).getYear() + listaTabla.get(i).getGenre());
        }
        return listaTabla;

    }

    /**
     * ***
     *
     */
    //INSERCIÓN Y CREACIÓN DE CANCIONES
    @FXML
    private void insertSong() {
        if (lists.getValue() == null) {
            errorLista.setVisible(true);
        } else {
            errorLista.setVisible(false);
            if (songName.getText().equalsIgnoreCase("")|| songArtist.getText().equalsIgnoreCase("")) {
                errorInsert.setVisible(true);
            } else {
                errorInsert.setVisible(false);
                Song cancion = new Song(songName.getText(), songArtist.getText(), songAlbum.getText(), songYear.getText(), genre.getValue());
                System.out.println("Canción: " + cancion.getGenre());
                System.out.println("Lista: " + lists.getValue());
                insertarCancion(con, cancion, lists.getValue());
                fillTabla();
            }
        }
    } //CREAMOS UN OBJETO DE CANCIÓN QUE 'MAPEAMOS' A LA BD

    /**
     * *****
     *
     */
    //BORRADO DE CANCIONES
    @FXML
    private void deleteSong() {
        if(delName.getText().equalsIgnoreCase("") || delArtist.getText().equalsIgnoreCase("")){
            errorDel.setVisible(true);
        }else{
        borrarCancion(con, lists.getValue(), delName.getText(), delArtist.getText());
        cerrarDel();
        fillTabla();
    }
    } //CON ESTE MÉTODO BORRAMOS LA CANCIÓN

    @FXML
    private void delSong() {
        if (lists.getValue() == null) {
            errorLista.setVisible(true);
        } else {
            errorLista.setVisible(false);
            ventanaBorrar.setVisible(true);
        }
    } //CON ESTE MÉTODO ABRIMOS LA VENTANA DE BORRADO

    @FXML
    private void cerrarDel() {
        ventanaBorrar.setVisible(false);
        delName.setText("");
        delArtist.setText("");
    } //CERRAMOS LA VENTANA DE BORRADO y limpiamos

    /**
     * ************
     *
     */
    //MODIFICACIÓN
    @FXML
    private void modifySong() {
        if (newName.getText().equalsIgnoreCase("")|| newArtist.getText().equalsIgnoreCase("")) {
            errorMod.setVisible(true);
        } else {
            errorMod.setVisible(false);
            modificarCancion(con, lists.getValue(), modName.getText(), modArtist.getText(), newName.getText(),
                    newArtist.getText(), newAlbum.getText(), newYear.getText(), newGenre.getValue());
            cerrarMod();
            cerrarModPrev();
            fillTabla();
        }
    }  //CON ESTE HACEMOS LA MODIFICACIÓN DE LA CANCIÓN

    @FXML
    private void modSongPrev() {
        if (lists.getValue() == null) {
            errorLista.setVisible(true);
        } else {
            errorLista.setVisible(false);
            ventanaModPrev.setVisible(true);
        }
    } // APERTURA DE LA VENTANA DE VALIDACIÓN PARA LA MODIFICACIÓN

    @FXML
    private void cerrarModPrev() {
        ventanaModPrev.setVisible(false);
    } //CIERRE DE LA VENTANA DE VALIDACIÓN

    @FXML
    private void modSong() {
        if (modName.getText().equalsIgnoreCase("")|| modArtist.getText().equalsIgnoreCase("")) {
            errorModPrev.setVisible(true);
        } else {
            ventanaMod.setVisible(true);
        }
    } //APERTURA DE PANTALLA FINAL DE MODIFICACIÓN

    @FXML
    private void cerrarMod() {
        ventanaMod.setVisible(false);
        modName.setText("");
        modArtist.setText("");
        newName.setText("");
        newArtist.setText("");
        newAlbum.setText("");
        newYear.setText("");
        newGenre.setValue("");

    } //CIERRE DE PANTALLA FINAL DE MODIFICACIÓN y limpieza de campos

    /**
     * *********
     *
     */
    //GESTOR DE SETLIST
    @FXML
    private void gestorSetlist() throws IOException {
        switchToSecondary();
    } //CAMBIO AL CONTROLADOR DE SETLIST

}
