package org.well.test.poiexcel.factory.standard.machine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.well.test.poiexcel.factory.standard.point.OriginalPoint;
import org.well.test.poiexcel.factory.standard.point.Point;
import org.well.test.poiexcel.util.FieldUtil;
import org.well.test.poiexcel.util.StringUtil;

import lombok.Data;

/**
 * @ClassName:Config
 * @Description:每个模板配置文件对应的配置对象
 * @author well
 * @date:2020年4月10日
 *
 */
@Data
public class Config {

    private Point currDate;
    
    private Point currOrg;
    
    private Point currency;
    
    private List<Point> currIndiNameX;
    
    private List<Point> currIndiNameY;
    
    private Point currIndiType;
    
    private Point currIndiFreq;
    
    private List<Point> currIndiValue;
    
    private Point currIndiLimitFlag;
    
    private Point currIndiLimit;
    
    private Point subject;
    
    private Point businessType;
    
    private Point module1;
    
    private Point module2;
    
    private Point security;
            
    /**
     * 从配置文件中获取配置对象
     *
     * @param configFile
     * @return
     */
    public static Config getInstance(File configFile) {
        if (configFile == null 
                || configFile.exists() == false 
                || configFile.canRead() == false) {
            throw new RuntimeException("配置文件不存在或不可读");
        }
        try (FileReader fr = new FileReader(configFile)) {
            return getInstance(fr);
        } catch (IOException e) {
            throw new RuntimeException("配置文件不存在或不可读", e);
        }
    }

    /**
     * 从配置文件中获取配置对象
     *
     * @param configFile
     * @return
     */
    public static Config getInstance(InputStream configFile) {
        return getInstance(new InputStreamReader(configFile));
    }

    /**
     * 从配置文件中获取配置对象
     *
     * @param configFile
     * @return
     */
    public static Config getInstance(Reader configFile) {
        Config config = new Parser(configFile).parse();
        try {
            configFile.close();
        } catch (IOException e) {
            // ignore
        }
        return config;
    }
    
    /**
     * @ClassName:Parser
     * @Description:配置文件解析器
     * @author well
     * @date:2020年4月10日
     *
     */
    public static class Parser {
        
        private static final String ERROR_MSG = "配置文件不存在、不可读或不规范";
        
        private Reader fileReader;

        public Parser(Reader fileReader) {
            if (fileReader == null) {
                throw new RuntimeException(ERROR_MSG);
            }
            this.fileReader = fileReader;
        }
        
        /**
         * 从配置文件中解析出配置对象
         *
         * @return
         */
        public Config parse() {
            Config config = new Config();
            try (BufferedReader br = new BufferedReader(fileReader)) {
                String oneLine = null; // 每一行
                while ((oneLine = StringUtils.trim(br.readLine())) != null) {
                    // 跳过注释行
                    if (oneLine.isEmpty() 
                            || oneLine.startsWith("//") 
                            || oneLine.startsWith("#")) {
                        continue;
                    }
                    String[] tags = oneLine.split(":");
                    if (tags.length != 2) {
                        throw new RuntimeException("配置有误。应为 key:value 形式");
                    }
                    String configFieldName = resolveFieldName(tags);
                    Field configField = ReflectionUtils.findField(Config.class, configFieldName);
                    // 字段不存在，跳过处理
                    if (configField == null) {
                        continue;
                    }
                    List<Point> configFieldValue = resolveFieldValue(tags);
                    // 值不存在，跳过处理
                    if (CollectionUtils.isEmpty(configFieldValue)) {
                        continue;
                    }
                    // 为 Config 对象赋值
                    if (configField.getType().isAssignableFrom(List.class)) {
                        FieldUtil.setBeanLocalFieldValue(config, configFieldName, configFieldValue);
                    } else {
                        FieldUtil.setBeanLocalFieldValue(config, configFieldName, configFieldValue.get(0));
                    }
                }
                // 检查Config对象中的指标X与指标Y的坐标交叉点是否等于指标值的坐标数量
                int xSize = CollectionUtils.emptyIfNull(config.getCurrIndiNameX()).size();
                int ySize = CollectionUtils.emptyIfNull(config.getCurrIndiNameY()).size();
                int tSize = CollectionUtils.emptyIfNull(config.getCurrIndiValue()).size();
                if ((xSize * ySize) != tSize) {
                    throw new RuntimeException("指标X与指标Y的交叉坐标点数量必须等于指标值的坐标数量");
                }
            } catch (Exception e) {
                // 所有异常都转换为运行时异常
                throw new RuntimeException(ERROR_MSG, e);
            }
            return config;
        }

        /**
         * 解析出配置对象的字段名
         *
         * @param tags
         * @return
         */
        private String resolveFieldName(String[] tags) {
            String fieldName = StringUtils.trimToEmpty(tags[0]);
            if (!ConfigUtil.isAlphaNumber(fieldName)) {
                throw new RuntimeException("键名必须是全英文且与配置对象Config中的字段名保持一致");
            }
            fieldName = ConfigUtil.captureName(fieldName);
            return fieldName;
        }
        
        /**
         * 解析出配置对象的字段值
         *
         * @param tags
         * @return
         */
        private List<Point> resolveFieldValue(String[] tags) {
            // 最终的坐标点集合
            List<Point> finalPoints = new ArrayList<>();
            // 当前坐标定义字符串
            String temp = StringUtil.trimBlankChar(tags[1]);
            // 空
            if (StringUtils.isBlank(temp)) {
                finalPoints.add(OriginalPoint.POINT_ORIGINAL_EMPTY);
                return finalPoints;
            }
            // 字符串
            if (temp.startsWith("#")) {
                finalPoints.add(new OriginalPoint(temp.substring(1)));
                return finalPoints;
            }
            // 坐标
            if (temp.startsWith("@")) {
                String[] pointStrs = temp.substring(1).split("\\|");
                for (int i = 0; i < pointStrs.length; i++) {
                    String pointStr = pointStrs[i];
                    // 坐标规范校验
                    String[] tempPointStrs = pointStr.split("\\+");
                    for (int j = 0; j < tempPointStrs.length; j++) {
                        if (!ConfigUtil.isPointStr(tempPointStrs[j])) {
                            throw new RuntimeException(tags[0] + "配置中，第" + (i+1) + "组第" + (j+1) + "个坐标定义不符合正则：" + ConfigUtil.PATTERN_POINT_STR);
                        }
                    }
                    // 解析坐标点
                    List<Point> allPoints = parseAllPoints(pointStr);
                    finalPoints.addAll(allPoints);
                }
                return finalPoints;
            }
            finalPoints.add(OriginalPoint.POINT_ORIGINAL_EMPTY);
            return finalPoints;
        }

        /**
         * 解析所有的坐标点
         *
         * @param allPointStr
         * @return
         */
        private List<Point> parseAllPoints(String allPointStr) {
            boolean hasParent = allPointStr.contains("+");
            if (!hasParent) { // 没有父坐标
                return parsePoints(allPointStr, null);
            }
            List<Point> finalPoints = new ArrayList<>();
            
            String[] pointStrs = allPointStr.split("\\+");
            String currPointStr = pointStrs[0];
            
            Point firstParentPoint = null;
            Point parentPoint = null;
            for (int i = 1; i < pointStrs.length; i++) {
                String parentPointStr = pointStrs[i];
                if (parentPointStr.contains("-")) {
                    throw new RuntimeException(allPointStr + "坐标配置有误，父坐标只能1个");
                }
                Point temp = parsePoint(parentPointStr);
                if (i == 1 
                        && firstParentPoint == null) {
                    firstParentPoint = temp;
                }
                if (parentPoint == null) {
                    parentPoint = temp;
                } else {
                    parentPoint.setParent(temp);
                }
                parentPoint = temp;
            }
            finalPoints.addAll(parsePoints(currPointStr, firstParentPoint));
            return finalPoints;
        }

        /**
         * 解析坐标点
         *
         * @param pointStr
         * @param parentPoint
         * @return
         */
        private List<Point> parsePoints(String pointStr, Point parentPoint) {
            String[] xyStr = StringUtils.substringBetween(pointStr, "(", ")").split(",");
            String[] xStr = xyStr[0].split("-");
            String[] yStr = xyStr[1].split("-");
            int xStart = Integer.parseInt(xStr[0]);
            int xEnd = Integer.parseInt(xStr[xStr.length - 1]);
            int yStart = Integer.parseInt(yStr[0]);
            int yEnd = Integer.parseInt(yStr[yStr.length - 1]);
            if (xEnd < xStart 
                    || yEnd < yStart) {
                throw new RuntimeException(pointStr + "坐标定义中，X轴或Y轴的结束值必须大于起始值");
            }
            List<Point> points = new ArrayList<>();
            for (int i = xStart; i <= xEnd; i++) {
                for (int j = yStart; j <= yEnd; j++) {
                    Point newPoint = new Point(i, j);
                    newPoint.setParent(parentPoint);
                    points.add(newPoint);
                }
            }
            return points;
        }
        
        /**
         * 解析坐标点
         *
         * @param pointStr
         * @return
         */
        private Point parsePoint(String pointStr) {
            List<Point> points = parsePoints(pointStr, null);
            if (CollectionUtils.isEmpty(points)) {
                return OriginalPoint.POINT_ORIGINAL_EMPTY;
            }
            return points.get(0);
        }
    }
}
