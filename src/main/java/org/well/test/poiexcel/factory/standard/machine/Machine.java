package org.well.test.poiexcel.factory.standard.machine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.well.test.poiexcel.factory.Matter;
import org.well.test.poiexcel.factory.standard.point.Point;
import org.well.test.poiexcel.product.IProduct;
import org.well.test.poiexcel.product.standard.StandardProduct;
import org.well.test.poiexcel.util.DateTimeUtil;
import org.well.test.poiexcel.util.ExcelUtil;
import org.well.test.poiexcel.util.StringUtil;

/**
 * @ClassName:Machine
 * @Description:生产产品的机器
 * @author well
 * @date:2020年4月10日
 *
 */
public class Machine {
    
    /**
     * 原材料
     */
    private Matter matter;
    
    /**
     * 配置对象
     */
    private Config config;

    /**
     * 确保原材料与配置对象成对存在
     * 
     * @param matter
     * @param config
     */
    public Machine(Matter matter, Config config) {
        this.matter = matter;
        this.config = config;
    }
    
    public List<? extends IProduct> fabricateProduct() {
        // 最终产品
        List<IProduct> products = new ArrayList<>();
        
        if (matter.getMaterial() == null) {
            return products;
        }
        
        List<Point> xPoints = config.getCurrIndiNameX();
        List<Point> yPoints = config.getCurrIndiNameY();
        List<Point> vPoints = config.getCurrIndiValue();
        
        for (int i = 0; i < xPoints.size(); i++) {
            Point xPoint = xPoints.get(i);
            for (int j = 0; j < yPoints.size(); j++) {
                Point yPoint = yPoints.get(j);
                Point vPoint = new Point(xPoint.getX(), yPoint.getY());
                if (vPoints.contains(vPoint)) {
                    StandardProduct product = new StandardProduct();
                    // 日期 
                    String currDate = ExcelUtil.getCellValue(matter.getMaterial(), config.getCurrDate());
                    currDate = StringUtils.trimToEmpty(currDate.substring(5));
                    currDate = DateTimeUtil.dateFormat(DateTimeUtil.dateParse(currDate, DateTimeUtil.DATE_PATTERN_CH), DateTimeUtil.DATE_PATTERN_EN);
                    product.setDateStr(currDate);
                    // 机构
                    String currOrg = ExcelUtil.getCellValue(matter.getMaterial(), config.getCurrOrg());
                    currOrg = StringUtils.trimToEmpty(currOrg.substring(5));
                    product.setOrgan(currOrg);
                    // 币种
                    String currency = ExcelUtil.getCellValue(matter.getMaterial(), config.getCurrency());
                    product.setCurrency(currency);
                    // 指标X名称
                    String currIndiNameX = ExcelUtil.getCellValue(matter.getMaterial(), xPoint);
                    product.setIndiNameX(currIndiNameX);
                    // 指标Y名称
                    String currIndiNameY = ExcelUtil.getCellValue(matter.getMaterial(), yPoint);
                    currIndiNameY = StringUtil.trimBlankChar(currIndiNameY);
                    product.setIndiNameY(currIndiNameY);
                    // 指标类型
                    String currIndiType = ExcelUtil.getCellValue(matter.getMaterial(), config.getCurrIndiType());
                    product.setIndiType(currIndiType);
                    // 指标频率
                    String currIndiFreq = ExcelUtil.getCellValue(matter.getMaterial(), config.getCurrIndiFreq());
                    product.setIndiFreq(currIndiFreq);
                    // 指标值
                    String currIndiValue = ExcelUtil.getCellValue(matter.getMaterial(), vPoint);
                    product.setIndiValue(currIndiValue);
                    // 是否有阈值
                    String currIndilimitFlag = ExcelUtil.getCellValue(matter.getMaterial(), config.getCurrIndiLimitFlag());
                    product.setIndiLimitFlag(currIndilimitFlag);
                    // 阈值
                    String currIndilimit = ExcelUtil.getCellValue(matter.getMaterial(), config.getCurrIndiLimit());
                    product.setIndiLimitFlag(currIndilimit);
                    // 主题
                    String subject = ExcelUtil.getCellValue(matter.getMaterial(), config.getSubject());
                    product.setSubject(subject);
                    // 业务类型
                    String businessType = ExcelUtil.getCellValue(matter.getMaterial(), config.getBusinessType());
                    product.setBizType(businessType);
                    // 模块1
                    String module1 = ExcelUtil.getCellValue(matter.getMaterial(), config.getModule1());
                    product.setModule1(module1);
                    // 模块2
                    String module2 = ExcelUtil.getCellValue(matter.getMaterial(), config.getModule2());
                    product.setModule1(module2);
                    // 密级
                    String security = ExcelUtil.getCellValue(matter.getMaterial(), config.getSecurity());
                    product.setSecurity(security);
                    
                    // 添加到集合
                    products.add(product);
                }
            }
        }
        return products;
    }

}
