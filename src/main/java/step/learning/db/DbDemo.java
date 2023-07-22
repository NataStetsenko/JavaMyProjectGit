package step.learning.db;

import com.mysql.cj.jdbc.Driver;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbDemo {
    private String url;
    private String user;
    private String password;
    private com.mysql.cj.jdbc.Driver driverSql;
    private java.sql.Connection connection;

    public void run() {
        System.out.println("DB Demo");
        JSONObject conf = config();

        JSONObject dbConf = conf.getJSONObject("DataProviders").getJSONObject("PlanetScale");
        url = dbConf.getString("url");
        user = dbConf.getString("user");
        password = dbConf.getString("password");
        if ((connection = this.connect()) == null) {
            return;
        }
        this.ensureCreated();

        this.disConnect();

    }

    private void ensureCreated() {
        String sql = "CREATE TABLE IF NOT EXISTS jpu121_randoms (" +
                " `id`          CHAR(36) PRIMARY KEY," +
                " `val_int`     INT," +
                " `val_str`     VARCHAR(256)," +
                " `val_float`   FLOAT" +
                ")";
        try (Statement statement = this.connection.createStatement()) { // ADO.NET SqlCommand
            statement.executeUpdate(sql); // executeUpdate - для запитів без повернення
            System.out.println("Ok");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private JSONObject config() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("appsetting.json"))) {
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return new JSONObject(sb.toString());
    }

    private java.sql.Connection connect() {
        // реєструємо драйвер
        // а) через Class.forName("com.mysql.cj.jdbc.Driver");
        // б) через пряме створення драйвера
        try {
            driverSql = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driverSql);
            // підключаємось, маючи зареєстрований драйвер
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void disConnect() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (driverSql != null) {
                DriverManager.deregisterDriver(driverSql);
            }

        } catch (SQLException ignored) {
        }
    }
}
/*Робота з базами даних. JDBC.
        1. Конфігурація. Робота з варіативним JSON
        - підключаємо пакет (залежність) org.json
        - деталі роботи - у методах config() та run()
        2. Конектор (драйвер підключення)
        - на Maven шукаємо відповідний драйвер (MySQL) для JDBC
        3. Робота з БД
        - підключення

        Налаштування IDE
        Database (tool-window) - + - DataSource - MySQL -
        option: URL Only
        вводимо дані з конфігурації
        Test Connection - Apply - OK

        */

    /*
Д.З. Реалізувати внесення даних у БД (jpu121_randoms):
- запитати у користувача кількість рядків, що їх внести до БД
- згенерувати відповідну кількість випадкових даних
- виконати команди внесення даних у БД (засобами Java)

INSERT INTO jpu121_randoms(`id`, `val_int`, `val_str`,`val_float`) VALUES( UUID(), 10, 'asdgas', 0.12 )
     */