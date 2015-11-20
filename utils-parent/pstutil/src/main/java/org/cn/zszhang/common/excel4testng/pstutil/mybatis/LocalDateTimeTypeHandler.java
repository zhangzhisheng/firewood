package org.cn.zszhang.common.excel4testng.pstutil.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

//由于mybatis不检测jdbcType，所以增加此选项，将导致mybatis在注册的typeHandler列表中找不到处理器
//@MappedJdbcTypes(JdbcType.TIMESTAMP)
@MappedTypes(LocalDateTime.class)
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			LocalDateTime parameter, JdbcType jdbcType) throws SQLException {

		Timestamp sts = Timestamp.valueOf(parameter);
		ps.setTimestamp(i, sts);
		
	}

	@Override
	public LocalDateTime getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		
		Timestamp stm = rs.getTimestamp(columnName);
		if ( null == stm ) return null;
		
		LocalDateTime ldt = stm.toLocalDateTime();
		return ldt;
	}

	@Override
	public LocalDateTime getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		
		Timestamp stm = rs.getTimestamp(columnIndex);
		if ( null == stm ) return null;
		
		LocalDateTime ldt = stm.toLocalDateTime();
		return ldt;
	}

	@Override
	public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		
		Timestamp stm = cs.getTimestamp(columnIndex);
		if ( null == stm ) return null;
		
		LocalDateTime ldt = stm.toLocalDateTime();
		return ldt;
	}

}
