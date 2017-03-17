# Simple example #1

This is a simple Kura component without any logic. It gets registered into
the Kura configuration system and can be configured during runtime.

## Kura specifics

* The `@Component` annotation needs to have `immediate = true` and
`configurationPolicy = REQUIRE`, otherwise it won't show up in the
configuration service.

* Although it is possible to use the OSGi metatype annotations it is not
possible to use the actual value conversion. So the activate/modified methods
must use property maps and cannot use the actual `@Config` interface.
That would require OSGi DS 1.3, which is not supported by Kura.</li>

* The `id` value of the `@ObjectClassDefinition` annotation on
the `@Config` interface must be the full qualified name of this
class/component (`ExampleConfigurableComponent`. This is a
special requirement by Kura. Otherwise the metatype configuration
will not be detected.
