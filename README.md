# 톰캣 DataSource user 정보 암호화 시키기

톰캣에 jndi를 설정 하기 위해서 Resource를 추가 하는데 그곳에 계정 정보가 들어가게 된다 

서버 해킹시 계정 정보가 암호화 되있지 않고 노출 되어 있어서 암호화 복호화 로직이 들어가게 된다.


https://issues.sonatype.org/browse/OSSRH-41242

톰캣 7.0.55 버전과 6.0.41버전으로 테스트를 해보았다.

Resource 설정 

톰캣 7.0 이상 
 
```xml

<Resource name="jdbc/TestDB"
          factory="com.github.sejoung.support.tomcat.jdbc.EncryptedDataSourceFactory"
          auth="Container"
          type="javax.sql.DataSource"
          maxActive="100"
          maxIdle="30"
          maxWait="10000"
          username="808233982b9c435fb8a3331634a3c48b"
          password="3b8dcdcf348d8b466915f66c30003e95"
          driverClassName="org.mariadb.jdbc.Driver"
          url="jdbc:mariadb://localhost:3306/test"/> 
          
```

톰캣 7.0이하 
 
```xml

<Resource name="jdbc/TestDB"
          factory="com.github.sejoung.support.tomcat.jdbc.EncryptedDataSourceFactoryDbcp"
          auth="Container"
          type="javax.sql.DataSource"
          maxActive="100"
          maxIdle="30"
          maxWait="10000"
          username="808233982b9c435fb8a3331634a3c48b"
          password="3b8dcdcf348d8b466915f66c30003e95"
          driverClassName="org.mariadb.jdbc.Driver"
          url="jdbc:mariadb://localhost:3306/test"/>

 ```

pom.xml에 추가후에 위에 처럼 사용가능

```xml

<dependency>
    <groupId>com.github.sejoung</groupId>
    <artifactId>tomcat-jdbc-encrypt</artifactId>
    <version>1.5</version>
</dependency>

```
1.5 버전에 decrypt 추가

USAGE: java -jar tomcat-jdbc-encrypt-[version].jar [encrypt,decrypt] [secretKey] [string-to-encrypt,string-to-decrypt]

```
java -jar tomcat-jdbc-encrypt-1.4.jar encrypt key 1

java -jar tomcat-jdbc-encrypt-1.4.jar decrypt key eb77d942479a6b2e44841d653175e8a3

```
