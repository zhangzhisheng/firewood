package org.cn.zszhang.common.excel4testng.webutil.spring.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MappingFastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object>{

	public final static Charset UTF8     = Charset.forName("UTF-8");

    private Charset             charset  = UTF8;

    private SerializerFeature[] features = {SerializerFeature.DisableCircularReferenceDetect};

    public MappingFastJsonHttpMessageConverter(){
        super(new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8));
        // 以下注册内容是希望date信息以字符串形式序列化、反序列化。如果希望以对象方式注掉以下语句即可。
        SerializeConfig.getGlobalInstance().put(LocalDateTime.class,  new Jdk8DateSerializer("yyyy-MM-dd HH:mm:ss"));
        SerializeConfig.getGlobalInstance().put(LocalDate.class,  new Jdk8DateSerializer("yyyy-MM-dd"));
        SerializeConfig.getGlobalInstance().put(LocalTime.class,  new Jdk8DateSerializer("HH:mm:ss"));
        ParserConfig.getGlobalInstance().putDeserializer(LocalDateTime.class, Jdk8DateDeserializer.instance);
        ParserConfig.getGlobalInstance().putDeserializer(LocalDate.class, Jdk8DateDeserializer.instance);
        ParserConfig.getGlobalInstance().putDeserializer(LocalTime.class, Jdk8DateDeserializer.instance);
        ParserConfig.getGlobalInstance().putDeserializer(ZonedDateTime.class, Jdk8DateDeserializer.instance);
        ParserConfig.getGlobalInstance().putDeserializer(OffsetDateTime.class, Jdk8DateDeserializer.instance);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public SerializerFeature[] getFeatures() {
        return features;
    }

    public void setFeatures(SerializerFeature... features) {
        this.features = features;
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,
                                                                                               HttpMessageNotReadableException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        InputStream in = inputMessage.getBody();

        byte[] buf = new byte[1024];
        for (;;) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }

            if (len > 0) {
                baos.write(buf, 0, len);
            }
        }

        byte[] bytes = baos.toByteArray();
        return JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(), clazz);
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
                                                                             HttpMessageNotWritableException {

        OutputStream out = outputMessage.getBody();
        String text = JSON.toJSONString(obj, features);
        byte[] bytes = text.getBytes(charset);
        out.write(bytes);
    }
	
}

