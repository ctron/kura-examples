# Generator #1 – Sawtooth value generator

This is a simple generator, publishing to the Kura cloud API. This example demonstrates the use
of a plain Maven + Maven Bundle Plugin approach, using proper configuration parsing using OSGi
SCR 1.3 functionality.

Instead of using a `Map<String,Object>` for receiving OSGi component configuration, the configuration
is passed in the configuration data annotation.

**Note:** This example uses OSGi SCR 1.3 functionality which isn't supported in current Kura versions.
          You need to use a development version of Kura, or wait for Kura 4 to be released.

## Prepare

Start up a new Kura instance, e.g. using the docker image: https://hub.docker.com/r/ctron/kura-emulator/ 

## Build and install

This component can be built using Maven. The following command will perform a full build:

    mvn clean package

This will create a file named `generator-1_*-SNAPSHOT.dp`, which contains all required dependencies
and can directly be installed into Kura.

## Testing and debugging

If you want to rebuild and directly test, you can leverage Apache File install. Run the Kura docker image
as described above (with `-v`) and use the absolute location of `target` as volume mapping
(e.g. `/home/user/git/kura-examples/camel/camel-example-3/target`).

Once Kura is started up you can drop all required artifacts into this folder my executing:

    mvn clean install

Apache File Install will periodically scan this folder, pick up any changes and deploy them into Kura
automatically. It will also update the bundles the next time this command is being called.

For remote debugging and JMX support please see: https://github.com/ctron/kura-emulator#running-with-jmx-enabled

## Configure component

Log in to Kura and configure the component "Generator #1":

* Set the components parameters

## Checking the result

Check in the target system. You should see a random stream numbers on the topic `generator-1/data`
containing `value` as the single metric.
