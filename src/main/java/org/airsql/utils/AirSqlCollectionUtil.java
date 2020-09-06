package org.airsql.utils;

import org.airsql.domain.Configuration;
import org.airsql.exception.AirSqlUtilsException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * <p>
 * 集合工具类
 * </p>
 *
 * @author raindrop
 * @since 2020/8/4
 */
public class AirSqlCollectionUtil {

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

    /**
     * 获取最少的连接数ID
     * @param userConfig
     * @return
     */
    public static String findLessUseConnection(Map<String, Integer> userConfig){
        Set<String> keySet = userConfig.keySet();

        if (CollectionUtils.isEmpty(keySet)){
            return null;
        }
        String maxId = "";
        Integer maxCount = 0;

        for (String key: keySet){
            Integer count = userConfig.get(key);
            if (count > maxCount){
                maxId = key;
                maxCount = count;
            }
        }

        return maxId;
    }

    /**
     * 根据Id获取连接信息
     * @param configId
     * @param configurations
     * @return
     */
    public static Configuration getConfigById(String configId, ArrayList<Configuration> configurations){
        Configuration configuration = null;
        for (Configuration config: configurations){
            if (config.getConfigId().equals(configId)){
                configuration = config;
                break;
            }
        }

        return configuration;
    }
}
