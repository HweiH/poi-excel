package org.well.test.poiexcel.factory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName:PropertiesUtil
 * @Description:properties 文件相关的工具类
 * @author well
 * @date:2020年4月10日
 *
 */
public class PropertiesUtil {

    /**
     * 模板ID=模板名
     */
    public static Map<String, String> MAP_TID_CONTENT;
    
    /**
     * 工厂类名=模板ID1,模板ID2,模板ID3,...
     */
    public static Map<String, String> MAP_FACTORY_TIDS;
    
    static {
        MAP_TID_CONTENT = initMap("content.properties", "/factory/content.properties"); // 后面覆盖前面
        MAP_FACTORY_TIDS = initMap("factory.properties", "/factory/factory.properties");
    }

    /**
     * 初始化
     *
     * @param props
     * @return
     */
    private synchronized static Map<String, String> initMap(String... props) {
        Map<String, String> tempMap = new HashMap<>();
        for (String prop : props) {
            Map<String, String> tmp = loadPropToMap(prop);
            tmp.forEach((key, value) -> tempMap.merge(key, value, (v1, v2) -> v2));
        }
        return MapUtils.unmodifiableMap(tempMap);
    }

    /**
     * 将 properties 文件内容加载为 Map
     *
     * @param specifiedFile
     * @return
     */
    private static Map<String, String> loadPropToMap(String specifiedFile) {
        if (StringUtils.isBlank(specifiedFile)) {
            return Collections.emptyMap();
        }
        Map<String, String> tempMap = new HashMap<>();
        try (InputStream is = PropertiesUtil.class.getResourceAsStream(specifiedFile)) {
            if (is == null) {
                return Collections.emptyMap();
            }
            Properties prop = new Properties();
            prop.load(new InputStreamReader(is, "UTF-8"));
            Set<Entry<Object, Object>> entrySet = prop.entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                String key = String.valueOf(entry.getKey());
                String value = String.valueOf(entry.getValue());
                tempMap.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempMap;
    }
    
    /**
     * 在 Map 中通过 value 找 key，第一个匹配的 value
     *
     * @param map
     * @param value
     * @return
     */
    public static String getKeyOf(Map<String, String> map, String value) {
        String key = StringUtils.EMPTY;
        if (MapUtils.isEmpty(map) 
                || StringUtils.isBlank(value)) {
            return key;
        }
        Set<Entry<String, String>> entrySet = map.entrySet();
        for (Entry<String, String> entry : entrySet) {
            if (StringUtils.equals(entry.getValue(), value) 
                    || StringUtils.contains(entry.getValue(), value)) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }
    
    /**
     * 在 Map 中通过 key 找 value，第一个匹配的 key
     *
     * @param map
     * @param key
     * @return
     */
    public static String getValueOf(Map<String, String> map, String key) {
        String value = StringUtils.EMPTY;
        if (MapUtils.isEmpty(map) 
                || StringUtils.isBlank(key)) {
            return value;
        }
        Set<Entry<String, String>> entrySet = map.entrySet();
        for (Entry<String, String> entry : entrySet) {
            if (StringUtils.equals(entry.getKey(), key) 
                    || StringUtils.contains(entry.getKey(), key)) {
                value = entry.getValue();
                break;
            }
        }
        return value;
        
    }
}
