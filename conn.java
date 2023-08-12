package ATM_Interface;

import java.sql.*;

public class conn {
    Connection c;
    Statement s;
    public conn()
    {
        try
        {
            c = DriverManager.getConnection("jdbc:mysql:///ATMInterface", "root", "MdMySQL@310702");
            s = c.createStatement();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
