package com.kol_room.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * �׹��̳��Զ�����Ӧ�ṹ
 */
public class JsonUtils {

    // ����jackson����
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * ������ת����json�ַ�����
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * ��json�����ת��Ϊ����
     * 
     * @param jsonData json����
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        if(jsonData!=null) {
            try {
                T t = MAPPER.readValue(jsonData, beanType);
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }

    }
    
    /**
     * ��json����ת����pojo����list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
}
