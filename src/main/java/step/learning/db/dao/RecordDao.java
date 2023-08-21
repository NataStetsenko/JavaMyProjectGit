package step.learning.db.dao;
import step.learning.db.dto.Record;
import java.sql.*;
import java.util.*;

/*
Д.З. Реалізувати повний набір CRUD інструкцій:
C: create, createRandom
R: getById, getAll
U: boolean update( Record )  -- по id в переданому записі внести нові дані
D: boolean delete( Record )  -- по id в переданому записі видалити
   boolean delete( UUID ) -- видалити по id
 */
public class RecordDao {
    private final Random random ;
    private final Connection connection ;

    public RecordDao(Random random, Connection connection) {
        this.random = random;
        this.connection = connection;
    }

    public boolean delete( UUID id){
        if( id == null ) {
            return false;
        }
        String sql = String.format(
                "DELETE FROM jpu121_randoms WHERE id='%s' ", id.toString()) ;
        try(Statement statement = this.connection.createStatement() ) {
            statement.executeUpdate(sql);
            return true;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return false ;
        }
    }
    public boolean deleteRecord(Record record){
        if(record == null|| record.getId()==null) {
            return false;
        }

        String sql = String.format(
                "DELETE FROM jpu121_randoms WHERE id='%s' ",  record.getId().toString()) ;
        try(Statement statement = this.connection.createStatement() ) {
            statement.executeUpdate(sql);
            return true;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return false ;
        }
    }

    public boolean updateRecord(Record record){
        if(record == null || record.getId() == null ) {
            return false;
        }
        String sql = "UPDATE jpu121_randoms SET val_int=?, val_str=?, val_float=? WHERE id=? " ;
        try( PreparedStatement prep = this.connection.prepareStatement( sql ) ) {
            prep.setInt(1, record.getValInt());
            prep.setString(2, record.getValStr());
            prep.setDouble(3, record.getValFloat());
            prep.setString(4, record.getId().toString());
            prep.execute() ;
            return true;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return false ;
        }
    }
    public Record getById( UUID id ) {
        if( id == null ) {
            return null ;
        }
        String sql = String.format(
                "SELECT * FROM jpu121_randoms WHERE id='%s' ",
                id.toString() ) ;
        try( Statement statement = this.connection.createStatement() ) {
            ResultSet res = statement.executeQuery( sql ) ;

            if(!res.next()){
                System.out.println("Not found");
                return null ;
            }
            return new Record( res ) ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return null ;
        }
    }
    public List<Record> getAll() {
        String sql = "SELECT * FROM jpu121_randoms" ;
        try( Statement statement = this.connection.createStatement() ) {
            ResultSet res = statement.executeQuery( sql ) ;
            List<Record> ret = new ArrayList<>() ;
            while( res.next() ) {
                ret.add( new Record( res ) ) ;
            }
            return ret ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return null ;
        }
    }
    public boolean ensureCreated() { // run()
        String sql = "CREATE TABLE IF NOT EXISTS jpu121_randoms (" +
                " `id`        CHAR(36) PRIMARY KEY," +
                " `val_int`   INT," +
                " `val_str`   VARCHAR(256)," +
                " `val_float` FLOAT" +
                ")" ;
        System.out.print( "ensureCreated: " ) ;
        try( Statement statement = this.connection.createStatement() ) {   // ADO.NET: SqlCommand
            statement.executeUpdate( sql ) ;   // executeUpdate - для запитів без повернення
            return true ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return false ;
        }
    }
    public void createRandomRecord() {
        String sql = String.format(
                Locale.US,  // Для того щоб десятична кома була точкою
                "INSERT INTO jpu121_randoms(`id`, `val_int`, `val_str`,`val_float`)" +
                        " VALUES( UUID(), %d, '%s', %f )",
                random.nextInt(),
                random.nextInt() + "",
                random.nextDouble()
        ) ;
        try( Statement statement = this.connection.createStatement() ) {
            statement.executeUpdate( sql ) ;
            System.out.println( "INSERT OK" ) ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
        }
    }
    public boolean create( Record record ) {
        String sql = "INSERT INTO jpu121_randoms(`id`, `val_int`, `val_str`,`val_float`)" +
                " VALUES( ?, ?, ?, ? ) " ;
        try( PreparedStatement prep = this.connection.prepareStatement( sql ) ) {
            prep.setString( 1,
                    record.getId() == null
                            ? UUID.randomUUID().toString()
                            : record.getId().toString()
            ) ;
            prep.setInt( 2, record.getValInt() ) ;
            prep.setString( 3, record.getValStr() ) ;
            prep.setDouble( 4, record.getValFloat() ) ;
            return prep.execute() ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return false ;
        }
    }
    public int getCount() {
        try( PreparedStatement prep = this.connection.prepareStatement(
                "SELECT COUNT(id) FROM jpu121_randoms"
        )) {
            ResultSet res = prep.executeQuery() ;
            res.next() ;
            return res.getInt( 1 ) ;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return -1 ;
        }
    }
    private boolean createRandomRecords(int rowCount) {
        /*
        Підготовлені (prepared) запити - можна вважати тимчасовими збережними процедурами
        (скомпільовані запити, які зберігаються з боку СУБД)
        Ідея - запит компілюється і скомпільований код залишається у СУБД протягом відкритого
        з'єднання. Протягом цього часу запит можна повторити, у т.ч. з іншими параметрами
        (за що їх також називають параметризованими запитами)
         */
        String sql = "INSERT INTO jpu121_randoms(`id`, `val_int`, `val_str`, `val_float`)" +
                " VALUES( UUID(), ?, ?, ? )" ;
        /*
        Місця для варіативних даних замінюються на "?", незмінні дані (як-то UUID()) залишаються
        у запиті. Якщо значення беруться у лапки, то знаки "?" все одно - без лапок
         */
        try( PreparedStatement prep = this.connection.prepareStatement( sql ) ) {
            /*
            На другому етапі (після підготовки - створення тимчасової процедури)
            здійснюється заповнення параметрів. Бажано використовувати конкретні
            типи даних сеттерів (уникати без потреби узагальненого setObject)
             */
            for (int i = 0; i < rowCount; i++) {
                prep.setInt(1, random.nextInt());  // !!! відлік від 1 -- на місце першого "?"
                prep.setString(2, random.nextInt() + "");
                prep.setDouble(3, random.nextDouble());
                prep.execute();
            }
            return true;
        }
        catch( SQLException ex ) {
            System.err.println( ex.getMessage() ) ;
            return false;
        }
    }
}