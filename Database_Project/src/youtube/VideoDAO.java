package youtube;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VideoDAO {
	private static final long serialVersionUID = 1L;
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	public VideoDAO() {}
	
	protected void connect_func() throws SQLException {
        if (connect == null || connect.isClosed()) {
    		System.out.println("Connecting to the database...");        
    		try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/youtube?" + "user=john&password=pass1234");
            System.out.println(connect);
        }
		System.out.println("Connection established.");        
    }
	
	protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
	
	// Function that creates and seeds the table
 	public void dropTable() throws SQLException {
		connect_func();
		statement = (Statement) connect.createStatement();
		statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
		statement.executeUpdate("DROP TABLE IF EXISTS video");
		statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
 	}
 	
 	public void insert(Video video) throws SQLException {
        connect_func();        
        String sql = "insert into  video (url, title, description, date, comedianid) values (?, ?, ?, ?, ?)";
        preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
        preparedStatement.setString(1, video.getUrl());
        preparedStatement.setString(2, video.getTitle());
        preparedStatement.setString(3, video.getDescription());
        preparedStatement.setString(4, video.getDate());
        preparedStatement.setString(5, video.getComedianid());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        disconnect();
    }
 	
	// Function that creates and seeds the table
	public void createTable() throws SQLException {
		try {
			connect_func();
			
			// create the user table
			String s = "CREATE TABLE video (" +
					"url VARCHAR(50) NOT NULL," +
					"title VARCHAR(50) NOT NULL," +
					"description VARCHAR(100) NOT NULL," +
					"date VARCHAR(20) NOT NULL," +
					"comedianId VARCHAR(2) NOT NULL," +
					"PRIMARY KEY(url) );";
			statement.executeUpdate(s);
			System.out.println("'Video' table created.");
			
			// seed the table with 10 users
			String s2 = "INSERT INTO video(url, title, description, date, comedianId) VALUES" +
					"('youtube.com', 'videos', 'Share your own videos', '2014-12-28', '1'), " +
					"('google.com', 'general', 'Search anything you want', '2015-1-29', '2'), " +
					"('wix.com', 'website creation', 'Very helpful in making sites', '2016-2-18', '3'), " +
					"('yahoo.com', 'general', 'A very helpful search engine', '2017-3-19', '4'), " +
					"('gmail.com', 'mailing services', 'Can send emails from any part to the world and recieve emails too', '2017-4-20', '5'), " +
					"('facebook.com', 'social media', 'Upload photos and videos', '2018-4-24', '6'), " +
					"('amazon.com', 'shopping', 'Purchase anything you want and get it delivered in 2 days', '2011-3-2', '7'), " +
					"('instagram.com', 'social media', 'Upload status and stories', '2019-6-13', '8'), " +
					"('samsung.com', 'shopping', 'Purchase phones you want', '2011-3-2', '9'), " +
					"('ebay.com', 'shopping', 'Very cheap shopping but ships slow', '2020-1-1', '10');";
			statement.executeUpdate(s2);
			System.out.println("10 videos added.");
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			close();
		}
	}
	
	private void close() throws SQLException {
		if (resultSet != null)
			resultSet.close();
		if (statement != null)
			statement.close();
	}
}