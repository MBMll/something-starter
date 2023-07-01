package com.github.mbmll.starters.utils.hutool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author xlc
 * @Description
 * @Date 2023/7/2 0:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cursor {
    private int top;
    private int left;

    public Cursor move(int top, int left) {
        return new Cursor(this.top + top, this.left + left);
    }

    public Cursor moveTo(int top, int left) {
        return new Cursor(this.top, this.left);
    }
}
