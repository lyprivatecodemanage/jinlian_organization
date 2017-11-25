package com.xiangshangban.organization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationApplicationTests {

	@Test
	public void contextLoads() {
	}
	public static void main(String[] args) {
		for(int i =0 ;i<5;i++){
			System.out.print("="+i);
			System.out.println();
			for(int j=0;j<4;j++){
				if(j==1){
					break;
				}
				System.out.print(j);
			}
			System.out.println();
			
		}
	}
}
