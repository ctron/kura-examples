# Example #3 – Random numbers to Kura API

This examples shows how to publish data to the Kura API.

## Prepare

Start up a new Kura instance, e.g. using the docker image: https://hub.docker.com/r/ctron/kura-emulator/ 

## Build and install

This component can be built using Maven. The following command will perform a full build:

    mvn clean package

This will create a file named `camel-example-3_*-SNAPSHOT.dp` which contains all required dependencies
and can directly be installed into Kura.

## Testing and debugging

If you want to rebuild and directly test, you can leverage Apache File install. Run the Kura docker image
as described above (with `-v`) and use the absolute location of `target/local` as volume mapping
(e.g. `/home/user/git/kura-examples/camel/camel-example-3/target/local`).

Once Kura is started up you can drop all required artifacts into this folder by executing:

    mvn clean install -Plocal-test

Apache File Install will periodically scan this folder, pick up any changes and deploy them into Kura
automatically. It will also update the bundles the next time this command is being called.

For remote debugging and JMX support please see: https://github.com/ctron/kura-emulator#running-with-jmx-enabled

## Configure component

Log in to Kura and configure the component "Camel Example #3":

* Set the target cloud instance

## Checking the result

Check in the target system. You should see a random stream numbers on the topic `myapp/data`
containing `random` as the single metric.
