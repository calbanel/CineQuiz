# Spécifiez l'image de base
FROM node:19.1.0-alpine

# Créez un répertoire pour notre application
RUN mkdir -p /src/app

# Définissez le répertoire de travail
WORKDIR /src/app

# Copiez les fichiers de votre application dans le conteneur
COPY . .

# Installez les dépendances de votre application
RUN npm install

# Compilez l'application Angular
RUN npm run build --prod

# Exposez le port par défaut de l'application
EXPOSE 4200

# Définissez la commande à exécuter pour lancer l'application
CMD ["npm", "start"]