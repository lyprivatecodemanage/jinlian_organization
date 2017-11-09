package com.xiangshangban.organization.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangshangban.organization.bean.Company;

@Mapper
public interface CompanyDao {
    
    int deleteByCompany(String companyId);
  
    int insertCompany(Company company);
   
    Company selectByCompany(String companyId);

    int updateByCompany(Company company);
    
    List<Company> fingdByAllCompany();
    
    Company  findBycompanyNo(String companyNo);
    //查询一个人加入了哪些公司
    List<Company>selectByUserCompany(String Account);
}