package oldboy.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

public class GlobalInterceptor extends EmptyInterceptor {
    /*
    Вызывается при обнаружении загрязнения объекта во время сброса.
    Перехватчик может изменить обнаруженное текущее состояние, которое
    будет распространено как на базу данных, так и на постоянный объект.
    Обратите внимание, что не все сбросы заканчиваются фактической
    синхронизацией с базой данных, и в этом случае новое текущее состояние
    будет распространено на объект, но не обязательно (немедленно) на
    базу данных.

    Настоятельно рекомендуется, чтобы перехватчик не модифицировал
    предыдущее состояние.
    */
    @Override
    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) {
        /*
        Для грубой демонстрации работы перехватчика - Interceptor-a,
        просто выведем текст, который отобразится в консоли при
        выполнении соответствующего запроса.
        */
        System.out.println("\n************************\n" +
                           "OnFlushDirty Interceptor start!" +
                           "\n************************\n");
        return super.onFlushDirty(entity, id, currentState,
                                  previousState, propertyNames, types);
    }
}
