package zongyi.crm.workbench.service;

import zongyi.crm.workbench.domain.Clue;

import java.util.Map;

public interface ClueService {


    boolean save(Clue c);


    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(Map<String, Object> map);
}
