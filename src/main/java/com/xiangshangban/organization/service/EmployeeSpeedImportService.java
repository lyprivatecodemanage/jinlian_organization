package com.xiangshangban.organization.service;

import com.xiangshangban.organization.bean.ReturnData;

public interface EmployeeSpeedImportService {
	ReturnData speedImport(String operateUserId,String companyId,String filePath);
}
