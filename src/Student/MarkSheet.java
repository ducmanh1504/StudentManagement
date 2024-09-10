/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Student;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HI
 */
public class MarkSheet {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    
    public boolean isIdExist (int id){
        try{
            ps = con.prepareStatement("select * from score where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch ( SQLException ex){
            Logger.getLogger(MarkSheet.class.getName()).log(Level.SEVERE,null,ex);
        }
        return false;
    }
    
    public void getValueByStudentId(JTable table,  String searchValue) throws SQLException {
        String sql = "SELECT * FROM student_management.MarkSheet WHERE CONCAT(student_id) LIKE ? order by id desc ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Xóa dữ liệu cũ

                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("id"); 
                    row[1] = rs.getInt("student_id"); 
                    row[2] = rs.getString("student_name");
                    row[3] = rs.getString("course_id");                 
                    row[4] = rs.getString("course_name"); 
                    row[5] = rs.getString("semester");
                    row[6] = rs.getInt("credit_hour");                    
                    row[7] = rs.getDouble("average");
                 

                    model.addRow(row);
                }
            } catch (SQLException ex) {
                Logger.getLogger(MarkSheet.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarkSheet.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public double getGPA(int sid) {
        double cgpa = 0.0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select avg(average) from MarkSheet where student_id ="+sid+"");
            if(rs.next()){
                cgpa = rs.getDouble(1);
            }            
        } catch (SQLException ex){
            Logger.getLogger(MarkSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cgpa;
    }
    
    public int getTotalCreditHour(int sid) {
        int credit = 0;
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select sum(credit_hour) from MarkSheet where student_id ="+sid+"");
            if(rs.next()){
                credit = rs.getInt(1);
            }            
        } catch (SQLException ex){
            Logger.getLogger(MarkSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credit;
    }
    
    
}
