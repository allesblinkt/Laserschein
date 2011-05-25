# Status

The Laserschein library for Processing is in **development** and not really suited to be used out of the box.
Hello Lapland!

For now the interfaces supported include:

- Pangolin LD2000 & LD2000.NET via the Pangolin SDK DLL (Windows only)
- Easylase USB (planned)


# Structure

## Interfacing

There is at least one class subclassing *AbstractLaserOutput* for each individual laser interface.
For now the library defaults to using the LD2000Adaptor, so when adding support for the Easylase interface, there should be some mechanism to choose which driver to use.


## Directories

### data/
Data files (graphics etc.)

### devlib/
Java libraries needed for development 

### distribution
Here go the generated libraries by the ANT script

### examples/
Example sketches to be distributed with the library

### lib/
Libraries needed by Laserschein (to be included in the distribution)

### resources/
Stuff needed to build the Processing library

### src/
Main source code

### tests/
Source code for various tests. All the stuff and experiments that are not included in the library go here.

### web/
Website for the library