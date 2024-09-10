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
public class Student {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    
    //get table max row
    public int getMax() {
        int maxId = 0;
        String query = "SELECT COALESCE(MAX(student_id), 0) FROM student"; // Lấy giá trị tối đa hoặc 0 nếu bảng trống

        try (Statement stmt = con.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                maxId = rs.getInt(1); // Lấy giá trị tối đa hiện tại
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }

        return maxId + 1;
    }
    

    //insert data into student table
    /**
 * Inserts a new student record into the database.
 *
 * @param id the student ID
 * @param name the student's name
 * @param date the student's date of birth
 * @param gender the student's gender
 * @param email the student's email address
 * @param phone the student's phone number
 * @param class_id the student's class ID
 * @param advisor_id the student's advisor ID
 * @param major the student's major
 * @param address the student's address
 * @param imagePath the file path to the student's image
 */
public void insert(int id, String name, String date, String gender, String email, String phone, String class_id, String advisor_id, String major, String address, String imagePath){
    String sql = "INSERT INTO student VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    ps = null;
    try {
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, date);
        ps.setString(4, gender);
        ps.setString(5, email);
        ps.setString(6, phone);
        ps.setString(7, class_id);
        ps.setString(8, advisor_id);
        ps.setString(9, major);
        ps.setString(10, address);
        ps.setString(11, imagePath);

        if(ps.executeUpdate() > 0){
            JOptionPane.showMessageDialog(null, "New student added successfully");
        }
    } catch (SQLException ex) {
        Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (ps != null) ps.close(); // Đảm bảo đóng PreparedStatement
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
        
    // kiểm tra email có bị trùng không
    public boolean isEmailExist (String email){
        try{
            ps = con.prepareStatement("select * from student where email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch ( SQLException ex){
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE,null,ex);
        }
        return false;
    }
    
    //kiểm tra số điện thoại có bị trùng không
    public boolean isPhoneExist (String phone){
        try{
            ps = con.prepareStatement("select * from student where phone = ?");
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch ( SQLException ex){
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE,null,ex);
        }
        return false;
    }
    
    //kiểm tra student_id có tồn tại k
    public boolean isIdExist (int id){
        try{
            ps = con.prepareStatement("select * from student where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch ( SQLException ex){
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE,null,ex);
        }
        return false;
    }
    
    //lấy tất cả giá trị từ bảng student
    public void getStudentValue(JTable table, String searchValue) throws SQLException {
        String sql = "SELECT * FROM student_management.student WHERE CONCAT(student_id, name, email, phone) LIKE ? order by student_id desc";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Xóa dữ liệu cũ

                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("student_id"); // Cột 1: student_id
                    row[1] = rs.getString("name"); // Cột 2: name
                    row[2] = rs.getDate("date_of_birth"); // Cột 3: date_of_birth
                    row[3] = rs.getString("gender"); // Cột 4: gender
                    row[4] = rs.getString("email"); // Cột 5: email
                    row[5] = rs.getString("phone"); // Cột 6: phone
                    row[6] = rs.getString("class_id"); // Cột 7: class_id
                    row[7] = rs.getString("advisor_id"); // Cột 8: advisor_id
                    row[8] = rs.getString("major"); // Cột 9: major
                    row[9] = rs.getString("address"); // Cột 10: address
                    row[10] = rs.getString("image_path"); // Cột 11: image_path

                    model.addRow(row);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public void getStudentValueByStudentId(JTable table, String searchValue) throws SQLException {
        String sql = "SELECT * FROM student_management.student WHERE CONCAT(student_id) LIKE ? order by student_id desc ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Xóa dữ liệu cũ

                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("student_id"); // Cột 1: student_id
                    row[1] = rs.getString("name"); // Cột 2: name
                    row[2] = rs.getDate("date_of_birth"); // Cột 3: date_of_birth
                    row[3] = rs.getString("gender"); // Cột 4: gender
                    row[4] = rs.getString("email"); // Cột 5: email
                    row[5] = rs.getString("phone"); // Cột 6: phone
                    row[6] = rs.getString("class_id"); // Cột 7: class_id
                    row[7] = rs.getString("advisor_id"); // Cột 8: advisor_id
                    row[8] = rs.getString("major"); // Cột 9: major
                    row[9] = rs.getString("address"); // Cột 10: address
                    row[10] = rs.getString("image_path"); // Cột 11: image_path

                    model.addRow(row);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    //cập nhật dữ liệu bảng student
        public void Update(int id, String name, String date, 
                String gender, String email, String phone, String class_id, String advisor_id, String major, String address, String imagePath){
            String sql = "update student set name=?, date_of_birth = ?, gender=?, "
                    + "email=?, phone=?, class_id=?, advisor_id=? ,major=?, address=?, image_path=? where student_id=?";
            try{
                ps=con.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, date);
                ps.setString(3, gender);
                ps.setString(4, email);
                ps.setString(5, phone);
                ps.setString(6, class_id);
                ps.setString(7, advisor_id);
                ps.setString(8, major);
                ps.setString(9, address);
                ps.setString(10, imagePath);
                ps.setInt(11, id);
                
                if(ps.executeUpdate()>0){
                    JOptionPane.showMessageDialog(null, "Student data updated successfully");
                }
            } catch(SQLException ex){
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    //xóa dữ liệu sinh viên 
        public void Delete(int id){
            int yesOrNo = JOptionPane.showConfirmDialog(null, "Course and score records will alse be deleted","Student Delete",JOptionPane.OK_CANCEL_OPTION);
            if(yesOrNo == JOptionPane.OK_OPTION){
                try{
                    ps = con.prepareStatement("delete from student where student_id = ?");
                    ps.setInt(1, id);
                    if(ps.executeUpdate()>0){
                        JOptionPane.showMessageDialog(null, "student deleted successfully");
                    }                      
                } catch(SQLException ex){
                    Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        }
    
}
