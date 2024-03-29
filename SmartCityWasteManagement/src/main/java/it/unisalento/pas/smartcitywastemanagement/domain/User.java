package it.unisalento.pas.smartcitywastemanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("users")
public class User {

    @Id
    private String id;
    private String name;
    private String surname;
    private String mail;
    private Integer age;
    private String username;
    private String password;
    private String role;
    private Boolean isAwared;
    private Integer incorrectDisposalCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getAwared() {
        return isAwared;
    }

    public void setAwared(Boolean awared) {
        isAwared = awared;
    }

    public Integer getIncorrectDisposalCount() {
        return incorrectDisposalCount;
    }

    public void setIncorrectDisposalCount(Integer incorrectDisposalCount) {
        this.incorrectDisposalCount = incorrectDisposalCount;
    }
}
