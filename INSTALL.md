# Installation

Ce projet nécessite **Java Runtime Environment (JRE) 21** pour fonctionner et l’exécution d’un fichier **.jar**.

---

## Prérequis

- Système d’exploitation : Windows, Linux ou macOS
- Accès à un terminal / invite de commandes

---

## Installation de Java (JRE 21)

### 1. Télécharger JRE 21

Téléchargez une distribution Java 21 (JRE ou JDK) depuis l’un des sites suivants :

- https://www.oracle.com/java/technologies/downloads/
- https://adoptium.net (recommandé – Eclipse Temurin)

Sélectionnez la version **Java 21** adaptée à votre système d’exploitation.

---

### 2. Installer Java

#### Windows
- Exécutez l’installateur `.msi` ou `.exe`
- Suivez les instructions à l’écran

#### Linux
Exemple pour une distribution basée sur Debian/Ubuntu :
```bash
sudo apt update
sudo apt install openjdk-21-jre
```

#### macOS
Via Homebrew :
```
brew install openjdk@21
```

### 3. Vérifier l'installation :

Ouvrez un terminal et exécutez :
```bash
java -version
```
La version affichée doit être 21.
Si ce n’est pas le cas, vérifiez que Java est installé correctement et que le PATH est configuré.

### 4. Télécharger et lancer l’application
Téléchargez le fichier **.jar** fourni avec le projet et placez-le dans un dossier de votre choix.
Dans ce dossier, exécutez :
```bash
java -jar bataille-navale-vX.jar
```

