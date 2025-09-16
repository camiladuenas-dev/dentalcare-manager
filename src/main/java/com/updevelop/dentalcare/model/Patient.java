package com.updevelop.dentalcare.config.model;

public class Patient {
  private int id; private String document, fullName, phone, email;
  public Patient(int id, String document, String fullName, String phone, String email){
    this.id=id; this.document=document; this.fullName=fullName; this.phone=phone; this.email=email;
  }
  public Patient(String document, String fullName, String phone, String email){
    this(0, document, fullName, phone, email);
  }
  public int getId(){ return id; } public String getDocument(){ return document; }
  public String getFullName(){ return fullName; } public String getPhone(){ return phone; }
  public String getEmail(){ return email; }
  public void setId(int id){ this.id=id; } public void setDocument(String v){ this.document=v; }
  public void setFullName(String v){ this.fullName=v; } public void setPhone(String v){ this.phone=v; }
  public void setEmail(String v){ this.email=v; }
}
