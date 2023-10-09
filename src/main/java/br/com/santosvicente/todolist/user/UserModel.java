package br.com.santosvicente.todolist.user;

public class UserModel {
  private String name;
  private String username;
  private String password;

  public UserModel(String name, String username, String password) {
    this.name = name;
    this.username = username;
    this.password = password;
  }

  public String getName() {
    return this.name;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    String returnString = "Name: " + this.name + "\nUsername: " + this.username + "\nPassword: " + this.password;

    return returnString;
  }
}
