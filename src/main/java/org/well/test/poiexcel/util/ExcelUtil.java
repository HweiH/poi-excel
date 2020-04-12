package org.well.test.poiexcel.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.well.test.poiexcel.factory.standard.point.OriginalPoint;
import org.well.test.poiexcel.factory.standard.point.Point;

/**
 * @ClassName:ExcelUtil
 * @Description:Excel操作相关的工具类
 * @author well
 * @date:2020年4月10日
 *
 */
public class ExcelUtil {

    /**
     * 将列字母（最多4位）转成对应的数字，如：A=1，AA=27，AAA=703，即：26进制
     *
     * @param alphas
     * @return
     */
    public static int getColumnNum(String alphas) {
        String temp = alphas;
        if (StringUtils.isBlank(temp) || !StringUtils.isAlpha(temp)) {
            // 空白字符或者带有非字母，默认不存在
            return 0;
        }
        char[] ch = temp.toCharArray();
        int result = 0;
        for (int i = 0; i < ch.length; i++) {
            int num = getAlphaNum(ch[i]);
            result += num * (int) Math.pow(26, ch.length - i - 1);
        }
        return result;
    }

    /**
     * 获取字母排序，如：A=1，B=2，...，Z=26，其余字符=0
     *
     * @param alpha
     * @return
     */
    public static int getAlphaNum(char alpha) {
        if (!StringUtils.isAlpha(String.valueOf(alpha))) {
            return 0;
        }
        int index = alpha;
        if (index >= 97) {
            index -= (97 - 65);
        }
        return index - 64;
    }

    /**
     * 获取 Excel 中指定单元格的值，统一为字符串
     *
     * @param sheet
     * @param point
     * @return
     */
    public static String getCellValue(Sheet sheet, Point point) {
        if (point == null) {
            return "";
        }
        if (point.isOriginalPoint()) {
            return ((OriginalPoint) point).getValue();
        }
        int row = point.getX();
        int col = point.getY();
        return new StringBuilder(getCellValue(sheet, point.getParent())).append(getCellValue(sheet, row, col)).toString();
    }

    /**
     * 获取 Excel 中指定单元格的值，统一为字符串
     *
     * @param sheet
     * @param row
     * @param col
     * @return
     */
    public static String getCellValue(Sheet sheet, int row, int col) {
        if (sheet == null) {
            return null;
        }
        // 行
        int excelRow = (row > 0 ? row - 1 : 0);
        Row currRow = sheet.getRow(excelRow);
        if (currRow == null) {
            return null;
        }
        // 单元格
        int excelCol = (col > 0 ? col - 1 : 0);
        Cell currCell = currRow.getCell(excelCol);
        if (currCell == null) {
            return null;
        }
        return getCellValue(currCell);
    }

    /**
     * 获取 Excel 中指定单元格的值，统一为字符串
     *
     * @param currCell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String value = null;
        if (cell == null) {
            return value;
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
        case NUMERIC: // 数字
            String temp = null;
            if (DateUtil.isCellDateFormatted(cell)) { // 日期
                temp = DateTimeUtil.dateFormat(cell.getDateCellValue(), DateTimeUtil.DATE_PATTERN_EN);
            } else { // 数值
                temp = Double.toString(cell.getNumericCellValue());
            }
            value = String.valueOf(temp);
            break;
        case STRING: // 字符串
            value = String.valueOf(cell.getStringCellValue());
            break;
        case FORMULA: // 公式
            try {
                value = String.valueOf(cell.getNumericCellValue());
            } catch (IllegalStateException e) {
                value = String.valueOf(cell.getCellFormula());
            }
            break;
        case BLANK: // 空白单元格
            value = String.valueOf(StringUtils.EMPTY);
            break;
        case BOOLEAN: // 布尔值
            value = String.valueOf(cell.getBooleanCellValue());
            break;
        case ERROR: // 故障单元格
            value = String.valueOf(cell.getErrorCellValue());
            break;
        default:
            value = "未知类型";
            break;
        }
        // 去除左右空格
        value = StringUtils.trim(value);
        return value;
    }

    /**
     * 根据指标名称前边的数字，确定缩进，一级指标不缩进，二级指标缩进1个空格，三级指标缩进2个空格 ...
     *
     * @param indiName
     * @return
     */
    public static String indentIndiName(String indiName) {
        if (StringUtils.isBlank(indiName)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String prefix = getIndiNamePrefix(indiName);
        int level = ListUtils
                .emptyIfNull(Arrays.asList(prefix.split("\\.")).stream().filter(item -> StringUtils.isNotBlank(item)).collect(Collectors.toList()))
                .size();
        if (level > 1) {
            int blankNum = level;
            while ((--blankNum) > 0) {
                sb.append(" "); // 拼接空格
            }
        }
        sb.append(indiName);
        return sb.toString();
    }

    /**
     * 获取指标名前缀，序号
     *
     * @param indiName
     * @return
     */
    private static String getIndiNamePrefix(String indiName) {
        if (StringUtils.isBlank(indiName)) {
            return null;
        }
        int limit = indiName.length();
        StringBuilder sb = new StringBuilder(indiName);
        for (int i = 0; i < limit; i++) {
            char currChar = sb.charAt(i);
            // 第一次遇到中文或空格
            if ((currChar >= 0x4E00 && currChar <= 0x9FA5) || " ".equals(String.valueOf(currChar))) {
                if (i > 0) {
                    char prevChar = sb.charAt(i - 1);
                    // 前一个字符是数字或点.
                    if (Character.isDigit(prevChar) || ".".equals(String.valueOf(prevChar))) {
                        sb.insert(i, "|");
                        break;
                    }
                }
            }
        }
        String finalIndiName = sb.toString();
        return finalIndiName.split("\\|")[0];
    }

    /**
     * 获取表名，默认为 主题 = 主标题(A1) + 副标题(A,3)
     *
     * @param sheet
     * @return
     */
    public static String getTableName(Sheet sheet) {
        if (sheet == null) {
            return "";
        }
        // 主题 = 主标题(1, 1) + 副标题(3, 1)
        StringBuilder tableNameBuilder = new StringBuilder();
        String masterName = StringUtil.trimBlankCharToEmpty(ExcelUtil.getCellValue(sheet, 1, 1));
        String slaveName = StringUtil.trimBlankCharToEmpty(ExcelUtil.getCellValue(sheet, 3, 1));
        tableNameBuilder.append(masterName);
        if (slaveName.length() > 5) { // 副标题满足格式：[第X部分：]，因此长度最少为 6
            tableNameBuilder.append(slaveName);
        }
        String tableName = tableNameBuilder.toString();
        return tableName;
    }
}
