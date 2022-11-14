package com.mycompany.jhb_music_setlist;

import com.mongodb.MongoClient;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static utilidades.CRUD_Song.borrarSetlist;
import static utilidades.CRUD_Song.crearSetlist;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
    MongoClient con;
    
    @FXML
    private Button nuevo;
    
    @FXML
    private TextField nameNewSetlist;
    
    @FXML
    private Label newSetListError;
    
    
    @FXML
    private Button borrar;
    
    @FXML
    private TextField nameDelSetlist;
    
    @FXML
    private Label delSetListError;
    
    @FXML
    private Button volver;
    
    @FXML
    private void returnPrincipal() throws IOException{
        switchToPrimary();
    }
    
    @FXML
    private void newSetlist() throws IOException{
        if(nameNewSetlist.getText().equalsIgnoreCase("")){
            newSetListError.setVisible(true);
        }else{
            crearSetlist(con, nameNewSetlist.getText());
            newSetListError.setVisible(false);
            switchToPrimary();
        }
    }
    
    @FXML
    private void dropSetlist() throws IOException{
        if(nameDelSetlist.getText().equalsIgnoreCase("")){
            delSetListError.setVisible(true);
        }else{
            borrarSetlist(con, nameDelSetlist.getText());
            delSetListError.setVisible(false);
            switchToPrimary();
        }
    }
    
}
    