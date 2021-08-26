package zongyi.crm.workbench.service.impl;

import zongyi.crm.utils.SqlSessionUtil;
import zongyi.crm.utils.UUIDUtil;
import zongyi.crm.workbench.dao.ClueDao;
import zongyi.crm.workbench.domain.Clue;
import zongyi.crm.workbench.domain.ClueActivityRelation;
import zongyi.crm.workbench.service.ClueService;

import java.util.Map;

public class ClueServiceImpl implements ClueService {


    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);


    // _______________________________________________________________________________________________________创建线索
    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if(count != 1){
            flag = false;
        }

        return flag;
    }


    // _______________________________________________________________________________________________________跳转到详细信息页
    public Clue detail(String id) {

        Clue c = clueDao.detail(id);


        return c;
    }

    // _______________________________________________________________________________________________________解除连接
    public boolean unbund(String id) {

        boolean flag = true;

        int count = clueDao.unbund(id);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    // _____________________________________________________________________________________________________关联市场活动和线索
    public boolean bund(Map<String, Object> map) {

        boolean flag = true;

        String cid = (String)map.get("cid");
        String[] aids = (String[]) map.get("aids");

        for (String aid : aids){

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueDao.bund(car);

            if(count != 1){
                flag = false;
            }

        }

        return flag;
    }
}





















