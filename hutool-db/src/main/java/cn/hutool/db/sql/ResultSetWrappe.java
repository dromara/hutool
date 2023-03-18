package cn.hutool.db.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * 实现ResultSet
 * @author HeYuJie
 * @date 2023/3/17
 */
public class ResultSetWrappe implements ResultSet {

    private final ResultSet resultSet;

    public ResultSetWrappe(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public void updateBytes(String args0, byte[] args1) throws SQLException {
        resultSet.updateBytes(args0, args1);
    }

    @Override
    public void updateBytes(int args0, byte[] args1) throws SQLException {
        resultSet.updateBytes(args0, args1);
    }

    @Override
    public Object getObject(int args0, Map args1) throws SQLException {
        return resultSet.getObject(args0, args1);
    }

    @Override
    public Object getObject(String args0) throws SQLException {
        return resultSet.getObject(args0);
    }

    @Override
    public Object getObject(int args0) throws SQLException {
        return resultSet.getObject(args0);
    }

    @Override
    public Object getObject(String args0, Map args1) throws SQLException {
        return resultSet.getObject(args0, args1);
    }

    @Override
    public Object getObject(int args0, Class args1) throws SQLException {
        return resultSet.getObject(args0, args1);
    }

    @Override
    public Object getObject(String args0, Class args1) throws SQLException {
        return resultSet.getObject(args0, args1);
    }

    @Override
    public boolean getBoolean(int args0) throws SQLException {
        return resultSet.getBoolean(args0);
    }

    @Override
    public boolean getBoolean(String args0) throws SQLException {
        return resultSet.getBoolean(args0);
    }

    @Override
    public byte getByte(String args0) throws SQLException {
        return resultSet.getByte(args0);
    }

    @Override
    public byte getByte(int args0) throws SQLException {
        return resultSet.getByte(args0);
    }

    @Override
    public short getShort(int args0) throws SQLException {
        return resultSet.getShort(args0);
    }

    @Override
    public short getShort(String args0) throws SQLException {
        return resultSet.getShort(args0);
    }

    @Override
    public int getInt(int args0) throws SQLException {
        return resultSet.getInt(args0);
    }

    @Override
    public int getInt(String args0) throws SQLException {
        return resultSet.getInt(args0);
    }

    @Override
    public long getLong(String args0) throws SQLException {
        return resultSet.getLong(args0);
    }

    @Override
    public long getLong(int args0) throws SQLException {
        return resultSet.getLong(args0);
    }

    @Override
    public float getFloat(String args0) throws SQLException {
        return resultSet.getFloat(args0);
    }

    @Override
    public float getFloat(int args0) throws SQLException {
        return resultSet.getFloat(args0);
    }

    @Override
    public double getDouble(String args0) throws SQLException {
        return resultSet.getDouble(args0);
    }

    @Override
    public double getDouble(int args0) throws SQLException {
        return resultSet.getDouble(args0);
    }

    @Override
    public byte[] getBytes(int args0) throws SQLException {
        return resultSet.getBytes(args0);
    }

    @Override
    public byte[] getBytes(String args0) throws SQLException {
        return resultSet.getBytes(args0);
    }

    @Override
    public boolean next() throws SQLException {
        return resultSet.next();
    }

    @Override
    public Array getArray(String args0) throws SQLException {
        return resultSet.getArray(args0);
    }

    @Override
    public Array getArray(int args0) throws SQLException {
        return resultSet.getArray(args0);
    }

    @Override
    public URL getURL(int args0) throws SQLException {
        return resultSet.getURL(args0);
    }

    @Override
    public URL getURL(String args0) throws SQLException {
        return resultSet.getURL(args0);
    }

    @Override
    public boolean first() throws SQLException {
        return resultSet.first();
    }

    @Override
    public void close() throws SQLException {
        resultSet.close();
    }

    @Override
    public int getType() throws SQLException {
        return resultSet.getType();
    }

    @Override
    public Ref getRef(String args0) throws SQLException {
        return resultSet.getRef(args0);
    }

    @Override
    public Ref getRef(int args0) throws SQLException {
        return resultSet.getRef(args0);
    }

    @Override
    public boolean previous() throws SQLException {
        return resultSet.previous();
    }

    @Override
    public Time getTime(String args0, Calendar args1) throws SQLException {
        return resultSet.getTime(args0, args1);
    }

    @Override
    public Time getTime(String args0) throws SQLException {
        return resultSet.getTime(args0);
    }

    @Override
    public Time getTime(int args0) throws SQLException {
        return resultSet.getTime(args0);
    }

    @Override
    public Time getTime(int args0, Calendar args1) throws SQLException {
        return resultSet.getTime(args0, args1);
    }

    @Override
    public boolean last() throws SQLException {
        return resultSet.last();
    }

    @Override
    public Date getDate(String args0, Calendar args1) throws SQLException {
        return resultSet.getDate(args0, args1);
    }

    @Override
    public Date getDate(int args0, Calendar args1) throws SQLException {
        return resultSet.getDate(args0, args1);
    }

    @Override
    public Date getDate(String args0) throws SQLException {
        return resultSet.getDate(args0);
    }

    @Override
    public Date getDate(int args0) throws SQLException {
        return resultSet.getDate(args0);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return resultSet.isClosed();
    }

    @Override
    public String getString(int args0) throws SQLException {
        return resultSet.getString(args0);
    }

    @Override
    public String getString(String args0) throws SQLException {
        return resultSet.getString(args0);
    }

    @Override
    public boolean absolute(int args0) throws SQLException {
        return resultSet.absolute(args0);
    }

    @Override
    public void afterLast() throws SQLException {
        resultSet.afterLast();
    }

    @Override
    public void beforeFirst() throws SQLException {
        resultSet.beforeFirst();
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        resultSet.cancelRowUpdates();
    }

    @Override
    public void clearWarnings() throws SQLException {
        resultSet.clearWarnings();
    }

    @Override
    public void deleteRow() throws SQLException {
        resultSet.deleteRow();
    }

    @Override
    public int findColumn(String args0) throws SQLException {
        return resultSet.findColumn(args0);
    }

    @Override
    public InputStream getAsciiStream(String args0) throws SQLException {
        return resultSet.getAsciiStream(args0);
    }

    @Override
    public InputStream getAsciiStream(int args0) throws SQLException {
        return resultSet.getAsciiStream(args0);
    }

    @Override
    public BigDecimal getBigDecimal(String args0, int args1) throws SQLException {
        return resultSet.getBigDecimal(args0, args1);
    }

    @Override
    public BigDecimal getBigDecimal(int args0) throws SQLException {
        return resultSet.getBigDecimal(args0);
    }

    @Override
    public BigDecimal getBigDecimal(int args0, int args1) throws SQLException {
        return resultSet.getBigDecimal(args0, args1);
    }

    @Override
    public BigDecimal getBigDecimal(String args0) throws SQLException {
        return resultSet.getBigDecimal(args0);
    }

    @Override
    public InputStream getBinaryStream(String args0) throws SQLException {
        return resultSet.getBinaryStream(args0);
    }

    @Override
    public InputStream getBinaryStream(int args0) throws SQLException {
        return resultSet.getBinaryStream(args0);
    }

    @Override
    public Blob getBlob(int args0) throws SQLException {
        return resultSet.getBlob(args0);
    }

    @Override
    public Blob getBlob(String args0) throws SQLException {
        return resultSet.getBlob(args0);
    }

    @Override
    public Reader getCharacterStream(int args0) throws SQLException {
        return resultSet.getCharacterStream(args0);
    }

    @Override
    public Reader getCharacterStream(String args0) throws SQLException {
        return resultSet.getCharacterStream(args0);
    }

    @Override
    public Clob getClob(int args0) throws SQLException {
        return resultSet.getClob(args0);
    }

    @Override
    public Clob getClob(String args0) throws SQLException {
        return resultSet.getClob(args0);
    }

    @Override
    public int getConcurrency() throws SQLException {
        return resultSet.getConcurrency();
    }

    @Override
    public String getCursorName() throws SQLException {
        return resultSet.getCursorName();
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return resultSet.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws SQLException {
        return resultSet.getFetchSize();
    }

    @Override
    public int getHoldability() throws SQLException {
        return resultSet.getHoldability();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return resultSet.getMetaData();
    }

    @Override
    public Reader getNCharacterStream(String args0) throws SQLException {
        return resultSet.getNCharacterStream(args0);
    }

    @Override
    public Reader getNCharacterStream(int args0) throws SQLException {
        return resultSet.getNCharacterStream(args0);
    }

    @Override
    public NClob getNClob(String args0) throws SQLException {
        return resultSet.getNClob(args0);
    }

    @Override
    public NClob getNClob(int args0) throws SQLException {
        return resultSet.getNClob(args0);
    }

    @Override
    public String getNString(int args0) throws SQLException {
        return resultSet.getNString(args0);
    }

    @Override
    public String getNString(String args0) throws SQLException {
        return resultSet.getNString(args0);
    }

    @Override
    public int getRow() throws SQLException {
        return resultSet.getRow();
    }

    @Override
    public RowId getRowId(int args0) throws SQLException {
        return resultSet.getRowId(args0);
    }

    @Override
    public RowId getRowId(String args0) throws SQLException {
        return resultSet.getRowId(args0);
    }

    @Override
    public SQLXML getSQLXML(int args0) throws SQLException {
        return resultSet.getSQLXML(args0);
    }

    @Override
    public SQLXML getSQLXML(String args0) throws SQLException {
        return resultSet.getSQLXML(args0);
    }

    @Override
    public Statement getStatement() throws SQLException {
        return resultSet.getStatement();
    }

    @Override
    public Timestamp getTimestamp(String args0, Calendar args1) throws SQLException {
        return resultSet.getTimestamp(args0, args1);
    }

    @Override
    public Timestamp getTimestamp(int args0) throws SQLException {
        return resultSet.getTimestamp(args0);
    }

    @Override
    public Timestamp getTimestamp(int args0, Calendar args1) throws SQLException {
        return resultSet.getTimestamp(args0, args1);
    }

    @Override
    public Timestamp getTimestamp(String args0) throws SQLException {
        return resultSet.getTimestamp(args0);
    }

    @Override
    public InputStream getUnicodeStream(String args0) throws SQLException {
        return resultSet.getUnicodeStream(args0);
    }

    @Override
    public InputStream getUnicodeStream(int args0) throws SQLException {
        return resultSet.getUnicodeStream(args0);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return resultSet.getWarnings();
    }

    @Override
    public void insertRow() throws SQLException {
        resultSet.insertRow();
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return resultSet.isAfterLast();
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return resultSet.isBeforeFirst();
    }

    @Override
    public boolean isFirst() throws SQLException {
        return resultSet.isFirst();
    }

    @Override
    public boolean isLast() throws SQLException {
        return resultSet.isLast();
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        resultSet.moveToCurrentRow();
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        resultSet.moveToInsertRow();
    }

    @Override
    public void refreshRow() throws SQLException {
        resultSet.refreshRow();
    }

    @Override
    public boolean relative(int args0) throws SQLException {
        return resultSet.relative(args0);
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return resultSet.rowDeleted();
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return resultSet.rowInserted();
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return resultSet.rowUpdated();
    }

    @Override
    public void setFetchDirection(int args0) throws SQLException {
        resultSet.setFetchDirection(args0);
    }

    @Override
    public void setFetchSize(int args0) throws SQLException {
        resultSet.setFetchSize(args0);
    }

    @Override
    public void updateArray(int args0, Array args1) throws SQLException {
        resultSet.updateArray(args0, args1);
    }

    @Override
    public void updateArray(String args0, Array args1) throws SQLException {
        resultSet.updateArray(args0, args1);
    }

    @Override
    public void updateAsciiStream(String args0, InputStream args1, long args2) throws SQLException {
        resultSet.updateAsciiStream(args0, args1, args2);
    }

    @Override
    public void updateAsciiStream(int args0, InputStream args1, long args2) throws SQLException {
        resultSet.updateAsciiStream(args0, args1, args2);
    }

    @Override
    public void updateAsciiStream(int args0, InputStream args1) throws SQLException {
        resultSet.updateAsciiStream(args0, args1);
    }

    @Override
    public void updateAsciiStream(String args0, InputStream args1, int args2) throws SQLException {
        resultSet.updateAsciiStream(args0, args1, args2);
    }

    @Override
    public void updateAsciiStream(String args0, InputStream args1) throws SQLException {
        resultSet.updateAsciiStream(args0, args1);
    }

    @Override
    public void updateAsciiStream(int args0, InputStream args1, int args2) throws SQLException {
        resultSet.updateAsciiStream(args0, args1, args2);
    }

    @Override
    public void updateBigDecimal(int args0, BigDecimal args1) throws SQLException {
        resultSet.updateBigDecimal(args0, args1);
    }

    @Override
    public void updateBigDecimal(String args0, BigDecimal args1) throws SQLException {
        resultSet.updateBigDecimal(args0, args1);
    }

    @Override
    public void updateBinaryStream(String args0, InputStream args1, long args2) throws SQLException {
        resultSet.updateBinaryStream(args0, args1, args2);
    }

    @Override
    public void updateBinaryStream(int args0, InputStream args1, int args2) throws SQLException {
        resultSet.updateBinaryStream(args0, args1, args2);
    }

    @Override
    public void updateBinaryStream(int args0, InputStream args1) throws SQLException {
        resultSet.updateBinaryStream(args0, args1);
    }

    @Override
    public void updateBinaryStream(String args0, InputStream args1, int args2) throws SQLException {
        resultSet.updateBinaryStream(args0, args1, args2);
    }

    @Override
    public void updateBinaryStream(String args0, InputStream args1) throws SQLException {
        resultSet.updateBinaryStream(args0, args1);
    }

    @Override
    public void updateBinaryStream(int args0, InputStream args1, long args2) throws SQLException {
        resultSet.updateBinaryStream(args0, args1, args2);
    }

    @Override
    public void updateBlob(String args0, InputStream args1) throws SQLException {
        resultSet.updateBlob(args0, args1);
    }

    @Override
    public void updateBlob(int args0, Blob args1) throws SQLException {
        resultSet.updateBlob(args0, args1);
    }

    @Override
    public void updateBlob(int args0, InputStream args1) throws SQLException {
        resultSet.updateBlob(args0, args1);
    }

    @Override
    public void updateBlob(String args0, Blob args1) throws SQLException {
        resultSet.updateBlob(args0, args1);
    }

    @Override
    public void updateBlob(int args0, InputStream args1, long args2) throws SQLException {
        resultSet.updateBlob(args0, args1, args2);
    }

    @Override
    public void updateBlob(String args0, InputStream args1, long args2) throws SQLException {
        resultSet.updateBlob(args0, args1, args2);
    }

    @Override
    public void updateBoolean(String args0, boolean args1) throws SQLException {
        resultSet.updateBoolean(args0, args1);
    }

    @Override
    public void updateBoolean(int args0, boolean args1) throws SQLException {
        resultSet.updateBoolean(args0, args1);
    }

    @Override
    public void updateByte(int args0, byte args1) throws SQLException {
        resultSet.updateByte(args0, args1);
    }

    @Override
    public void updateByte(String args0, byte args1) throws SQLException {
        resultSet.updateByte(args0, args1);
    }

    @Override
    public void updateCharacterStream(int args0, Reader args1, long args2) throws SQLException {
        resultSet.updateCharacterStream(args0, args1, args2);
    }

    @Override
    public void updateCharacterStream(int args0, Reader args1) throws SQLException {
        resultSet.updateCharacterStream(args0, args1);
    }

    @Override
    public void updateCharacterStream(String args0, Reader args1, int args2) throws SQLException {
        resultSet.updateCharacterStream(args0, args1, args2);
    }

    @Override
    public void updateCharacterStream(String args0, Reader args1) throws SQLException {
        resultSet.updateCharacterStream(args0, args1);
    }

    @Override
    public void updateCharacterStream(String args0, Reader args1, long args2) throws SQLException {
        resultSet.updateCharacterStream(args0, args1, args2);
    }

    @Override
    public void updateCharacterStream(int args0, Reader args1, int args2) throws SQLException {
        resultSet.updateCharacterStream(args0, args1, args2);
    }

    @Override
    public void updateClob(int args0, Reader args1) throws SQLException {
        resultSet.updateClob(args0, args1);
    }

    @Override
    public void updateClob(int args0, Reader args1, long args2) throws SQLException {
        resultSet.updateClob(args0, args1, args2);
    }

    @Override
    public void updateClob(String args0, Reader args1) throws SQLException {
        resultSet.updateClob(args0, args1);
    }

    @Override
    public void updateClob(String args0, Clob args1) throws SQLException {
        resultSet.updateClob(args0, args1);
    }

    @Override
    public void updateClob(String args0, Reader args1, long args2) throws SQLException {
        resultSet.updateClob(args0, args1, args2);
    }

    @Override
    public void updateClob(int args0, Clob args1) throws SQLException {
        resultSet.updateClob(args0, args1);
    }

    @Override
    public void updateDate(String args0, Date args1) throws SQLException {
        resultSet.updateDate(args0, args1);
    }

    @Override
    public void updateDate(int args0, Date args1) throws SQLException {
        resultSet.updateDate(args0, args1);
    }

    @Override
    public void updateDouble(String args0, double args1) throws SQLException {
        resultSet.updateDouble(args0, args1);
    }

    @Override
    public void updateDouble(int args0, double args1) throws SQLException {
        resultSet.updateDouble(args0, args1);
    }

    @Override
    public void updateFloat(int args0, float args1) throws SQLException {
        resultSet.updateFloat(args0, args1);
    }

    @Override
    public void updateFloat(String args0, float args1) throws SQLException {
        resultSet.updateFloat(args0, args1);
    }

    @Override
    public void updateInt(int args0, int args1) throws SQLException {
        resultSet.updateInt(args0, args1);
    }

    @Override
    public void updateInt(String args0, int args1) throws SQLException {
        resultSet.updateInt(args0, args1);
    }

    @Override
    public void updateLong(int args0, long args1) throws SQLException {
        resultSet.updateLong(args0, args1);
    }

    @Override
    public void updateLong(String args0, long args1) throws SQLException {
        resultSet.updateLong(args0, args1);
    }

    @Override
    public void updateNCharacterStream(int args0, Reader args1) throws SQLException {
        resultSet.updateNCharacterStream(args0, args1);
    }

    @Override
    public void updateNCharacterStream(String args0, Reader args1, long args2) throws SQLException {
        resultSet.updateNCharacterStream(args0, args1, args2);
    }

    @Override
    public void updateNCharacterStream(String args0, Reader args1) throws SQLException {
        resultSet.updateNCharacterStream(args0, args1);
    }

    @Override
    public void updateNCharacterStream(int args0, Reader args1, long args2) throws SQLException {
        resultSet.updateNCharacterStream(args0, args1, args2);
    }

    @Override
    public void updateNClob(int args0, Reader args1, long args2) throws SQLException {
        resultSet.updateNClob(args0, args1, args2);
    }

    @Override
    public void updateNClob(int args0, Reader args1) throws SQLException {
        resultSet.updateNClob(args0, args1);
    }

    @Override
    public void updateNClob(int args0, NClob args1) throws SQLException {
        resultSet.updateNClob(args0, args1);
    }

    @Override
    public void updateNClob(String args0, Reader args1, long args2) throws SQLException {
        resultSet.updateNClob(args0, args1, args2);
    }

    @Override
    public void updateNClob(String args0, Reader args1) throws SQLException {
        resultSet.updateNClob(args0, args1);
    }

    @Override
    public void updateNClob(String args0, NClob args1) throws SQLException {
        resultSet.updateNClob(args0, args1);
    }

    @Override
    public void updateNString(int args0, String args1) throws SQLException {
        resultSet.updateNString(args0, args1);
    }

    @Override
    public void updateNString(String args0, String args1) throws SQLException {
        resultSet.updateNString(args0, args1);
    }

    @Override
    public void updateNull(String args0) throws SQLException {
        resultSet.updateNull(args0);
    }

    @Override
    public void updateNull(int args0) throws SQLException {
        resultSet.updateNull(args0);
    }

    @Override
    public void updateObject(String args0, Object args1, SQLType args2, int args3) throws SQLException {
        resultSet.updateObject(args0, args1, args2, args3);
    }

    @Override
    public void updateObject(int args0, Object args1, SQLType args2) throws SQLException {
        resultSet.updateObject(args0, args1, args2);
    }

    @Override
    public void updateObject(int args0, Object args1, SQLType args2, int args3) throws SQLException {
        resultSet.updateObject(args0, args1, args2, args3);
    }

    @Override
    public void updateObject(String args0, Object args1) throws SQLException {
        resultSet.updateObject(args0, args1);
    }

    @Override
    public void updateObject(int args0, Object args1, int args2) throws SQLException {
        resultSet.updateObject(args0, args1, args2);
    }

    @Override
    public void updateObject(String args0, Object args1, SQLType args2) throws SQLException {
        resultSet.updateObject(args0, args1, args2);
    }

    @Override
    public void updateObject(int args0, Object args1) throws SQLException {
        resultSet.updateObject(args0, args1);
    }

    @Override
    public void updateObject(String args0, Object args1, int args2) throws SQLException {
        resultSet.updateObject(args0, args1, args2);
    }

    @Override
    public void updateRef(String args0, Ref args1) throws SQLException {
        resultSet.updateRef(args0, args1);
    }

    @Override
    public void updateRef(int args0, Ref args1) throws SQLException {
        resultSet.updateRef(args0, args1);
    }

    @Override
    public void updateRow() throws SQLException {
        resultSet.updateRow();
    }

    @Override
    public void updateRowId(String args0, RowId args1) throws SQLException {
        resultSet.updateRowId(args0, args1);
    }

    @Override
    public void updateRowId(int args0, RowId args1) throws SQLException {
        resultSet.updateRowId(args0, args1);
    }

    @Override
    public void updateSQLXML(String args0, SQLXML args1) throws SQLException {
        resultSet.updateSQLXML(args0, args1);
    }

    @Override
    public void updateSQLXML(int args0, SQLXML args1) throws SQLException {
        resultSet.updateSQLXML(args0, args1);
    }

    @Override
    public void updateShort(String args0, short args1) throws SQLException {
        resultSet.updateShort(args0, args1);
    }

    @Override
    public void updateShort(int args0, short args1) throws SQLException {
        resultSet.updateShort(args0, args1);
    }

    @Override
    public void updateString(String args0, String args1) throws SQLException {
        resultSet.updateString(args0, args1);
    }

    @Override
    public void updateString(int args0, String args1) throws SQLException {
        resultSet.updateString(args0, args1);
    }

    @Override
    public void updateTime(int args0, Time args1) throws SQLException {
        resultSet.updateTime(args0, args1);
    }

    @Override
    public void updateTime(String args0, Time args1) throws SQLException {
        resultSet.updateTime(args0, args1);
    }

    @Override
    public void updateTimestamp(String args0, Timestamp args1) throws SQLException {
        resultSet.updateTimestamp(args0, args1);
    }

    @Override
    public void updateTimestamp(int args0, Timestamp args1) throws SQLException {
        resultSet.updateTimestamp(args0, args1);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return resultSet.wasNull();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return resultSet.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return resultSet.isWrapperFor(iface);
    }
}
