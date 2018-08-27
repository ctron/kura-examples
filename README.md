# Eclipse Kura™ examples [![Travis](https://img.shields.io/travis/ctron/kura-examples.svg)](https://travis-ci.org/ctron/kura-examples)

This is a repository containing examples for [Eclipse Kura™](https://eclipse.org/kura "Eclipse Kura™").

* [simple-example-1](simple/simple-example-1): A simple Kura component using a pure Maven build workflow
* [camel-example-1](camel/camel-example-1): A simple example running Camel
* [camel-example-2](camel/camel-example-2): A more complex Camel example including JDBC and full featured configuration
* [camel-example-3](camel/camel-example-3): A simple example publishing to the cloud API using Camel
* [generator-1](generator/generator-1): A simple value generator, publishing to the cloud API

## Pre-requisites

You will need the following pre-requisites in order compile and use these examples:

 1. A local installation of Java 1.8.x
 1. A local installation of Apache Maven 3.5.x
 1. A local installation of Git
 1. A Eclipse Kura capable device version 3.2+

### Java 8

Java 8 is required as build and runtime environment. 

### Apache Maven 3.5.x

The build process requires Apache Maven 3.5.x. Earlier versions are not supported.

### Git

It is necessary to clone this repository with Git.

There is no requirement for a specific version or distribution of git. You will
just need "some git tool". All following examples will however assume you
have the classic `git` command line tool installed. So you may need to adapt
to your local setup.

### Eclipse Kura

You will of course need a Kura capable device with support for Camel. You will need version
3.2 or newer of Kura.

**Note:** If you are using ESF, you will need to install the "FUSE addon" in order to have
support of Apache Camel as ESF does not ship Camel/FUSE out of the box.

Also see [Dockerized Emulator](#running-with-the-dockerized-emulator) if you don't have a Kura device at hand.

## Perform the build

First you need to check out the sources:

    git clone https://github.com/ctron/kura-examples
    cd kura-examples

Now you can trigger the build:

    mvn clean install

This will create `.dp` packages in the various `target` folders which you
can directly upload to your Kura device or to the Kura emulator.

## Running with the dockerized emulator

There is a dockerized Kura emulator available at
[ctron/kura-emulator](https://github.com/ctron/kura-emulator "Kura Emulator GitHub repository").

The simplest way to start it is:

    docker run -ti -p 8080:8080 ctron/kura-emulator

A which will start the emulator and export the web console on port 8080. The default
credentials for the Kura Web UI are `admin`/`admin`.

The docker container also includes Apache Felix File Install so
that if you run the docker container with:

    docker run -ti -p 8080:8080 -v /home/user/path/to/bundles:/opt/eclipse/kura/load:z ctron/kura-emulator

You can drop the bundles (the `jar` files, not the `dp` packages) into
your directory `/home/user/path/to/bundles` on the host and Kura will
pick them up.

Of course you may need to adapt the path to your local setup and you may
also drop the trailing `:z` if you are not running SElinux.

The Kura emulator image is based on the Kura 3.2.0 release, adding a few
necessary patches to let it run inside of docker.

The container can also be run on Windows and Mac OS X.

If you encounter a message on startup like the following, then this may be
ignored:


    !ENTRY org.eclipse.equinox.ds 1 0 2017-03-17 10:18:43.618
    !MESSAGE Could not bind a reference of component org.eclipse.kura.core.configuration.CloudConfigurationHandler. The reference is: Reference[name = CloudService, interface = org.eclipse.kura.cloud.CloudService, policy = dynamic, cardinality = 0..1, target = (kura.service.pid=org.eclipse.kura.cloud.CloudService), bind = setCloudService, unbind = unsetCloudService]

