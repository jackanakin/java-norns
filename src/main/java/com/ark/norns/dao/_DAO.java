package com.ark.norns.dao;

import com.ark.norns.application.Logging;
import com.ark.norns.util.JsonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;

public abstract class _DAO<T, PK extends Serializable> {
    private PagingAndSortingRepository<T, PK> baseRepository;

    public _DAO(PagingAndSortingRepository<T, PK> baseRepository) {
        this.setBaseRepository(baseRepository);
    }

    protected PagingAndSortingRepository<T, PK> getBaseRepository() {
        return this.baseRepository;
    }

    protected void setBaseRepository(PagingAndSortingRepository<T, PK> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public T save(T entity) {
        T t = this.getBaseRepository().save(entity);
        Logging.audit.info(this.getClass().toString() + "@Usuário X#" + JsonUtil.entityToJson(t));
        return t;
    }

    public Iterable<T> saveAll(Iterable<T> entity) {
        Iterable<T> t = this.getBaseRepository().save(entity);
        Logging.audit.info(this.getClass().toString() + "@Usuário X#" + JsonUtil.entityToJson(t));
        return t;
    }

    public void delete(T entity) {
        this.getBaseRepository().delete(entity);
        Logging.audit.info(this.getClass().toString() + "@Usuário X#" + JsonUtil.entityToJson(entity));

    }

    public void deleteAll(Iterable<T> entity) {
        this.getBaseRepository().delete(entity);
        Logging.audit.info(this.getClass().toString() + "@Usuário X#" + JsonUtil.entityToJson(entity));
    }

    public T findOne(PK id) {
        return this.getBaseRepository().findOne(id);
    }

    public List<T> findAll() {
        Sort order = new Sort(Sort.Direction.DESC, "id");
        return (List<T>) this.getBaseRepository().findAll(order);
    }

    public Page<T> findAll(int first_element, int page_size) {
        int page = first_element / page_size;
        PageRequest pageRequest = new PageRequest(page, page_size);

        return this.getBaseRepository().findAll(pageRequest);
    }

    public Page<T> findAll(int first_element, int page_size, Sort sort) {
        int page = first_element / page_size;
        PageRequest pageRequest = new PageRequest(page, page_size, sort);
        return this.getBaseRepository().findAll(pageRequest);
    }

}
