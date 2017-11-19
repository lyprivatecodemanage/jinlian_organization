package com.xiangshangban.organization.util;

import java.io.IOException;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtils {
	
	private static Resource resource; 
	private static Properties props; 
	
	public static void setPropertyName( String propertyFileName ) throws IOException{
		resource = new ClassPathResource( propertyFileName );
		props = PropertiesLoaderUtils.loadProperties( resource );
	}

	public static String  redisProperty( String property ) throws IOException{
		setPropertyName( "/properties/redis.properties" );
		return props.getProperty( property );
	}
	
	public static String  rmqProperty( String property ) throws IOException{
		setPropertyName( "/properties/rmq.properties" );
		return props.getProperty( property );
	}
	public static String  ossProperty( String property ) throws IOException{
		setPropertyName( "/properties/oss.properties" );
		return props.getProperty( property );
	}
	
	public static String  deviceProperty( String property ) throws IOException{
		setPropertyName( "/properties/device.properties" );
		return props.getProperty( property );
	}
	public static String  pathUrl( String property ) throws IOException{
		setPropertyName( "/properties/pathUrl.properties" );
		return props.getProperty( property );
	}
	
	public static String  versionProperty( String property ) throws IOException{
		setPropertyName( "/properties/version.properties" );
		return props.getProperty( property );
	}
}
