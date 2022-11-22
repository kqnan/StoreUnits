package me.kqn.storeunits.Data.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.kqn.storeunits.Config.Config;
import me.kqn.storeunits.Data.PlayerData;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class Database implements DataSource{


    private static String SAVE = "INSERT INTO StoreUnits(UUID,Data) VALUE(?,?) ON DUPLICATE KEY UPDATE Data=?";

    private HikariDataSource hikari;


    public Database() {


            int port = Integer.parseInt(Config.getMysql_port());
            String ip =Config.getMysql_host();
            String database = Config.getMysql_database();
            String username = Config.getMysql_username();
            String password = Config.getMysql_password();
            String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?autoReconnect=true";
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(connectionString);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("databaseName", database);
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.addDataSourceProperty("useServerPrepStmts", true);
            config.addDataSourceProperty("useLocalSessionState", true);
            config.addDataSourceProperty("rewriteBatchedStatements", true);
            config.addDataSourceProperty("cacheResultSetMetadata", true);
            config.addDataSourceProperty("cacheServerConfiguration", true);
            config.addDataSourceProperty("elideSetAutoCommits", true);
            config.addDataSourceProperty("maintainTimeStats", false);
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("encoding", "UTF-8");
            config.addDataSourceProperty("useUnicode", "true");
            config.addDataSourceProperty("useSSL", false);
            config.addDataSourceProperty("tcpKeepAlive", true);
            config.setPoolName("StoreUnits " + UUID.randomUUID().toString());
            config.setMaxLifetime(Long.MAX_VALUE);
            config.setMinimumIdle(0);
            config.setIdleTimeout(30000L);
            config.setConnectionTimeout(10000L);
            config.setMaximumPoolSize(30);
            hikari = new HikariDataSource(config);
            createTable();
    }


    private void close(Connection connection, PreparedStatement statement, ResultSet result) {
        if ( connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private void createTable() {
        Statement statement = null;
        try {
            Connection connection=hikari.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS StoreUnits(UUID varchar(100) primary key,Data blob);");
            statement.executeUpdate("ALTER TABLE StoreUnits MODIFY Data blob;");
            connection.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public PlayerData readToPlayerData(UUID uuid) {
        String SELECT = "SELECT * FROM StoreUnits WHERE UUID=?";
        byte[] bytes=new byte[0];
        try {
            Connection connection=hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1,uuid.toString());
            ResultSet result=  statement.executeQuery();
            if(result.next()){
                bytes=  result.getBytes("Data");
                close(connection,statement,result);
            }
            else {//如果查询不到
                close(connection,statement,result);
                return new PlayerData(uuid);
            }

        }catch (Exception e){
            e.printStackTrace();

            return new PlayerData(uuid);
        }
        String json=DataSource.Gunzip(bytes);

        Gson gson=new Gson();
        JsonArray jsonArray=gson.fromJson(json, JsonArray.class);
        return PlayerData.fromJson(jsonArray);
    }
    @Override
    public void write(PlayerData playerData,UUID uuid) {
        String json=PlayerData.toJson(playerData).toString();
        byte[] bytes=DataSource.Gzip(json);
        try {
            Connection connection=hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement(SAVE);
            statement.setString(1,uuid.toString());
            statement.setBytes(2, bytes);
            statement.setBytes(3,bytes);
            statement.execute();
            close(connection,statement,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable(){
        Map<UUID,PlayerData> dataMap=PlayerData.getData();
        for (UUID uuid : dataMap.keySet()) {
            write(dataMap.get(uuid),uuid);
        }
        hikari.close();
    }





}