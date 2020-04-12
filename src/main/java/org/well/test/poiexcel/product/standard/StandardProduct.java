package org.well.test.poiexcel.product.standard;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName:StandardProduct
 * @Description:标准产品，由标准工厂生产
 * @author well
 * @date:2020年4月10日
 *
 */
@Setter
@ToString
@EqualsAndHashCode
public class StandardProduct implements IStandardProduct {

    private String dateStr;

    private String organ;

    private String currency;

    private String indiNameX;

    private String indiNameY;

    private String indiType;

    private String indiFreq;

    private String indiValue;

    private String indiLimitFlag;

    private String indiLimit;

    private String subject;

    private String bizType;

    private String module1;

    private String module2;

    private String security;

    @Override
    public String getDateStr() {
        return dateStr;
    }

    @Override
    public String getOrgan() {
        return organ;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public String getIndiNameX() {
        return indiNameX;
    }

    @Override
    public String getIndiNameY() {
        return indiNameY;
    }

    @Override
    public String getIndiType() {
        return indiType;
    }

    @Override
    public String getIndiFreq() {
        return indiFreq;
    }

    @Override
    public String getIndiValue() {
        return indiValue;
    }

    @Override
    public String getIndiLimitFlag() {
        return indiLimitFlag;
    }

    @Override
    public String getIndiLimit() {
        return indiLimit;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getBizType() {
        return bizType;
    }

    @Override
    public String getModule1() {
        return module1;
    }

    @Override
    public String getModule2() {
        return module2;
    }

    @Override
    public String getSecurity() {
        return security;
    }
}
