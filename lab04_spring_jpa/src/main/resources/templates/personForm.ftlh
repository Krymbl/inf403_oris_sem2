<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Страница формы</title>
</head>
<body>

<h2>Форма</h2>

    <div>
        <form action="/persons/add" method="post">
            <div>
                <label>Имя</label>
                <input type="text" name="name">
            </div>

            <div>
                <label>Телефон</label>
                <input type="text" name="phone">
            </div>

            <div>
                <label>Роль</label>
                <select class="role" name="role">
                    <option value="ADMIN">Админ</option>
                    <option value="CLIENT">Клиент</option>
                </select>
            </div>

            <div class="extra-information">
                <label class="extra-label">Почта</label>
                <input type="text" name="extra">
            </div>

            <button type="submit">Сохранить</button>
        </form>

        <a href="/persons">Посмотреть всех</a>

</div>

<script>
    const roleElement = document.querySelector(".role");
    const extraLabelElement = document.querySelector(".extra-label");

    roleElement.addEventListener('change' , (event) => {
        const value = roleElement.value;

        if (value === 'ADMIN') {
            extraLabelElement.textContent = "Почта";
        } else {
            extraLabelElement.textContent = "Адрес";
        }
    })
</script>
</body>