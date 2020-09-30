package com.josipcode.usermanagment.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.josipcode.usermanagment.model.User;

/**

 * Preko DAO klase se pruža moguænost CRUD unosa korisnika u bazu
 * 
 * @author Josip Batiniæ -> po uzoru na tutorijale sa JavaGuides: 
 * 							https://www.youtube.com/channel/UC1Be9fnFTlcsUlejgfqag0g
 * 
 * Ako se pojavi NullPointException Error potrebno je dodati sljedeæu liniju u fajlu my koji se nalazi na putanji C:\xampp\mysql\bin
 * U fajlu(otvoren s Notepad++) se poslje linije 28 treba ubaciti: skip-grant-tables (premošæivanje problema s passwordom)
 * 
 * Treba se uzeti u obzir nova verzija DriverManagera, prije je bio: com.mysql.jdbc.Driver sada je com.mysql.cj.jdbc.Driver
 * ->moze bacati error zbog starije verzije
 */
//Kao bazu za jdbc koristim MySQL Workbench 8.0 CE
public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbcUsername ="root";
	private String jdbcPassword ="root";

	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES "
			+ " (?, ?, ?);";

	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";

	public UserDAO() {
	}
	//Metoda za konekciju - pozivanje DriverManagera
	protected Connection getConnection() {
		Connection connection = null;
		try {
			//===Nova verzija drivera, prije je bio: com.mysql.jdbc.Driver->moze bacati error zbog starije verzije
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	//===UNOS NOVIH KORISNIKA
	public void insertUser(User user) throws SQLException {
		System.out.println(INSERT_USERS_SQL);
		// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	//===SELEKCIJA KORISNIKA PO ID-u
	public User selectUser(int id) {
		User user = null;
		//1: Uspostavljanje konekcije
		try (Connection connection = getConnection();
				//2:Kreiranje statementa pomocu connection objekta
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			//3: Izvrši ili updejtaj query
			ResultSet rs = preparedStatement.executeQuery();

			// 4: Procesiranje ResultSet objekta
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return user;
	}

	public List<User> selectAllUsers() {
		// korištenje try-with-resources kako bi se izbjeglo zatvaranje resursa (boiler plate code)
		List<User> users = new ArrayList<>();
		//  1: Uspostavljanje konekcije
		try (Connection connection = getConnection();

				// Step 2:Kreiranje statementa pomocu connection objekt
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println(preparedStatement);
			//3: Izvrši ili updejtaj query
			ResultSet rs = preparedStatement.executeQuery();

			// 4: Procesiranje ResultSet objekta
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}
	//===BRISANJE KORISNIKA
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());

			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	//===IZBACIVANJE GREŠAKA -> specifikacija greški
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

}
