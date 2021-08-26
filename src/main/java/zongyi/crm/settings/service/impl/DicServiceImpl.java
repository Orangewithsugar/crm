package zongyi.crm.settings.service.impl;

import zongyi.crm.settings.dao.DicTypeDao;
import zongyi.crm.settings.dao.DicValueDao;
import zongyi.crm.settings.domain.DicType;
import zongyi.crm.settings.domain.DicValue;
import zongyi.crm.settings.service.DicService;
import zongyi.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {


    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);



    // 获取数据字典
    public Map<String, List<DicValue>> getAll() {

        // 原理：将所有字典类型取出来，然后将其分门别类的保存到map中，返回

        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();

        // 将字典类型列表遍历
        List<DicType> dtList = dicTypeDao.getTypeList();

        // 将字典类型列表遍历
        for (DicType dt : dtList) {

            String code = dt.getCode();

            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code + "List", dvList);

        }

        return map;
    }
}


























