package cn.hutool.db.dialect;

import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static cn.hutool.db.dialect.DriverNamePool.DRIVER_CLICK_HOUSE;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_DB2;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_DERBY;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_DM7;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_GAUSS;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_GBASE;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_H2;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_HIGHGO;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_HIVE;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_HIVE2;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_HSQLDB;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_IGNITE_THIN;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_KINGBASE8;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_MARIADB;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_MYSQL_V6;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_ORACLE;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_OSCAR;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_PHOENIX;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_POSTGRESQL;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_SQLLITE3;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_SQLSERVER;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_SYBASE;
import static cn.hutool.db.dialect.DriverNamePool.DRIVER_XUGU;

public class DialectFactoryTest {

	@Test
	public void identifyDriverTest(){
		final Map<String,String> map = new HashMap<>(25);
		map.put("mysql",DRIVER_MYSQL_V6);
		map.put("cobar",DRIVER_MYSQL_V6);
		map.put("oracle",DRIVER_ORACLE);
		map.put("postgresql",DRIVER_POSTGRESQL);
		map.put("sqlite",DRIVER_SQLLITE3);
		map.put("sqlserver",DRIVER_SQLSERVER);
		map.put("microsoft",DRIVER_SQLSERVER);
		map.put("hive2",DRIVER_HIVE2);
		map.put("hive",DRIVER_HIVE);
		map.put("h2",DRIVER_H2);
		map.put("derby",DRIVER_DERBY);
		map.put("hsqldb",DRIVER_HSQLDB);
		map.put("dm",DRIVER_DM7);
		map.put("kingbase8",DRIVER_KINGBASE8);
		map.put("ignite",DRIVER_IGNITE_THIN);
		map.put("clickhouse",DRIVER_CLICK_HOUSE);
		map.put("highgo",DRIVER_HIGHGO);
		map.put("db2",DRIVER_DB2);
		map.put("xugu",DRIVER_XUGU);
		map.put("phoenix",DRIVER_PHOENIX);
		map.put("zenith",DRIVER_GAUSS);
		map.put("gbase",DRIVER_GBASE);
		map.put("oscar",DRIVER_OSCAR);
		map.put("sybase",DRIVER_SYBASE);
		map.put("mariadb",DRIVER_MARIADB);


		map.forEach((k,v) -> Assert.assertEquals(v,
				DialectFactory.identifyDriver(k+ RandomUtil.randomString(2),null) ));

	}
}
