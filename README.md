# Estudo sobre o quarkus

###### Após baixar o graalvm, crie a variável de ambiente abaixo:
```
GRAALVM_HOME=/home/spark/Documentos/graalvm-ce-java11-20.2.0
export GRAALVM_HOME
PATH="$GRAALVM_HOME/bin:$PATH"
```

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

# Openshift

##### Instalação do minishift no virtualbox
- minishift config set vm-driver virtualbox
- minishift config set disk-size 32G
- minishift config set memory 4096
- minishift config set openshift-version v3.11.0
- minishift start
- crie a váriavel de ambiente: export PATH=$PATH:/home/spark/.minishift/cache/oc/v3.11.0/linux

##### Criando um namespace
- oc new-project quarkus-hello-okd

##### Definindo um objeto de compilação binário
- oc new-build --binary --name=quarkus-hello-okd -l app=quarkus-hello-okd
- para consultar: oc get bc

##### Definindo o local do dockerfile
- oc patch bc/quarkus-hello-okd -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.native"}}}}

##### Verificar o binário criado
- oc describe bc/quarkus-hello-okd

##### Exemplo buildando o projeto e subindo para o minishift. (pega os dados da raiz, ou seja, execute dentro do seu projeto)
- oc start-build quarkus-hello-okd --from-dir=. --follow
