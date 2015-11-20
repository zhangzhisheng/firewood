package org.cn.zszhang.common.excel4testng.pstutil.mybatis;

import java.sql.CallableStatement;
import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(LocalTime.class)
public class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			LocalTime parameter, JdbcType jdbcType) throws SQLException {
		
		Time st = Time.valueOf(parameter);
		ps.setTime(i, st);
		
	}

	@Override
	public LocalTime getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		
		Time st = rs.getTime(columnName);
		if( null == st ) return null;
		
		LocalTime ld = st.toLocalTime();
		return ld;
	}

	@Override
	public LocalTime getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		
		Time st = rs.getTime(columnIndex);
		if( null == st ) return null;
		
		LocalTime ld = st.toLocalTime();
		return ld;
	}

	@Override
	public LocalTime getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		
		Time st = cs.getTime(columnIndex);
		if( null == st ) return null;
		
		LocalTime ld = st.toLocalTime();
		return ld;
	}

}
