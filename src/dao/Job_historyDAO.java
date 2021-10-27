package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ConnectionFactory;
import vo.Employees;
import vo.Job_history;

public class Job_historyDAO implements IDao<Job_history, String> {

	@Override
	public int insert(Job_history vo) throws SQLException {
		Connection conn = ConnectionFactory.create();
		// EMPLOYEE_ID,START_DATE,END_DATE,JOB_ID,DEPARTMENT_ID
		String sql = "INSERT INTO JOB_HISTORY "
				+ "(EMPLOYEE_ID,START_DATE,END_DATE,JOB_ID,DEPARTMENT_ID) "
				+ "VALUES(?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, vo.getEmployee_id());
		pstmt.setDate(2, vo.getStart_date());
		pstmt.setDate(3, vo.getEnd_date());
		pstmt.setString(4, vo.getJob_id());
		pstmt.setInt(5, vo.getDepartment_id());
		
		int res = pstmt.executeUpdate();
		conn.close();
		return res;
	}
	// 복합키 분해해서 employee_id와 start_date 얻기
	public Map<String,Object> getKeyMap(String key){
		Map<String,Object> keyMap = new HashMap<>();
		String[] compositeKey = key.split(",");
		int id = Integer.parseInt(compositeKey[0]);
		String[] ymd = compositeKey[1].split("-");
		int year = Integer.parseInt(ymd[0])-1900;
		int month = Integer.parseInt(ymd[1])-1;
		int day = Integer.parseInt(ymd[2]);
		Date startDay = new Date(year,month,day);
		
		keyMap.put("employee_id",id);
		keyMap.put("start_date", startDay);
		return keyMap;
	}
	
	

	@Override
	public int delete(String key) throws SQLException {
		// key : "133,2021-06-01"
		Map<String,Object> keyMap = getKeyMap(key);
		Connection conn = ConnectionFactory.create();
		String sql = "DELETE JOB_HISTORY WHERE EMPLOYEE_ID=? AND START_DATE=? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, (int) keyMap.get("employee_id"));
		pstmt.setDate(2, (Date) keyMap.get("start_date"));
		int res = pstmt.executeUpdate();
		conn.close();
		return res;
	}
	
	//직원 삭제로 인해 생성되는 고아레코드 제거용 메소드 
	public int deleteOrphanRecord(int empId) throws SQLException{
		Connection conn = ConnectionFactory.create();
		String sql = "DELETE JOB_HISTORY WHERE EMPLOYEE_ID=? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, empId);
		int res = pstmt.executeUpdate();
		conn.close();
		return res;
	}
	
	
	

	@Override
	public int update(Job_history vo) throws SQLException {
		// start_date 수정방법을 강구해야함 
		Connection conn = ConnectionFactory.create();
		String sql = "UPDATE JOB_HISTORY "
				+ "SET END_DATE=?,JOB_ID=?,DEPARTMENT_ID=? "
				+ "WHERE EMPLOYEE_ID=? AND START_DATE=? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setDate(1, vo.getEnd_date());
		pstmt.setString(2, vo.getJob_id());
		pstmt.setInt(3, vo.getDepartment_id());
		pstmt.setInt(4, vo.getEmployee_id());
		pstmt.setDate(5,vo.getStart_date());
		
		int res = pstmt.executeUpdate();
		conn.close();
		return res;
	}

	@Override
	public Job_history select(String key) throws SQLException {
		Job_history vo = new Job_history();
		Connection conn = ConnectionFactory.create();
		Map<String,Object> keyMap = getKeyMap(key);
		
		String sql = "SELECT * FROM JOB_HISTORY WHERE "
				+ "EMPLOYEE_ID= ? AND START_DATE=? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, (int) keyMap.get("employee_id"));
		pstmt.setDate(2, (Date) keyMap.get("start_date"));
		ResultSet rs = pstmt.executeQuery(sql);
		rs.next();
		
		vo.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
		vo.setStart_date(rs.getDate("START_DATE"));
		vo.setEnd_date(rs.getDate("END_DATE"));
		vo.setJob_id(rs.getString("JOB_ID"));
		vo.setDepartment_id(rs.getInt("DEPARTMENT_ID"));
		conn.close();
		return vo;
	}

	@Override
	public List<Job_history> selectAll() throws SQLException {
		List<Job_history> data = new ArrayList<>();
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT * FROM JOB_HISTORY ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery(sql);
		while(rs.next()) {
			Job_history vo = new Job_history();
			vo.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
			vo.setStart_date(rs.getDate("START_DATE"));
			vo.setEnd_date(rs.getDate("END_DATE"));
			vo.setJob_id(rs.getString("JOB_ID"));
			vo.setDepartment_id(rs.getInt("DEPARTMENT_ID"));
			data.add(vo);
		}
		conn.close();
		return data;
	}

	@Override
	public List<Job_history> selectByConditions(String conditions) throws SQLException {
		List<Job_history> data = new ArrayList<>();
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT * FROM JOB_HISTORY "+conditions;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery(sql);
		while(rs.next()) {
			Job_history vo = new Job_history();
			vo.setEmployee_id(rs.getInt("EMPLOYEE_ID"));
			vo.setStart_date(rs.getDate("START_DATE"));
			vo.setEnd_date(rs.getDate("END_DATE"));
			vo.setJob_id(rs.getString("JOB_ID"));
			vo.setDepartment_id(rs.getInt("DEPARTMENT_ID"));
			data.add(vo);
		}
		conn.close();
		return data;
	}

}
