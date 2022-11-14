module com.mycompany.jhb_music_setlist {
    requires javafx.controls;
    requires javafx.fxml;
    requires mongo.java.driver;
 
    opens com.mycompany.jhb_music_setlist to javafx.fxml;
    exports com.mycompany.jhb_music_setlist;
}
