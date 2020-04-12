package org.well.test.poiexcel.factory;

import org.apache.commons.lang3.StringUtils;
import org.well.test.poiexcel.factory.standard.StandardFactory;
import org.well.test.poiexcel.util.ExcelUtil;

/**
 * @ClassName:FactoryProducer
 * @Description:工厂生成器，用于根据标识获取对应的实体工厂
 * @author well
 * @date:2020年4月10日
 *
 */
public class FactoryProducer {
    
    /**
     * 根据工厂类型获取工厂
     *
     * @param factoryType
     * @return
     */
    public static AbstractFactory getFactory(FactoryType factoryType) {
        if (factoryType == null) {
            return new StandardFactory();
        }
        AbstractFactory factory = null;
        switch (factoryType) {
        case STANDARD:
            factory = new StandardFactory();
            break;
        default:
            factory = new StandardFactory();
            break;
        }
        return factory;
    }

    /**
     * 根据原材料在content.properties中得到标识，进而在factory.properties中确定工厂
     *
     * @param matter
     * @return
     */
    public static AbstractFactory getFactory(Matter matter) {
        String materialName = ExcelUtil.getTableName(matter.getMaterial());
        return getFactory(materialName);
    }

    /**
     * 根据原材料名或者标识在content.properties中得到标识，进而在factory.properties中确定工厂
     *
     * @param materialName
     * @return
     */
    public static AbstractFactory getFactory(String materialName) {
        if (StringUtils.isBlank(materialName)) {
            return new StandardFactory();
        }
        String factoryClassName = getFactoryClassName(materialName);
        if (StringUtils.isBlank(factoryClassName)) {
            return new StandardFactory();
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(factoryClassName);
            Object obj = clazz.newInstance();
            if (obj instanceof AbstractFactory) {
                return (AbstractFactory) obj;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            new RuntimeException("配置异常，采用默认配置：StandardFactory", e).printStackTrace();
        }
        return new StandardFactory();
    }

    /**
     * 通过原材料名或者标识，获取对应配置的工厂全类名
     *
     * @param materialName
     * @return
     */
    private static String getFactoryClassName(String materialName) {
        if (StringUtils.isBlank(materialName)) {
            return null;
        }
        String tempId = getTempIdInContent(materialName);
        String clsName = getClsNameInFactory(tempId);
        return clsName;
    }

    /**
     * 通过原材料名或者标识，获取对应配置的标识
     *
     * @param materialName
     * @return
     */
    private static String getTempIdInContent(String materialName) {
        String tempId = PropertiesUtil.getKeyOf(PropertiesUtil.MAP_TID_CONTENT, materialName);
        if (StringUtils.isBlank(tempId)) {
            if (PropertiesUtil.MAP_TID_CONTENT.keySet().contains(materialName)) {
                tempId = materialName;
            }
        }
        return tempId;
    }
    
    /**
     * 通过模板ID，获取对应配置的工厂全类名
     *
     * @param tempId
     * @return
     */
    private static String getClsNameInFactory(String tempId) {
        return PropertiesUtil.getKeyOf(PropertiesUtil.MAP_FACTORY_TIDS, tempId);
    }
}
