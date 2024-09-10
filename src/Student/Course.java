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
public class Course {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    
    public int getMax() {
        int maxId = 0;
        String query = "SELECT COALESCE(MAX(id), 0) FROM course"; // Lấy giá trị tối đa hoặc 0 nếu bảng trống

        try (Statement stmt = con.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                maxId = rs.getInt(1); // Lấy giá trị tối đa hiện tại
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }

        return maxId + 1;
    }
    
    public boolean getId(int id) {
        try {
            ps = con.prepareStatement("select * from student where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Home.jTextField9.setText(String.valueOf(rs.getInt(1)));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Stuent id doesn't exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public int countCreditHour(int id) {
        int total = 0;
        try {
            ps = con.prepareStatement("select sum(*) as 'total' from course where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt(1);
            }
            if (total == 130) {
                JOptionPane.showMessageDialog(null, "this student has completed all the courses");
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
    
    public boolean isSemesterExist(int id, String semester) {
        try {
            ps = con.prepareStatement("select * from course where student_id = ? and semester = ?");
            ps.setInt(1, id);
            ps.setString(2, semester);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean isCourseExist(int sid, String courseId, String semester) {
        String query = "SELECT * FROM course WHERE student_id = ? AND course_id = ? AND semester = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, sid);
            ps.setString(2, courseId);
            ps.setString(3, semester);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu có ít nhất một bản ghi
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, "SQL error occurred while checking course existence", ex);
        }
        return false;
    }
    
    public boolean isCourseExist1(int sid, String course) {
        String query = "SELECT * FROM course WHERE student_id = ? AND trim(course) = trim(?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, sid);
            ps.setString(2, course);           

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu có ít nhất một bản ghi
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, "SQL error occurred while checking course existence", ex);
        }
        return false;
    }
    public boolean isIdExist (int id){
        try{
            ps = con.prepareStatement("select * from course where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch ( SQLException ex){
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE,null,ex);
        }
        return false;
    }
    
//    public boolean isCourseIdExist (int cid){
//        try{
//            ps = con.prepareStatement("select * from course where course_id = ? and student_id = ?");
//            ps.setInt(1, cid);
//            //ps.setInt(2, sid);
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()){
//                return true;
//            }
//        } catch ( SQLException ex){
//            Logger.getLogger(Course.class.getName()).log(Level.SEVERE,null,ex);
//        }
//        return false;
//    }
    
    public boolean isCourseIdExist(String courseId, int sid) {
        String query = "SELECT * FROM course WHERE trim(course_id) = trim(?) and student_id = ?"; // Sử dụng tên cột đúng
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, courseId);
            ps.setInt(2, sid);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    
    public void insertCourse(int id, int student_id, String course_id, String course, String semester, String instructor_id, int Credit_Hour) {
        String sql = "INSERT INTO course VALUES(?, ?, ?, ?, ?, ?, ?)";
        ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, student_id);
            ps.setString(3, course_id);
            ps.setString(4, course);
            ps.setString(5, semester);
            ps.setString(6, instructor_id);
            ps.setInt(7, Credit_Hour);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "New course added successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close(); // Đảm bảo đóng PreparedStatement
                }
            } catch (SQLException ex) {
                Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean insertStudentCourse(int id, int student_id, String course_id, String course, String semester) {
        // Kiểm tra xem khóa học có tồn tại không
        if (isCourseIdExist(course_id, student_id)) {
            String sql = "INSERT INTO student_course (id, student_id, course_id, course, semester) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, student_id);
                ps.setString(3, course_id);
                ps.setString(4, course);
                ps.setString(5, semester);

                return ps.executeUpdate() > 0;
            } catch (SQLException ex) {
                Logger.getLogger(Course.class.getName()).log(Level.SEVERE, "SQL Error", ex);
            }
        } else {
            // Nếu khóa học không tồn tại, thông báo lỗi hoặc xử lý khác
            //JOptionPane.showMessageDialog(null, "Course ID does not exist.");
            return false;
        }
        return false;
    }


    
    public void getCourseValue(JTable table, String searchValue) throws SQLException {
        String sql = "SELECT * FROM student_management.course WHERE CONCAT(id,student_id,course_id, semester) LIKE ? order by id desc ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Xóa dữ liệu cũ

                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("id"); // Cột 1: id
                    row[1] = rs.getInt("student_id"); // Cột 2: student_id
                    row[2] = rs.getString("course_id"); // Cột 4: course
                    row[3] = rs.getString("course");
                    row[4] = rs.getString("semester"); // Cột 3: semester
                    row[5] = rs.getString("instructor_id"); // Cột 6: instructor_id
                    row[6] = rs.getInt("Credit_Hour"); // Cột 7: Credit_Hour
                 

                    model.addRow(row);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public void getCourseValueByStudentId(JTable table, String studentId) throws SQLException {
        String query = "SELECT id, student_id, course_id, course, semester, instructor_id, Credit_Hour "
                + "FROM course "
                + "WHERE student_id = ?"
                + "ORDER BY id DESC"; // Đảm bảo câu lệnh SQL hợp lệ và có dấu cách trước ORDER BY

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, studentId.trim());

            ResultSet rs = pstmt.executeQuery();

            // Cập nhật dữ liệu vào bảng
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Xóa các hàng cũ

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("course_id"),
                    rs.getString("course"),
                    rs.getString("semester"),
                    rs.getString("instructor_id"),
                    rs.getInt("Credit_Hour"),
                    
                };
                model.addRow(row);
            }

            rs.close();
        }
    }


    
    public void Update(int id, int student_id, String semester, String course_id, String course, int credit, String instructor) {
        String sql = "UPDATE course SET student_id = ?, semester = ?, course = ?, course_id = ?, Credit_Hour = ?, instructor_id = ? WHERE id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, student_id);
            ps.setString(2, semester);
            ps.setString(3, course);
            ps.setString(4, course_id);
            ps.setInt(5, credit);
            ps.setString(6, instructor);
            ps.setInt(7, id); // Ensure the WHERE clause uses the correct ID

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Course updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. No course found with the specified ID.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, "SQL error occurred: " + ex.getMessage(), ex);
        }
    }
    
    public void Delete(int id){
            int yesOrNo = JOptionPane.showConfirmDialog(null, "Score records will alse be deleted","Course Delete",JOptionPane.OK_CANCEL_OPTION);
            if(yesOrNo == JOptionPane.OK_OPTION){
                try{
                    ps = con.prepareStatement("delete from course where id = ?");
                    ps.setInt(1, id);
                    if(ps.executeUpdate()>0){
                        JOptionPane.showMessageDialog(null, "course deleted successfully");
                    }                      
                } catch(SQLException ex){
                    Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        }


}


