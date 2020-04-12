package org.well.test.poiexcel.factory.standard;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.well.test.poiexcel.factory.AbstractFactory;
import org.well.test.poiexcel.factory.Matter;
import org.well.test.poiexcel.factory.PropertiesUtil;
import org.well.test.poiexcel.factory.standard.machine.Config;
import org.well.test.poiexcel.factory.standard.machine.ConfigUtil;
import org.well.test.poiexcel.factory.standard.machine.Machine;
import org.well.test.poiexcel.product.IProduct;
import org.well.test.poiexcel.util.ExcelUtil;

/**
 * @ClassName:StandardFactory
 * @Description:标准产品工厂
 * @author well
 * @date:2020年4月10日
 *
 */
public class StandardFactory extends AbstractFactory {

    /* (non-Javadoc)
     * @see org.well.test.poiexcel.factory.AbstractFactory#getProduct(org.well.test.poiexcel.factory.Matter)
     */
    @Override
    public List<? extends IProduct> getProduct(Matter matter) {
        // 检测产品
        checkMatter(matter);
        // 原材料
        Matter material = matter;
        // 配置对象
        Config config = Config.getInstance(ConfigUtil.getConfigStream(material));
        //使用机器进行生产
        return new Machine(material, config).fabricateProduct();
    }

    /**
     * 检测原材料是否符合生产规范
     *
     * @param matter
     */
    private void checkMatter(Matter matter) {
        if (matter == null 
                || matter.getMaterial() == null) {
            throw new RuntimeException("matter不能为null，matter.material也不能为null");
        }
        if (StringUtils.isBlank(matter.getId())) {
            String tempId = PropertiesUtil.getKeyOf(PropertiesUtil.MAP_TID_CONTENT, ExcelUtil.getTableName(matter.getMaterial()));
            if (StringUtils.isBlank(tempId)) {
                throw new RuntimeException("matter.material解析出来的id为空");
            }
        }
    }
}
