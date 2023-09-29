# java-filmorate
Учебный проект Яндекс.Практикум.

## Описание
![spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![postgres](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
) ![ide](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white
) ![java](https://img.shields.io/badge/Java11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
) ![markdown](https://img.shields.io/badge/Markdown-000000?style=for-the-badge&logo=markdown&logoColor=white
) ![junit](https://img.shields.io/badge/JUnit_Test-DC143C?style=for-the-badge&logo=junit&logoColor=white
) ![maven](https://img.shields.io/badge/Maven-008000?style=for-the-badge&logo=maven&logoColor=white) 

Filmorate - это бэкэнд-сервис на основе Restful API для хранения и управления информацией о фильмах(название, рейтинг MPA, жанр, описание и продолжительность), составления рейтинга фильмов на основе отзывов пользователей, поиска фильма, а также для общения пользователей.
## Блок-схема Базы Данных приложения:

![alt text](src/main/resources/image/Filmorate.jpg)

<details>
<summary>Примеры запросов</summary>
Список всех фильмов:

```roomsql
SELECT * FROM films
ORDER BY film_id ASC;
```

Список всех пользователей:

```roomsql
SELECT *
FROM user
ORDER BY user_id ASC;
```

Список всех фильмов в жанре {Название жанра}:

```roomsql
SELECT f.*
FROM film AS f
LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id
LEFT JOIN genre AS g ON g.genre_id=fg.genre_id
WHERE g.genre_name = '{Жанр}'
ORDER BY f.film_id ASC;
```

Топ 10 фильмов по количеству лайков:

```roomsql
SELECT * FROM films AS f 
LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id 
LEFT JOIN film_likes AS l ON f.film_id = l.film_id 
GROUP BY f.film_id 
ORDER BY SUM(l.film_id) DESC, f.name 
LIMIT(?)
```

</details>

## Диаграмма классов

![diagramm](src/main/resources/image/java-filmorate.png)

