package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.ConnectionFactory;
import vo.Departments;
import vo.Job_history;

public class DepartmentsDAO implements IDao<Departments, Integer> {

	@Override
	public int insert(Departments vo) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Integer key) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Departments vo) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Departments select(Integer key) throws SQLException {
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT * FROM DEPARTMENTS WHERE DEPARTMENT_ID ="+key;
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Departments vo = new Departments();
		while(rs.next()) {
			vo.setDepartment_id(rs.getInt("DEPARTMENT_ID"));
			vo.setDeparment_name(rs.getString("DEPARTMENT_NAME"));
			vo.setManager_id(rs.getInt("MANAGER_ID"));
			vo.setLocation_id(rs.getInt("LOCATION_ID"));
		}
		conn.close();
		return vo;
	}

	@Override
	public List<Departments> selectAll() throws SQLException {
		List<Departments> data = new ArrayList<>();
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT * FROM DEPARTMENTS ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery(sql);
		while(rs.next()) {
			Departments vo = new Departments();
			vo.setDepartment_id(rs.getInt("DEPARTMENT_ID"));
			vo.setDeparment_name(rs.getString("DEPARTMENT_NAME"));
			vo.setManager_id(rs.getInt("MANAGER_ID"));
			vo.setLocation_id(rs.getInt("LOCATION_ID"));
			
			data.add(vo);
		}
		conn.close();
		return data;
	}

	@Override
	public List<Departments> selectByConditions(String conditions) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
