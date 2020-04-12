package org.well.test.poiexcel.factory;

import java.util.List;

import org.well.test.poiexcel.product.IProduct;

/**
 * @ClassName:AbstractFactory
 * @Description:抽象工厂
 * @author well
 * @date:2020年4月10日
 *
 */
public abstract class AbstractFactory {

    /**
     * 输入原材料，获取生产的产品
     *
     * @param matter
     * @return
     */
    public abstract List<? extends IProduct> getProduct(Matter matter);
    
}
