package com.xiangshangban.organization.service;

import java.util.List;

import com.xiangshangban.organization.bean.Company;

public interface CompanyService {
	String deleteByCompany(String companyId);
	  
    String insertCompany(Company company);
   
    Company selectByCompany(String companyId);

    String updateByCompany(Company company);
    
    List<Company> fingdByAllCompany();
}
