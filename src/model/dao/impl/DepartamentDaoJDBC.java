package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartamentDaoJDBC implements DepartmentDao {
    private Connection conn = null;
    public DepartamentDaoJDBC(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Department obj) {
        PreparedStatement stmt = null;
        try{
            String sql = "INSERT INTO department "+
                    "(Name) " +
                    "VALUES " +
                    "(?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getName());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){

                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                    System.out.println("Done! rows affected " + rowsAffected);
                }
                DB.closeConnection(rs);

            }else{
                throw new DbException("Unexpected error! No rows Affected!");
            }

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeConnection(stmt);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement stmt = null;
        try{
            String sql = "UPDATE department " +
                    "SET Name = ? " +
                    "WHERE Id = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, obj.getName());
            stmt.setInt(2, obj.getId());

            stmt.executeUpdate();
        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeConnection(stmt);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement stmt = null;
        try{
            String sql = "DELETE FROM department " +
                    "WHERE Id = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if(rows == 0){
                throw new DbException("Id don't exists");
            }

        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeConnection(stmt);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM department " +
                    "WHERE department.Id = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                return dep;
            }
            return null;
        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeConnection(stmt, rs);
        }

    }
    @Override
    public List<Department> findAll() {
       PreparedStatement st = null;
       ResultSet rs = null;
       try{
           String sql = "SELECT * FROM department " +
                   "ORDER BY Id";
           st = conn.prepareStatement(sql);
           rs = st.executeQuery();

           List<Department> list = new ArrayList<>();
                //key       //value
           Map<Integer, Department> map = new HashMap<>();
           while(rs.next()){
               Department dep = map.get(rs.getInt("Id"));
               if(dep == null){
                   dep = instantiateDepartment(rs);
                   map.put(rs.getInt("Id"), dep);
               }
               list.add(dep);
           }
           return list;

       } catch (SQLException ex){
           throw new DbException(ex.getMessage());
       }finally {
           DB.closeConnection(st, rs);
       }
    }
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        /*Objeto department montado*/
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }
}
