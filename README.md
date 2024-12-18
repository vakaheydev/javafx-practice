# Entity Management Application

## 📋 Описание проекта
**Entity Management Application** — это JavaFX приложение для управления сущностями. Оно предоставляет функционал **CRUD** (создание, чтение, обновление, удаление) с возможностью **пагинации**, **поиска** и **фильтрации** данных. Проект разработан с чистой архитектурой и является **автономным**, не требующим внешних зависимостей для запуска.
---

## ⚙️ Функциональные возможности
- **Главное окно**:
  - Отображение списка всех сущностей с поддержкой пагинации.
  - Поиск и фильтрация данных.
  - Кнопки для создания, редактирования и удаления сущностей.
- **Создание сущности**:
  - Форма для ввода данных с валидацией.
- **Редактирование сущности**:
  - Возможность изменения данных выбранной сущности.
- **Удаление сущности**:
  - Подтверждение перед удалением записи.
- **Ошибки и уведомления**:
  - Удобные уведомления для взаимодействия с пользователем.
  
---

## 🛠️ Технологии
- **Java 21**  
- **JavaFX** — графический интерфейс пользователя  
- **TestFX** — тестирование графического интерфейса
- **FXML** — удобное описание GUI
- **SQLite** — база данных для хранения сущностей  
- **Maven** — система сборки проекта  
- **Lombok** — генерация геттеров, сеттеров и других вспомогательных методов
- **Logback** — логирование
- **JDBC** — взаимодействие с базой данных  

---

## 📂 Структура проекта

```plaintext
practice/
│
├── config/                       # Конфигурация приложения
│   └── DBConfig                  # Конфигурация базы данных
│
├── controller/                   # Контроллеры интерфейса
│   ├── CreateEntityController    # Логика создания сущности
│   ├── EditEntityController      # Логика редактирования сущности
│   └── MainController            # Главный контроллер приложения
│
├── dao/                          # Слой доступа к данным
│   ├── EntityDao                 # Интерфейс DAO
│   └── JdbcEntityDao             # Реализация DAO на JDBC
│
├── domain/                       # Доменные классы
│   ├── Entity                    # Класс сущности
│   └── EntityTable               # Класс для отображения в таблице
│
├── exception/                    # Обработка исключений
│   └── EntityNotFoundException   # Исключение при отсутствии сущности
│
├── factory/                      # Фабрики
│   └── ServiceFactory            # Фабрика для создания сервисов
│
├── service/                      # Сервисы
│   ├── EntityService             # Интерфейс сервиса сущностей
│   ├── SimpleEntityService       # Реализация сервиса для работы с сущностями
│   ├── SimpleValidationService   # Реализация сервиса валидации данных
│   └── ValidationService         # Интерфейс валидации
│
├── util/                         # Утилитные классы
│   ├── AlertUtil                 # Утилита для отображения уведомлений
│   └── JdbcUtils                 # Вспомогательные функции JDBC
│
├── App.java                      # Главный класс приложения
└── AppLauncher.java              # Запуск приложения
```
## 🚀 Запуск приложения

Скачайте исполняемый **JAR-файл** по ссылке:  
[![Download JAR](https://img.shields.io/badge/Download%20JAR-Yandex.Disk-blue)](https://disk.yandex.ru/d/VXZ_zVr0aoH9oQ)

### Инструкция по запуску:
1. Убедитесь, что у вас установлена **Java 21+**.  
2. Скачайте JAR-файл по ссылке выше.  
3. Запустите .jar файл:
   ```bash
   java -jar studying-practice-1.0-SNAPSHOT.jar
   ```
---

## 💡 Как использовать проект

1. **Клонируйте репозиторий**:
   ```bash
   git clone https://github.com/vakaheydev/javafx-practice.git
   cd javafx-practice
   ```
2. **Соберите проект**
   ```bash
   mvn clean package -DskipTests=true
   ```
3. **Запустите .jar файл**
   ```bash
   java -jar target/studying-practice-1.0-SNAPSHOT.jar
   ```
---

## 🎉 Приложение готово к использованию!
