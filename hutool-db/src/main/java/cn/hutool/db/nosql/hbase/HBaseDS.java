package cn.hutool.db.nosql.hbase;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.nosql.hbase.factory.ConnectionPoolConfig;
import cn.hutool.db.nosql.hbase.factory.HBaseRow;
import cn.hutool.db.nosql.hbase.factory.HbaseConnectionPool;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.StaticLog;
import cn.hutool.setting.Setting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>HBase增删改查工具类</p>
 * <p>setting文件需要指定group，默认config/hbase.setting</p>
 */
public class HBaseDS {
    private static final Log log = StaticLog.get();
    private static HbaseConnectionPool pool;

    public static final String HBASE_CONFIG_PATH = "config/hbase.setting";
    public static final String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
    public static final String ZOOKEEPER_ZNODE_PARENT = "zookeeper.znode.parent";
    public static final String HBASE_POOL_MAX_TOTAL = "hbase.pool.max-total";
    public static final String HBASE_POOL_MAX_IDLE = "hbase.pool.max-idle";
    public static final String HBASE_POOL_MAX_WAITMILLIS = "hbase.pool.max-waitmillis";
    public static final String HBASE_POOL_TESTONBORROW = "hbase.pool.testonborrow";

    private Setting setting;
    private String group;

    private HBaseDS() {}

    public HBaseDS(String group) {
        this.group = group;
        initCloud();
    }

    public HBaseDS(Setting hbaseSetting, String group) {
        this.setting = hbaseSetting;
        this.group = group;
        initCloud();
    }

    /**
     * 初始化集群<br>
     *
     * <pre>
     *
     * [hbase1]
     * hbase.zookeeper.quorum = 192.168.1.1,192.168.1.2:27117
     * zookeeper.znode.parent = /hbase-unsecure (optional)
     * hbase.pool.max-total=2000
     * hbase.pool.max-idle=1000
     * hbase.pool.max-waitmillis=5000
     * hbase.pool.testonborrow=true
     *
     * </pre>
     */
    synchronized private void initCloud() {
        if (setting == null) {
            // 若未指定配置文件，则使用默认配置文件
            this.setting = new Setting(HBASE_CONFIG_PATH, true);
        }

        Configuration hbaseConfig = HBaseConfiguration.create();
        @Nonnull String zkQuorum = setting.getStr(HBASE_ZOOKEEPER_QUORUM,group);
        hbaseConfig.set("hbase.zookeeper.quorum",zkQuorum);
        String znodeParent = setting.getStr(ZOOKEEPER_ZNODE_PARENT,group);
        if (StrUtil.isNotBlank(znodeParent))
            hbaseConfig.set("zookeeper.znode.parent",znodeParent);

        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setMaxTotal(setting.getInt(HBASE_POOL_MAX_TOTAL,group));
        config.setMaxIdle(setting.getInt(HBASE_POOL_MAX_IDLE,group));
        config.setMaxWaitMillis(setting.getInt(HBASE_POOL_MAX_WAITMILLIS,group));
        config.setTestOnBorrow(setting.getBool(HBASE_POOL_TESTONBORROW,group));

        this.pool = new HbaseConnectionPool(config, hbaseConfig);
        log.info("Init HBaseDB cloud Set pool with connection to {}", zkQuorum);
    }

    /**
     * 建表
     * @param tableNmae
     * @param cols
     * @throws IOException
     */
    public void createTable(@Nonnull String tableNmae, @Nonnull String... cols) throws IOException {
        Connection connection = pool.getConnection();
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf(tableNmae);

        if (admin.tableExists(tableName)) {
            System.out.println("talbe is exists!");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for (String col : cols) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
        }
        close(admin,connection);
    }

    /**
     * 删表
     * @param tableName
     * @throws IOException
     */
    public void deleteTable(@Nonnull String... tableName) throws IOException {
        Connection connection = pool.getConnection();
        Admin admin = connection.getAdmin();
        for (String name : tableName) {
            TableName tn = TableName.valueOf(name);
            if (admin.tableExists(tn)) {
                admin.disableTable(tn);
                admin.deleteTable(tn);
            }
        }
        close(admin,connection);
    }

    /**
     * 查看已有表
     * @throws IOException
     */
    public List<String> listTables() throws IOException {
        Connection connection = pool.getConnection();
        Admin admin = connection.getAdmin();
        List<String> list = new ArrayList<>();

        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            list.add(hTableDescriptor.getNameAsString());
        }
        close(admin,connection);
        return list;
    }

    /**
     * 插入数据<br>
     * 注意：多条插入时TableName需要一致
     * @param hbaseRow
     * @throws IOException
     */
    public void insterRow(@Nonnull HBaseRow... hbaseRow) throws IOException {
        Connection connection = pool.getConnection();
        Table table = connection.getTable(TableName.valueOf(hbaseRow[0].getTableName()));
        List<Put> putList = new ArrayList<>();
        for (HBaseRow row : hbaseRow) {
            Put put = new Put(Bytes.toBytes(row.getRowkey()));
            put.addColumn(Bytes.toBytes(row.getColFamily()),
                    Bytes.toBytes(row.getCol()),
                    Bytes.toBytes(row.getVal()));
            putList.add(put);
        }
        table.put(putList);
        table.close();
        close(connection);
    }

    /**
     * 删除数据-RowKEY<br>
     * @param hbaseRow
     * @throws IOException
     */
    public void deleRow(@Nonnull HBaseRow... hbaseRow) throws IOException {
        Connection connection = pool.getConnection();
        for (HBaseRow row : hbaseRow) {
            Table table = connection.getTable(TableName.valueOf(row.getTableName()));
            Delete delete = new Delete(Bytes.toBytes(row.getRowkey()));
            table.delete(delete);
            table.close();
        }
        close(connection);
    }

    /**
     * 删除数据-Rowkey-Family
     * @param hbaseRow
     * @throws IOException
     */
    public void deleRowFamily(@Nonnull HBaseRow... hbaseRow) throws IOException {
        Connection connection = pool.getConnection();
        for (HBaseRow row : hbaseRow) {
            Table table = connection.getTable(TableName.valueOf(row.getTableName()));
            Delete delete = new Delete(Bytes.toBytes(row.getRowkey()));
            delete.addFamily(Bytes.toBytes(row.getColFamily()));
            table.delete(delete);
            table.close();
        }
        close(connection);
    }

    /**
     * 删除数据-Rowkey-Family-Col
     * @param hbaseRow
     * @throws IOException
     */
    public void deleRowFamilyCol(@Nonnull HBaseRow... hbaseRow) throws IOException {
        Connection connection = pool.getConnection();
        for (HBaseRow row : hbaseRow) {
            Table table = connection.getTable(TableName.valueOf(row.getTableName()));
            Delete delete = new Delete(Bytes.toBytes(row.getRowkey()));
            delete.addColumn(Bytes.toBytes(row.getColFamily()),Bytes.toBytes(row.getCol()));
            table.delete(delete);
            table.close();
        }
        close(connection);
    }

    /**
     * 根据rowkey查找数据
     * @param hbaseRow
     * @throws IOException
     */
    public List<String> getData(@Nonnull HBaseRow... hbaseRow) throws IOException {
        Connection connection = pool.getConnection();
        List<String> list = new ArrayList<>();
        for (HBaseRow row : hbaseRow) {
            Table table = connection.getTable(TableName.valueOf(row.getTableName()));
            Get get = new Get(Bytes.toBytes(row.getRowkey()));
            Result result = table.get(get);
            list.add(showCell(result).toString());
            table.close();
        }
        close(connection);
        return list;
    }

    /**
     * 根据rowkey查找数据-指定列族数据
     * @param hbaseRow
     * @throws IOException
     */
    public List<String> getDataFamily(@Nonnull HBaseRow... hbaseRow) throws IOException {
        Connection connection = pool.getConnection();
        List<String> list = new ArrayList<>();
        for (HBaseRow row : hbaseRow) {
            Table table = connection.getTable(TableName.valueOf(row.getTableName()));
            Get get = new Get(Bytes.toBytes(row.getRowkey()));
            get.addFamily(Bytes.toBytes(row.getColFamily()));
            Result result = table.get(get);
            list.add(showCell(result).toString());
            table.close();
        }
        close(connection);
        return list;
    }

    /**
     * 根据rowkey查找数据-指定列族数据-定列数据
     * @param hbaseRow
     * @throws IOException
     */
    public List<String> getDataFamilyCol(@Nonnull HBaseRow... hbaseRow) throws IOException {
        Connection connection = pool.getConnection();
        List<String> list = new ArrayList<>();
        for (HBaseRow row : hbaseRow) {
            Table table = connection.getTable(TableName.valueOf(row.getTableName()));
            Get get = new Get(Bytes.toBytes(row.getRowkey()));
            get.addColumn(Bytes.toBytes(row.getColFamily()),Bytes.toBytes(row.getCol()));
            Result result = table.get(get);
            list.add(showCell(result).toString());
            table.close();
        }
        close(connection);
        return list;
    }

    /**
     * 查询标准输出
     * @param result
     * @return
     */
    private JSONArray showCell(Result result) {
        Cell[] cells = result.rawCells();
        JSONArray array = JSONUtil.createArray();
        for (Cell cell : cells){
            HBaseRow hbaseRow = new HBaseRow(new String(CellUtil.cloneRow(cell)),
                    new String(CellUtil.cloneFamily(cell)),
                    new String(CellUtil.cloneQualifier(cell)),
                    new String(CellUtil.cloneValue(cell)),
                    cell.getTimestamp());
            array.add(hbaseRow);
        }
        return array;
    }

    /**
     * 关闭操作
     * @param admin
     * @param connection
     * @throws IOException
     */
    private void close(Admin admin,Connection connection) {
        try {
            admin.close();
        }catch (IOException e){
            log.error("hbase admin关闭出现问题。",e);
        }
        close(connection);
    }

    /**
     * 关闭操作
     * @param connection
     */
    private void close(Connection connection) {
        try {
            pool.returnConnection(connection);
        }catch (Exception e){
            log.error("hbase connection关闭出现问题。",e);
        }
    }
}
