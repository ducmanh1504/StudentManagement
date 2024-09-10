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
public class Score {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    public int getMax() {
        int maxId = 0;
        String query = "SELECT COALESCE(MAX(id), 0) FROM score"; // Lấy giá trị tối đa hoặc 0 nếu bảng trống

        try (Statement stmt = con.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                maxId = rs.getInt(1); // Lấy giá trị tối đa hiện tại
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }

        return maxId + 1;
    }
    public boolean getStudentId(int id) {
        try {
            ps = con.prepareStatement("select * from student where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Home.jTextField12.setText(String.valueOf(rs.getInt(1)));
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Stuent id doesn't exist");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean getDetailScore(int id, String course) {
        try {
            // Loại bỏ khoảng trắng thừa từ biến course
            String trimmedCourse = course.trim();

            // Truy vấn sử dụng TRIM() để loại bỏ khoảng trắng thừa trong cơ sở dữ liệu
            String query = "SELECT * FROM course WHERE TRIM(course) = TRIM(?) AND student_id = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, trimmedCourse);
            ps.setInt(2, id);

            System.out.println("Executing query: " + query);  // Debugging
            System.out.println("ID: " + id + ", Course: " + trimmedCourse);  // Debugging

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                try {
//                    Home.jTextField15.setText(String.valueOf(rs.getInt(3)));
//                    Home.jTextField12.setText(String.valueOf(rs.getInt(4)));
                    //Home.jTextField15.setText(rs.getString(3));
                    
                    //Home.jTextField12.setText(rs.getString(3));
                    Home.jTextField16.setText(rs.getString(5));
                    Home.jComboBox34.setSelectedItem(rs.getString(4));
                    Home.jComboBox20.setSelectedItem(rs.getString(3));
                    return true;
                } catch (NumberFormatException e) {
                    // Xử lý lỗi nếu có                   
                    JOptionPane.showMessageDialog(null, "Data format error in the database.");
                }
            } else {
                System.out.println("No data found for ID: " + id + " and course: " + trimmedCourse);
                JOptionPane.showMessageDialog(null, "Student ID or course doesn't exist in the database.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }



    
    public boolean isIdExist (int id){
        try{
            ps = con.prepareStatement("select * from score where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch ( SQLException ex){
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE,null,ex);
        }
        return false;
    }
    
    public boolean isStudentIdExist (int id){
        try{
            ps = con.prepareStatement("select * from course where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch ( SQLException ex){
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE,null,ex);
        }
        return false;
    }
    
    public boolean isCourseIdExist(String courseId, int sid) {
        String query = "SELECT * FROM score WHERE course_id = ? and student_id = ?"; // Sử dụng tên cột đúng
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, courseId);
            ps.setInt(2, sid);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    public boolean isCourseExist(int sid, String courseId, String semester) {
        String query = "SELECT * FROM score WHERE student_id = ? AND course_id = ? AND semester = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, sid);
            ps.setString(2, courseId);
            ps.setString(3, semester);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu có ít nhất một bản ghi
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, "SQL error occurred while checking course existence", ex);
        }
        return false;
    }
    
    
    public void insertScore(int id, int student_id, String course_id, String course, String semester, double process, double sfinal, double aver) {
        String sql = "INSERT INTO score VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, student_id);
            ps.setString(3, course_id);
            ps.setString(4, course);
            ps.setString(5, semester);
            ps.setDouble(6, process);
            ps.setDouble(7, sfinal);
            ps.setDouble(8, aver);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "New score added successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close(); // Đảm bảo đóng PreparedStatement
                }
            } catch (SQLException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean insertStudentScore(int id, int student_id, String course_id, String course, String semester, double process, double sfinal, double aver) {
    // Kiểm tra xem khóa học có tồn tại không
    if (isCourseIdExist(course_id,student_id)) {
        String sql = "INSERT INTO student_course (id, student_id, course_id, course, semester, process, final ) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, student_id);
            ps.setString(3, course_id);
            ps.setString(4, course);
            ps.setString(5, semester);
            ps.setDouble(6, process);
            ps.setDouble(7, sfinal);
            ps.setDouble(8, aver);
            

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, "SQL Error", ex);
        }
    } else {
        // Nếu khóa học không tồn tại, thông báo lỗi hoặc xử lý khác
        //JOptionPane.showMessageDialog(null, "Course ID does not exist.");
        return false;
    }
    return false;
}
    public void getScoreValue(JTable table, String searchValue) throws SQLException {
        String sql = "SELECT * FROM student_management.score WHERE CONCAT(id,student_id,course_id, semester) LIKE ? order by id desc ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Xóa dữ liệu cũ

                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("id"); 
                    row[1] = rs.getInt("student_id"); 
                    row[2] = rs.getString("course_id");
                    row[3] = rs.getString("course");
                    row[4] = rs.getString("semester"); 
                    row[5] = rs.getDouble("process_score"); 
                    row[6] = rs.getDouble("final_score"); 
                    row[7] = rs.getDouble("average");
                 

                    model.addRow(row);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    public void getScoreValueByStudentId(JTable table,  String searchValue) throws SQLException {
        String sql = "SELECT * FROM student_management.score WHERE CONCAT(student_id) LIKE ? order by id desc ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0); // Xóa dữ liệu cũ

                while (rs.next()) {
                    Object[] row = new Object[11];
                    row[0] = rs.getInt("id"); 
                    row[1] = rs.getInt("student_id"); 
                    row[2] = rs.getString("course_id");
                    row[3] = rs.getString("course");
                    row[4] = rs.getString("semester"); 
                    row[5] = rs.getDouble("process_score"); 
                    row[6] = rs.getDouble("final_score"); 
                    row[7] = rs.getDouble("average");
                 

                    model.addRow(row);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    
    public void Delete(int id){
            int yesOrNo = JOptionPane.showConfirmDialog(null, "Score records will alse be deleted","Course Delete",JOptionPane.OK_CANCEL_OPTION);
            if(yesOrNo == JOptionPane.OK_OPTION){
                try{
                    ps = con.prepareStatement("delete from score where id = ?");
                    ps.setInt(1, id);
                    if(ps.executeUpdate()>0){
                        JOptionPane.showMessageDialog(null, "score deleted successfully");
                    }                      
                } catch(SQLException ex){
                    Logger.getLogger(Score.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        }
    
    public void Update(int id, int student_id, String semester, String course_id, String course, double process, double sfinal, double averange) {
        String sql = "UPDATE score SET student_id = ?, course_id = ?, course = ?, semester = ?, process_score = ?, final_score = ?, average = ? WHERE id = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, student_id);
            ps.setString(2, course_id);
            ps.setString(3, course);
            ps.setString(4, semester);
            ps.setDouble(5, process);
            ps.setDouble(6, sfinal);
            ps.setDouble(7, averange);
            ps.setInt(8, id); // Ensure the WHERE clause uses the correct ID

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Course updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. No course found with the specified ID.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Score.class.getName()).log(Level.SEVERE, "SQL error occurred: " + ex.getMessage(), ex);
        }
    }
}
