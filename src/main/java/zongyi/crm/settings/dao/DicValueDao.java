package zongyi.crm.settings.dao;

import zongyi.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {


    List<DicValue> getListByCode(String code);
}
