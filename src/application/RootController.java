package application;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import common.TableViewFactory;
import dao.DepartmentsDAO;
import dao.EmployeesDAO;
import dao.Job_historyDAO;
import dao.JobsDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import service.HRService;
import vo.Departments;
import vo.Employees;
import vo.Jobs;

public class RootController implements Initializable {

	@FXML
	private BorderPane root;

	@FXML
	private TextArea txtHistory;

	@FXML
	private TextField txtEmployee_id;

	@FXML
	private TextField txtFirst_name;

	@FXML
	private TextField txtLast_name;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtPhone_number;

	@FXML
	private TextField txtHire_date;

	@FXML
	private ComboBox<Jobs> comboJob_id;

	@FXML
	private TextField txtSalary;

	@FXML
	private TextField txtCommission_pct;

	@FXML
	private ComboBox<Employees> comboManager_id;

	@FXML
	private ComboBox<Departments> comboDepartment_id;

	@FXML
	private Button btnInsert;

	@FXML
	private Button btnDelete;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnSelectAll;

	@FXML
	private Button btnSelectByCon;

	@FXML
	private Button btnClear;

	// 주어진 기간의 입사자 목록 을 위한 컨트롤들
	@FXML
	private TextField txtStart;

	@FXML
	private TextField txtEnd;

	@FXML
	private Button btnTermHireList;

	// 근무부서별 직원 목록을 위한 컨트롤
	@FXML
	private ComboBox<Departments> comboDepartment_id1;

	// 급여순으로 보기 컨트롤
	@FXML
	private Button btnOrderByPay;
	
	@FXML
	void orderByPay(ActionEvent event) {
		List<Employees> result = service.getEmpListOrderbyPay();
		clear(new ActionEvent());
		tableView.getItems().addAll(result);
		
		dispHistory(" 급여 순으로 자료 정렬하기 완료");
	}

	// fxml과 연계되어 있지 않은 필드들(추가필드부분)
	DepartmentsDAO ddao = new DepartmentsDAO();
	EmployeesDAO edao = new EmployeesDAO();
	Job_historyDAO jhdao = new Job_historyDAO();
	JobsDAO jdao = new JobsDAO();
	HRService service = new HRService();
	//
	TableView tableView = TableViewFactory.getTable(Employees.class);

	//
	public Date str2Date(String x) {//type 2021-1-12 , 2021-01-07 
		String strs[] = x.split("-");
		if(strs[1].charAt(0)=='0') strs[1] = strs[1].replace("0","");
		if(strs[2].charAt(0)=='0') strs[2] = strs[2].replace("0","");
		
		int year = Integer.parseInt(strs[0]) - 1900;
		int month = Integer.parseInt(strs[1]) - 1;
		int day = Integer.parseInt(strs[2]);
		return new Date(year, month, day);
	}
	int hisNo;
	//
	public void dispHistory(String history) {
		txtHistory.appendText(">>"+(++hisNo)+". "+history+"\n");
	}
	
	
	@FXML
	void insert(ActionEvent event) {

		try {
			edao.insert(getVo());// DB에 반영
			int id = edao.getMaxid();
			System.out.println("id:" + id);
			Employees vo = edao.select(id);
			tableView.getItems().add(vo); // ui에 반영
			dispHistory(vo.toString()+ " 자료 입력 완료");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ------------------------------------
	private Employees getVo() {
		Employees vo = new Employees();
		// 이름
		vo.setFirst_name(txtFirst_name.getText());
		// 성
		vo.setLast_name(txtLast_name.getText());
		// 이메일
		vo.setEmail(txtEmail.getText());
		// 전화번호
		vo.setPhone_number(txtPhone_number.getText());
		// 입사일자
		vo.setHire_date(str2Date(txtHire_date.getText()));
		// 직업아이디
		vo.setJob_id(comboJob_id.getSelectionModel().getSelectedItem().getJob_id());
		// 급여
		vo.setSalary(Integer.parseInt(txtSalary.getText()));
		// 커밋션율
		vo.setCommission_pct(Double.parseDouble(txtCommission_pct.getText()));
		// 메니저아이디
		vo.setManager_id(comboManager_id.getSelectionModel().getSelectedItem().getEmployee_id());
		// System.out.println("선택된 메니져
		// 아이디:"+comboManager_id.getSelectionModel().getSelectedItem().getEmployee_id());
		// 부서아이디
		vo.setDepartment_id(comboDepartment_id.getSelectionModel().getSelectedItem().getDepartment_id());

		return vo;
	}// -------------------------------------

	@FXML
	void clear(ActionEvent event) {
		tableView.getItems().clear();
		dispHistory("뷰 자료 청소");
	}

	@FXML
	void delete(ActionEvent event) {
		int selNum = tableView.getSelectionModel().getSelectedIndex();
		Employees emp = (Employees) tableView.getSelectionModel().getSelectedItem();
		int empId = emp.getEmployee_id();
		try {
			
			// 고아레코드 제거하기 (선행) 
			jhdao.deleteOrphanRecord(empId);
			// db지우기
			edao.delete(empId);
			
			// ui지우기
			tableView.getItems().remove(selNum);
			dispHistory("직원번호 "+empId+" 삭제 완료");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void selectAll(ActionEvent event) {
		try {
			tableView.getItems().clear();
			tableView.getItems().addAll(edao.selectAll());
			dispHistory("모든 자료 불러오기");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void selectByCon(ActionEvent event) {
		String conditions = JOptionPane.showInputDialog("WHERE 포함한 조건");
		try {
			List<Employees> data = edao.selectByConditions(conditions);
			tableView.getItems().clear();
			tableView.getItems().addAll(data);
			dispHistory("조건 색인 완료("+conditions+")");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	void update(ActionEvent event) {
		try {
			Employees vo = getVo();
			vo.setEmployee_id(Integer.parseInt(txtEmployee_id.getText()));
			edao.update(vo);// DB에 반영
			int selNum = tableView.getSelectionModel().getSelectedIndex();
			tableView.getItems().set(selNum, vo);
			dispHistory(vo.toString()+" 수정완료");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			comboJob_id.getItems().addAll(jdao.selectAll());
			comboManager_id.getItems().addAll(edao.selectAll());
			comboDepartment_id.getItems().addAll(ddao.selectAll());
			comboDepartment_id1.getItems().addAll(ddao.selectAll());
			// root.setCenter(tableView);
			setTable();
			tableView.getItems().addAll(edao.selectAll());

			comboDepartment_id1.valueProperty().addListener(new ChangeListener<Departments>() {

				@Override
				public void changed(ObservableValue<? extends Departments> observable, Departments oldValue,
						Departments newValue) {
					List<Employees> result = service.getEmpListByDep(newValue.getDepartment_id());// **********
					// TableView tableView = TableViewFactory.getTable(Employees.class);
					clear(new ActionEvent());
					tableView.getItems().addAll(result);

				}
			});

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dispHistory("ui초기화 작업 완료");

	}

	// 새로운 테이블뷰가 만들어 지면 처리할 내용
	public void setTable() throws SQLException {
		dispHistory("테이블 세팅 완료");
		tableView.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO 테이블의 한 행을 선택했을 때 그 행의 정보를
				// 읽어 입력ui에 나타내기
				try {
					Employees selected = (Employees) tableView.getSelectionModel().getSelectedItem();
					txtEmployee_id.setText(selected.getEmployee_id() + "");
					txtFirst_name.setText(selected.getFirst_name());
					txtLast_name.setText(selected.getLast_name());
					txtEmail.setText(selected.getEmail());
					txtPhone_number.setText(selected.getPhone_number());
					txtHire_date.setText(selected.getHire_date().toString());

					comboJob_id.getSelectionModel().select(jdao.select(selected.getJob_id()));
					txtSalary.setText(selected.getSalary() + "");
					txtCommission_pct.setText(selected.getCommission_pct() + "");
					comboManager_id.getSelectionModel().select(edao.select(selected.getManager_id()));
					comboDepartment_id.getSelectionModel().select(ddao.select(selected.getDepartment_id()));

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		root.setCenter(tableView);
	}

	@FXML
	void termHireList(ActionEvent event) {
		// JOptionPane.showMessageDialog(null,"메롱!");

		Date a = str2Date(txtStart.getText());
		Date b = str2Date(txtEnd.getText());
		List<Employees> result = service.getEmpListByHireDate(a, b);
		// TableView tableView = TableViewFactory.getTable(Employees.class);
		clear(new ActionEvent());
		tableView.getItems().addAll(result);
		dispHistory(a.toString()+"~"+b.toString()+" 입사자 조회 완료!");
	}

}
