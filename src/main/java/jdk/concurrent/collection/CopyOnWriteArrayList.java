package jdk.concurrent.collection;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/*** 通过复制底层数组实现的对 修改（新增，修改等等）操作线程安全的list*/
public class CopyOnWriteArrayList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    private static final long serialVersionUID = 8673264195747942595L;

    /** 所有修改操作的锁 */
    final transient ReentrantLock lock = new ReentrantLock();

    /** 数据数组 */
    private transient volatile Object[] array;

    /** 获取数据数组 */
    final Object[] getArray() {
        return array;
    }

    /**
     * 设置数据数组
     */
    final void setArray(Object[] a) {
        array = a;
    }

    /*** 构造方法   */
    public CopyOnWriteArrayList() {
        setArray(new Object[0]);
    }

    /** 构造方法  */
    public CopyOnWriteArrayList(Collection<? extends E> c) {
        Object[] elements;
        if (c.getClass() == CopyOnWriteArrayList.class)
            elements = ((CopyOnWriteArrayList<?>)c).getArray();
        else {
            elements = c.toArray();
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elements.getClass() != Object[].class)
                elements = Arrays.copyOf(elements, elements.length, Object[].class);
        }
        setArray(elements);
    }

    /** 构造方法 */
    public CopyOnWriteArrayList(E[] toCopyIn) {
        setArray(Arrays.copyOf(toCopyIn, toCopyIn.length, Object[].class));
    }

    /** 获取列表中元素的个数 */
    public int size() {
        return getArray().length;
    }

    /** 判断是否为空  */
    public boolean isEmpty() {
        return size() == 0;
    }

    /** 判断是否相等 */
    private static boolean eq(Object o1, Object o2) {
        return (o1 == null) ? o2 == null : o1.equals(o2);
    }

    /**在数组elements的指定范围（index->fence）内查询数据对象o的索引位置 */
    private static int indexOf(Object o, Object[] elements,
                               int index, int fence) {
        if (o == null) {
            for (int i = index; i < fence; i++)
                if (elements[i] == null)
                    return i;
        } else {
            for (int i = index; i < fence; i++)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    /**在数组elements的指定范围（index->fence）内查询数据对象o的最后一个索引位置 */
    private static int lastIndexOf(Object o, Object[] elements, int index) {
        if (o == null) {
            for (int i = index; i >= 0; i--)
                if (elements[i] == null)
                    return i;
        } else {
            for (int i = index; i >= 0; i--)
                if (o.equals(elements[i]))
                    return i;
        }
        return -1;
    }

    /** 判断列表是否包含某个对象   */
    public boolean contains(Object o) {
        Object[] elements = getArray();
        return indexOf(o, elements, 0, elements.length) >= 0;
    }

    /** 获得对象在列表中的位置  */
    public int indexOf(Object o) {
        Object[] elements = getArray();
        return indexOf(o, elements, 0, elements.length);
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, searching forwards from {@code index}, or returns -1 if
     * the element is not found.
     * More formally, returns the lowest index {@code i} such that
     * <tt>(i&nbsp;&gt;=&nbsp;index&nbsp;&amp;&amp;&nbsp;(e==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;e.equals(get(i))))</tt>,
     * or -1 if there is no such index.
     *
     * @param e element to search for
     * @param index index to start searching from
     * @return the index of the first occurrence of the element in
     *         this list at position {@code index} or later in the list;
     *         {@code -1} if the element is not found.
     * @throws IndexOutOfBoundsException if the specified index is negative
     */
    public int indexOf(E e, int index) {
        Object[] elements = getArray();
        return indexOf(e, elements, index, elements.length);
    }

    /**
     * {@inheritDoc}
     */
    public int lastIndexOf(Object o) {
        Object[] elements = getArray();
        return lastIndexOf(o, elements, elements.length - 1);
    }

    /**
     * Returns the index of the last occurrence of the specified element in
     * this list, searching backwards from {@code index}, or returns -1 if
     * the element is not found.
     * More formally, returns the highest index {@code i} such that
     * <tt>(i&nbsp;&lt;=&nbsp;index&nbsp;&amp;&amp;&nbsp;(e==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;e.equals(get(i))))</tt>,
     * or -1 if there is no such index.
     *
     * @param e element to search for
     * @param index index to start searching backwards from
     * @return the index of the last occurrence of the element at position
     *         less than or equal to {@code index} in this list;
     *         -1 if the element is not found.
     * @throws IndexOutOfBoundsException if the specified index is greater
     *         than or equal to the current size of this list
     */
    public int lastIndexOf(E e, int index) {
        Object[] elements = getArray();
        return lastIndexOf(e, elements, index);
    }

    /** 浅copy这个list  */
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            CopyOnWriteArrayList<E> clone =
                (CopyOnWriteArrayList<E>) super.clone();
            clone.resetLock();
            return clone;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /** 将list转为数组返回 */
    public Object[] toArray() {
        Object[] elements = getArray();
        return Arrays.copyOf(elements, elements.length);
    }

    /** 将list 复制到数组，注意数组越界
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T a[]) {
        Object[] elements = getArray();
        int len = elements.length;
        if (a.length < len)
            return (T[]) Arrays.copyOf(elements, len, a.getClass());
        else {
            System.arraycopy(elements, 0, a, 0, len);
            if (a.length > len)
                a[len] = null;
            return a;
        }
    }


    /**从数组内获得索引位的元素*/
    @SuppressWarnings("unchecked")
    private E get(Object[] a, int index) {
        return (E) a[index];
    }

    /** 获取list内的指定索引位置的元素*/
    public E get(int index) {
        return get(getArray(), index);
    }

    /** 将指定的元素存储到数组的指定索引位置，并返回之前的元素*/
    public E set(int index, E element) {
        final ReentrantLock lock = this.lock;
        //抢锁
        lock.lock();
        try {
            //获取原有的值
            Object[] elements = getArray();
            E oldValue = get(elements, index);

            //判断修改值是否等于原值
            if (oldValue != element) {
                int len = elements.length;
                //复制一个新的数组，并在新数组内执行修改操作。
                //这样在新数组被赋值给list之前，list的读操作一直读的都是原有数组
                Object[] newElements = Arrays.copyOf(elements, len);
                newElements[index] = element;
                //赋值给list，完成修改
                setArray(newElements);
            } else {
                //相等，则什么都不用修改，直接写回
                setArray(elements);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }

    /** 添加一个新数据  */
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        //抢锁
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            //生成一个新的数组，并赋值原数组的数据，并在新数组内执行新增操作。
            //这样在新数组被赋值给list之前，list的读操作一直读的都是原有数组
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;
            //赋值给list，完成新增
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**将数据插入到list的指定位置   */
    public void add(int index, E element) {
        final ReentrantLock lock = this.lock;
        //抢锁
        lock.lock();
        try {
            //判断是否数组越界
            Object[] elements = getArray();
            int len = elements.length;
            if (index > len || index < 0)
                throw new IndexOutOfBoundsException("Index: "+index+
                                                    ", Size: "+len);
            //生成新数组，并在新数组上完成数据操作
            Object[] newElements;
            int numMoved = len - index;
            if (numMoved == 0)
                newElements = Arrays.copyOf(elements, len + 1);
            else {
                newElements = new Object[len + 1];
                System.arraycopy(elements, 0, newElements, 0, index);
                System.arraycopy(elements, index, newElements, index + 1,
                                 numMoved);
            }
            //新数组赋值给list
            newElements[index] = element;
            setArray(newElements);
        } finally {
            lock.unlock();
        }
    }

    /** 删除指定索引位置的数据*/
    public E remove(int index) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            E oldValue = get(elements, index);
            int numMoved = len - index - 1;
            if (numMoved == 0)
                //Arrays.copyOf()也有生成新数组的作用
                setArray(Arrays.copyOf(elements, len - 1));
            else {
                //生成新数组
                Object[] newElements = new Object[len - 1];
                System.arraycopy(elements, 0, newElements, 0, index);
                System.arraycopy(elements, index + 1, newElements, index,
                                 numMoved);
                setArray(newElements);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }

    /** 删除指定的数据对象  */
    public boolean remove(Object o) {
        Object[] snapshot = getArray();
        //先查询数据的索引位置
        int index = indexOf(o, snapshot, 0, snapshot.length);
        //按索引删除
        return (index < 0) ? false : remove(o, snapshot, index);
    }

    /**
     * A version of remove(Object) using the strong hint that given
     * recent snapshot contains o at the given index.
     */
    private boolean remove(Object o, Object[] snapshot, int index) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] current = getArray();
            int len = current.length;
            if (snapshot != current) findIndex: {
                int prefix = Math.min(index, len);
                for (int i = 0; i < prefix; i++) {
                    if (current[i] != snapshot[i] && eq(o, current[i])) {
                        index = i;
                        break findIndex;
                    }
                }
                if (index >= len)
                    return false;
                if (current[index] == o)
                    break findIndex;
                index = indexOf(o, current, index, len);
                if (index < 0)
                    return false;
            }
            Object[] newElements = new Object[len - 1];
            System.arraycopy(current, 0, newElements, 0, index);
            System.arraycopy(current, index + 1,
                             newElements, index,
                             len - index - 1);
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /*** 删除一段区间内的数据    */
    void removeRange(int fromIndex, int toIndex) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;

            if (fromIndex < 0 || toIndex > len || toIndex < fromIndex)
                throw new IndexOutOfBoundsException();
            int newlen = len - (toIndex - fromIndex);
            int numMoved = len - toIndex;
            if (numMoved == 0)
                setArray(Arrays.copyOf(elements, newlen));
            else {
                Object[] newElements = new Object[newlen];
                System.arraycopy(elements, 0, newElements, 0, fromIndex);
                System.arraycopy(elements, toIndex, newElements,
                                 fromIndex, numMoved);
                setArray(newElements);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Appends the element, if not present.
     *
     * @param e element to be added to this list, if absent
     * @return {@code true} if the element was added
     */
    public boolean addIfAbsent(E e) {
        Object[] snapshot = getArray();
        return indexOf(e, snapshot, 0, snapshot.length) >= 0 ? false :
            addIfAbsent(e, snapshot);
    }

    /**
     * A version of addIfAbsent using the strong hint that given
     * recent snapshot does not contain e.
     */
    private boolean addIfAbsent(E e, Object[] snapshot) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] current = getArray();
            int len = current.length;
            if (snapshot != current) {
                // Optimize for lost race to another addXXX operation
                int common = Math.min(snapshot.length, len);
                for (int i = 0; i < common; i++)
                    if (current[i] != snapshot[i] && eq(e, current[i]))
                        return false;
                if (indexOf(e, current, common, len) >= 0)
                        return false;
            }
            Object[] newElements = Arrays.copyOf(current, len + 1);
            newElements[len] = e;
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /** 判断是否包含参数容器内的全部数据元素
     */
    public boolean containsAll(Collection<?> c) {
        Object[] elements = getArray();
        int len = elements.length;
        for (Object e : c) {
            if (indexOf(e, elements, 0, len) < 0)
                return false;
        }
        return true;
    }

    /** 删除参数容器内的全部数据元素  */
    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (len != 0) {
                // temp array holds those elements we know we want to keep
                int newlen = 0;
                Object[] temp = new Object[len];
                for (int i = 0; i < len; ++i) {
                    Object element = elements[i];
                    if (!c.contains(element))
                        temp[newlen++] = element;
                }
                if (newlen != len) {
                    setArray(Arrays.copyOf(temp, newlen));
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /** 删除操作： list只保留在参数容器内的数据元素 */
    public boolean retainAll(Collection<?> c) {
        if (c == null) throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (len != 0) {
                // temp array holds those elements we know we want to keep
                int newlen = 0;
                Object[] temp = new Object[len];
                for (int i = 0; i < len; ++i) {
                    Object element = elements[i];
                    if (c.contains(element))
                        temp[newlen++] = element;
                }
                if (newlen != len) {
                    setArray(Arrays.copyOf(temp, newlen));
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Appends all of the elements in the specified collection that
     * are not already contained in this list, to the end of
     * this list, in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param c collection containing elements to be added to this list
     * @return the number of elements added
     * @throws NullPointerException if the specified collection is null
     * @see #addIfAbsent(Object)
     */
    public int addAllAbsent(Collection<? extends E> c) {
        Object[] cs = c.toArray();
        if (cs.length == 0)
            return 0;
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            int added = 0;
            // uniquify and compact elements in cs
            for (int i = 0; i < cs.length; ++i) {
                Object e = cs[i];
                if (indexOf(e, elements, 0, len) < 0 &&
                    indexOf(e, cs, 0, added) < 0)
                    cs[added++] = e;
            }
            if (added > 0) {
                Object[] newElements = Arrays.copyOf(elements, len + added);
                System.arraycopy(cs, 0, newElements, len, added);
                setArray(newElements);
            }
            return added;
        } finally {
            lock.unlock();
        }
    }

    /** 清除全部元素  */
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            setArray(new Object[0]);
        } finally {
            lock.unlock();
        }
    }

    /** 将参数容器内的全部元素加到list的尾部 */
    public boolean addAll(Collection<? extends E> c) {
        Object[] cs = (c.getClass() == CopyOnWriteArrayList.class) ?
            ((CopyOnWriteArrayList<?>)c).getArray() : c.toArray();
        if (cs.length == 0)
            return false;
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (len == 0 && cs.getClass() == Object[].class)
                setArray(cs);
            else {
                Object[] newElements = Arrays.copyOf(elements, len + cs.length);
                System.arraycopy(cs, 0, newElements, len, cs.length);
                setArray(newElements);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    /** 将参数容器内的全部元素加到list内，并从指定的索引位开始，相当于中间插入 */
    public boolean addAll(int index, Collection<? extends E> c) {
        Object[] cs = c.toArray();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (index > len || index < 0)
                throw new IndexOutOfBoundsException("Index: "+index+
                                                    ", Size: "+len);
            if (cs.length == 0)
                return false;
            int numMoved = len - index;
            Object[] newElements;
            if (numMoved == 0)
                newElements = Arrays.copyOf(elements, len + cs.length);
            else {
                newElements = new Object[len + cs.length];
                System.arraycopy(elements, 0, newElements, 0, index);
                System.arraycopy(elements, index,
                                 newElements, index + cs.length,
                                 numMoved);
            }
            System.arraycopy(cs, 0, newElements, index, cs.length);
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**遍历，并执行参数中的方法*/
    public void forEach(Consumer<? super E> action) {
        if (action == null) throw new NullPointerException();
        Object[] elements = getArray();
        int len = elements.length;
        for (int i = 0; i < len; ++i) {
            @SuppressWarnings("unchecked") E e = (E) elements[i];
            action.accept(e);
        }
    }

    /**根据过滤器函数，过滤list内的数据元素*/
    public boolean removeIf(Predicate<? super E> filter) {
        if (filter == null) throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (len != 0) {
                int newlen = 0;
                Object[] temp = new Object[len];
                for (int i = 0; i < len; ++i) {
                    @SuppressWarnings("unchecked") E e = (E) elements[i];
                    if (!filter.test(e))
                        temp[newlen++] = e;
                }
                if (newlen != len) {
                    setArray(Arrays.copyOf(temp, newlen));
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void replaceAll(UnaryOperator<E> operator) {
        if (operator == null) throw new NullPointerException();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len);
            for (int i = 0; i < len; ++i) {
                @SuppressWarnings("unchecked") E e = (E) elements[i];
                newElements[i] = operator.apply(e);
            }
            setArray(newElements);
        } finally {
            lock.unlock();
        }
    }

    public void sort(Comparator<? super E> c) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            Object[] newElements = Arrays.copyOf(elements, elements.length);
            @SuppressWarnings("unchecked") E[] es = (E[])newElements;
            Arrays.sort(es, c);
            setArray(newElements);
        } finally {
            lock.unlock();
        }
    }

    /** 写入到流   */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {

        s.defaultWriteObject();

        Object[] elements = getArray();
        // Write out array length
        s.writeInt(elements.length);

        // Write out all elements in the proper order.
        for (Object element : elements)
            s.writeObject(element);
    }

    /** 读取流，并转换为数组到list*/
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {

        s.defaultReadObject();

        // bind to new lock
        resetLock();

        // Read in array length and allocate array
        int len = s.readInt();
        Object[] elements = new Object[len];

        // Read in all elements in the proper order.
        for (int i = 0; i < len; i++)
            elements[i] = s.readObject();
        setArray(elements);
    }

    /** 转换为字符串*/
    public String toString() {
        return Arrays.toString(getArray());
    }

    /** 比对是否相等   */
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        List<?> list = (List<?>)(o);
        Iterator<?> it = list.iterator();
        Object[] elements = getArray();
        int len = elements.length;
        for (int i = 0; i < len; ++i)
            if (!it.hasNext() || !eq(elements[i], it.next()))
                return false;
        if (it.hasNext())
            return false;
        return true;
    }

    /** 取hashcode   */
    public int hashCode() {
        int hashCode = 1;
        Object[] elements = getArray();
        int len = elements.length;
        for (int i = 0; i < len; ++i) {
            Object obj = elements[i];
            hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    /** 数据元素迭代器  */
    public Iterator<E> iterator() {
        return new COWIterator<E>(getArray(), 0);
    }


    /** 数据元素迭代器快照。在迭代器创建的时候创建，并且不在不支持数据修改（增删改）操作 */
    public ListIterator<E> listIterator() {
        return new COWIterator<E>(getArray(), 0);
    }

    /** 数据元素迭代器快照。在迭代器创建的时候创建，并且不在不支持数据修改（增删改）操作 */
    public ListIterator<E> listIterator(int index) {
        Object[] elements = getArray();
        int len = elements.length;
        if (index < 0 || index > len)
            throw new IndexOutOfBoundsException("Index: "+index);

        return new COWIterator<E>(elements, index);
    }

    /**并行遍历迭代器 */
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator
            (getArray(), Spliterator.IMMUTABLE | Spliterator.ORDERED);
    }

    static final class COWIterator<E> implements ListIterator<E> {
        /** Snapshot of the array */
        private final Object[] snapshot;
        /** Index of element to be returned by subsequent call to next.  */
        private int cursor;

        private COWIterator(Object[] elements, int initialCursor) {
            cursor = initialCursor;
            snapshot = elements;
        }

        public boolean hasNext() {
            return cursor < snapshot.length;
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (! hasNext())
                throw new NoSuchElementException();
            return (E) snapshot[cursor++];
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            if (! hasPrevious())
                throw new NoSuchElementException();
            return (E) snapshot[--cursor];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         * @throws UnsupportedOperationException always; {@code remove}
         *         is not supported by this iterator.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         * @throws UnsupportedOperationException always; {@code set}
         *         is not supported by this iterator.
         */
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         * @throws UnsupportedOperationException always; {@code add}
         *         is not supported by this iterator.
         */
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            Object[] elements = snapshot;
            final int size = elements.length;
            for (int i = cursor; i < size; i++) {
                @SuppressWarnings("unchecked") E e = (E) elements[i];
                action.accept(e);
            }
            cursor = size;
        }
    }

    /** 截取一段list   */
    public List<E> subList(int fromIndex, int toIndex) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            if (fromIndex < 0 || toIndex > len || fromIndex > toIndex)
                throw new IndexOutOfBoundsException();
            return new COWSubList<E>(this, fromIndex, toIndex);
        } finally {
            lock.unlock();
        }
    }

    /** CopyOnWriteArrayList 的子列表 */
    private static class COWSubList<E>
        extends AbstractList<E>
        implements RandomAccess
    {
        private final CopyOnWriteArrayList<E> l;
        private final int offset;
        private int size;
        private Object[] expectedArray;

        // only call this holding l's lock
        COWSubList(CopyOnWriteArrayList<E> list,
                   int fromIndex, int toIndex) {
            l = list;
            expectedArray = l.getArray();
            offset = fromIndex;
            size = toIndex - fromIndex;
        }

        // only call this holding l's lock
        private void checkForComodification() {
            if (l.getArray() != expectedArray)
                throw new ConcurrentModificationException();
        }

        // only call this holding l's lock
        private void rangeCheck(int index) {
            if (index < 0 || index >= size)
                throw new IndexOutOfBoundsException("Index: "+index+
                                                    ",Size: "+size);
        }

        public E set(int index, E element) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                rangeCheck(index);
                checkForComodification();
                E x = l.set(index+offset, element);
                expectedArray = l.getArray();
                return x;
            } finally {
                lock.unlock();
            }
        }

        public E get(int index) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                rangeCheck(index);
                checkForComodification();
                return l.get(index+offset);
            } finally {
                lock.unlock();
            }
        }

        public int size() {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                checkForComodification();
                return size;
            } finally {
                lock.unlock();
            }
        }

        public void add(int index, E element) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                checkForComodification();
                if (index < 0 || index > size)
                    throw new IndexOutOfBoundsException();
                l.add(index+offset, element);
                expectedArray = l.getArray();
                size++;
            } finally {
                lock.unlock();
            }
        }

        public void clear() {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                checkForComodification();
                l.removeRange(offset, offset+size);
                expectedArray = l.getArray();
                size = 0;
            } finally {
                lock.unlock();
            }
        }

        public E remove(int index) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                rangeCheck(index);
                checkForComodification();
                E result = l.remove(index+offset);
                expectedArray = l.getArray();
                size--;
                return result;
            } finally {
                lock.unlock();
            }
        }

        public boolean remove(Object o) {
            int index = indexOf(o);
            if (index == -1)
                return false;
            remove(index);
            return true;
        }

        public Iterator<E> iterator() {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                checkForComodification();
                return new COWSubListIterator<E>(l, 0, offset, size);
            } finally {
                lock.unlock();
            }
        }

        public ListIterator<E> listIterator(int index) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                checkForComodification();
                if (index < 0 || index > size)
                    throw new IndexOutOfBoundsException("Index: "+index+
                                                        ", Size: "+size);
                return new COWSubListIterator<E>(l, index, offset, size);
            } finally {
                lock.unlock();
            }
        }

        public List<E> subList(int fromIndex, int toIndex) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                checkForComodification();
                if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
                    throw new IndexOutOfBoundsException();
                return new COWSubList<E>(l, fromIndex + offset,
                                         toIndex + offset);
            } finally {
                lock.unlock();
            }
        }

        public void forEach(Consumer<? super E> action) {
            if (action == null) throw new NullPointerException();
            int lo = offset;
            int hi = offset + size;
            Object[] a = expectedArray;
            if (l.getArray() != a)
                throw new ConcurrentModificationException();
            if (lo < 0 || hi > a.length)
                throw new IndexOutOfBoundsException();
            for (int i = lo; i < hi; ++i) {
                @SuppressWarnings("unchecked") E e = (E) a[i];
                action.accept(e);
            }
        }

        public void replaceAll(UnaryOperator<E> operator) {
            if (operator == null) throw new NullPointerException();
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                int lo = offset;
                int hi = offset + size;
                Object[] elements = expectedArray;
                if (l.getArray() != elements)
                    throw new ConcurrentModificationException();
                int len = elements.length;
                if (lo < 0 || hi > len)
                    throw new IndexOutOfBoundsException();
                Object[] newElements = Arrays.copyOf(elements, len);
                for (int i = lo; i < hi; ++i) {
                    @SuppressWarnings("unchecked") E e = (E) elements[i];
                    newElements[i] = operator.apply(e);
                }
                l.setArray(expectedArray = newElements);
            } finally {
                lock.unlock();
            }
        }

        public void sort(Comparator<? super E> c) {
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                int lo = offset;
                int hi = offset + size;
                Object[] elements = expectedArray;
                if (l.getArray() != elements)
                    throw new ConcurrentModificationException();
                int len = elements.length;
                if (lo < 0 || hi > len)
                    throw new IndexOutOfBoundsException();
                Object[] newElements = Arrays.copyOf(elements, len);
                @SuppressWarnings("unchecked") E[] es = (E[])newElements;
                Arrays.sort(es, lo, hi, c);
                l.setArray(expectedArray = newElements);
            } finally {
                lock.unlock();
            }
        }

        public boolean removeAll(Collection<?> c) {
            if (c == null) throw new NullPointerException();
            boolean removed = false;
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                int n = size;
                if (n > 0) {
                    int lo = offset;
                    int hi = offset + n;
                    Object[] elements = expectedArray;
                    if (l.getArray() != elements)
                        throw new ConcurrentModificationException();
                    int len = elements.length;
                    if (lo < 0 || hi > len)
                        throw new IndexOutOfBoundsException();
                    int newSize = 0;
                    Object[] temp = new Object[n];
                    for (int i = lo; i < hi; ++i) {
                        Object element = elements[i];
                        if (!c.contains(element))
                            temp[newSize++] = element;
                    }
                    if (newSize != n) {
                        Object[] newElements = new Object[len - n + newSize];
                        System.arraycopy(elements, 0, newElements, 0, lo);
                        System.arraycopy(temp, 0, newElements, lo, newSize);
                        System.arraycopy(elements, hi, newElements,
                                         lo + newSize, len - hi);
                        size = newSize;
                        removed = true;
                        l.setArray(expectedArray = newElements);
                    }
                }
            } finally {
                lock.unlock();
            }
            return removed;
        }

        public boolean retainAll(Collection<?> c) {
            if (c == null) throw new NullPointerException();
            boolean removed = false;
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                int n = size;
                if (n > 0) {
                    int lo = offset;
                    int hi = offset + n;
                    Object[] elements = expectedArray;
                    if (l.getArray() != elements)
                        throw new ConcurrentModificationException();
                    int len = elements.length;
                    if (lo < 0 || hi > len)
                        throw new IndexOutOfBoundsException();
                    int newSize = 0;
                    Object[] temp = new Object[n];
                    for (int i = lo; i < hi; ++i) {
                        Object element = elements[i];
                        if (c.contains(element))
                            temp[newSize++] = element;
                    }
                    if (newSize != n) {
                        Object[] newElements = new Object[len - n + newSize];
                        System.arraycopy(elements, 0, newElements, 0, lo);
                        System.arraycopy(temp, 0, newElements, lo, newSize);
                        System.arraycopy(elements, hi, newElements,
                                         lo + newSize, len - hi);
                        size = newSize;
                        removed = true;
                        l.setArray(expectedArray = newElements);
                    }
                }
            } finally {
                lock.unlock();
            }
            return removed;
        }

        public boolean removeIf(Predicate<? super E> filter) {
            if (filter == null) throw new NullPointerException();
            boolean removed = false;
            final ReentrantLock lock = l.lock;
            lock.lock();
            try {
                int n = size;
                if (n > 0) {
                    int lo = offset;
                    int hi = offset + n;
                    Object[] elements = expectedArray;
                    if (l.getArray() != elements)
                        throw new ConcurrentModificationException();
                    int len = elements.length;
                    if (lo < 0 || hi > len)
                        throw new IndexOutOfBoundsException();
                    int newSize = 0;
                    Object[] temp = new Object[n];
                    for (int i = lo; i < hi; ++i) {
                        @SuppressWarnings("unchecked") E e = (E) elements[i];
                        if (!filter.test(e))
                            temp[newSize++] = e;
                    }
                    if (newSize != n) {
                        Object[] newElements = new Object[len - n + newSize];
                        System.arraycopy(elements, 0, newElements, 0, lo);
                        System.arraycopy(temp, 0, newElements, lo, newSize);
                        System.arraycopy(elements, hi, newElements,
                                         lo + newSize, len - hi);
                        size = newSize;
                        removed = true;
                        l.setArray(expectedArray = newElements);
                    }
                }
            } finally {
                lock.unlock();
            }
            return removed;
        }

        public Spliterator<E> spliterator() {
            int lo = offset;
            int hi = offset + size;
            Object[] a = expectedArray;
            if (l.getArray() != a)
                throw new ConcurrentModificationException();
            if (lo < 0 || hi > a.length)
                throw new IndexOutOfBoundsException();
            return Spliterators.spliterator
                (a, lo, hi, Spliterator.IMMUTABLE | Spliterator.ORDERED);
        }

    }

    private static class COWSubListIterator<E> implements ListIterator<E> {
        private final ListIterator<E> it;
        private final int offset;
        private final int size;

        COWSubListIterator(List<E> l, int index, int offset, int size) {
            this.offset = offset;
            this.size = size;
            it = l.listIterator(index+offset);
        }

        public boolean hasNext() {
            return nextIndex() < size;
        }

        public E next() {
            if (hasNext())
                return it.next();
            else
                throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return previousIndex() >= 0;
        }

        public E previous() {
            if (hasPrevious())
                return it.previous();
            else
                throw new NoSuchElementException();
        }

        public int nextIndex() {
            return it.nextIndex() - offset;
        }

        public int previousIndex() {
            return it.previousIndex() - offset;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            int s = size;
            ListIterator<E> i = it;
            while (nextIndex() < s) {
                action.accept(i.next());
            }
        }
    }

    // Support for resetting lock while deserializing
    private void resetLock() {
        UNSAFE.putObjectVolatile(this, lockOffset, new ReentrantLock());
    }
    private static final sun.misc.Unsafe UNSAFE;
    private static final long lockOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = CopyOnWriteArrayList.class;
            lockOffset = UNSAFE.objectFieldOffset
                (k.getDeclaredField("lock"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
