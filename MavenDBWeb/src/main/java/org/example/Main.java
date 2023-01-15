package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class Main {     // BOTのアクション
    public static void main(String[] args) {
        try {       // DB接続
            Dotenv dotenv = Dotenv.configure().load();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dam", dotenv.get("UNAME"), dotenv.get("PSWD"));
            Statement stmt = con.createStatement(); // DB接続終わり

            dataAdd(stmt);
            dataShow(stmt);       // データを観る
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void dataShow(Statement stmt) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("select * from damdata;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                if (!rs.next()) break;
                Damdata dm = new Damdata();
                // id is index 1
                dm.year = rs.getInt(2);
                dm.month = rs.getInt(3);
                dm.day = rs.getInt(4);
                dm.hour = rs.getInt(5);
                dm.waterVolume = rs.getInt(6);
                dm.percentage = Float.parseFloat(rs.getString(7));
                dm.diff = Float.parseFloat(rs.getString(8));
                dm.dataCreated = rs.getString(9);
                System.out.println(dm.toStringwithid(rs.getInt(1)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dataAdd(Statement s) throws SQLException {   // 取得したデータをDBに格納する
        ResultSet datavolume = s.executeQuery("select count(*) from damdata;");     // データ数を取得
        String dd = Dam.main();     // ダム情報取得
        ObjectMapper objmap = new ObjectMapper();   // 値をクラスに入れ込む
        Damdata obj = null;

        try {       // 値をマッピング
            obj = objmap.readValue(dd, Damdata.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (datavolume.next() && obj != null) {      // データを追加させる
            int t = datavolume.getInt(1) + 1;
            String makeQuery = String.format("INSERT into damdata values(%d,%d,%d,%d,%d,%d,\'%s\',\'%s\',\'%s\')"
                    , t, obj.year, obj.month, obj.day, obj.hour, obj.waterVolume, obj.percentage, obj.diff, obj.dataCreated);
            s.executeUpdate(makeQuery);
        }
    }
}