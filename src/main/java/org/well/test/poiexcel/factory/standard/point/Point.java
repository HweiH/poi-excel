package org.well.test.poiexcel.factory.standard.point;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName:Point
 * @Description:Excel中的坐标点
 * @author well
 * @date:2020年4月10日
 *
 */
@Getter
@EqualsAndHashCode
@ToString
public class Point {

    /**
     * X坐标值
     */
    private int x;

    /**
     * Y坐标值
     */
    private int y;

    /**
     * 父坐标点，不在 equals 和 hashcode 方法中使用
     */
    @Setter
    @EqualsAndHashCode.Exclude
    private Point parent;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isOriginalPoint() {
        return this.x == 0 && this.y == 0;
    }
}
