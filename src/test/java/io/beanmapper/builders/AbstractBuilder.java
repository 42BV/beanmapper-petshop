package io.beanmapper.builders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Supplier;

public abstract class AbstractBuilder<E, R> {

    public JpaRepository<E, Long> repository;
    protected E entity;
    protected R result;
    private Supplier<E> constructor;
    private Supplier<R> resultConstructor;

    public AbstractBuilder(JpaRepository<E, Long> repository, Supplier<E> constructor) {
        this(repository, constructor, null);
    }

    public AbstractBuilder(JpaRepository<E, Long> repository, Supplier<E> constructor, Supplier<R> resultConstructor) {
        this.repository = repository;
        this.constructor = constructor;
        this.resultConstructor = resultConstructor;
        init(null);
    }

    public E build() {
        return init(this.entity);
    }

    public R result() {
        return init(this.result);
    }

    public E save() {
        return repository.saveAndFlush(init(this.entity));
    }

    private <X> X init(X object) {
        this.entity = constructor.get();
        if(resultConstructor != null) {
            this.result = resultConstructor.get();
        }
        return object;
    }
}
