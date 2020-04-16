package com.github.sejoung.support.tomcat.jdbc;

import com.github.sejoung.support.tomcat.encryptor.Encryptor;
import com.github.sejoung.support.tomcat.encryptor.impl.AesEncryptor;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class EncryptedDataSourceFactoryDbcp implements ObjectFactory {

	private static final String PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit";
	private static final String PROP_DEFAULTREADONLY = "defaultReadOnly";
	private static final String PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation";
	private static final String PROP_DEFAULTCATALOG = "defaultCatalog";
	private static final String PROP_DRIVERCLASSNAME = "driverClassName";
	private static final String PROP_MAXACTIVE = "maxActive";
	private static final String PROP_MAXIDLE = "maxIdle";
	private static final String PROP_MINIDLE = "minIdle";
	private static final String PROP_INITIALSIZE = "initialSize";
	private static final String PROP_MAXWAIT = "maxWait";
	private static final String PROP_TESTONBORROW = "testOnBorrow";
	private static final String PROP_TESTONRETURN = "testOnReturn";
	private static final String PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";
	private static final String PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun";
	private static final String PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";
	private static final String PROP_TESTWHILEIDLE = "testWhileIdle";
	private static final String PROP_PASSWORD = "password";
	private static final String PROP_URL = "url";
	private static final String PROP_USERNAME = "username";
	private static final String PROP_VALIDATIONQUERY = "validationQuery";
	private static final String PROP_VALIDATIONQUERY_TIMEOUT = "validationQueryTimeout";
	private static final String PROP_INITCONNECTIONSQLS = "initConnectionSqls";
	private static final String PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED = "accessToUnderlyingConnectionAllowed";
	private static final String PROP_REMOVEABANDONED = "removeAbandoned";
	private static final String PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout";
	private static final String PROP_LOGABANDONED = "logAbandoned";
	private static final String PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements";
	private static final String PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements";
	private static final String PROP_CONNECTIONPROPERTIES = "connectionProperties";
	private static final int UNKNOWN_TRANSACTIONISOLATION = -1;
	private static final String[] ALL_PROPERTIES = {"defaultAutoCommit", "defaultReadOnly",
			"defaultTransactionIsolation", "defaultCatalog", "driverClassName", "maxActive", "maxIdle",
			"minIdle", "initialSize", "maxWait", "testOnBorrow", "testOnReturn",
			"timeBetweenEvictionRunsMillis", "numTestsPerEvictionRun", "minEvictableIdleTimeMillis",
			"testWhileIdle", "password", "url", "username", "validationQuery", "validationQueryTimeout",
			"initConnectionSqls", "accessToUnderlyingConnectionAllowed", "removeAbandoned",
			"removeAbandonedTimeout", "logAbandoned", "poolPreparedStatements",
			"maxOpenPreparedStatements", "connectionProperties", "secretKey"};

	public static DataSource createDataSource(Properties properties) throws Exception {
		BasicDataSource dataSource = new BasicDataSource();

		String value = null;
		String secretKey = properties.getProperty("secretKey");
		Encryptor encryptor = new AesEncryptor(secretKey);
		System.out.println("EncryptedDataSourceFactoryDbcp secretKey = " + secretKey);
		value = properties.getProperty("defaultAutoCommit");
		if (value != null) {
			dataSource.setDefaultAutoCommit(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("defaultReadOnly");
		if (value != null) {
			dataSource.setDefaultReadOnly(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("defaultTransactionIsolation");
		if (value != null) {
			int level = -1;
			if ("NONE".equalsIgnoreCase(value)) {
				level = 0;
			} else if ("READ_COMMITTED".equalsIgnoreCase(value)) {
				level = 2;
			} else if ("READ_UNCOMMITTED".equalsIgnoreCase(value)) {
				level = 1;
			} else if ("REPEATABLE_READ".equalsIgnoreCase(value)) {
				level = 4;
			} else if ("SERIALIZABLE".equalsIgnoreCase(value)) {
				level = 8;
			} else {
				try {
					level = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					System.err.println("Could not parse defaultTransactionIsolation: " + value);
					System.err.println("WARNING: defaultTransactionIsolation not set");
					System.err.println("using default value of database driver");
					level = -1;
				}
			}
			dataSource.setDefaultTransactionIsolation(level);
		}
		value = properties.getProperty("defaultCatalog");
		if (value != null) {
			dataSource.setDefaultCatalog(value);
		}
		value = properties.getProperty("driverClassName");
		if (value != null) {
			dataSource.setDriverClassName(value);
		}
		value = properties.getProperty("maxActive");
		if (value != null) {
			dataSource.setMaxActive(Integer.parseInt(value));
		}
		value = properties.getProperty("maxIdle");
		if (value != null) {
			dataSource.setMaxIdle(Integer.parseInt(value));
		}
		value = properties.getProperty("minIdle");
		if (value != null) {
			dataSource.setMinIdle(Integer.parseInt(value));
		}
		value = properties.getProperty("initialSize");
		if (value != null) {
			dataSource.setInitialSize(Integer.parseInt(value));
		}
		value = properties.getProperty("maxWait");
		if (value != null) {
			dataSource.setMaxWait(Long.parseLong(value));
		}
		value = properties.getProperty("testOnBorrow");
		if (value != null) {
			dataSource.setTestOnBorrow(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("testOnReturn");
		if (value != null) {
			dataSource.setTestOnReturn(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("timeBetweenEvictionRunsMillis");
		if (value != null) {
			dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(value));
		}
		value = properties.getProperty("numTestsPerEvictionRun");
		if (value != null) {
			dataSource.setNumTestsPerEvictionRun(Integer.parseInt(value));
		}
		value = properties.getProperty("minEvictableIdleTimeMillis");
		if (value != null) {
			dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(value));
		}
		value = properties.getProperty("testWhileIdle");
		if (value != null) {
			dataSource.setTestWhileIdle(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("password");
		if (value != null) {
			dataSource.setPassword(encryptor.decrypt(value));
		}
		value = properties.getProperty("url");
		if (value != null) {
			dataSource.setUrl(value);
		}
		value = properties.getProperty("username");
		if (value != null) {
			dataSource.setUsername(encryptor.decrypt(value));
		}
		value = properties.getProperty("validationQuery");
		if (value != null) {
			dataSource.setValidationQuery(value);
		}
		value = properties.getProperty("validationQueryTimeout");
		if (value != null) {
			dataSource.setValidationQueryTimeout(Integer.parseInt(value));
		}
		value = properties.getProperty("accessToUnderlyingConnectionAllowed");
		if (value != null) {
			dataSource.setAccessToUnderlyingConnectionAllowed(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("removeAbandoned");
		if (value != null) {
			dataSource.setRemoveAbandoned(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("removeAbandonedTimeout");
		if (value != null) {
			dataSource.setRemoveAbandonedTimeout(Integer.parseInt(value));
		}
		value = properties.getProperty("logAbandoned");
		if (value != null) {
			dataSource.setLogAbandoned(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("poolPreparedStatements");
		if (value != null) {
			dataSource.setPoolPreparedStatements(Boolean.valueOf(value).booleanValue());
		}
		value = properties.getProperty("maxOpenPreparedStatements");
		if (value != null) {
			dataSource.setMaxOpenPreparedStatements(Integer.parseInt(value));
		}
		value = properties.getProperty("initConnectionSqls");
		if (value != null) {
			StringTokenizer tokenizer = new StringTokenizer(value, ";");
			dataSource.setConnectionInitSqls(Collections.list(tokenizer));
		}
		value = properties.getProperty("connectionProperties");
		if (value != null) {
			Properties p = getProperties(value);
			Enumeration e = p.propertyNames();
			while (e.hasMoreElements()) {
				String propertyName = (String) e.nextElement();
				dataSource.addConnectionProperty(propertyName, p.getProperty(propertyName));
			}
		}
		if (dataSource.getInitialSize() > 0) {
			dataSource.getLogWriter();
		}
		return dataSource;
	}

	private static Properties getProperties(String propText) throws Exception {
		Properties p = new Properties();
		if (propText != null) {
			p.load(new ByteArrayInputStream(propText.replace(';', '\n').getBytes()));
		}
		return p;
	}

	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment)
			throws Exception {
		if ((obj == null) || (!(obj instanceof Reference))) {
			return null;
		}
		Reference ref = (Reference) obj;
		if (!"javax.sql.DataSource".equals(ref.getClassName())) {
			return null;
		}
		Properties properties = new Properties();
		for (int i = 0; i < ALL_PROPERTIES.length; i++) {
			String propertyName = ALL_PROPERTIES[i];
			RefAddr ra = ref.get(propertyName);
			if (ra != null) {
				String propertyValue = ra.getContent().toString();
				properties.setProperty(propertyName, propertyValue);
			}
		}
		return createDataSource(properties);
	}
}
