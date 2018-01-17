import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.gtranslate.Language;
import com.gtranslate.Translator;
/**
 * Created by anh on 12/28/17.
 */
public class JsonToDatabase {
    private static String url = "jdbc:mysql://localhost/plantsensor";
    private static String baseUrl = "jdbc:mysql://localhost/";
    private static String user = "root";
    private static String password = "Tuan1991apha";
    private static Connection conn ;
    private static int userID = 0;
    public static void main (String[] args) {

        JsonParser parser = new JsonParser();
        try {
            Object obj = parser.parse(new FileReader("output_json.json"));
            JsonArray array = (JsonArray) obj;
            for (int i = 0; i < array.size(); i++) {
                addOrFullfillObjectToDatabase(array.get(i).getAsJsonObject());
                //addJsonObjectToDatabase(array.get(i).getAsJsonObject());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String getJsonObject(JsonObject obj, String atr) {
        try {
            return obj.get(atr).getAsString();
        } catch (NullPointerException e) {
            //System.out.print(atr + " not found at " + obj.get("name")+"\n");
            System.out.print(obj.toString()+"\n");
            return "";
        }
    }

    public static void transferAllToGerman(String germanDatabase) {
        String urlTrans = baseUrl+germanDatabase;
        String selectAllQuery = "select * from planttype;";
        ResultSet set ;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = conn.prepareStatement(selectAllQuery);
            set = statement.executeQuery();
            String queryGerman = "INSERT INTO planttypegerman ( name,familyName, waterGuide,fertilizerGuide,mistGuide,lightGuide,tempGuide,phGuide,acidGuide,toxicGuide,climateGuide) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            while (set.next()) {
                String name = set.getString(1);
                String familyName = set.getString(2);
                String waterGuide = set.getString(3);
                String fertilizerGuide = set.getString(4);
                String mistGuide = set.getString(5);
                String lightGuide = set.getString(6);
                String tempGuide = set.getString(7);
                String phGuide = set.getString(8);
                String acidGuide = set.getString(9);
                String toxicGuide = set.getString(10);
                String climateGuide = set.getString(11);

                Translator translator = Translator.getInstance();
                name = translator.translate(name, Language.ENGLISH, Language.GERMAN);
                familyName = translator.translate(familyName, Language.ENGLISH, Language.GERMAN);
                waterGuide = translator.translate(waterGuide, Language.ENGLISH, Language.GERMAN);
                fertilizerGuide = translator.translate(fertilizerGuide, Language.ENGLISH, Language.GERMAN);
                mistGuide = translator.translate(mistGuide, Language.ENGLISH, Language.GERMAN);
                lightGuide = translator.translate(lightGuide, Language.ENGLISH, Language.GERMAN);
                tempGuide = translator.translate(tempGuide, Language.ENGLISH, Language.GERMAN);
                phGuide = translator.translate(phGuide, Language.ENGLISH, Language.GERMAN);
                acidGuide = translator.translate(acidGuide, Language.ENGLISH, Language.GERMAN);
                toxicGuide = translator.translate(toxicGuide, Language.ENGLISH, Language.GERMAN);
                climateGuide = translator.translate(climateGuide, Language.ENGLISH , Language.GERMAN);

                statement.setString(1, name);
                statement.setString(2, familyName);
                statement.setString(3, waterGuide);
                statement.setString(4, fertilizerGuide);
                statement.setString(5, mistGuide);
                statement.setString(6, lightGuide);
                statement.setString(7, tempGuide);
                statement.setString(8, phGuide);
                statement.setString(9, acidGuide);
                statement.setString(10, toxicGuide);
                statement.setString(11, climateGuide);

                statement.executeUpdate();



            }
            conn.close();

        } catch (Exception e) {

        }
    }

    public static void addOrFullfillObjectToDatabase(JsonObject object) {
        String existenzQuery = "SELECT * FROM planttype WHERE familyName LIKE ?;";
        String addQuery =  "INSERT INTO planttype ( name,familyName,category, availableColors, bloomTime,spaceRange,heightRange,basiccare, waterGuide,fertilizerGuide,lightGuide,tempGuide,soil) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        boolean exist = false;
        int id = 0 ;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = conn.prepareStatement(existenzQuery);
            statement.setString(1,object.get("family").getAsString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                //System.out.print("found duplicate " +  object.get("name").getAsString());
                conn.close();
                exist = true;
                id = set.getInt("id");

            }
            conn.close();
        } catch (Exception e) {

        }

        if (!exist) {
            //System.out.print("w");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
                conn.setAutoCommit(false);
                PreparedStatement statement = conn.prepareStatement(addQuery);

                statement.setString(1, getJsonObject(object, "name"));
                statement.setString(2, getJsonObject(object, "family"));


                statement.setString(3, getJsonObject(object, "category"));
                statement.setString(4, getJsonObject(object, "availableColors"));

                statement.setString(5, getJsonObject(object, "bloomTime"));

                statement.setString(6, getJsonObject(object, "spaceRange"));

                statement.setString(7, getJsonObject(object, "heightRange"));

                statement.setString(8, getJsonObject(object, "basicCare"));

                statement.setString(9, getJsonObject(object, "watering"));

                statement.setString(10, getJsonObject(object, "feed"));


                statement.setString(11, getJsonObject(object, "plantLight"));

                statement.setString(12, getJsonObject(object, "lowTemp"));

                statement.setString(13, getJsonObject(object, "soil"));
                statement.executeUpdate();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String fillingQuery = "UPDATE planttype SET category = ?, availableColors = ?" +
                    ", bloomTime = ?, heightRange = ?, spaceRange = ?, basiccare = ?, soil = ? WHERE id = ?";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
                conn.setAutoCommit(false);
                PreparedStatement statement = conn.prepareStatement(fillingQuery);

                statement.setString(1, getJsonObject(object, "category"));

                statement.setString(2, getJsonObject(object, "availableColors"));

                statement.setString(3, getJsonObject(object, "bloomTime"));

                statement.setString(4, getJsonObject(object, "heightRange"));

                statement.setString(5, getJsonObject(object, "spaceRange"));

                statement.setString(6, getJsonObject(object, "basicCare"));

                statement.setString(7, getJsonObject(object, "soil"));

                statement.setInt(8, id);

                statement.executeUpdate();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void addJsonObjectToDatabase(JsonObject object) {

        String existenzQuery = "SELECT * FROM planttype WHERE name= ?;";
        boolean exist = false;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = conn.prepareStatement(existenzQuery);
            statement.setString(1,object.get("name").getAsString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                //System.out.print("found duplicate " +  object.get("name").getAsString());
                conn.close();
                return;

            }
            conn.close();
        } catch (Exception e) {

        }
        if (true) {
            String query = "INSERT INTO planttype ( name,familyName, waterGuide,fertilizerGuide,mistGuide,lightGuide,tempGuide,phGuide,acidGuide,toxicGuide,climateGuide) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
            System.out.print("w");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
                conn.setAutoCommit(false);
                PreparedStatement statement = conn.prepareStatement(query);

                statement.setString(1, getJsonObject(object, "name"));

                statement.setString(2, getJsonObject(object, "family"));

                statement.setString(3, getJsonObject(object, "water"));

                statement.setString(4, getJsonObject(object, "fertilizer"));

                statement.setString(5, getJsonObject(object, "mist"));

                statement.setString(6, getJsonObject(object, "light"));

                statement.setString(7, getJsonObject(object, "temperature"));

                statement.setString(8, getJsonObject(object, "ph"));

                statement.setString(9, getJsonObject(object, "acid"));


                statement.setString(10, getJsonObject(object, "toxic"));

                statement.setString(11, getJsonObject(object, "climate"));
                statement.executeUpdate();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
