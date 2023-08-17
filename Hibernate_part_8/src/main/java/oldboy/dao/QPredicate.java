package oldboy.dao;
/*
В качестве примера, для чего весь 'этот огород' - фильтры на
сайтах магазинах, маркетплейсах, когда идет выбор товара по
характеристикам (цена, производитель и т.д.)
*/
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/* Прикроемся от случайной инициализации пустым конструктором */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {
    /* Наше единственное поле List будущих предикатов */
    private final List<Predicate> predicates = new ArrayList<>();
    /* Создадим подобие билдера */
    public static QPredicate builder() {
        return new QPredicate();
    }
    /*
    Самый главный метод - добавление предикатов в наш список предикатов.
    Мы не знаем по какому 'параметру' будет 'фильтроваться' наш запрос,
    по этому параметризуем его. В качестве параметров метод принимает,
    сам 'объект фильтрации' и условие по-которому мы его будем 'фильтровать'.

    см. DOC/FunctionPackageDoc/InterfaceFunction.txt
        DOC/QueryDslCoreTypes/InterfacePredicate.txt
    */
    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }
    /* См. DOC/QueryDslCoreTypes/ExpressionUtilsClass.txt */
    public Predicate buildAnd() {
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate buildOr() {
        return ExpressionUtils.anyOf(predicates);
    }
}