package com.hzqykeji.banner.utils;



import com.hzqykeji.travel.annotation.Excel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;


public class ExcelUtil {
	/**
	 *
	 * @param title     Sheet名字
	 * @param pojoClass Excel对象Class
	 * @param dataSet   Excel对象数据List
	 * @param out       输出流
	 */
	private static void exportExcel(String title, Class<?> pojoClass,
	        Collection<?> dataSet, OutputStream out) {
	    // 使用userModel模式实现的，当excel文档出现10万级别的大数据文件可能导致OOM内存溢出
	    exportExcelInUserModel(title, pojoClass, dataSet, out);
	    // 使用eventModel实现，可以一边读一边处理，效率较高，但是实现复杂，暂时未实现
	}

	private static void exportExcelInUserModel(String title, Class<?> pojoClass,
	        Collection<?> dataSet, OutputStream out) {
	    try {
	        // 首先检查数据看是否是正确的
	    	//update-begin-Alex---Date:20180910---for:修改逻辑，数据为空时可导出表头------
	        if (dataSet == null) {
	        //update-end-Alex---Date:20180910---for:修改逻辑，数据为空时可导出表头------
	            throw new Exception("导出数据为空！");
	        }
	        if (title == null || out == null || pojoClass == null) {
	            throw new Exception("传入参数不能为空！");
	        }
	        // 声明一个工作薄
				Workbook workbook = new XSSFWorkbook();
	        // 生成一个表格
	        Sheet sheet = workbook.createSheet(title);

	        // 标题
	        List<String> exportFieldTitle = new ArrayList<String>();
	        List<Integer> exportFieldWidth = new ArrayList<Integer>();
	        // 拿到所有列名，以及导出的字段的get方法
	        List<Method> methodObj = new ArrayList<Method>();
	        Map<String, Method> convertMethod = new HashMap<String, Method>();
	        // 得到所有字段
	        Field fileds[] = pojoClass.getDeclaredFields();
	        // 遍历整个filed
	        for (int i = 0; i < fileds.length; i++) {
	            Field field = fileds[i];
	            Excel excel = field.getAnnotation(Excel.class);
	            // 如果设置了annottion
	            if (excel != null) {
	                // 添加到标题
	                exportFieldTitle.add(excel.exportName());
	                // 添加标题的列宽
	                exportFieldWidth.add(excel.exportFieldWidth());
	                // 添加到需要导出的字段的方法
	                String fieldname = field.getName();
	                // System.out.println(i+"列宽"+excel.exportName()+" "+excel.exportFieldWidth());
	                StringBuffer getMethodName = new StringBuffer("get");
	                getMethodName.append(fieldname.substring(0, 1)
	                        .toUpperCase());
	                getMethodName.append(fieldname.substring(1));

	                Method getMethod = pojoClass.getMethod(getMethodName
	                        .toString(), new Class[] {});

	                methodObj.add(getMethod);
	                if (excel.exportConvertSign() == 1) {
	                    StringBuffer getConvertMethodName = new StringBuffer(
	                            "get");
	                    getConvertMethodName.append(fieldname.substring(0, 1)
	                            .toUpperCase());
	                    getConvertMethodName.append(fieldname.substring(1));
	                    getConvertMethodName.append("Convert");
	                    Method getConvertMethod = pojoClass
	                            .getMethod(getConvertMethodName.toString(),
	                                    new Class[] {});
	                    convertMethod.put(getMethodName.toString(),
	                            getConvertMethod);
	                }
	            }
	        }

	     //2020-01-01-2020-01-31惠州行订单
				Row row0=sheet.createRow(0);//第一行
	  		row0.setHeight((short) ((short) 30*20));//设置行高
				Cell cellTitle=row0.createCell(0); //创建单元格
				CellStyle styleTitle = createStyle(workbook, "黑体", 20, HSSFCellStyle.ALIGN_CENTER,false,true);//设置单元格样式
				cellTitle.setCellStyle(styleTitle);
				cellTitle.setCellValue(title);//设置单元格内容
				sheet.addMergedRegion(new CellRangeAddress(0,0,0,exportFieldTitle.size()-1));

				// 产生表格标题行
				int index = 1;
			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setFontName("黑体");
			titleFont.setFontHeightInPoints((short) 12);//设置字体大小
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
			titleStyle.setFont(titleFont);
			Row row = sheet.createRow(index);
			for (int i = 0, exportFieldTitleSize = exportFieldTitle.size(); i < exportFieldTitleSize; i++) {
	            Cell cell = row.createCell(i);
	            cell.setCellStyle(titleStyle);
	            RichTextString text = new XSSFRichTextString(exportFieldTitle
	                    .get(i));
	            cell.setCellValue(text);
	        }

	        // 设置每行的列宽
	        for (int i = 0; i < exportFieldWidth.size(); i++) {
	            // 256=65280/255
	            sheet.setColumnWidth(i, 256 * exportFieldWidth.get(i));
	        }
	        @SuppressWarnings("rawtypes")
			Iterator its = dataSet.iterator();
	        // 循环插入剩下的集合
	        while (its.hasNext()) {
	            // 从第二行开始写，第一行是标题
	            index++;
	            row = sheet.createRow(index);
	            Object t = its.next();
	            for (int k = 0, methodObjSize = methodObj.size(); k < methodObjSize; k++) {
	                Cell cell = row.createCell(k);
	                Method getMethod = methodObj.get(k);
	                Object value = null;
	                if (convertMethod.containsKey(getMethod.getName())) {
	                    Method cm = convertMethod.get(getMethod.getName());
	                    value = cm.invoke(t, new Object[] {});
	                } else {
	                    value = getMethod.invoke(t, new Object[] {});
	                }
	                cell.setCellValue(value == null ? "" : value.toString());
	            }
	        }

	        workbook.write(out);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}


    /**
     *
     * @param request
     * @param response
     * @param
     * @param pojoClass 导出数据的实体
     * @param title 导出文件名
     * @throws Exception
     */
    public static void exportExcel(HttpServletRequest request, HttpServletResponse response, Collection<?> listUser, Class<?> pojoClass, String title) throws Exception {
    	response.setContentType("application/vnd.ms-excel;charset=utf-8");
    	String fileName = title+".xlsx";

    	//update-begin-Alex----Date:20180910----for:解决IE浏览器导出时名称乱码问题---
    	String finalFileName = null;
    	String userAgent = request.getHeader("user-agent");

    	if (userAgent != null && userAgent.indexOf("MSIE") >= 0 || userAgent.indexOf("Trident") >= 0 || userAgent.indexOf("Edge") >= 0) {
    		//IE（Trident）内核
    		finalFileName =URLEncoder.encode(fileName,"UTF-8");
    	} else if(userAgent != null && userAgent.indexOf("chrome") >= 0 || userAgent.indexOf("safari") >= 0 || userAgent.indexOf("Firefox") >= 0) {
    		//谷歌、火狐等浏览器
    		finalFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
    	}else{
    		finalFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
    	}
    	//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
    	response.setHeader("Content-Disposition", "attachment; filename="+ finalFileName);
    	//update-end-Alex----Date:20180910----for:解决IE浏览器导出时名称乱码问题---
    	File file = new File(new Date(0).getTime()+".xls");
    	OutputStream outputStream = new FileOutputStream(file);
    	ExcelUtil.exportExcel(title,pojoClass, listUser, outputStream);
    	InputStream is = new BufferedInputStream(new FileInputStream(file.getPath()));
    	file.delete();
    	outputStream.close();
    	OutputStream os = response.getOutputStream();
    	byte[] b = new byte[2048];
    	int length;
    	while ((length = is.read(b)) > 0) {
    		os.write(b, 0, length);
    	}
    	os.close();
    	is.close();
	}


	public static CellStyle createStyle(Workbook wb, String fontName, int fontHeightInPoints,short alignment,boolean border,boolean boldweight){
		//设置内容字体
		Font fontContent = wb.createFont();
		if (boldweight){
			// 字体加粗
			fontContent.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		fontContent.setFontName(fontName);
		fontContent.setFontHeightInPoints((short) fontHeightInPoints);
		//创建内容样式
		CellStyle styleContent = wb.createCellStyle();
		styleContent.setFont(fontContent);
		styleContent.setWrapText(true);
		styleContent.setAlignment(alignment);
		styleContent.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		if (border){
			styleContent.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleContent.setBottomBorderColor(HSSFColor.BLACK.index);
			styleContent.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleContent.setLeftBorderColor(HSSFColor.BLACK.index);
			styleContent.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleContent.setRightBorderColor(HSSFColor.BLACK.index);
			styleContent.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleContent.setTopBorderColor(HSSFColor.BLACK.index);
		}
		return styleContent;
	}


}
