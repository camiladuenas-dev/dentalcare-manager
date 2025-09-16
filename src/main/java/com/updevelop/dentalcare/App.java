package com.updevelop.dentalcare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.updevelop.dentalcare.config.model.Appointment;
import com.updevelop.dentalcare.config.model.Patient;
import com.updevelop.dentalcare.dao.AppointmentDao;
import com.updevelop.dentalcare.dao.PatientDao;

public class App {
  private static final Scanner SC = new Scanner(System.in);
  private static final DateTimeFormatter DTF =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());

  private final PatientDao patientDao = new PatientDao();
  private final AppointmentDao appointmentDao = new AppointmentDao();

  public static void main(String[] args) { new App().run(); }

  private void run() {
    boolean exit = false;
    while (!exit) {
      System.out.println("\n=== DENTALCARE MANAGER ===");
      System.out.println("1) Crear paciente");
      System.out.println("2) Listar pacientes");
      System.out.println("3) Actualizar paciente");
      System.out.println("4) Eliminar paciente");
      System.out.println("5) Crear cita");
      System.out.println("6) Listar citas / cambiar estado");
      System.out.println("0) Salir");
      String opt = read("Seleccione opción: ");
      switch (opt) {
        case "1" -> createPatient();
        case "2" -> listPatients();
        case "3" -> updatePatient();
        case "4" -> deletePatient();
        case "5" -> createAppointment();
        case "6" -> listAppointmentsAndMaybeChangeStatus();
        case "0" -> exit = true;
        default -> System.out.println("Opción inválida.");
      }
      if (!exit) { System.out.print("\nENTER para continuar..."); SC.nextLine(); }
    }
  }

  // --- Pacientes
  private void createPatient() {
    try {
      var p = new Patient(
        read("Documento: "), read("Nombre completo: "),
        read("Teléfono (opcional): "), read("Email (opcional): "));
      int id = patientDao.create(p);
      System.out.println("Paciente creado con id: " + id);
    } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
  }
  private void listPatients() {
    try {
      List<Patient> list = patientDao.findAll();
      if (list.isEmpty()) { System.out.println("No hay pacientes."); return; }
      System.out.println("\nID | DOC | NOMBRE | TEL | EMAIL");
      for (var p: list)
        System.out.printf("%d | %s | %s | %s | %s%n",
          p.getId(), p.getDocument(), p.getFullName(),
          nz(p.getPhone()), nz(p.getEmail()));
    } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
  }
  private void updatePatient() {
    try {
      int id = Integer.parseInt(read("ID a actualizar: "));
      var ex = patientDao.findById(id);
      if (ex == null) { System.out.println("No existe ese ID."); return; }
      System.out.println("Vacío = conservar valor actual.");
      String d = read("Documento ["+ex.getDocument()+"]: ");
      String n = read("Nombre ["+ex.getFullName()+"]: ");
      String t = read("Tel ["+nz(ex.getPhone())+"]: ");
      String m = read("Email ["+nz(ex.getEmail())+"]: ");
      if (!d.isBlank()) ex.setDocument(d);
      if (!n.isBlank()) ex.setFullName(n);
      if (!t.isBlank()) ex.setPhone(t);
      if (!m.isBlank()) ex.setEmail(m);
      System.out.println(patientDao.update(ex) ? "Actualizado." : "Sin cambios.");
    } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
  }
  private void deletePatient() {
    try {
      int id = Integer.parseInt(read("ID a eliminar: "));
      System.out.println(patientDao.delete(id) ? "Eliminado." : "No se eliminó.");
    } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
  }

  // --- Citas
  private void createAppointment() {
    try {
      int pid = Integer.parseInt(read("ID paciente: "));
      if (patientDao.findById(pid) == null) { System.out.println("Paciente no existe."); return; }
      LocalDateTime dt;
      try { dt = LocalDateTime.parse(read("Fecha y hora (yyyy-MM-dd HH:mm): "), DTF); }
      catch (DateTimeParseException ex) { System.out.println("Formato inválido."); return; }
      int id = appointmentDao.create(new Appointment(pid, dt, read("Motivo: "), "SCHEDULED"));
      System.out.println("Cita creada con id: " + id);
    } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
  }
  private void listAppointmentsAndMaybeChangeStatus() {
    try {
      var rows = appointmentDao.findAllDetailed();
      if (rows.isEmpty()) { System.out.println("No hay citas."); return; }
      System.out.println("\nID | PACIENTE | FECHA-HORA | MOTIVO | ESTADO");
      for (var r: rows)
        System.out.printf("%d | %s | %s | %s | %s%n",
          r.id(), r.patientName(), r.dateTime().format(DTF), nz(r.reason()), r.status());
      String c = read("¿Cambiar estado? (s/N): ").toLowerCase(Locale.ROOT);
      if (c.equals("s") || c.equals("si") || c.equals("sí")) {
        int id = Integer.parseInt(read("ID de la cita: "));
        String st = read("Nuevo estado [SCHEDULED/DONE/CANCELLED]: ").toUpperCase(Locale.ROOT);
        if (!st.matches("SCHEDULED|DONE|CANCELLED")) { System.out.println("Estado inválido."); return; }
        System.out.println(appointmentDao.updateStatus(id, st) ? "Estado actualizado." : "No se actualizó.");
      }
    } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
  }

  // util
  private static String read(String p){ System.out.print(p); return SC.nextLine().trim(); }
  private static String nz(String s){ return (s==null || s.isBlank()) ? "-" : s; }
}
