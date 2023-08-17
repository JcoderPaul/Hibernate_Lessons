package oldboy.mapper;
/*
Создаем интерфейс для преобразователей
чего-то 'From' во что-то 'To' с
единственным методом:
*/
public interface Mapper<F, T> {
    /*
    Нам нужно получить нечто - 'T'
    сконвертированное из чего-то - 'F'
    */
    T mapFrom(F object);
}
