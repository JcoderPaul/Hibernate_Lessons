package oldboy.lesson_35;
/*
Демонстрация принципа работы слушателей,
использующих в своей структуре callback - и
*/

import oldboy.Util.HibernateUtil;
import oldboy.entity.Chat;
import oldboy.entity.User;
import oldboy.entity.UserChat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AuditTableEventListener {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            sessionOne.beginTransaction();

            /* Получаем из базы будущего члена чата */
            User takeUser = sessionOne.get(User.class, 2L);
            /* Создаем чат */
            Chat addChat = Chat.
                    builder().
                    chatName("StarGazer").
                    build();
            /* Сохраняем и пробрасываем в БД - т.е. задействован PreInsertEvent */
            sessionOne.save(addChat);
            sessionOne.flush();
            /* Вносим соответствующие изменения в таблицу users_chats БД */
            UserChat newChat = UserChat.
                    builder().
                    user(takeUser).
                    chat(addChat).
                    build();
            /* И сохраняем их - т.е. задействован PreInsertEvent*/
            sessionOne.save(newChat);
            /* Извлекаем чат с ID = 5 и удаляем его из БД */
            Chat removeChat = sessionOne.get(Chat.class, 5L);
            /* И тут в работу включается PreDeleteEvent */
            sessionOne.delete(removeChat);

            sessionOne.getTransaction().commit();
            /*
            Все данные видны в соответствующих таблицах и конечно
            в таблице 'audit' появляются 3-и записи:
            - 2-a INSERT-a;
            - 1-н DELETE;
            с расшифровками в нужных полях: entity_name, entity_content
            */
        }
    }
}
