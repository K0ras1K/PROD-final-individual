# Зависимости

В этом документе описаны зависимости, используемые в проекте, и приведены обоснования их включения.

## Telegram Bot API

Зависимость: `implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")`

Эта библиотека используется для взаимодействия с Telegram Bot API. Она предоставляет удобный и идиоматичный способ создания и управления Telegram-ботами с помощью Kotlin. Используя эту библиотеку, мы можем легко обрабатывать входящие сообщения и отправлять ответы пользователям.

## Dotenv

Зависимость: `implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")`

Dotenv - это библиотека, которая позволяет управлять переменными окружения для нашего приложения. Она загружает переменные из файла `.env` в окружение системы, облегчая настройку нашего приложения для различных окружений (например, разработка, тестирование, производство) без непосредственного кодирования конфиденциальной информации.

## Логгирование

Зависимости:

* `implementation("ch.qos.logback:logback-classic:1.4.14")`
* `implementation("ch.qos.logback:logback-core:1.4.14")`
* `implementation("org.slf4j:slf4j-api:2.0.10")`
* `implementation("org.slf4j:slf4j-simple:2.0.10")`

Эти библиотеки используются для целей логгирования. Logback - это популярный фреймворк для ведения журнала событий в Java-приложениях, в то время как SLF4J предоставляет простой фасад для различных фреймворков ведения журнала. Используя эти библиотеки, мы можем легко регистрировать события, ошибки и другую информацию во время выполнения нашего приложения.

## Redis

Зависимость: `implementation("redis.clients:jedis:5.0.0")`

Jedis - это клиентская библиотека Java для Redis, высокопроизводимого хранилища данных в оперативной памяти. Мы используем Redis для кеширования и хранения временных данных. Jedis предоставляет простой и эффективный способ взаимодействия с Redis из нашего приложения.

## База данных

Зависимости:

* `implementation("org.jetbrains.exposed:exposed-core:$exposed_version")`
* `implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")`
* `implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")`
* `implementation("com.zaxxer:HikariCP:$hikaricp_version")`
* `implementation("org.postgresql:postgresql:42.7.0")`

Эти зависимости используются для работы с базой данных. Exposed - это легковесный фреймворк для работы с SQL в Kotlin. Он предоставляет простой и удобный способ взаимодействия с базой данных. HikariCP - это пул соединений с базой данных, который обеспечивает эффективное использование ресурсов. Постгрес - это мощная объектно-реляционная система управления базами данных, которую мы используем для хранения данных нашего приложения.

## Клиент

Зависимости:

* `implementation("io.ktor:ktor-client-serialization:2.3.9")`
* `implementation("io.ktor:ktor-client-core:2.3.9")`
* `implementation("io.ktor:ktor-client-cio:2.3.9")`я
* `implementation("io.ktor:ktor-client-serialization:2.3.9")`
* `implementation("io.ktor:ktor-client-content-negotiation:2.3.9")`
* `implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.9")`
* `implementation("com.googlecode.json-simple:json-simple:1.1.1")`

Эти зависимости используются для работы с клиентом. Ktor - это легковесный фреймворк для создания веб-приложений и веб-сервисов в Kotlin. Мы используем его для взаимодействия с различными API в нашем приложении. JSON Simple - это простая библиотека для кодирования и декодирования JSON-строк, которую мы используем для работы с JSON-данными.