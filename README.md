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

##### Logando
- oc login -u system:admin

##### Usando o registro do minishift
- eval $(./minishift docker-env)

##### Criando um namespace
- oc new-project quarkus-hello-okd

##### Definindo um objeto de compilação binário
- oc new-build --binary --name=quarkus-hello-okd -l app=quarkus-hello-okd
- para consultar: oc get bc

##### Definindo o local do dockerfile
- oc patch bc/quarkus-hello-okd -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.native"}}}}'

##### Verificar o binário criado
- oc describe bc/quarkus-hello-okd

##### Exemplo buildando o projeto e subindo para o minishift. (pega os dados da raiz, ou seja, execute dentro do seu projeto)
- mvn package -Pnative
- oc start-build quarkus-hello-okd --from-dir=. --follow

##### Ver as imagens que estão no openshift
- oc get is

##### Mudar de namespaces (no exemplo mudando para o namespace default)
- oc project default
- oc get pods -n default -o wide (mostrar os detalhes dos pods do namespace default

##### Imagem criada
- oc get is

##### Criar um aplicativo com base na imagem
- oc new-app --image-stream=quarkus-hello-okd:latest

##### Expondo o serviço da app criada
- oc expose svc/quarkus-hello-okd
- oc get route quarkus-hello-okd -o jsonpath --template="{.spec.host}" (pegando o endereço virtual)

##### Endpotins relacionados a saúde da aplicação
- http://localhost:8080/health/live
- http://localhost:8080/health/ready

```
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-smallrye-health</artifactId>
</dependency>
```

##### Implementando tolerancia a falhas
- Timeout: define um tempo para chamada do serviço
- Fallback: providencia uma solução de contingência em casos de falha
- Retry: estipula um número de retentativas, com base em critérios.
- Bulkhead: isola falhas parciais, enquanto o restante do serviço ainda pode funcionar.
- Circuit Breaker: com base em critérios, executa metodos alternativos, afim de evitar a degradação do sistema causada pela sobrecarga.
- asynchronous: permite invocar uma operação assíncrona.

```
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-smallrye-fault-tolerance</artifactId>
</dependency>
```    

##### Metrics
Algumas métrics configuraveis:
- @Counted - conta o número de requisições foram efetuads.
- @Timed - medi a duraçã do evento.
- Endpoint que retorna todas as métricas: http://localhost:8080/metrics
- Endpoint que retorna as  métricas das aplicações registradas: http://localhost:8080/metrics/application
```
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-smallrye-metrics</artifactId>
</dependency>
```
##### Health
- Para habiltiar o pontos de saúde da nossa aplicação.

```
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-health</artifactId>
    </dependency>
http://localhost:8081/health
```

##### Gerando documentação dos nosso endpoints
```
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>

http://localhost:8081/swagger-ui/index.html
```    

###### Segurança
- Primeiro utilizaremos o elytron, um lib que realiza um processo de segurança mais simplista.
- keycloak, uma forma mais robusca de assegurar seus serviços, em suma eles validarão os tokens recebidos nele a cada requisição. Mais algumas features, como:
  - Client adapters
  - Single Sign-On (SSO)
  - Identity management and social login
  - Standard protocols (OpenID connect or SAML) : openid -> é baseado em oauth2, ou seja, o usuario através do aplicativo cliente consegui se autenticar do servidor de autorização, que este lhe proporcionará um token que fará uso nas chamadas do serviços. SAML -> uma forma de provedores passarem as credenciais de autorização para os provedores de serviços.
  - Console admin
  - Gerenciamento de contas de usuário.
  
- Dependëncias:
```
<dependency>
   <groupId>io.quarkus</groupId>
   <artifactId>quarkus-keycloak-authorization</artifactId>
</dependency>
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-oidc</artifactId>
</dependency>
```

- Configurando o serviço para uso dos recursos de token jwt.
obs: Antes retire as dependências do keycloak e adicione a abaixo:
```
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-jwt</artifactId>
    </dependency>
```

###### Configurando uma comunicação https:
- Gere o certificado autoassinado .pem por exemplo:
```
openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem
```
- Referencie o certificado nas propriedades do serviço.

```
quarkus.http.ssl.certificate.file=/path/cert.pem
quarkus.http.ssl.certificate.key-file=/path/key.pem
```

- Podemos gerar um certificado pelo keystore tambémÇ
```
keytool -genkey -keyalg RSA -alias quarkus -keystore keystore.jks -storepass password -validity 365 -keysize 2048
quarkus.http.ssl.certificate.key-store-file=/path/keystore.jks
quarkus.http.ssl.certificate.key-store-password=password
```
- Por fim, podemos especificar a porta usada pelo servidor undertow, para vincular o protocolo https.
```
quarkus.http.ssl-port=8443
https://localhost:8443/customers
```

###### Gerando a lista de configurações possíveis (application.properties) com base nas dependências do projeto.
```
mvn quarkus:generate-config
```
 ###### Mudando a porta da aplicação
 - Opção 1:
 ```
 java -Dquarkus.http.port=8180 app.jar
 ```
 
 -Opção 2 (via variável de ambiente):
 ```
 export QUARKUS_HTTP_PORT=8180
 ```
 
 - Opção 3, utilizando uma configuração externa:
   - Implemente a classe ConfigSource
   - A classe implementada tem que ter seupath no arquivo META-INF/services/org.eclipse.microprofile.config.spi.ConfigSource e valor com.fabriciolfj.github.client.customer.config.FileConfigSource.
   - Exemplo de uso:
``` 
@ConfigProperty(name = "greeting")
private String greeting;
```
   
###### Uso de profiles.
- Para separar as configurações em profiles, inicie-as com:
  - %dev ou
  - %test ou
  - %prod

- Para iniciar o quarkus apontando para as configurações de um profile específico:
```
mvn quarkus:dev
mvn quarkus:test
mvn quarkus:prod
```
  
##### Vertx
- É um conjunto de ferramentos, ou jars, que lhe auxilia no desenvolvimento de aplicações no modelo reativo, no mais:
  - Responsivo: um sistema reativo precisa ser capaz de lidar com solicitações em um tempo razoável.
  - Resiliente: diante de falhas, o sistema continue funcionando.
  - Elástico: Um sistema reativo deve ser capaz de ser escalado, conforme a demanda.
  - Orientado por mensagens: o sistema reativo interagem entre si, por mensagens.
  
###### Alguns componentes do Vertx.
- Verticles: unidade básica de implantação
- Events bus: ferramenta principal que permite que os verticles se comuniquem.

###### Formas de comunicação
- Point-to-Point: as mensagens são encaminhadas para apenas um dos manipuladores registrados.
- Request-response:  similar ao point-to-point, exceto que inclui um manipulador de resposta opcional, que proporciona ao destinatário a possibilidade de responder ou não. 
- Publish-subscribe: ao publicar a mensagem, o event-bus envia a todos os manipuladores registrados.
