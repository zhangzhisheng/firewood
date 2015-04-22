package org.cn.zszhang.comm.webutil.spring.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

/*
 * @author : zszhang
 * @doc: 实现对java.time.Local* 类的JSON序列化
 */
public class Jdk8DateSerializer implements ObjectSerializer {
    private final String pattern;
    
    public Jdk8DateSerializer(String pattern) {
    	this.pattern = pattern;
    }

	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType, int features) throws IOException {
    	if (object == null) {
    		serializer.getWriter().writeNull();
    		return;
    	}

    	DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
    	serializer.write(fmt.format((TemporalAccessor) object));
	}

}
