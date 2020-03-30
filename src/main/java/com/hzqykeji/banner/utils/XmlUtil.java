package com.hzqykeji.banner.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.util.*;

public class XmlUtil {

  public static Document fromXml(String content) throws DocumentException {
    return DocumentHelper.parseText(content);
  }

  public static String parseXML(SortedMap<String, String> parameters) {
    StringBuffer sb = new StringBuffer();
    sb.append("<xml>");
    Set es = parameters.entrySet();
    Iterator<Map.Entry> it = es.iterator();
    while (it.hasNext()) {
      Map.Entry<String,String> entry = it.next();
      String k = entry.getKey();
      String v = entry.getValue();
      if (null != v && !"".equals(v) && !"appkey".equals(k)) {
        sb.append("<" + k + ">" + parameters.get(k) + "</" + k + ">\n");
      }
    }
    sb.append("</xml>");
    return sb.toString();
  }

  /**
   * 转XMLmap
   * @author
   * @param xmlBytes
   * @param charset
   * @return
   * @throws Exception
   */
  public static Map<String, String> toMap(byte[] xmlBytes,String charset) throws Exception{
    SAXReader reader = new SAXReader(false);
    InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
    source.setEncoding(charset);
    Document doc = reader.read(source);
    Map<String, String> params = XmlUtil.toMap(doc.getRootElement());
    return params;
  }

  /**
   * 转MAP
   * @author
   * @param element
   * @return
   */
  public static Map<String, String> toMap(Element element){
    Map<String, String> rest = new HashMap<String, String>();
    List<Element> els = element.elements();
    for(Element el : els){
      rest.put(el.getName().toLowerCase(), el.getTextTrim());
    }
    return rest;
  }

  public static String toXml(Map<String, String> params){
    StringBuilder buf = new StringBuilder();
    List<String> keys = new ArrayList<String>(params.keySet());
    Collections.sort(keys);
    buf.append("<xml>");
    for(String key : keys){
      buf.append("<").append(key).append(">");
      buf.append("<![CDATA[").append(params.get(key)).append("]]>");
      buf.append("</").append(key).append(">\n");
    }
    buf.append("</xml>");
    return buf.toString();
  }

}
