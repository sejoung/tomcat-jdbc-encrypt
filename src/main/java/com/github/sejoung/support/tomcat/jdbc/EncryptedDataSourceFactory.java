package com.github.sejoung.support.tomcat.jdbc;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;

import com.github.sejoung.support.tomcat.encryptor.Encryptor;
import com.github.sejoung.support.tomcat.encryptor.impl.AesEncryptor;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;

public class EncryptedDataSourceFactory extends DataSourceFactory {
	private static final Log log = LogFactory.getLog(EncryptedDataSourceFactory.class);
	private Encryptor encryptor = null;

	public EncryptedDataSourceFactory() {
		if (this.encryptor == null) {
			this.encryptor = new AesEncryptor();
		}
	}

	public javax.sql.DataSource createDataSource(Properties properties, Context context, boolean XA) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SQLException, NoSuchAlgorithmException, NoSuchPaddingException {
		PoolConfiguration poolProperties = parsePoolProperties(properties);

		log.debug("password " + poolProperties.getPassword() + " decrypt password " + this.encryptor.decrypt(poolProperties.getPassword()));

		poolProperties.setPassword(this.encryptor.decrypt(poolProperties.getPassword()));

		log.debug("username " + poolProperties.getUsername() + " decrypt username " + this.encryptor.decrypt(poolProperties.getUsername()));

		poolProperties.setUsername(this.encryptor.decrypt(poolProperties.getUsername()));
		if ((poolProperties.getDataSourceJNDI() != null) && (poolProperties.getDataSource() == null)) {
			performJNDILookup(context, poolProperties);
		}
		org.apache.tomcat.jdbc.pool.DataSource dataSource = XA ? new XADataSource(poolProperties) : new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
		dataSource.createPool();

		return dataSource;
	}
}
