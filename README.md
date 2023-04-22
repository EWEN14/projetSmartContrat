# projetSmartContrat

Ce projet contient (entre autres) un script python et un fichier de type geojson. Le script Python va permettre d'ouvrir une carte depuis laquelle on peut tracer un polygone représentant un bail rural. En appuyant sur le bouton "Export", un fichier **data.geojson** sera généré et les coordonnées présentes dans celui-ci seront envoyés à une API Spring Boot qui s'occupera d'enregister le bail rural avec les coordonnées associées.

Si les coordonnées envoyées correspondent à un polygone et que l'enregistrement s'est bien déroulé, un message de succès s'affichera dans la console et un message d'erreur s'affichera dans le cas contraire.

Le projet de l'API Spring Boot est disponible ici : https://github.com/EWEN14/projet-integ-si
