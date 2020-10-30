# Estudo sobre o quarkus

###### Criando uma imagem nativa:
- mvn package -Pnative

###### Executando de forma interetiva o container:

- docker run -i --rm -p 8080:8080 quarkus/hello-okd

###### Empacotar em código nativo
- mvn package -Pnative -Dquarkus.native.container-build=true

###### Subindo a aplicação
- mvn quarkus:dev
- mvn quarkus:dev -Ddebug=true (precisa atachar o processo na ide)

###### Adicionando uma nova depêndencia
- mvn quarkus:add-extension -Dextensions="quarkus-undertow-websockets"

###### Cache hibernate
- Podemos cachear as entidades, colocando sobre as mesmas @Cacheable e hints, que são utilizados dentro do cache (para normeação).

###### Panache
Existem 2 formas de uso do panache:
- Extender a classe PanacheEntity, para uso mais simples
- Extender a classe PanacheEntityBase, caso queria customizar a geração od id.
