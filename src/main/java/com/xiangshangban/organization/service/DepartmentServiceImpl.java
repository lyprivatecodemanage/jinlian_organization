package com.xiangshangban.organization.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.DepartmentTree;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.dao.DepartmentDao;
import com.xiangshangban.organization.dao.EmployeeDao;
import com.xiangshangban.organization.dao.PostDao;
import com.xiangshangban.organization.util.FormatUtil;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	DepartmentDao departmentDao;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	PostDao postDao;


	@Override
	public String deleteByDepartment(String departmentId) {
		String i ="0";
		try {
			departmentDao.deleteByDepartment(departmentId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String insertDepartment(Department department) {
		String i ="0";
		Department departmentNumbe = departmentDao.findByDepartmentNumber(department.getDepartmentNumbe());
		try {
			department.setDepartmentId(FormatUtil.createUuid());			
			if(department.getDepartmentParentId().equals("")){
				department.setDepartmentParentId("0");				
			}else{
				department.getDepartmentParentId();
			}			
			if(departmentNumbe != null ){
				i="2";
				return i;//部门编号已存在				
			}						
			departmentDao.insertDepartment(department);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Department selectByDepartment(String departmentId,String companyId) {
		// TODO Auto-generated method stub
		return departmentDao.selectByDepartment(departmentId,companyId);
	}

	@Override
	public String updateByDepartment(Department department) {
		String i ="0";		
		try {
			Department DepartmentNumbe = departmentDao.findByDepartmentNumber(department.getDepartmentNumbe());
			if(DepartmentNumbe != null ){
				i="2";
				return i;//部门编号已存在				
			}	
			departmentDao.updateByDepartment(department);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}


	@Override
	public List<DepartmentTree> getDepartmentTree(String departmentParentId,String companyId, DepartmentTree departmentTree) {		
		List<Department> DepartmentList=departmentDao.findDepartmentTree(departmentParentId,companyId);
		List<DepartmentTree> list = new ArrayList<DepartmentTree>();
		 //根部门
		if(departmentParentId.equals("0")){
			departmentTree = new DepartmentTree();
			departmentTree.setLabel(DepartmentList.get(0).getDepartmentName());
			departmentTree.setValue("1");
			departmentTree.setKey("1");					
			departmentTree.setChildren(getDepartmentTree("1", companyId, departmentTree));
			
	        list.add(departmentTree);
		}else{
			if(null != DepartmentList){
				for(Department department : DepartmentList){
					departmentTree = new DepartmentTree();
					departmentTree.setLabel(department.getDepartmentName());					
					departmentTree.setValue(department.getDepartmentId());
					departmentTree.setKey(department.getDepartmentId()); 					
					departmentTree.setChildren(getDepartmentTree(department.getDepartmentId(),companyId, departmentTree));;										
			        list.add(departmentTree);
				}
			}
		 }
		return list;
	}

	@Override
	public List<DepartmentTree> getDepartmentTreeAll(String companyId) {
		List<Department> DepartmentList=departmentDao.findDepartmentTree("0",companyId);		
		List<DepartmentTree> list = new ArrayList<DepartmentTree>();
		for(Department department: DepartmentList){
			DepartmentTree departmentTree = new DepartmentTree();			
			departmentTree.setLabel(department.getDepartmentName());						
			departmentTree.setValue(department.getDepartmentId());
			departmentTree.setKey(department.getDepartmentId());			
			departmentTree.setChildren(getDepartmentTree(department.getDepartmentId(), companyId, departmentTree));
			list.add(departmentTree);
		}
		return list;
	}

	@Override
	public List<Department> findByMoreDepartment(Map<String, String> map) {
		// TODO Auto-generated method stub
		return departmentDao.findByMoreDepartment(map);
	}

	@Override
	public List<Department> findByAllDepartment(String companyId) {
		// TODO Auto-generated method stub
		return departmentDao.findByAllDepartment(companyId);
	}

	@Override
	public List<DepartmentTree> getDepartmentempTree(String departmentParentId,String companyId, DepartmentTree departmentTree) {		
		List<Department> DepartmentList=departmentDao.findDepartmentTree(departmentParentId,companyId);
		List<DepartmentTree> list = new ArrayList<DepartmentTree>();
		 //根部门
		if(departmentParentId.equals("0")){
			departmentTree = new DepartmentTree();
			departmentTree.setLabel(DepartmentList.get(0).getDepartmentName());
			departmentTree.setValue("1");
			departmentTree.setKey("1");					
			departmentTree.setChildren(getDepartmentTree("1", companyId, departmentTree));
			
	        list.add(departmentTree);
		}else{
			if(null != DepartmentList){
				for(Department department : DepartmentList){
					departmentTree = new DepartmentTree();
					departmentTree.setLabel(department.getDepartmentName());					
					departmentTree.setValue(department.getDepartmentId());
					departmentTree.setKey(department.getDepartmentId()); 
					List<Post> postlist = postDao.findBydepartmentPost(department.getDepartmentId(), companyId);
					for (int i = 0; i < postlist.size(); i++) {
						String postId = postlist.get(i).getPostId();
						List<Employee> employeelist = employeeDao.findByposcounttemp(postId, companyId);
						postlist.get(i).setEmployeelist(employeelist);
					}
					List<Employee> employeeemp = employeeDao.findBydeptemployee(department.getDepartmentId(), companyId);
					departmentTree.setCountNumber(employeeemp.size());	
					departmentTree.setPostlist(postlist);
					departmentTree.setChildren(getDepartmentTree(department.getDepartmentId(),companyId, departmentTree));;										
			        list.add(departmentTree);
				}
			}
		 }
		return list;
	}

	@Override
	public List<DepartmentTree> getDepartmentempTreeAll(String companyId) {
		List<Department> DepartmentList=departmentDao.findDepartmentTree("0",companyId);		
		List<DepartmentTree> list = new ArrayList<DepartmentTree>();
		for(Department department: DepartmentList){			
			DepartmentTree departmentTree = new DepartmentTree();			
			departmentTree.setLabel(department.getDepartmentName());						
			departmentTree.setValue(department.getDepartmentId());
			departmentTree.setKey(department.getDepartmentId());	
			List<Post> postlist = postDao.findBydepartmentPost(department.getDepartmentId(), companyId);
			for (int i = 0; i < postlist.size(); i++) {
				String postId = postlist.get(i).getPostId();
				List<Employee> employeelist = employeeDao.findByposcounttemp(postId, companyId);
				postlist.get(i).setEmployeelist(employeelist);
			}
			List<Employee> employeeemp = employeeDao.findBydeptemployee(department.getDepartmentId(), companyId);
			departmentTree.setCountNumber(employeeemp.size());	
			departmentTree.setPostlist(postlist);					
			departmentTree.setChildren(getDepartmentTree(department.getDepartmentId(), companyId, departmentTree));
			list.add(departmentTree);
		}
		return list;
	}

	@Override
	public List<Department> findByAllFenyeDepartment(Map<String, String> map) {
		// TODO Auto-generated method stub
		return departmentDao.findByAllFenyeDepartment(map);
	}

	
}
