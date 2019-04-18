package cn.hutool.db.nosql.hbase.factory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

import java.util.Properties;

public class HbaseConnectionPool extends ConnectionPoolBase<Connection> implements ConnectionPool<Connection> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9126420905798370243L;

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 默认构造方法</p>
     */
    public HbaseConnectionPool() {

        this(HbaseConfig.DEFAULT_HOST, HbaseConfig.DEFAULT_PORT);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host 地址
     * @param port 端口
     */
    public HbaseConnectionPool(final String host, final String port) {

        this(new ConnectionPoolConfig(), host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param host    地址
     * @param port    端口
     * @param master  hbase主机
     * @param rootdir hdfs目录
     */
    public HbaseConnectionPool(final String host, final String port, final String master, final String rootdir) {

        this(new ConnectionPoolConfig(), host, port, master, rootdir);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param hadoopConfiguration hbase配置
     */
    public HbaseConnectionPool(final Configuration hadoopConfiguration) {

        this(new ConnectionPoolConfig(), hadoopConfiguration);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     */
    public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final String host, final String port) {

        this(poolConfig, host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig          池配置
     * @param hadoopConfiguration hbase配置
     */
    public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final Configuration hadoopConfiguration) {

        super(poolConfig, new HbaseConnectionFactory(hadoopConfiguration));
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param host       地址
     * @param port       端口
     * @param master     hbase主机
     * @param rootdir    hdfs目录
     */
    public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final String host, final String port, final String master, final String rootdir) {

        super(poolConfig, new HbaseConnectionFactory(host, port, master, rootdir));
    }

    /**
     * @param poolConfig 池配置
     * @param properties 参数配置
     * @since 1.2.1
     */
    public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final Properties properties) {

        super(poolConfig, new HbaseConnectionFactory(properties));
    }

    @Override
    public Connection getConnection() {

        return super.getResource();
    }

    @Override
    public void returnConnection(Connection conn) {

        super.returnResource(conn);
    }

    @Override
    public void invalidateConnection(Connection conn) {

        super.invalidateResource(conn);
    }

}
