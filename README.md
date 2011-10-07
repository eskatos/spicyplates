# Spicy Plates

Spicy Plates ease the use of templating engines for simple use cases supporting [Apache Velocity](http://velocity.apache.org/), [StringTemplate](http://www.stringtemplate.org/) and [eRuby](http://www.ruby-doc.org/stdlib/libdoc/erb/rdoc/) templates.

Boilerplate code is provided to use templates from simple strings, classpath resources, webapp resources or files.

Webapp support can be used through Servlets or Filters.

Templates are cached and when it's appropriate they are reloaded when changed (eg. not when loaded from a jar).

Several artifacts allow you to choose what templating engine:

* spicyplates-velocity
* spicyplates-stringtemplate
* spicyplates-eruby
* spicyplates-multi allow mixing template types in a single application

# Dependency

No release nor snapshots have been published yet.
For now you'll have to clone this repository and run a "mvn install" command in the projects root.

SpicyPlates depends on a SNAPSHOT version of [java-toolbox](https://github.com/codeartisans/java-toolbox), you will need to "mvn install" it too.

# Usage

A servlet and a servlet filter are provided for quick usage in a webapp:

* spicyplates-eruby/ERubySpicyServlet
* spicyplates-eruby/ERubySpicyFilter
* spicyplates-stringtemplate/STSpicyServlet
* spicyplates-stringtemplate/STSpicyFilter
* spicyplates-velocity/VelocitySpicyServlet
* spicyplates-velocity/VelocitySpicyFilter
* spicyplates-multi/MultiSpicyServlet
* spicyplates-multi/MultiSpicyFilter

They share the very same configuration:

* packages: coma separated list of root template packages
* directories: coma separated list of root template directories
* web-resources: if equals to true, use the webapp resources root as a template root
* allow-query: if equals to true, put the query attributes in the template context, DO NOT USE IN PRODUCTION, default to false

When loading a template, classpath, directories and web-resources roots are used in this order, first template found wins.

For programmatic usage see the unit tests in spicyplates-multi.

# Changelog
