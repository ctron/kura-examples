# Example #4 – Plain Camel Example 

This example shows a plain Camel example, running inside, but also outside of Kura, including unit tests.

## Prepare

Start up a new Kura 4+ instance, e.g. using the docker image: https://hub.docker.com/r/ctron/kura

## Build and install

This component can be built using Maven. The following command will perform a full build:

    mvn clean package

This will create a file named `camel-example-4_*-SNAPSHOT.dp` which contains all required dependencies
and can directly be installed into Kura.

## Testing and debugging

If you want to rebuild and directly test, you can leverage Apache FileInstall. Run the Kura docker image
as described in the link above (with `-v`) and use the absolute location of `target/load` as volume mapping
(e.g. `/home/user/git/kura-examples/camel/camel-example-4/target/load`).

Once Kura is started up, you can drop all required artifacts into this folder by executing:

    mvn clean install -Plocal-test

Apache File Install will periodically scan this folder, pick up any changes and deploy them into Kura
automatically. It will also update the bundles the next time this command is being called.

For remote debugging and JMX support please see: https://github.com/ctron/kura-container#running-with-jmx-enabled

## Checking the result

You can access either the IEC 60870 server, the HTTP server using the REST API and the SwaggerUI at
http://localhost:8090/api, or hooking up the Camel context with the ID `camel.example.4` with Kura Wires
on the endpoints `seda:wiresInput1` and `seda:wiresOutput1`.
