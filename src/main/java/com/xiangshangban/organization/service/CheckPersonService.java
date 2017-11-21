package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;

import com.xiangshangban.organization.bean.CheckPerson;
import com.xiangshangban.organization.bean.ReturnData;

public interface CheckPersonService {
	/**
	 * 更改申请状态
	 * @param companyId
	 * @param userId
	 * @param status
	 * @return
	 */
	ReturnData updateApplyStatus(String companyId, String userId, String status);
	/**
	 * 分页查询待审核、待完善列表
	 * @param params
	 * @return
	 */
	List<CheckPerson> getcheckListByPage(Map<String, String> params);
	/**
	 * 查询待审核、待完善列表总数
	 * @param params
	 * @return
	 */
	int getcheckListByPageAllLength(Map<String, String> params);
	
}
