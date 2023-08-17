package oldboy.lesson_27;
/*
Проблема N+1 и ее решения.

Использование Entity Graph.
*/
import oldboy.Util.HibernateUtil;
import oldboy.entity.User;
import oldboy.entity.UserChat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.Map;

public class ProgramEntityGraphDemo {
    public static void main(String[] args) {
        /* Создаем фабрику сессий */
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            /* Открываем сессию */
            Session currentSession = sessionFactory.openSession()) {
            /* Начинаем транзакцию */
            currentSession.beginTransaction();

            /*
            Перед проверкой создания и настройки графа сущности User средствами Session,
            для наглядности, лучше закомментировать (или удалить) аннотацию @NamedEntityGraph
            в выбранном классе, хотя явного конфликта настроек мы не увидим.

            Дальнейшие шаги очень похожи на настройку аннотации @NamedEntityGraph над классом User:

            ШАГ 1. - строим граф сущности средствами класса Session
            */
            RootGraph<User> myFirstUserGraph = currentSession.createEntityGraph(User.class);

            /*
            ШАГ 2. - передаем атрибуты в наш граф - 'какие связки нас интересуют'
            */
            myFirstUserGraph.addAttributeNodes("company", "userChats");

            /*
            Создаем сабграф из поля userChat:
            ШАГ 3. - добавляем сабграф и указываем сущность на базе которой он строится UserChat
            */
            SubGraph<UserChat> myFirstUserChatSubGraph =
                    myFirstUserGraph.addSubGraph("userChats", UserChat.class);

            /*
            ШАГ 4. - передаем атрибуты с сабграф, как и в основной граф,
                     для примера см. настройку аннотации в классе User.
            */
            myFirstUserChatSubGraph.addAttributeNodes("chat");

            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(), myFirstUserGraph
            );
            User user = currentSession.find(User.class, 12L, properties);
            System.out.println(user.getCompany().getCompanyName());
            System.out.println(user.getUserChats().size());

            /* Коммитим транзакцию */
            currentSession.getTransaction().commit();
        }
    }
}
