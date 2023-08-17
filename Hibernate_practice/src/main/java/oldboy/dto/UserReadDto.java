package oldboy.dto;
/*
Данный класс нам нужен когда мы прочитали данные из БД
и возвращаем их по запросу пользователя web-приложения,
естественно мы возвращаем только те данные которые ему
позволено видеть. Т.е. сущность User превращается в
UserReadDto с выбранным набором полей.
*/
import oldboy.entity.accessory.PersonalInfo;
import oldboy.entity.accessory.Role;
/*
Records представляют классы, которые предназначены для
создания контейнеров неизменяемых данных. Кроме того,
records позволяют упростить разработку, сократив объем
кода.

По компании мы тоже возвращаем только определенный нами
набор полей, тоже создаем DTO класс.
*/
public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          String info,
                          Role role,
                          CompanyReadDto company) {
}
