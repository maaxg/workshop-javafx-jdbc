package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)";              //Para retornar o ID criado
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getEmail());
            stmt.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            stmt.setDouble(4, obj.getBaseSalary());
            stmt.setInt(5, obj.getDepartment().getId());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Done! ");
                    obj.setId(id);
                }
                DB.closeConnection(rs);
            }else{
                throw new DbException("Unexpected error! No rows Affected!");
            }
        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeConnection(stmt);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?";              //Para retornar o ID criado
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getEmail());
            stmt.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            stmt.setDouble(4, obj.getBaseSalary());
            stmt.setInt(5, obj.getDepartment().getId());
            stmt.setInt(6, obj.getId());

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
            String sql = "DELETE FROM seller\n" +
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
    public Seller findById(Integer id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT seller.*,department.Name as DepName\n" +
                         "FROM seller INNER JOIN department\n" +
                         "ON seller.DepartmentId = department.Id\n" +
                         "WHERE seller.Id = ?";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            //Se esse proximo resultset no caso o de valor 1 existir
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                Seller obj = intstantiateSeller(rs, dep);
                return obj;
            }
            return null;

        }catch (SQLException ex) {
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeConnection(stmt, rs);
        }
    }

    private Seller intstantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        //passagem do department montado para o objeto
        obj.setDepartment(dep);
        return obj;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        /*Objeto department montado*/
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT seller.*,department.Name as DepName\n" +
                    "FROM seller INNER JOIN department\n" +
                    "ON seller.DepartmentId = department.Id\n" +
                    "ORDER BY Id";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            List<Seller> list = new ArrayList<>();
            //Key       //Value
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));

                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = intstantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;


        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeConnection(stmt, rs);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT seller.*,department.Name as DepName "+
                         "FROM seller INNER JOIN department "+
                        "ON seller.DepartmentId = department.Id " +
                        "WHERE DepartmentId = ? "+
                        "ORDER BY Name";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, department.getId());
            rs = stmt.executeQuery();

            List<Seller> list = new ArrayList<>();
            //Key       //Value
            Map<Integer, Department> map = new HashMap<>();

            //Se esse proximo resultset no caso o de valor 1 existir
            while(rs.next()){

                Department dep = map.get(rs.getInt("DepartmentId"));

                if(dep == null){
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = intstantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;

        }catch (SQLException ex) {
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeConnection(stmt, rs);
        }
    }
}
