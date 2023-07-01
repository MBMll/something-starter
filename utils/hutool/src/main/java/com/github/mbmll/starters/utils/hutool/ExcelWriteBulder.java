package com.github.mbmll.starters.utils.hutool;

import cn.hutool.poi.excel.ExcelWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xlc
 * @Description
 * @Date 2023/7/2 0:35
 */
@Data
@NoArgsConstructor
public class ExcelWriteBulder {
    private Cursor current;
    private ExcelWriter writer;

    public ExcelWriteBulder moveTo(int top, int left) {
        current = current.moveTo(top, left);
        return this;
    }

    public ExcelWriteBulder write(int top, int left, Object value, boolean isSetHeaderStyle) {
        if (top > 1 || top < -1 || left > 1 || left < -1) {
            Cursor moved = current.move(top, left);
            writer.merge(current.getTop(), moved.getTop(), current.getLeft(), moved.getLeft(), value, isSetHeaderStyle);
        } else {
            writer.writeCellValue(current.getLeft(), current.getTop(), value);
        }
        return this;
    }

}
