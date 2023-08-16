package oldboy.type;
/* Этот класс приведен в качестве примера по созданию собственных типов */
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonType implements UserType {
    /*
    Разберем вкратце методы образующие класс:

    1. sqlTypes - этот метод должен вернуть массив целых чисел - кодов.
       Т.е. мы должны сообщить hibernate о том, какие типы данных полей
       необходимы для хранения полей класса пользовательского типа данных.

       Т.к. мой класс AddressType содержит четыре строковые поля, то я и
       возвращаю массив из четырех кодов Hibernate.STRING.sqlType().
    */
    @Override
    public int[] sqlTypes() {
        return new int[0];
    }

    @Override
    public Class returnedClass() {
        return null;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return false;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return 0;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {

    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return null;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return null;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return null;
    }
}
