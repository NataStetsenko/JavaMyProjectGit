package step.learning.db;

import com.mysql.cj.jdbc.Driver;
import org.json.JSONObject;
import step.learning.control.HomeWork1;
import step.learning.db.dao.RecordDao;
import step.learning.db.dto.Record;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DbDemo {
    private String url;
    private String user;
    private String password;
    private com.mysql.cj.jdbc.Driver driverSql;
    private java.sql.Connection connection;
    private RandomValue randomValue;
    private final Random random = new Random() ;

    private RecordDao recordDao ;
    public void run() {
        JSONObject conf = config();
        JSONObject dbConf = conf.getJSONObject("DataProviders").getJSONObject("PlanetScale");
        url = dbConf.getString("url");
        user = dbConf.getString("user");
        password = dbConf.getString("password");
        if ((connection = this.connect()) == null) {
            return;
        }
        recordDao = new RecordDao(random, connection) ;
        if( recordDao.ensureCreated() ) {
            System.out.println( "Ensure OK" ) ;
        }
       /* createRandomRecords( 3 ) ;
         ShowRandomsCount() ;
         System.out.println(recordDao.getById(
                UUID.fromString("5216e187-905f-4311-a39c-566734e99715"))) ;*/

      /*  Record record = new Record() ;
        record.setValInt( 100 ) ;
        record.setValFloat( 100.500 ) ;
        record.setValStr( "Hello" ) ;
        recordDao.create( record ) ;*/
        System.out.println("-------------------HomeWork 2------------------");

        recordDao.delete(UUID.fromString("5216e187-905f-4311-a39c-566734e99715"));

           recordDao.deleteRecord(recordDao.getById(
                UUID.fromString("f69d5ed9-2946-11ee-b81c-e686aec5586c")));

        Record record = recordDao.getById(UUID.fromString("081a4f15-2949-11ee-b81c-e686aec5586c"));
        record.setValInt(99);
        record.setValStr("updateRecord");
        record.setValFloat(99.999);
        recordDao.updateRecord(record);

        showAll();



       /* this.rowsCountInSegment();*/
        /*this.ensureCreated();*/
        /* showRandoms();*/
        /*  this.client();*/
        this.disConnect();
    }

    private void showAll() {
        List<Record> records = recordDao.getAll() ;
        if( records == null ) {
            System.out.println( "Error getting list" ) ;
        }
        else {
            for( Record record : records ) {
                System.out.println( record ) ;
            }
        }
    }
    private void ShowRandomsCount() {
        int cnt = recordDao.getCount() ;
        if( cnt == -1 ) {
            System.out.println( "Counter error" ) ;
        }
        else {
            System.out.println( "Rows count: " + cnt ) ;
        }
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

    /* -----------------config-------------------- */
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
/*
DTO, DAO
DTO - Data Transfer Object - об'єкт для передачі даних - структура, яка
містить поля (або властивості), їх аксесори, конструктори та утіліти
(toString(), toJson(), тощо). Не містять логіку. Аналог - сутність (Entity)

DAO - Data Access Object - об'єкт доступу до даних - логіка роботи з
об'єктами DTO. Аналог LINQ

Наприклад
UserDTO {
    private UUID id; String name;
    public UUID getId()...
}

UserDAO {
    public UserDTO getUserById( UUID id ) {...}
}
 */