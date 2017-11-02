package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.dao.PostDao;
import com.xiangshangban.organization.util.FormatUtil;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	PostDao postDao;
	
	@Override
	public String deleteByPost(String postId) {
		String i ="0";
		try {
			postDao.deleteByPost(postId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public String insertPost(Post post) {
		String i ="0";
		try {
			post.setPostId(FormatUtil.createUuid());
			postDao.insertPost(post);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Post selectByPost(String postId,String companyId) {
		// TODO Auto-generated method stub
		return postDao.selectByPost(postId, companyId);
	}

	@Override
	public String updateByPost(Post post) {
		String i ="0";
		try {
			postDao.updateByPost(post);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<Post> selectByPostName(String employeeId,String departmentId) {
		// TODO Auto-generated method stub
		return postDao.selectByPostName(employeeId, departmentId);
	}


	@Override
	public List<Post> selectByAllPostInfo(String companyId) {
		// TODO Auto-generated method stub
		return postDao.selectByAllPostInfo(companyId);
	}

	@Override
	public List<Post> findByMorePostIfon(Map<String, String> map) {
		// TODO Auto-generated method stub
		return postDao.findByMorePostIfon(map);
	}

	@Override
	public List<Post> selectByPostemp(String employeeId, String departmentId) {
		// TODO Auto-generated method stub
		return postDao.selectByPostemp(employeeId, departmentId);
	}

	

	
	

}
