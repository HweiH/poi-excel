package org.well.test.poiexcel.factory.standard.point;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @ClassName:OriginalPoint
 * @Description:原点，值直接取
 * @author well
 * @date:2020年4月10日
 *
 */
@Getter
@EqualsAndHashCode(callSuper = true) // 需要调用父类的 equals 和 hashcode 方法
@ToString
public class OriginalPoint extends Point {

    /**
     * 原点，空值
     */
    public static final Point POINT_ORIGINAL_EMPTY = new OriginalPoint("");

    /**
     * 原点对应的值
     */
    private String value;

    public OriginalPoint(String value) {
        super(0, 0); // 原点坐标
        this.value = value;
    }
}
