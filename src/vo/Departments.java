package vo;

public class Departments {
	private int department_id;
	private String deparment_name;
	private int manager_id;
	private int location_id;
	public int getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}
	public String getDeparment_name() {
		return deparment_name;
	}
	public void setDeparment_name(String deparment_name) {
		this.deparment_name = deparment_name;
	}
	public int getManager_id() {
		return manager_id;
	}
	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	@Override
	public String toString() {
		return department_id + "/" + deparment_name ;
	}
	
}
