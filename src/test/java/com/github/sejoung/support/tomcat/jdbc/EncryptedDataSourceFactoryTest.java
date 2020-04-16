package com.github.sejoung.support.tomcat.jdbc;


import java.util.Properties;
import org.junit.Test;

public class EncryptedDataSourceFactoryTest {

	@Test
	public void createDataSource() throws Exception {

		Properties properties = new Properties();

		properties.setProperty("secretKey","aaa");
		properties.setProperty("","");
		properties.setProperty("","");

		EncryptedDataSourceFactoryDbcp.createDataSource(properties);

	}
}