package cn.hutool.db.nosql.hbase.factory;

import cn.hutool.json.JSONUtil;

public class HBaseRow {
    private String tableName;
    private String rowkey;
    private String colFamily;
    private String col;
    private String val;
    private long timetamp;

    public long getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(long timetamp) {
        this.timetamp = timetamp;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getColFamily() {
        return colFamily;
    }

    public void setColFamily(String colFamily) {
        this.colFamily = colFamily;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public HBaseRow(String tableName, String rowkey) {
        this.tableName = tableName;
        this.rowkey = rowkey;
    }

    public HBaseRow(String tableName, String rowkey, String colFamily, String col) {
        this.tableName = tableName;
        this.rowkey = rowkey;
        this.colFamily = colFamily;
        this.col = col;
    }

    public HBaseRow(String tableName, String rowkey, String colFamily, String col, String val) {
        this.tableName = tableName;
        this.rowkey = rowkey;
        this.colFamily = colFamily;
        this.col = col;
        this.val = val;
    }

    public HBaseRow(String tableName, String rowkey, String colFamily, String col, String val, long timetamp) {
        this.tableName = tableName;
        this.rowkey = rowkey;
        this.colFamily = colFamily;
        this.col = col;
        this.val = val;
        this.timetamp = timetamp;
    }

    public HBaseRow(String rowkey, String colFamily, String col, String val, long timetamp) {
        this.rowkey = rowkey;
        this.colFamily = colFamily;
        this.col = col;
        this.val = val;
        this.timetamp = timetamp;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
