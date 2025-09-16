package com.updevelop.dentalcare.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.updevelop.dentalcare.config.Database;
import com.updevelop.dentalcare.config.model.Appointment;

public class AppointmentDao {
  public int create(Appointment a) throws SQLException {
    String sql = "INSERT INTO appointments(patient_id, date_time, reason, status) VALUES(?,?,?,?)";
    try (Connection cn = Database.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setInt(1, a.getPatientId());
      ps.setTimestamp(2, Timestamp.valueOf(a.getDateTime()));
      ps.setString(3, a.getReason());
      ps.setString(4, a.getStatus());
      ps.executeUpdate();
      try (ResultSet rs = ps.getGeneratedKeys()) { return rs.next()? rs.getInt(1) : -1; }
    }
  }

  public List<DetailedAppt> findAllDetailed() throws SQLException {
    String sql = """
        SELECT ap.id, ap.patient_id, p.full_name, ap.date_time, ap.reason, ap.status
        FROM appointments ap
        JOIN patients p ON p.id = ap.patient_id
        ORDER BY ap.date_time DESC
        """;
    List<DetailedAppt> list = new ArrayList<>();
    try (Connection cn = Database.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        list.add(new DetailedAppt(
          rs.getInt(1), rs.getInt(2), rs.getString(3),
          rs.getTimestamp(4).toLocalDateTime(), rs.getString(5), rs.getString(6)
        ));
      }
    }
    return list;
  }

  public boolean updateStatus(int id, String status) throws SQLException {
    String sql = "UPDATE appointments SET status=? WHERE id=?";
    try (Connection cn = Database.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setString(1, status); ps.setInt(2, id);
      return ps.executeUpdate() == 1;
    }
  }

  public record DetailedAppt(int id, int patientId, String patientName,
                             LocalDateTime dateTime, String reason, String status) {}
}
