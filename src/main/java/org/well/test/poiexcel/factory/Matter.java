package org.well.test.poiexcel.factory;

import org.apache.poi.ss.usermodel.Sheet;
import org.well.test.poiexcel.util.ExcelUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @ClassName:Matter
 * @Description:原材料
 * @author well
 * @date:2020年4月10日
 *
 */
@Getter
@EqualsAndHashCode
@ToString
public class Matter {

    /**
     * 标识
     */
    private String id;

    /**
     * 原材料内容
     */
    @EqualsAndHashCode.Exclude
    private Sheet material;

    public Matter(String id, Sheet material) {
        this.id = id;
        this.material = material;
    }

    public Matter(Sheet material) {
        String tempId = PropertiesUtil.getKeyOf(PropertiesUtil.MAP_TID_CONTENT, ExcelUtil.getTableName(material));
        this.id = tempId;
        this.material = material;
    }

}
