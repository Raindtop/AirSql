package org.airsql.utils;

import org.airsql.exception.AirSqlUtilsException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 集合工具类
 * </p>
 *
 * @author raindrop
 * @since 2020/8/4
 */
public class AirSqlCollectionUtils {

    /**
     * 检查列表中的数据是否唯一
     * @param list
     * @throws AirSqlUtilsException
     */
    public static void checkListCountOnlyOne(List<?> list) throws AirSqlUtilsException{
        Map<Object, Integer> map = frequencyOfCollectionElements(list);

        for(Object key: map.keySet()){
            if (!((Integer)map.get(key)).equals(1)){
                throw new AirSqlUtilsException("Double ConfigID:" + key);
            }
        }
    }

    /**
     * 获取集合中的各个类型的个数
     * @param items
     * @return
     */
    public static Map<Object,Integer> frequencyOfCollectionElements(Collection<?> items) {
        if (items == null || items.size() == 0){
            return null;
        }

        Map<Object, Integer> map = new HashMap<Object, Integer>();
        for (Object temp : items) {
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        return map;
    }
}
