package oldboy.lesson_34;
/* Демонстрация работы CallBacks в слушателях - listeners */

import oldboy.Util.HibernateUtil;
import oldboy.entity.Chat;
import oldboy.entity.User;
import oldboy.entity.UserChat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CountListenerDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession()) {
            sessionOne.beginTransaction();

            /* Получаем из базы будущего члена чата */
            User takeUser = sessionOne.get(User.class, 1L);
            /* Получаем пустой чат */
            Chat takeChat = sessionOne.get(Chat.class, 2L);
            /* Вносим соответствующие изменения в таблицу users_chats БД */
            UserChat newChatBox = UserChat.
                            builder().
                            user(takeUser).
                            chat(takeChat).
                            build();
            /* И сохраняем их */
            sessionOne.save(newChatBox);
            /*
            Полный каскад запросов приводить нет смысла кроме последнего:
                update
                    part_four_base.chats
                set
                    chat_name=?,
                    count=?
                where
                    chat_id=?
            в БД счетчик в таблице был 0 стало 1 и т.д. в случае добавления
            новых User в ChatUser.
            */
            sessionOne.getTransaction().commit();

            sessionOne.beginTransaction();
            /* Удаляем запись о пользователе и связном с ним чатом chat_id = 7, а счетчик = 4 */
            UserChat userChatMinusOneUser = sessionOne.get(UserChat.class, 8L);
            sessionOne.delete(userChatMinusOneUser);

            sessionOne.getTransaction().commit();
            /*
            Два последних ключевых запроса от:
            Hibernate:
                update
                    part_four_base.chats
                set
                    chat_name=?,
                    count=?
                where
                    chat_id=?
            Hibernate:
                delete
                from
                    part_four_base.users_chats
                where
                    users_chats_id=?

            Счетчик в соответствующей таблице 'chat' у чата с ID = 7 уменьшился на 1.
            */
        }
    }
}
