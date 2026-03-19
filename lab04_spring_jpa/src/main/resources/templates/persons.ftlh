<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список людей</title>
</head>
<body>

<h2>Список людей</h2>

<a href="/persons/add">Добавить человека</a>
<div>
    <#list persons as person>
        <div style="margin: 20px 0">
            <span>ID: ${person.id}</span><br>
            <span>Имя: ${person.name!"-"}</span><br>
            <span>Номер телефона: ${person.phone.number!"-"}</span><br>
            <span>Роль: ${person.role!"-"}</span><br>
            <span>Дополнительная информация: ${person.extra!"-"}</span>
        </div>
    </#list>

</div>

</body>