/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import com.mycompany.jhb_music_setlist.Song;
import BD.ConnectionDB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mycompany.jhb_music_setlist.Song;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;

/**
 *
 * @author julen
 */
public class CRUD_Song {

    MongoClient con;

    public static void insertarCancion(MongoClient con, Song song, String setlist) {
        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        MongoCollection<Document> collection = database.getCollection(setlist);
        Document c1 = new Document();
        c1.append("name", song.getName())
                .append("artist", song.getArtist())
                .append("album", song.getAlbum())
                .append("year", song.getYear())
                .append("genre", song.getGenre());

        collection.insertOne(c1);
    }
    

    public static void borrarCancion(MongoClient con, String setlist, String nombre, String artista) {
        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        MongoCollection<Document> collection = database.getCollection(setlist);
        collection.deleteMany(Filters.and(Filters.eq("name", nombre), Filters.eq("artist", artista)));
    }

    public static void modificarCancion(MongoClient con, String setlist, String nombre, String artista, String newNombre, String newArtista, String newAlbum, String newYear, String newGenero) {
        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        MongoCollection<Document> collection = database.getCollection(setlist);
        try {
            Document d1 = collection.find(Filters.and(Filters.eq("name", nombre), Filters.eq("artist", artista))).first();
            //System.out.println(d1.toJson());
            String id = d1.get("_id").toString();
            System.out.println(id);
            collection.updateOne(new Document(collection.find(Filters.and(Filters.eq("name", nombre), Filters.eq("artist", artista))).first()),
                    new Document("$set", new Document("name", newNombre)
                            .append("artist", newArtista)
                            .append("album", newAlbum)
                            .append("year", newYear)
                            .append("genre", newGenero)));
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public static void crearSetlist(MongoClient con, String nameCollection) {
        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        database.createCollection(nameCollection);
        System.out.println("Nuevo setlist añadido");
    }

    public static void borrarSetlist(MongoClient con, String nameCollection) {
        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        MongoCollection<Document> collection = database.getCollection(nameCollection);
        collection.drop();
        System.out.println("Setlist eliminada");
    }

    public static void fillListas(MongoClient con, ArrayList<String> listas) {
        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        MongoIterable<String> list = database.listCollectionNames();
        try {
            for (String name : list) {
                listas.add(name);
            }
        } catch (NullPointerException e) {
            System.out.println("No hay setlist existentes, crea una nueva");
        }

    }

    /*public static ObservableList<Song> fillArrayTabla(MongoClient con, String setlist) {
        con = ConnectionDB.conectar();
        MongoDatabase database = con.getDatabase("jhbmusic");
        MongoCollection<Document> collection = database.getCollection(setlist);
        ObservableList<Song> listaTabla = FXCollections.observableArrayList();
        MongoCursor<Document> data = collection.find().iterator();

        while (data.hasNext()) {
            Document s1 = data.next();
            listaTabla.add(new Song(s1.getString("name"), s1.getString("artist"), s1.getString("album"), s1.getString("year"), s1.getString("genre")));
            
            }
        for(int i = 0; i<listaTabla.size(); i++){
            System.out.println(listaTabla.get(i).getName() + listaTabla.get(i).getArtist() + listaTabla.get(i).getAlbum() + listaTabla.get(i).getYear() + listaTabla.get(i).getGenre());
            }
        return listaTabla;

    }*/

    public static void fillGenres(ArrayList<String> generos) {
        generos.add("Desconocido");
        generos.add("Alternativo");
        generos.add("Classic");
        generos.add("Pop");
        generos.add("Pop/Rock");
        generos.add("R&B");
        generos.add("Soul");
        generos.add("Jazz");
        generos.add("Blues");
        generos.add("Bebop");
        generos.add("Indie");
        generos.add("Indie/Rock");
        generos.add("Rock&Roll");
        generos.add("Funk");
        generos.add("Rock Alternativo");
        generos.add("Hard Rock");
        generos.add("Punk");
        generos.add("Punk Rock");
        generos.add("Post-Punk");
        generos.add("Oi!");
        generos.add("Metal");
        generos.add("Heavy Metal");
        generos.add("Nu-Metal");
        generos.add("Thrash Metal");
        generos.add("Grunge");
        generos.add("Techno");
        generos.add("Acid Techno");
        generos.add("Hard Techno");
        generos.add("House Techno");
        generos.add("Rap");
        generos.add("Trap");
        generos.add("Flamenco");
        generos.add("Rumba");
        generos.add("Reggae");
        generos.add("Reggaeton");
        generos.add("Bachata");
        generos.add("Merengue");
        generos.add("Salsa");
        generos.add("Bolero");
        generos.add("Tango");
        generos.add("Latin");
        generos.add("K-Pop");
        generos.add("Urban");
        generos.add("Lo-Fi");
        generos.add("Drum&Bass");
        generos.add("Disco");
        generos.add("Otro");
    }

}
