package org.cn.zszhang.comm.pstutil.mybatis;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(LocalDate.class)
public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			LocalDate parameter, JdbcType jdbcType) throws SQLException {
		Date sqlDate = Date.valueOf(parameter);
		ps.setDate(i, sqlDate);
	}

	@Override
	public LocalDate getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		
		Date sd = rs.getDate(columnName);
		if( null == sd ) return null;
		
		LocalDate ld = sd.toLocalDate();
		return ld;
	}

	@Override
	public LocalDate getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		
		Date sd = rs.getDate(columnIndex);
		if( null == sd ) return null;
		
		LocalDate ld = sd.toLocalDate();
		return ld;
	}

	@Override
	public LocalDate getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		
		Date sd = cs.getDate(columnIndex);
		if( null == sd ) return null;
		
		LocalDate ld = sd.toLocalDate();
		return ld;
	}

}
