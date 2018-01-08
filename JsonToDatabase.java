import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by anh on 12/28/17.
 */
public class JsonToDatabase {
    private static String url = "jdbc:mysql://localhost/plantsensor";
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
                addJsonObjectToDatabase(array.get(i).getAsJsonObject());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String getJsonObject(JsonObject obj, String atr) {
        try {
            return obj.get(atr).getAsString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static void addJsonObjectToDatabase(JsonObject object) {
        //TODO : Check if plant already exist
        String query = "INSERT INTO planttype ( name,familyName, waterGuide,fertilizerGuide,mistGuide,lightGuide,tempGuide,phGuide,acidGuide,toxicGuide,climateGuide) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1,getJsonObject(object,"name"));

            statement.setString(2,getJsonObject(object,"family"));

            statement.setString(3,getJsonObject(object,"water"));

            statement.setString(4,getJsonObject(object,"fertilizer"));

            statement.setString(5,getJsonObject(object,"mist"));

            statement.setString(6,getJsonObject(object,"light"));

            statement.setString(7,getJsonObject(object,"temperature"));

            statement.setString(8,getJsonObject(object,"ph"));

            statement.setString(9,getJsonObject(object,"acid"));


            statement.setString(10,getJsonObject(object,"toxic"));

            statement.setString(11,getJsonObject(object,"climate"));
            statement.executeUpdate();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
