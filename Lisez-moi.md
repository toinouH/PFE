# Railflow

Il s'agit d'une application spring-boot avec une base h2 en stockage sur disque.
Pour lancer l'application, il faut lancer la commande suivante depuis le dossier racine du projet :

## Installation et lancement selon l'environnement:
Dans le terminal (universel) :
- **Nécessite <u>le JDK java 25</u> pour tous les types d'installation** On peut télécharger la version [ici](https://adoptium.net/fr/temurin/releases) selon la plateforme
```shell
mvn spring-boot:run
```

Dans NetBeans (Testé le 11/03):
- Importer le projet Maven
- Lancer l'exécution en ciblant la classe Main comme étant `SncfApplication`
dans `src/main/java/fr/univlyon3/sncf/SncfApplication.java`

Dans Eclipse 2025-12 (Testé le 11/03) :
- Importer le projet puis exécuter en appuyant sur le bouton "Executer" en tant qu'application Java (pas en déploiement serveur)

Dans IntellIJ IDEA (Testé le 11/03) :
- Importer le projet puis exécuter en appuyant sur le bouton "Executer"
- Si le projet n'est pas trouver aller chercher la classe principale dans le dossier `src/main/java/fr/univlyon3/sncf/SncfApplication.java`

Dans Visual Studio Code (Testé le 11/03) :
- Importer le projet
- Installer les extensions recommandées (pas obligatoire)
- Lancer le projet en appuyant sur le bouton "Executer"

A faire :
- Logique de remplacement du fichier json


## Choix techniques:

1. Utilisation du framework Spring Boot 
   - Très facile d'utilisation et de mise en place; lancement d'un nouveau projet en moins de 5 minutes montre en mains.
   - Il n'y a même pas besoin d'installer un serveur d'application: il est embarqué.
2. Utilisation d'une base H2 en disque
   - Les bases H2 ne doivent être en principe pas être utilisées en stockage mais en mémoire uniquement. Pourtant à des fins de POC et pédagogie nous avons choisi de le faire. Si le POC est repris il faudra changer.

## Configuration

Le fichier de configuration est `src/main/resources/application.properties` permet de configurer les fonctionnalités suivantes:
- la localisation du fichier de localisation des gares en Json
- la localisation des fichiers CAVE non traités
- les dossiers de destination des fichiers CAVE enrichis.
