package org.cn.zszhang.comm.webutil.spring.web;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public class CSVView extends AbstractView{

	public static final String CSV_EXPORT_FILE_NAME = "exportFileName";
	public static final String CSV_TEMPLATE_FILE_NAME_PREFIX = "QPTemp-";
	
	/** The content type for an CSV response */
	private static final String CONTENT_TYPE = "application/csv";
	
	/**
	 * Default Constructor.
	 * Sets the content type of the view to "application/csv".
	 */
	public CSVView() {
		setContentType(CONTENT_TYPE);
	}
	
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = request.getParameter(CSV_EXPORT_FILE_NAME);
		fileName = fileName.replaceAll(" ", "");
		response.setHeader("content-disposition","attachment; filename="+new String(fileName.getBytes("gbk"),"ISO8859-1"));
		List<String> list = (List<String>)model.get("Attributes");
		response.setCharacterEncoding("utf-8");
		ServletOutputStream out = response.getOutputStream();
		OutputStreamWriter ow = new OutputStreamWriter(out,"UTF-8");  
		for(String name:list){
			ow.write(name);
			ow.write(",");
			ow.flush();
		}
	}

}

