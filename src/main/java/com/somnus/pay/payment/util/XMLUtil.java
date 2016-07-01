package com.somnus.pay.payment.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML的工具类，主要作用于对象与之间状态
 */
public class XMLUtil {

    private static Log logger = LogFactory.getLog(XMLUtil.class);

    /**
     * 更改文件中节点内容并保存
     *
     * @param fileName 文件路径
     * @param nodeName 节点名称
     * @param nodeValue 节点内容
     */
    public static void updateNodeValue(String fileName, String nodeName, String nodeValue) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(fileName));
            Element root = document.getRootElement();

            Element element = root.element(nodeName);
            element.setText(nodeValue);

            XMLWriter writer = new XMLWriter(new FileOutputStream(fileName));
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 获取节点内容
     *
     * @param xml 原始字符串
     * @param nodeName 名称
     * @return 结果
     */
    public static String getNodeValue(String xml, String nodeName) {
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            Element element = root.element(nodeName);

            document = null;
            root = null;
            return element == null ? StringUtils.EMPTY : element.getText();
        } catch (DocumentException e) {
            logger.error("", e);
        }

        return StringUtils.EMPTY;
    }


    /**
     * 转换成MAP
     */
    public static Map<String, Object> parseXml(String xml) {
        if (StringUtils.isBlank(xml)) {
            return Collections.emptyMap();
        }
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();

            HashMap<String, Object> map;

            if (root.isTextOnly()) {
                map = new HashMap<String, Object>(1);
                map.put(root.getName(), parseEle(root));
            } else {
                map = (HashMap<String, Object>) parseEle(root);
            }

            document = null;
            root = null;
            return map;
        } catch (DocumentException e) {
            logger.error("", e);
        }
        return Collections.emptyMap();
    }

    public static Object parseEle(Element ele) {
        if (ele.isTextOnly()) {
            return ele.getTextTrim();
        } else {
            List<Element> list = ele.elements();
            HashMap<String, Object> map = new HashMap<String, Object>(list.size());
            for (Element element : list) {
                map.put(element.getName(), parseEle(element));
            }
            return map;
        }
    }

    /**
     * java 转换成xml
     * @param obj 对象实例
     * @return String xml字符串
     */
    public static String toXML(Object obj){
        XStream xstream=new XStream();
        xstream.processAnnotations(obj.getClass()); //通过注解方式的，一定要有这句话
        return xstream.toXML(obj);
    }

    /**
     * 使用给定的 XStream 实例序列化 XML
     * @param obj 待序列化的对象
     * @param xStream XStream 实例
     * @return XML
     */
    public static String toXML(Object obj, XStream xStream) {
        StringWriter sw = new StringWriter();
        CompactWriter writer = new CompactWriter(sw);
        try {
            xStream.marshal(obj, writer);
        } finally {
            writer.close();
        }
        String str = sw.toString();
        return str.replaceAll("__", "_").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"");
    }

    /**
     *  将传入xml文本转换成Java对象
     * @param xmlStr
     * @param cls  xml对应的class类
     * @return T   xml对应的class类的实例对象
     */
    public static <T> T  toBean(String xmlStr,Class<T> cls){
        XStream xstream=new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        T obj=(T)xstream.fromXML(xmlStr);
        return obj;
    }

    /**
     * 写到xml文件中去
     * @param obj 对象
     * @param absPath 绝对路径
     * @param fileName    文件名
     * @return boolean
     */
    public static boolean toXMLFile(Object obj, String absPath, String fileName ){
        String strXml = toXML(obj);
        String filePath = absPath + fileName;
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("创建{"+ filePath +"}文件失败!!!e:" + e);
                return false ;
            }
        }// end if
        OutputStream ous = null ;
        try {
            ous = new FileOutputStream(file);
            ous.write(strXml.getBytes());
            ous.flush();
        } catch (Exception e1) {
            logger.error("写{"+ filePath +"}文件失败!!!e:" + e1);
            return false;
        }finally{
            if(ous != null )
                try {
                    ous.close();
                } catch (IOException e) {
                    logger.error("写{"+ filePath +"}文件关闭输出流异常!!!e:" + e);
                }
        }
        return true ;
    }


    /**
     * 使用给定的 XStream 实例和 XML 填充给定的对象
     * 此方法会直接修改入参 bean 中的属性。谨慎使用此方法，尽可能使用创建新实例的
     * @param xml XML
     * @param bean 待填充的对象
     * @param xStream XStream 实例
     * @return bean itself. null if bean is null or xml is blank
     */
    public static <T> T fillByXml(String xml, T bean, XStream xStream) {
        if (null == bean || StringUtils.isBlank(xml)) {
            return null;
        }
        return (T) xStream.fromXML(xml, bean);
    }
}
