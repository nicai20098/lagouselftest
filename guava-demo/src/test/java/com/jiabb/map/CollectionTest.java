package com.jiabb.map;

import com.google.common.collect.*;
import org.junit.Test;

import java.util.Collection;

/**
 * @description: guava 扩展map相关工具
 * @author: jiabb-b
 * @date: 2024/2/01 001 16:30
 * @since: 1.0
 */
public class CollectionTest {


    /**
     *
     */
    @Test
    public void multiMapTest001() {
        Multimap<String, String> myMultimap = ArrayListMultimap.create();

        // 添加键值对
        myMultimap.put("Fruits", "Banana");
        //给Fruits元素添加另一个元素
        myMultimap.put("Fruits", "Apple");
        myMultimap.put("Fruits", "Pear");
        myMultimap.put("Vegetables", "Carrot");

        //获得multiMap的size
        int size = myMultimap.size();
        System.out.println(size);  // 4
        // 获得Fruits对应的所有的值
        Collection<String> fruits = myMultimap.get("Fruits");
        System.out.println(fruits); // [Banana, Apple, Pear]
        Collection<String> vegetables = myMultimap.get("Vegetables");
        System.out.println(vegetables); // [Carrot]
        //遍历multiMap
        for (String value : myMultimap.values()) {
            System.out.println(value);
        }

    }


    @Test
    public void multiMapTest002() {

        System.out.println("====1.Multiset=======");
        /** 1.Multiset 专用于统计元素出现次数 */
        Multiset<String> wordsMultiset = HashMultiset.create();
        // 添加元素
        wordsMultiset.addAll(Lists.newArrayList("a", "b", "c", "a", "b", "a"));
        //遍历不同元素集合，打印次数
        wordsMultiset.elementSet().forEach(e -> System.out.println(e + ":" + wordsMultiset.count(e)));

        System.out.println("====2.Multimap=======");
        /** 2.Multimap 1个key多value映射 */
        Multimap<String, Integer> multimap = HashMultimap.create();
        multimap.put("a", 1);
        multimap.put("b", 2);
        multimap.put("c", 3);
        multimap.put("a", 4);
        System.out.println("键-值集合映射：");
        // 键-值集合映射：asMap()转成map(key，Collection<E>),再调用map相关方法，打印
        multimap.asMap().forEach((key, value) -> System.out.println(key + ":" + value));
        System.out.println("键-单个值映射：");
        // 键-单个值映射：包括重复键
        multimap.entries().forEach(e -> System.out.println(e.getKey() + ":" + e.getValue()));

        System.out.println("====3.BiMap=======");
        /** 3.BiMap 键值反转 */
        BiMap<String, Integer> biMap = HashBiMap.create();
        biMap.put("a", 1);
        biMap.put("b", 2);
        System.out.println("键值对" + biMap);
        System.out.println("键值反转：" + biMap.inverse());

        System.out.println("====4.Table=======");
        /** 4.Table <R rowKey, C columnKey, V value> */
        Table<String, String, Integer> table = HashBasedTable.create();
        table.put("a", "b", 1);
        table.put("a", "c", 2);
        table.put("d", "b", 3);
        System.out.println(table);
        System.out.println("row a=" + table.row("a"));
        System.out.println("column b=" + table.column("b"));

        /** 5.ClassToInstanceMap 类、实例映射 */
        System.out.println("====5.ClassToInstanceMap=======");
        ClassToInstanceMap<Number> classToInstanceMap = MutableClassToInstanceMap.create();
        classToInstanceMap.putInstance(Integer.class, 1);
        classToInstanceMap.putInstance(Double.class, 2D);
        classToInstanceMap.putInstance(Long.class, 3L);
        System.out.println(classToInstanceMap);

        /** 6. RangeSet 区间运算; RangeMap 区间映射*/
        System.out.println("====6.RangeSet、RangeMap=======");
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        // [1,10]
        rangeSet.add(Range.closed(1, 10));
        // 不相连区间  [1,10] [11,15)
        rangeSet.add(Range.closedOpen(11, 15));
        // 相连合并[11,15）+[15,20)=[11，20），最终结果：[1,10] [11,20)
        rangeSet.add(Range.closedOpen(15, 20));
        // [1,10]-(5,10)=[1,5][10,10] ,最终结果：[1,5][10,10][11,20]
        rangeSet.remove(Range.open(5, 10));
        System.out.println("rangeSet=" + rangeSet);
        RangeMap<Integer, String> rangeMap = TreeRangeMap.<Integer, String>create();
        rangeMap.put(Range.closed(1, 10), "区间1");
        // 不处理任何key的区间交集，只是简单映射
        rangeMap.put(Range.closed(5, 20), "区间2");
        System.out.println("rangeMap=" + rangeMap);

    }

    @Test
    public void hashBasedTableTest001() {

        HashBasedTable<String, String, String> hashBasedTable = HashBasedTable.create();

        hashBasedTable.put("k1", "r1", "v1");
        hashBasedTable.put("k1", "r1", "v2");
        hashBasedTable.put("k2", "r2", "v3");
        hashBasedTable.put("k2", "r3", "v4");

        String s = hashBasedTable.get("k1", "r1");
        System.out.println(s);
    }


}
