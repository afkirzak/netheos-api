# netheos-api

### Frameworks utilisés

 - Java 8
 - Maven
 - Spring boot
 - H2
 - Elasticsearch

### Installation d'Elasticsearch

Télécharger Elasticsearch : https://www.elastic.co/downloads/elasticsearch. Une fois téléchargé, ouvrez l’archive et lancez la commande suivante :

- **Linux** et **Mac** : `./bin/elasticsearch`
    
- **Windows** : `./bin/elasticsearch.bat`

On suppose qu'Elasticsearch est installé en local, vous pouvez vérifier s'il est bien lancé : http://localhost:9200/

Une fois que le war est déployé sur Tomcat, vous pouvez tester les 3 user stories, sachant que l'application utilise deux utilisateurs par défaut :

|Profil|Nom  |Mot de passe|
|--|--|--|
| Administrateur       | admin | password
| Utilisateur standard | afkir | password

### User story 1

> En tant qu'utilisateur ayant les droits "administrateur", je peux insérer une question / réponse dans la base de connaissances (FAQ) du produit. Une question / réponse est définie par :
> 1.  Le libellé de la question ;
> 2.  Le libellé de la réponse ;
> 3.  La liste des tags associés.

La requête à utiliser pour tester cette user story :

    URI: /private/faq/save
    HTTP Method: POST
    Basic Authentication: admin/password
    Body: JSON Object
    {
	"question":"What are the three V's of Big Data?",
	"answer":"Volume, Velocity and Variety",
	"tags": ["Big data", "Volume", "Velocity", "Variety"]
	}

Cette user story renvoie l'objet inséré.

### User story 2

> En tant qu'utilisateur ayant les droits "administrateur", je peux lister toutes les questions / réponses de la base de connaissances.

La requête à utiliser pour tester cette user story :

    URI: /private/faq/list
    HTTP Method: GET
    Basic Authentication: admin/password

Cette user story renvoie la liste des questions / réponses.

### User story 3

> En tant qu'utilisateur, je peux rechercher la réponse à une question sans avoir à saisir le texte exact correspondant à une question ou à une réponse de la base de connaissances.

La requête à utiliser pour tester cette user story :

    URI: /public/faq/search
    HTTP Method: GET
    Basic Authentication: afkir/password
    Parameters: search=HDFS

Cette user story renvoie la liste des questions / réponses contenant la valeur du paramètre `search`.

Vous pouvez également tester les user stories en utilisant le fichier Postman.
