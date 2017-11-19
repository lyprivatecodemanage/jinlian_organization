package com.xiangshangban.organization.service;

public interface CheckPersonService {
	/**
	 * 更改申请状态
	 * @param companyId
	 * @param userId
	 * @param status
	 * @return
	 */
	Integer updateApplyStatus(String companyId, String userId, String status);
	
}
