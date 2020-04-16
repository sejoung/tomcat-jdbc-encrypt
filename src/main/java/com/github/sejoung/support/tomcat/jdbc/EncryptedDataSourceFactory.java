package com.github.sejoung.support.tomcat.jdbc;

import com.github.sejoung.support.tomcat.encryptor.Encryptor;
import com.github.sejoung.support.tomcat.encryptor.impl.AesEncryptor;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;

public class EncryptedDataSourceFactory extends DataSourceFactory {

	private static final Log log = LogFactory.getLog(EncryptedDataSourceFactory.class);

	public EncryptedDataSourceFactory() {
	}

	public javax.sql.DataSource createDataSource(Properties properties, Context context, boolean XA)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SQLException, NoSuchAlgorithmException, NoSuchPaddingException {
		PoolConfiguration poolProperties = parsePoolProperties(properties);
		String secretKey = properties.getProperty("secretKey");
		log.debug("secretKey =" + secretKey);

		Encryptor encryptor = new AesEncryptor(properties.getProperty("secretKey"));

		log.debug("password " + poolProperties.getPassword() + " decrypt password " + encryptor
				.decrypt(poolProperties.getPassword()));

		poolProperties.setPassword(encryptor.decrypt(poolProperties.getPassword()));

		log.debug("username " + poolProperties.getUsername() + " decrypt username " + encryptor
				.decrypt(poolProperties.getUsername()));

		poolProperties.setUsername(encryptor.decrypt(poolProperties.getUsername()));
		if ((poolProperties.getDataSourceJNDI() != null) && (poolProperties.getDataSource() == null)) {
			performJNDILookup(context, poolProperties);
		}
		org.apache.tomcat.jdbc.pool.DataSource dataSource = XA ? new XADataSource(poolProperties)
				: new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
		dataSource.createPool();

		return dataSource;
	}
}
