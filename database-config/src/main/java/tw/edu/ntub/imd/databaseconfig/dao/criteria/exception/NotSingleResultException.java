package tw.edu.ntub.imd.databaseconfig.dao.criteria.exception;

import tw.edu.ntub.imd.exception.ProjectException;

public class NotSingleResultException extends ProjectException {
    public NotSingleResultException(Class<?> entityClass) {
        super(entityClass.getSimpleName() + "查詢結果不為單一結果");
    }

    @Override
    public String getErrorCode() {
        return "Query - NotSingleResult";
    }
}
