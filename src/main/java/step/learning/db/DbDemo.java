package step.learning.db;

import com.mysql.cj.jdbc.Driver;
import org.json.JSONObject;
import step.learning.control.HomeWork1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

public class DbDemo {
    private String url;
    private String user;
    private String password;
    private com.mysql.cj.jdbc.Driver driverSql;
    private java.sql.Connection connection;
    private RandomValue randomValue;

    public void run() {
        JSONObject conf = config();
        JSONObject dbConf = conf.getJSONObject("DataProviders").getJSONObject("PlanetScale");
        url = dbConf.getString("url");
        user = dbConf.getString("user");
        password = dbConf.getString("password");
        if ((connection = this.connect()) == null) {
            return;
        }
        this.rowsCountInSegment();
        /*this.ensureCreated();*/
        /* showRandoms();*/
        /*  this.client();*/
        this.disConnect();
    }
   /* -----------------HomeWork-------------------- */
    private void rowsCountInSegment(){
        System.out.println("Enter the lower limit of the range:");
        Scanner scanner = new Scanner(System.in);
        int lower = scanner.nextInt();
        System.out.println("Enter the upper limit of the range:");
        int upper = scanner.nextInt();

        String sql =  "SELECT * FROM jpu121_randoms " +
                "WHERE val_int >= ? AND val_int <= ?";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, lower);
            preparedStatement.setInt(2, upper);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.printf(
                        "%s %d  %s %f %n",
                        resultSet.getString( 1 ) ,
                        resultSet.getInt( 2),
                        resultSet.getString( 3 ) ,
                        resultSet.getFloat(4)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void client() {
        System.out.println("Enter the numbers og lines:");
        Scanner scanner = new Scanner(System.in);
        int lines = scanner.nextInt();
        try {
            String sql = "INSERT INTO jpu121_randoms(" +
                    "`id`, `val_int`, `val_str`, `val_float`)" +
                    " VALUES(UUID(), ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < lines; i++) {
                this.ensureInsert(preparedStatement);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
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
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    private void ShowCount(){
        try( PreparedStatement prep = this.connection.prepareStatement(
                "SELECT COUNT(id) FROM jpu121_randoms"
        )) {
            ResultSet res = prep.executeQuery() ;// посілання на перший рядок--> Iterator#
            res.next() ;
            System.out.println( "Rows count: " + res.getInt( 1 ) ) ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
        }
    }
    private void ensureInsert(PreparedStatement preparedStatement) {
        randomValue = new RandomValue();
        int val_int = randomValue.randomInteger();
        float val_float = randomValue.randomFloat();
        String val_str = randomValue.randomString();
        try {
            preparedStatement.setInt(1, val_int);
            preparedStatement.setString(2, val_str);
            preparedStatement.setFloat(3, val_float);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
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

    private void showRandoms() {
        String sql = "SELECT `id`, `val_int`, `val_str`,`val_float` FROM jpu121_randoms" ;   // ';' у кінці SQL команди не потрібна
        try( Statement statement = this.connection.createStatement() ) {
            ResultSet res = statement.executeQuery( sql ) ;  // ADO ~ SqlDataReader
            // ResultSet res - об'єкт для трансферу даних, що є результатом запиту
            // Особливість БД - робота з великими даними, що означає відсутність
            // єдиного результату, та одержання даних рядок-за-рядком (ітерування)
            // res.next() - одержання нового рядка (якщо є - true, немає - false)
            while( res.next() ) {
                System.out.printf(
                        "%s %d  %s %f %n",
                        // дані рядку доступні через get-тери
                        res.getString( 1 ) ,      // !!! у JDBC відлік починається з 1 !!!
                        res.getInt( "val_int" ),
                        res.getString( 3 ) ,
                        res.getInt( "val_float" )// за іменем колонки
                );
            }
            res.close() ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
        }
    }

    private void insertPrepared(int val_int, float val_float, String val_str) {
          /*
        Підготовлені (prepared) запити - можна вважати тимчасовими збережними процедурами
        (скомпільовані запити, які зберігаються з боку СУБД)
        Ідея - запит компілюється і скомпільований код залишається у СУБД протягом відкритого
        з'єднання. Протягом цього часу запит можна повторити, у т.ч. з іншими параметрами
        (за що їх також називають параметризованими запитами)
         */
        String sql = "INSERT INTO jpu121_randoms(" +
                "`id`, `val_int`, `val_str`, `val_float`)" +
                " VALUES(UUID(), ?, ?, ?)";
          /*
        Місця для варіативних даних замінюються на "?", незмінні дані (як-то UUID()) залишаються
        у запиті. Якщо значення беруться у лапки, то знаки "?" все одно - без лапок
         */
            /*
            На другому етапі (після підготовки - створення тимчасової процедури)
            здійснюється заповнення параметрів. Бажано використовувати конкретні
            типи даних сеттерів (уникати без потреби узагальненого setObject)
             */
        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setInt(1, val_int);
            prep.setString(2, val_str);
            prep.setFloat(3, val_float);
               /*Третій етап - виконання*/
            prep.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
    /*        Java                                                  DB
       prepareStatement                                           proc_tmp() {
    "SELECT COUNT(id) FROM jpu121_randoms"     ---------------->    return SELECT COUNT(id) FROM jpu121_randoms }
       res = prep.executeQuery()               ----------------> CALL proc_tmp() --> Iterator#123
           (res==Iterator#123)               <-- Iterator#123 --
       res.next()                              ---------------->   Iterator#123.getNext() - береться 1й рядок
                                            <-- noname: 7 ------
       res.getInt( 1 ) - 7
     */
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
    String sql = String.format(
                Locale.US,  // Для того щоб десятична кома була точкою
                "INSERT INTO jpu121_randoms(`id`, `val_int`, `val_str`,`val_float`)" +
                " VALUES( UUID(), %d, '%s', %f )",
                random.nextInt(),
                random.nextInt() + "",
                random.nextDouble()
        ) ;
        */

