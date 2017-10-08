package com.example.ian.rentermanager2;

import java.util.List;

/**
 * Created by lyd10892 on 2016/8/23.
 */

public class BillUtils {
    public static <D> boolean isEmpty(List<D> list) {
        return list == null || list.isEmpty();
    }
}
