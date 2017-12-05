package com.xiangshangban.organization.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.Company;
import com.xiangshangban.organization.dao.CompanyDao;
import com.xiangshangban.organization.util.FormatUtil;

@Service
public class CompanyServiceImpl implements CompanyService {
   @Autowired
   CompanyDao companyDao;
	
	@Override
	public String deleteByCompany(String companyId) {
		String i ="0";
		try {
			companyDao.deleteByCompany(companyId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String insertCompany(Company company) {
		String i ="0";
		Company companyNo = companyDao.findBycompanyNo(company.getCompanyNo());
		try {
			company.setCompanyId(FormatUtil.createUuid());
			company.setCompanyCreatTime(company.getCompanyCreatTime());					
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			company.setCompanyCreatTime(simpleDateFormat.format(new Date()));
			if(companyNo != null ){
				i="2";
				return i;//公司编号已存在				
			}	
			companyDao.insertCompany(company);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Company selectByCompany(String companyId) {
		// TODO Auto-generated method stub
		return companyDao.selectByCompany(companyId);
	}

	@Override
	public String updateByCompany(Company company) {
		String i ="0";
		try {
			companyDao.updateByCompany(company);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<Company> fingdByAllCompany() {
		// TODO Auto-generated method stub
		return companyDao.fingdByAllCompany();
	}

	@Override
	public List<Company> selectByUserCompany(String Account) {
		// TODO Auto-generated method stub
		return companyDao.selectByUserCompany(Account);
	}

	@Override
	public int updateCompanyLogoByCompanyId(String companyLogo, String companyId) {
		
		return companyDao.updateCompanyLogoByCompanyId(companyLogo, companyId);
	}

	
	
}
