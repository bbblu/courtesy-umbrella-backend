package tw.edu.ntub.imd.courtesyumbrella.service.impl;

import org.springframework.transaction.annotation.Transactional;
import tw.edu.ntub.imd.courtesyumbrella.bean.BaseBean;
import tw.edu.ntub.imd.courtesyumbrella.service.BaseService;
import tw.edu.ntub.imd.databaseconfig.dao.BaseDAO;
import tw.edu.ntub.imd.utils.bean.BeanUtils;

import java.io.Serializable;
import java.util.Optional;

public abstract class BaseServiceImpl<D extends BaseDAO<E, ID>, E, ID extends Serializable, B extends BaseBean> extends BaseViewServiceImpl<D, E, ID, B> implements BaseService<B, ID> {
    public BaseServiceImpl(D d, BeanEntityTransformer<E, B> beanEntityTransformer) {
        super(d, beanEntityTransformer);
    }

    @Transactional
    @Override
    public void update(ID id, B b) {
        try {
            Optional<E> optional = baseDAO.findById(id);
            if (optional.isPresent()) {
                E entity = optional.get();
                BeanUtils.copy(b, entity);
                baseDAO.save(entity);
            } else {
                throw new Exception("找不到資料, id = " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void delete(ID id) {
        try {
            Optional<E> optional = baseDAO.findById(id);
            if (optional.isPresent()) {
                E entity = optional.get();
                baseDAO.delete(entity);
            } else {
                throw new Exception("找不到資料, id = " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
