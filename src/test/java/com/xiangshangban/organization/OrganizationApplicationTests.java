package com.xiangshangban.organization;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiangshangban.organization.bean.Post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationApplicationTests {

	@Test
	public void contextLoads() {
	}
	public static void main(String[] args) {
		List<Post> list = new ArrayList<Post>();
		list.add(null);
		list.add(null);
		System.out.println(list.size());
		Post one = list.get(0);
		Post tow = list.get(1);
		System.out.println(one);
		System.out.println(tow);
		
	}
}
