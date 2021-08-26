package zongyi.crm.workbench.dao;


import zongyi.crm.workbench.domain.Clue;
import zongyi.crm.workbench.domain.ClueActivityRelation;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);

    int unbund(String id);

    int bund(ClueActivityRelation car);
}
