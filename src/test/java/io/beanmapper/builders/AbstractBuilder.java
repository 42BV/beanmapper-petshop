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
        init();
    }

    public Build build() {
        return init();
    }

    public E save() {
        return repository.saveAndFlush(init().entity);
    }

    private Build<E, R> init() {
        Build<E, R> build = new Build<>(entity, result);
        this.entity = constructor.get();
        if(resultConstructor != null) {
            this.result = resultConstructor.get();
        }
        return build;
    }

    public class Build<BE, BR> {
        public BE entity;
        public BR result;

        public Build(BE entity, BR result) {
            this.entity = entity;
            this.result = result;
        }
    }
}
