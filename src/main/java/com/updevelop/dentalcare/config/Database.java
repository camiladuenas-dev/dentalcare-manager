package com.updevelop.dentalcare.config;
import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;

public class Database {
  private static final String URL =
  "jdbc:mysql://localhost:3306/dentalcare?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String USER = "updev";
private static final String PASS = "UpDev_2025!";

public static Connection getConnection() throws SQLException {
  try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException ignored) {}
  return DriverManager.getConnection(URL, USER, PASS);
}

}
