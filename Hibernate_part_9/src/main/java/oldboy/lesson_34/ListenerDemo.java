package oldboy.lesson_34;
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

public class ListenerDemo {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session sessionOne = sessionFactory.openSession();
             Session sessionTwo = sessionFactory.openSession()) {
            sessionOne.beginTransaction();

            /* Получаем из базы будущего члена чата */
            User takeUser = sessionOne.get(User.class, 1L);
            /* Создаем чат */
            Chat addChat = Chat.
                    builder().
                    chatName("StarFog").
                    build();
            /* Сохраняем и пробрасываем в БД */
            sessionOne.save(addChat);
            sessionOne.flush();
            /* Вносим соответствующие изменения в таблицу users_chats БД */
            UserChat newChat = UserChat.
                    builder().
                    user(takeUser).
                    chat(addChat).
                    build();
            /* И сохраняем их */
            sessionOne.save(newChat);
            sessionOne.getTransaction().commit();

            /* Запустим вторую транзакцию в другой сессии */
            sessionTwo.beginTransaction();
            /* Внесем изменения в БД - вносим только имя */
            UserChat userChatForUpdate = sessionTwo.find(UserChat.class, 1L);
            userChatForUpdate.setCreatedBy("Kuark");
            sessionTwo.flush();

            sessionTwo.getTransaction().commit();
            /*
            Изменения отобразятся в БД в таблице users_chats
            в поле updated_at - сработает слушатель см.
            CreateUpdateListener.java
            */
        }
    }
}
