package oldboy.listener;
/*
Создадим индивидуальный слушатель для добавления
User-a в Chat и его удаления. Естественно, что
такой функционал будет работать, только в случае
использования Hibernate операций, прямые SQL
запросы будут вносить искажения в значения 'count'
сущности Chat, т.к. будут проходить мимо слушателя.

Т.к. прямой запрос пройдет помимо жизненного цикла
сущности, и следовательно, callback не зафиксирует
нужных изменений.

Но тут мы демонстрируем принцип и возможности, с
некоторыми важными оговорками.
*/
import oldboy.entity.Chat;
import oldboy.entity.UserChat;

import javax.persistence.PostPersist;
import javax.persistence.PreRemove;

public class UserChatListener {
    /*
    Обратный вызов сработает после добавления User - а
    в чат, а если точнее после создания нового UserChat - а
    */
    @PostPersist
    public void postPersist(UserChat userChat) {
        Chat chat = userChat.getChat();
        chat.setCount(chat.getCount() + 1);
    }

    /* Callback сработает после удаления записи из таблицы users_chats */
    @PreRemove
    public void preRemove(UserChat userChat) {
        Chat chat = userChat.getChat();
        chat.setCount(chat.getCount() - 1);
    }
}
