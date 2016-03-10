package io.beanmapper.builders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Supplier;

public abstract class AbstractBuilder<E, R, F> {

    public JpaRepository<E, Long> repository;
    protected E entity;
    protected R result;
    protected F form;
    private Supplier<E> constructor;
    private Supplier<R> resultConstructor;
    private Supplier<F> formConstructor;

    public AbstractBuilder(JpaRepository<E, Long> repository, Supplier<E> constructor) {
        this(repository, constructor, null);
    }

    public AbstractBuilder(JpaRepository<E, Long> repository, Supplier<E> constructor, Supplier<R> resultConstructor) {
        this(repository, constructor, resultConstructor, null);
    }

    public AbstractBuilder(JpaRepository<E, Long> repository, Supplier<E> constructor, Supplier<R> resultConstructor, Supplier<F> formConstructor) {
        this.repository = repository;
        this.constructor = constructor;
        this.resultConstructor = resultConstructor;
        this.formConstructor = formConstructor;
        build();
    }

    public E save() {
        return repository.saveAndFlush(build().entity);
    }

    public Build<E, R, F> build() {
        Build<E, R, F> build = new Build<>(entity, result, form);
        this.entity = constructor.get();
        if(resultConstructor != null) {
            this.result = resultConstructor.get();
        }
        if(formConstructor != null) {
            this.form = formConstructor.get();
        }
        return build;
    }

    public class Build<BE, BR, BF> {
        public BE entity;
        public BR result;
        public BF form;

        public Build(BE entity, BR result, BF form) {
            this.entity = entity;
            this.result = result;
            this.form = form;
        }
    }
}
