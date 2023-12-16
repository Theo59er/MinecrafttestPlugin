**FirstPlugin** ist ein Minecraft Bukkit-Plugin, das Spielern erlaubt, sich zufällig oder an eine bestimmte Position zu teleportieren.

## Installation

## Installation

```bash
# Klone das Repository
git clone https://github.com/DeinBenutzername/FirstPlugin.git

# Wechsle in das Verzeichnis des geklonten Projekts
cd FirstPlugin

# Führe den Gradle-Build aus
./gradlew build
```
Kopiere die generierte JAR-Datei vom build/libs Verzeichnis in den Plugins-Ordner deines Bukkit-Servers und starte den Server neu.



## Befehle

- `/rtp`: Teleportiert dich zufällig an eine Position.
- `/stp <x> <z>`: Teleportiert dich an die gewünschte Position auf den Boden.

## Konfiguration

Das Plugin benötigt keine spezielle Konfiguration. Du kannst jedoch die [config.yml](src/main/resources/config.yml) anpassen, um bestimmte Einstellungen zu ändern.

## Lizenz

Dieses Projekt ist unter der [MIT-Lizenz](LICENSE) lizenziert - siehe die [LICENSE](LICENSE)-Datei für weitere Details.

## Beitrag

- Wenn du einen Fehler gefunden hast, erstelle bitte ein [Issue](https://github.com/DeinBenutzername/FirstPlugin/issues).
- Wenn du zur Entwicklung beitragen möchtest, erstelle bitte einen [Pull Request](https://github.com/DeinBenutzername/FirstPlugin/pulls).

## Autoren

- Dein Name (@DeinBenutzername)

## Dank an

- [Bukkit](https://bukkit.org/) - Die Plattform für Minecraft-Server-Plugins.

## Hinweis

Dieses Projekt wurde für Bildungszwecke erstellt und ist möglicherweise nicht für den produktiven Einsatz geeignet.
