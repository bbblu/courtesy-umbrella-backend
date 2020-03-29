package tw.edu.ntub.imd.databaseconfig.dao.criteria.exception;

import tw.edu.ntub.imd.exception.ProjectException;

public class WhereRestrictionExistException extends ProjectException {
    public WhereRestrictionExistException() {
        super("條件已存在，此方法會覆蓋先前設定的條件");
    }

    @Override
    public String getErrorCode() {
        return "DAO - ConditionExistError";
    }
}
