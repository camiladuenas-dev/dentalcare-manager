package com.updevelop.dentalcare.config.model;
import java.time.LocalDateTime;

public class Appointment {
  private int id, patientId; private LocalDateTime dateTime; private String reason, status;
  public Appointment(int id,int patientId,LocalDateTime dateTime,String reason,String status){
    this.id=id; this.patientId=patientId; this.dateTime=dateTime; this.reason=reason; this.status=status;
  }
  public Appointment(int patientId,LocalDateTime dateTime,String reason,String status){
    this(0, patientId, dateTime, reason, status);
  }
  public int getId(){ return id; } public int getPatientId(){ return patientId; }
  public LocalDateTime getDateTime(){ return dateTime; } public String getReason(){ return reason; }
  public String getStatus(){ return status; }
  public void setId(int id){ this.id=id; } public void setPatientId(int v){ this.patientId=v; }
  public void setDateTime(LocalDateTime v){ this.dateTime=v; } public void setReason(String v){ this.reason=v; }
  public void setStatus(String v){ this.status=v; }
}
