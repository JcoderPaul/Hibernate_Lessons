package oldboy.interceptor;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class TransactionInterceptor {
    /*
    Так-как наш перехватчик будет открывать и закрывать
    транзакции, то нам нужна зависимость SessionFactory.
    */
    private final SessionFactory sessionFactory;

    /* */

    @RuntimeType
    public Object intercept(@SuperCall Callable<Object> call,
                            @Origin Method method) throws Exception {
        Transaction transaction = null;
        /* Предустанавливаем флаг запущенной транзакции */
        boolean transactionStarted = false;
        /* Проверяем у оригинального метода Service класса наличие аннотации @Transactional*/
        if (method.isAnnotationPresent(Transactional.class)) {
            /*
            Далее простая логика, мы получаем текущую сессию, транзакцию и смотрим
            активна ли транзакция, т.к. мы можем из одного метода Service класса
            вызвать другой метод, того же класса и тоже аннотированный @Transactional

            Т.е. например, методы нашего UserService могут, как вызывать друг-друга,
            так и быть вызванными из других Service классов и слоев (а если они все
            помечены, как @Transactional - будет каскадное отрытие транзакций, хотя
            нам нужна одна). Поскольку у нас все идет внутри одного потока, то
            открыта должна быть только одна транзакция.
            */
            transaction = sessionFactory.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                /* Если не активна начинаем транзакцию и поднимаем флаг true */
                transaction.begin();
                transactionStarted = true;
            }
        }
        /* Метод intercept возвращает результат, а так же и исключения */
        Object result;
        try {
            /*
            Если с результатом все 'ок', И ТОЛЬКО ЕСЛИ флаг transactionStarted = true
            коммитим нашу транзакцию, не раньше и не позже. Как коммит, так и роллбэк
            транзакции идет из первоначальной точки доступа к ней.
                           * findByid * -> saveCompany -> saveLocales
            */
            result = call.call();
            if (transactionStarted) {
                transaction.commit();
            }
        } catch (Exception exception) {
            /*
            Если результат выкинул исключение, И ТОЛЬКО ЕСЛИ флаг
            transactionStarted = true мы 'откатываем' нашу транзакцию
            */
            if (transactionStarted) {
                transaction.rollback();
            }
            /* И пробрасываем исключение дальше */
            throw exception;
        }

        return result;
    }
}
