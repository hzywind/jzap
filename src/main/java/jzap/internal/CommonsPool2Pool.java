package jzap.internal;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;

public class CommonsPool2Pool<E> implements Pool<E> {

    private final ObjectPool<E> pool;
    private final Creator<E> creator;

    public CommonsPool2Pool(int size, Creator<E> creator) {
        this.creator = creator;
        pool = new SoftReferenceObjectPool<E>(new BasePooledObjectFactory<E>() {

            @Override
            public E create() throws Exception {
                return creator.create();
            }

            @Override
            public PooledObject<E> wrap(E e) {
                return new DefaultPooledObject<>(e);
            }
        });
        try {
            pool.addObjects(size);
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

    @Override
    public E get() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            // fall back to new an object
            return creator.create();
        }
    }

    @Override
    public void put(E obj) {
        try {
            pool.returnObject(obj);
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }
}
