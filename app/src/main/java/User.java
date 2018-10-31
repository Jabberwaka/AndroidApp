import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {

    private String username;
    private String userpass;

    //constructor puts in username and scrambles the
    public User (String uN, String uP){
        this.username = uN;
        this.userpass = scramble(uP);
    }

    //function to scramble the password of the user for security purposes
    private String scramble (String uP){
        String passScramble = null;

        for (int i = 0; i < uP.length()-1; ++i){
            if(uP.charAt(i) > 'Z'){
                passScramble.concat(Integer.toString(uP.charAt(i)-i));
            }
            else{
                passScramble.concat(Integer.toString(uP.charAt(i)+i));
            }

            //salting
            if(i == uP.length()/2){
                passScramble.concat("0x00");
            }

        }
        return passScramble;
    }


    public void passwordScrambler (){
        if(userpass != null){
            userpass = scramble(userpass);
        }
    }


    //adds the created user to the database
    public String addToDatabase(){

        //this would be where we add constraints to make sure the username/password are up to permits
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String db = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection dbCon = DriverManager.getConnection("jdbc:jtds:sqlserver://traffic-cam.database.windows.net:1433/Android;user=tyler@traffic-cam;password=Password!;");
            db = dbCon.toString();

            Statement stmt = dbCon.createStatement();

            //checks to see if the username is already created in the database
            String query = "SELECT COUNT(*) FROM Users WHERE user_name = '" + username + "';";
            ResultSet rs = stmt.executeQuery(query);
            int check = 2;
            if(rs.next()){
                if (Integer.parseInt(rs.getString(1)) != 0){ //if the username is already in the database
                    return "User already has a profile created";
                }
            }

             query = "INSERT INTO Users (user_name, user_password) VALUES " +
                    "(" + username + ", " + userpass + ");";
            stmt.executeQuery(query);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return "User could not be created";
        }

        return "Successfully created user";
    }

}
