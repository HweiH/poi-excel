package org.well.test.poiexcel.product;

import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName:IProduct
 * @Description:抽象产品的形式
 * @author well
 * @date:2020年4月10日
 *
 */
public interface IProduct {

    /**
     * 完整标题，主标题+副标题
     *
     * @return
     */
    default String getTitle() {
        return StringUtils.EMPTY;
    };

    /**
     * 报表日期，符合某种格式，如：yyyy-MM-dd
     *
     * @return
     */
    default String getDateStr() {
        return StringUtils.EMPTY;
    }

    /**
     * 报送口径，即：机构
     *
     * @return
     */
    default String getOrgan() {
        return StringUtils.EMPTY;
    }

    /**
     * 币种
     *
     * @return
     */
    default String getCurrency() {
        return StringUtils.EMPTY;
    }

    /**
     * 货币单位
     *
     * @return
     */
    default String getMtyUnit() {
        return StringUtils.EMPTY;
    }

    /**
     * X指标名
     *
     * @return
     */
    default String getIndiNameX() {
        return StringUtils.EMPTY;
    }

    /**
     * Y指标名
     *
     * @return
     */
    default String getIndiNameY() {
        return StringUtils.EMPTY;
    }

    /**
     * 指标类型
     *
     * @return
     */
    default String getIndiType() {
        return StringUtils.EMPTY;
    }

    /**
     * 指标频率
     *
     * @return
     */
    default String getIndiFreq() {
        return StringUtils.EMPTY;
    }

    /**
     * 指标值
     *
     * @return
     */
    default String getIndiValue() {
        return StringUtils.EMPTY;
    }

    /**
     * 是否有阈值
     *
     * @return
     */
    default String getIndiLimitFlag() {
        return StringUtils.EMPTY;
    }

    /**
     * 阈值
     *
     * @return
     */
    default String getIndiLimit() {
        return StringUtils.EMPTY;
    }

    /**
     * 主题
     *
     * @return
     */
    default String getSubject() {
        return StringUtils.EMPTY;
    }

    /**
     * 业务类型
     *
     * @return
     */
    default String getBizType() {
        return StringUtils.EMPTY;
    }

    /**
     * 模块1
     *
     * @return
     */
    default String getModule1() {
        return StringUtils.EMPTY;
    }

    /**
     * 模块2
     *
     * @return
     */
    default String getModule2() {
        return StringUtils.EMPTY;
    }

    /**
     * 密级
     *
     * @return
     */
    default String getSecurity() {
        return StringUtils.EMPTY;
    }

    /**
     * 填表人
     *
     * @return
     */
    default String getPreparer() {
        return StringUtils.EMPTY;
    }

    /**
     * 复核人
     *
     * @return
     */
    default String getReviewer() {
        return StringUtils.EMPTY;
    }

    /**
     * 负责人
     *
     * @return
     */
    default String getLeader() {
        return StringUtils.EMPTY;
    }
}
