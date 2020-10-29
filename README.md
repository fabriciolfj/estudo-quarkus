# Estudo sobre o quarkus

###### Criando uma imagem nativa:
- mvn package -Pnative

###### Executando de forma interetiva o container:

- docker run -i --rm -p 8080:8080 quarkus/hello-okd

###### Empacotar em código nativo
- mvn package -Pnative -Dquarkus.native.container-build=true

##### Subindo a aplicação
- mvn quarkus:dev
