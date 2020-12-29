package com.takaichi00.sample.badjdbcconnection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class UpdateAttribute {
  private Integer id;
  private String name;
  private String address;

  public String toSql() {
    StringBuilder result = new StringBuilder("UPDATE TABLE_A SET ");
    result.append("address='");
    result.append(this.address);
    result.append("' WHERE id=");
    result.append(this.id);
    result.append(";");
    return result.toString();
  }
}

public class Main {

  private static final String localConnectionUrl = "jdbc:mysql://127.0.0.1:13306/test_database";

  private static String connectionUrl =localConnectionUrl;

  /* Bad Pattern */
  public static void main(String[] args) throws SQLException {

    System.out.println(connectionUrl);

    List<UpdateAttribute> updateAttributes = null;

    try (Connection con = DriverManager.getConnection(connectionUrl, "docker", "docker")) {

      updateAttributes = new ArrayList<>();

      String sql1 = "SELECT id,name,address FROM TABLE_A WHERE address is NULL";
      PreparedStatement stmt1 = con.prepareStatement(sql1);
      ResultSet rs1 = stmt1.executeQuery(sql1);
      while (rs1.next()) {
        updateAttributes.add(new UpdateAttribute(rs1.getInt("id"), rs1.getString("name"), null));
      }

      stmt1.close();
      rs1.close();

      for (UpdateAttribute updateAttribute : updateAttributes) {
        String sql2 = "SELECT name,address FROM TABLE_B WHERE name='" + updateAttribute.getName() + "'";
        System.out.println("SQL:" + sql2);
        PreparedStatement stmt2 = con.prepareStatement(sql2);
        ResultSet rs2 = stmt2.executeQuery(sql2);

        while (rs2.next()) {
          String sql3 = "SELECT name,address FROM TABLE_B WHERE name='" + updateAttribute.getName() + "'";
          PreparedStatement stmt3 = con.prepareStatement(sql3);
          ResultSet rs3 = stmt3.executeQuery(sql3);
          while (rs3.next()) {
            // Do something
            updateAttribute.setAddress(rs3.getString("address"));
          }
          stmt3.close();
          rs3.close();
        }

        System.out.println("now:" + updateAttribute.getId());

        stmt2.close();
        rs2.close();
      }

      printAllInformation(updateAttributes);

      toSqlFile(new File("Update.sql"), updateAttributes);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private static void toSqlFile(File outputFile, List<UpdateAttribute> updateAttributes) {
    try (BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath())) {
      for (UpdateAttribute updateAttribute : updateAttributes) {
          writer.write(updateAttribute.toSql());
          writer.newLine();
      }
      System.out.println("create update sql file: " + outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void printAllInformation(List<UpdateAttribute> updateAttributes) {
    for (UpdateAttribute i : updateAttributes) {
      System.out.println(i.getId() + ", " + i.getName() + ", " + i.getAddress());
    }
  }
}
