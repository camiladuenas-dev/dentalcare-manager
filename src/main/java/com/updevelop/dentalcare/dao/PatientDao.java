package com.updevelop.dentalcare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.updevelop.dentalcare.config.Database;
import com.updevelop.dentalcare.config.model.Patient;

public class PatientDao {
  public int create(Patient p) throws SQLException {
    String sql = "INSERT INTO patients(document, full_name, phone, email) VALUES(?,?,?,?)";
    try (Connection cn = Database.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, p.getDocument()); ps.setString(2, p.getFullName());
      ps.setString(3, p.getPhone()); ps.setString(4, p.getEmail());
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next()? rs.getInt(1) : -1; }
    }
  }
  public Patient findById(int id) throws SQLException {
    String sql = "SELECT * FROM patients WHERE id=?";
    try (Connection cn = Database.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next())
          return new Patient(rs.getInt("id"), rs.getString("document"),
            rs.getString("full_name"), rs.getString("phone"), rs.getString("email"));
      }
    }
    return null;
  }
  public List<Patient> findAll() throws SQLException {
    String sql = "SELECT * FROM patients ORDER BY id DESC";
    List<Patient> list = new ArrayList<>();
    try (Connection cn = Database.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next())
        list.add(new Patient(rs.getInt("id"), rs.getString("document"),
            rs.getString("full_name"), rs.getString("phone"), rs.getString("email")));
    }
    return list;
  }
  public boolean update(Patient p) throws SQLException {
    String sql = "UPDATE patients SET document=?, full_name=?, phone=?, email=? WHERE id=?";
    try (Connection cn = Database.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setString(1, p.getDocument()); ps.setString(2, p.getFullName());
      ps.setString(3, p.getPhone()); ps.setString(4, p.getEmail()); ps.setInt(5, p.getId());
      return ps.executeUpdate() == 1;
    }
  }
  public boolean delete(int id) throws SQLException {
    String sql = "DELETE FROM patients WHERE id=?";
    try (Connection cn = Database.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setInt(1, id); return ps.executeUpdate() == 1;
    }
  }
}
