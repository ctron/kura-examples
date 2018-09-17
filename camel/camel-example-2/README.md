# Example #2 – JDBC endpoint with MySQL

This example shows using the `camel-jdbc` component in combination with MySQL.

## Prepare

Start up the MySQL server and the Kura emulator by executing:

    docker run --name mysql-1 -e MYSQL_RANDOM_ROOT_PASSWORD=true -e MYSQL_DATABASE=camel -e MYSQL_USER=camel -e MYSQL_PASSWORD=password -d -p 3306:3306 mysql/mysql-server
    docker run --name kura -ti -p 8080:8080 --link mylsq-1:sql ctron/kura-emulator

With Apache File Install support for testing (see [Testing and debugging](#testing-and-debugging)):

    docker run --name kura -ti -p 8080:8080 --link mylsq-1:sql -v /home/user/path/to/bundles:/opt/eclipse/kura/load:z ctron/kura-emulator

You can now navigate to http://localhost:8080 and log into Kura with the credentials `admin:admin`.

**Note:** If you are not using Linux for running docker images, "locahost" may actually be a different IP address.
Please check with your local system setup.

**Note:** Be sure to set the hostname as described in [Configure Component](#configure-component)!

If you do have a MySQL server up and running you don't need to start the dockerized MySQL server.
Be sure to configure the example component properly later on with the correct settings.

## Build and install

This component can be built using Maven. The following command will perform a full build:

    mvn clean package

This will create a file named `camel-example-2_*-SNAPSHOT.dp` which contains all required dependencies
and can directly be installed into Kura.

## Testing and debugging

If you want to rebuild and directly test, you can leverage Apache File install. Run the Kura docker image
as described above (with `-v`) and use the absolute location of `target/local` as volume mapping
(e.g. `/home/user/git/kura-examples/camel/camel-example-2/target/local`).

Once Kura is started up you can drop all required artifacts into this folder by executing:

    mvn clean install -Plocal-test

Apache File Install will periodically scan this folder, pick up any changes and deploy them into Kura
automatically. It will also update the bundles the next time this command is being called.

For remote debugging and JMX support please see: https://github.com/ctron/kura-emulator#running-with-jmx-enabled

## Configure component

Log in to Kura and configure the component "Camel Example #2":

* Set the parameter "Host" to: `${SQL_PORT_3306_TCP_ADDR}`

This will use the environment variable, set by the docker link as a host name for the MySQL server.
Of course it is also possible to use a different database server.

## Checking the result

Looking at the Kura console (in the docker container named `kura`) you should see the following output:

    …
    [{now()=2017-05-24 09:15:31.0}]
    [{now()=2017-05-24 09:15:32.0}]
    [{now()=2017-05-24 09:15:33.0}]
    [{now()=2017-05-24 09:15:34.0}]
    …

This is the output of the SQL query, processed by `MyProcessor`.
