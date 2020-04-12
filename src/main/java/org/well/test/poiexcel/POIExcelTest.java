package org.well.test.poiexcel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.well.test.poiexcel.factory.standard.machine.Config;
import org.well.test.poiexcel.factory.standard.point.Point;
import org.well.test.poiexcel.util.DateTimeUtil;
import org.well.test.poiexcel.util.ExcelUtil;
import org.well.test.poiexcel.util.StringUtil;

import lombok.Data;

/**
 * @ClassName:POIExcelTest
 * @Description:测试
 * @author well
 * @date:2020年4月10日
 *
 */
public class POIExcelTest {

    @Data
    public static class VO {

        private String currDate;

        private String currOrg;

        private String currency;

        private String currIndiNameX;

        private String currIndiNameY;

        private String currIndiType;

        private String currIndiFreq;

        private String currIndiValue;

        private String currIndiLimitFlag;

        private String currIndiLimit;

        private String subject;

        private String businessType;

        private String module1;

        private String module2;

        private String security;

    }

    /**
     * 测试
     *
     * @param args
     * @throws EncryptedDocumentException
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {

        // Workbook workbook = WorkbookFactory.create(new File("D:\\test\\excel\\G25流动性覆盖率和净稳定资金比例情况表第I部分：流动性覆盖率.xlsx"));
        Workbook workbook = WorkbookFactory.create(new File("D:\\test\\excel\\G25流动性覆盖率和净稳定资金比例情况表第II部分：净稳定资金比例.xlsx"));
        Sheet sheet = workbook.getSheetAt(0);
        
        //InputStream is = POIExcelTest.class.getResourceAsStream("/factory/standard/config/G2501.config");
        InputStream is = POIExcelTest.class.getResourceAsStream("/factory/standard/config/G2502.config");
        Config configObj = Config.getInstance(is);
        
        List<VO> result = new ArrayList<>();
        
        List<Point> xPoints = configObj.getCurrIndiNameX();
        List<Point> yPoints = configObj.getCurrIndiNameY();
        List<Point> vPoints = configObj.getCurrIndiValue();
        
        for (int i = 0; i < xPoints.size(); i++) {
            Point xPoint = xPoints.get(i);
            for (int j = 0; j < yPoints.size(); j++) {
                Point yPoint = yPoints.get(j);
                Point vPoint = new Point(xPoint.getX(), yPoint.getY());
                if (vPoints.contains(vPoint)) {
                    VO vo = new VO();
                    
                    // 日期 
                    String currDate = ExcelUtil.getCellValue(sheet, configObj.getCurrDate());
                    currDate = StringUtils.trimToEmpty(currDate.substring(5));
                    currDate = DateTimeUtil.dateFormat(DateTimeUtil.dateParse(currDate, DateTimeUtil.DATE_PATTERN_CH), DateTimeUtil.DATE_PATTERN_EN);
                    vo.setCurrDate(currDate);
                    // 机构
                    String currOrg = ExcelUtil.getCellValue(sheet, configObj.getCurrOrg());
                    currOrg = StringUtils.trimToEmpty(currOrg.substring(5));
                    vo.setCurrOrg(currOrg);
                    // 币种
                    String currency = ExcelUtil.getCellValue(sheet, configObj.getCurrency());
                    vo.setCurrency(currency);
                    // 指标X名称
                    String currIndiNameX = ExcelUtil.getCellValue(sheet, xPoint);
                    vo.setCurrIndiNameX(currIndiNameX);
                    // 指标Y名称
                    String currIndiNameY = ExcelUtil.getCellValue(sheet, yPoint);
                    currIndiNameY = StringUtil.trimBlankChar(currIndiNameY);
                    vo.setCurrIndiNameY(currIndiNameY);
                    // 指标类型
                    String currIndiType = ExcelUtil.getCellValue(sheet, configObj.getCurrIndiType());
                    vo.setCurrIndiType(currIndiType);
                    // 指标频率
                    String currIndiFreq = ExcelUtil.getCellValue(sheet, configObj.getCurrIndiFreq());
                    vo.setCurrIndiFreq(currIndiFreq);
                    // 指标值
                    String currIndiValue = ExcelUtil.getCellValue(sheet, vPoint);
                    vo.setCurrIndiValue(currIndiValue);
                    // 是否有阈值
                    String currIndilimitFlag = ExcelUtil.getCellValue(sheet, configObj.getCurrIndiLimitFlag());
                    vo.setCurrIndiLimitFlag(currIndilimitFlag);
                    // 阈值
                    String currIndilimit = ExcelUtil.getCellValue(sheet, configObj.getCurrIndiLimit());
                    vo.setCurrIndiLimitFlag(currIndilimit);
                    // 主题
                    String subject = ExcelUtil.getCellValue(sheet, configObj.getSubject());
                    vo.setSubject(subject);
                    // 业务类型
                    String businessType = ExcelUtil.getCellValue(sheet, configObj.getBusinessType());
                    vo.setBusinessType(businessType);
                    // 模块1
                    String module1 = ExcelUtil.getCellValue(sheet, configObj.getModule1());
                    vo.setModule1(module1);
                    // 模块2
                    String module2 = ExcelUtil.getCellValue(sheet, configObj.getModule2());
                    vo.setModule1(module2);
                    // 密级
                    String security = ExcelUtil.getCellValue(sheet, configObj.getSecurity());
                    vo.setSecurity(security);
                    
                    // 添加到集合
                    result.add(vo);
                }
            }
        }
        
        for (VO vo : result) {
            System.out.println(vo);
        }
        
        workbook.close();
        is.close();
    }

}
