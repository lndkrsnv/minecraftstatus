# MinecraftStatus API

Spring Boot сервис для получения статуса Minecraft серверов через TCP/UDP протоколы. Возвращает JSON с версией, игроками, пингом и MOTD. Работает на Java 17+

## Быстрый запуск

```bash
# Собрать JAR
./gradlew bootJar

# Запустить
java -jar build/libs/minecraftstatus-1.0.1.jar
```

## Systemd установка

```bash
sudo mkdir -p /somewhere/statuschecker/
sudo cp build/libs/minecraftstatus-1.0.1.jar /somewhere/statuschecker/minecraftstatus.jar

sudo useradd -r -d /somewhere/statuschecker/ special_user
sudo chown -R special_user:special_user /somewhere/statuschecker/

# Systemd юнит
sudo tee /etc/systemd/system/minecraftstatus.service > /dev/null <<EOF
[Unit]
Description=Minecraft Status API
After=network.target

[Service]
Type=simple
User=special_user
Group=special_user
WorkingDirectory=/somewhere/statuschecker/

Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="JAVA_OPTS=-Xms128m -Xmx512m"

ExecStart=/usr/bin/java \$JAVA_OPTS -jar /somewhere/statuschecker/minecraftstatus.jar

SuccessExitStatus=143
Restart=on-failure
RestartSec=3
PrivateTmp=true

[Install]
WantedBy=multi-user.target
EOF

# Запуск
sudo systemctl daemon-reload
sudo systemctl enable --now minecraftstatus
sudo systemctl status minecraftstatus
```

## API примеры

```bash
# Основной запрос
curl "http://localhost:port/api/minecraft/status/host?port=25565&protocol=TCP"

# UDP (нужен query в server.properties)
curl "http://localhost:port/api/minecraft/status/host?port=25565&protocol=UDP_BASIC"

# Ответ JSON:
# {
#   "hostName": "Host",
#   "version": "1.20.4",
#   "players": {"online": 15, "max": 50},
#   "description": "Some server",
#   "latency": 25
# }
```

## Протоколы

| Протокол | Описание | Требования |
|----------|----------|------------|
| `TCP` | Server List Ping, всегда работает | Нет |
| `UDP_BASIC` | Базовая UDP информация | Query в server.properties |
| `UDP_FULL` | Полная информация + моды  | Query + `query.port` |

**Timeout**: 5 сек, favicon удаляется из ответа