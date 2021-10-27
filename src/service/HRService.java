package service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import dao.DepartmentsDAO;
import dao.EmployeesDAO;
import dao.Job_historyDAO;
import dao.JobsDAO;
import vo.Employees;

public class HRService {
	/////////////////////////////////////////////
	// 주어진 기간의 입사자 목록찾기 
	// 근무부서별 직원 목록 
	// 많이 받는 급여순으로 본 직원 목록
	// 커밋션율이 높은 순으로 본 직원 목록 
	// 직업id별로 본 직원 목록 
	// 성으로 직원들 찾기( 
	// 전화번호로 직원 찾기
	// 이메일로 직원 찾기 
	/////////////////////////////////////////////
	EmployeesDAO edao; 
	DepartmentsDAO ddao;
	Job_historyDAO jhdao;
	JobsDAO jdao;
	////////////////////////////////////////////// 
	public HRService() {
		this.edao = new EmployeesDAO();
		this.ddao = new DepartmentsDAO();
		this.jhdao = new Job_historyDAO();
		this.jdao = new JobsDAO();
	}
	// 1. 주어진 기간의 입사자 목록찾기 
	public List<Employees> getEmpListByHireDate(Date a,Date b){
		List<Employees> result = null;
		try {
			Predicate<Employees> bt= m->{
				Date x = m.getHire_date();
				return (x.after(a) && x.before(b))
						|| x.equals(a) || x.equals(b);
			};
			result = (List<Employees>) edao.selectAll().stream().filter(bt).collect(Collectors.toList());
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	// 2. 근무부서별 직원 목록 
	public List<Employees> getEmpListByDep(int depId){
		List<Employees> result = null;
		try {
			Predicate<Employees> bt= m->{
				return m.getDepartment_id()==depId;
			};
			result = (List<Employees>) edao.selectAll().stream().filter(bt).collect(Collectors.toList());
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	// 3. 많이 받는 급여순으로 본 직원 목록
	public List<Employees> getEmpListOrderbyPay(){
		List<Employees> result = null;
		try {
			result = (List<Employees>) edao.selectAll().stream().sorted(
					(em,em2) ->em2.getSalary() - em.getSalary()
					).collect(Collectors.toList());
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	
	
	// 4. 커밋션율이 높은 순으로 본 직원 목록 
	// 5. 직업id별로 본 직원 목록 
	// 6. 성으로 직원들 찾기( 
	// 7. 전화번호로 직원 찾기
	// 8. 이메일로 직원 찾기 

	//----------------------------------------------
	public static void main(String[] args) {
		new HRService().test();
	}
	public void test() {
		//1 테스트 2005년도 입사직원 목록 
		Date a = new Date(105,0,1);
		Date b = new Date(105,11,31);
		getEmpListByHireDate(a,b).stream().forEach(e->System.out.println(e+":"+e.getHire_date()));
		
	}
	//----------------------------------------------
}
