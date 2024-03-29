package oldboy.dto;

import lombok.Value;
/*
@Value — это неизменяемый вариант @Data. Все поля делаются приватными и
final по умолчанию, методы установки значений не генерируются. Класс тоже
делается final, потому что неизменяемость — это не то, что распространяется
на подклассы. Так же как и в @Data, методы toString(), equals() и hashCode()
methods генерируются, каждое поле получает метод получения значения,
генерируется конструктор, покрывающий все аргументы (кроме полей final,
инициализированных при объявлении).
*/
@Value
public class CompanyDto {
    String nameOfCompany;
    Double amount;
}